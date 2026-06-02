/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

package dk.tbsalling.aismessages.ais;

import java.util.Arrays;

/**
 * Six-bit ASCII codec: pack and unpack a {@link BitString} as a string of printable ASCII
 * characters, six bits per character, against a caller-supplied 64-entry alphabet.
 *
 * <p>The codec is alphabet-agnostic — it embodies the mechanism (shift/mask packing into a
 * {@link BitString}, table-lookup unpacking back to characters) but has no built-in knowledge
 * of any specific alphabet. Concrete alphabets (NMEA armouring, AIS text, …) live with the
 * code that uses them and are passed in to the constructor.
 *
 * <p>Instances are immutable and thread-safe.
 */
public final class SixBitAsciiCodec {

    private final char[] sixBitToChar;
    private final byte[] charToSixBit;

    /**
     * Construct a codec for the given 64-entry alphabet. The alphabet must contain 64 distinct
     * 7-bit ASCII characters; entry {@code i} is the character representing six-bit value
     * {@code i}.
     *
     * @param alphabet the alphabet (length 64, distinct 7-bit ASCII entries)
     * @throws IllegalArgumentException if {@code alphabet} is null, of wrong length, contains
     *                                  a non-ASCII character, or contains duplicates
     */
    public SixBitAsciiCodec(char[] alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet cannot be null");
        }
        if (alphabet.length != 64) {
            throw new IllegalArgumentException("alphabet must have 64 entries");
        }
        this.sixBitToChar = alphabet.clone();
        this.charToSixBit = new byte[128];
        Arrays.fill(this.charToSixBit, (byte) -1);
        for (int i = 0; i < 64; i++) {
            char c = alphabet[i];
            if (c >= 128) {
                throw new IllegalArgumentException("alphabet must be 7-bit ASCII; got " + (int) c);
            }
            if (this.charToSixBit[c] >= 0) {
                throw new IllegalArgumentException("alphabet has duplicate character: '" + c + "'");
            }
            this.charToSixBit[c] = (byte) i;
        }
    }

    /**
     * Encode a {@link BitString} as a string of characters from this codec's alphabet.
     * If {@code bits.length()} is not a multiple of 6, the final six-bit chunk is zero-padded;
     * the implied padding-bit count is {@code (6 - bits.length() % 6) % 6}, so a round-trip is
     * {@code decode(encode(bs), (6 - bs.length() % 6) % 6).equals(bs)}.
     *
     * @param bits the bit string to encode
     * @return the encoded string of length {@code (bits.length() + 5) / 6}
     * @throws IllegalArgumentException if {@code bits} is null
     */
    public String encode(BitString bits) {
        if (bits == null) {
            throw new IllegalArgumentException("bits cannot be null");
        }
        int length = bits.length();
        if (length == 0) return "";
        int numChars = (length + 5) / 6;
        char[] chars = new char[numChars];
        for (int i = 0; i < numChars; i++) {
            int begin = i * 6;
            int bitsRead = Math.min(begin + 6, length) - begin;
            int sixBit = bits.getUnsignedInt(begin, begin + bitsRead);
            if (bitsRead < 6) {
                sixBit <<= (6 - bitsRead); // left-align partial value as the top bits of a 6-bit code
            }
            chars[i] = sixBitToChar[sixBit];
        }
        return new String(chars);
    }

    /**
     * Decode an encoded string into a {@link BitString}.
     *
     * @param encoded     the encoded string (each character must be in this codec's alphabet)
     * @param paddingBits number of trailing pad bits to discard from the last character (0..5)
     * @return the decoded bit string of length {@code encoded.length() * 6 - paddingBits}
     * @throws IllegalArgumentException if {@code encoded} is null, contains a character outside
     *                                  the alphabet, or {@code paddingBits} is out of range
     */
    public BitString decode(String encoded, int paddingBits) {
        if (encoded == null) {
            throw new IllegalArgumentException("encoded string cannot be null");
        }
        if (paddingBits < 0 || paddingBits > 5) {
            throw new IllegalArgumentException("paddingBits must be in range 0..5");
        }
        int length = encoded.length() * 6 - paddingBits;
        if (length < 0) length = 0;
        long[] words = new long[((length + 63) >>> 6) + 1];

        int bitPos = 0;
        int n = encoded.length();
        for (int i = 0; i < n; i++) {
            char c = encoded.charAt(i);
            if (c >= 128 || charToSixBit[c] < 0) {
                throw new IllegalArgumentException("Invalid six-bit character: '" + c + "'");
            }
            long sixBit = charToSixBit[c] & 0x3FL;
            int wordIdx = bitPos >>> 6;
            int bitOffset = bitPos & 63;
            int leftShift = 58 - bitOffset; // 58 = 64 - 6 (place 6 bits MSB-first at bitOffset)
            if (leftShift >= 0) {
                words[wordIdx] |= sixBit << leftShift;
            } else {
                int bitsInNext = -leftShift;     // 1..5
                words[wordIdx] |= sixBit >>> bitsInNext;
                words[wordIdx + 1] |= sixBit << (64 - bitsInNext);
            }
            bitPos += 6;
        }

        // Mask off stray bits past `length` in the last data word (paddingBits trimmed off the
        // last 6-bit chunk leaves up to 5 set bits past the logical end). The slack guard word
        // is naturally zero by Java's array-init contract.
        int validBitsInLastWord = length & 63;
        int lastWordIdx = length >>> 6;
        if (validBitsInLastWord > 0) {
            long mask = ~((1L << (64 - validBitsInLastWord)) - 1);
            words[lastWordIdx] &= mask;
        }

        return BitString.of(words, length);
    }

    /**
     * Decode the sub-range {@code [begin, end)} of {@code bits} as a string of characters from
     * this codec's alphabet. Bits beyond the largest multiple of 6 within the range are ignored.
     * No alphabet-specific transformation is applied.
     *
     * @param bits  the source bit string
     * @param begin the starting bit index (inclusive)
     * @param end   the ending bit index (exclusive)
     * @return the decoded string
     */
    public String decodeRange(BitString bits, int begin, int end) {
        int width = end - begin;
        int numChars = width / 6;
        if (numChars == 0) return "";
        char[] chars = new char[numChars];
        for (int i = 0; i < numChars; i++) {
            int code = bits.getUnsignedInt(begin + i * 6, begin + i * 6 + 6);
            chars[i] = sixBitToChar[code];
        }
        return new String(chars);
    }

    /**
     * Look up the alphabet character for a six-bit value (only the low 6 bits of {@code value}
     * are used). Intended for consumers that walk a {@link BitString} themselves to apply
     * per-character transformations.
     *
     * @param value six-bit value 0..63
     * @return the alphabet character for {@code value & 0x3F}
     */
    public char charFor(int value) {
        return sixBitToChar[value & 0x3F];
    }
}
