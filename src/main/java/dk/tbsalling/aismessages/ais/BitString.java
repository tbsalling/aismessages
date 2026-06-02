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
 * Packed, immutable bit-string used as the decoded AIS payload representation.
 *
 * <p>Bits are stored MSB-first across a {@code long[]}: bit {@code i} lives in
 * {@code words[i >>> 6]} at bit position {@code 63 - (i & 63)}. The array is allocated
 * with a one-word slack guard past the last logical word, so cross-word range reads
 * can fold into a single branchless shift-pair without bounds checks.
 *
 * <p>The public read accessors all use a half-open {@code [begin, end)} bit range,
 * with {@code end - begin} the field width in bits. Reads beyond the logical
 * {@link #length() length} yield zero bits — this mirrors the historical
 * "zero bit-stuffed string" behaviour of the predecessor {@code BitStringParser}.
 *
 * <p>Instances are immutable and safe for concurrent use.
 *
 * @author tbsalling
 */
public final class BitString {

    /**
     * An empty {@code BitString} of length 0.
     */
    public static final BitString EMPTY = new BitString(new long[1], 0);

    private final long[] words;
    private final int length;

    private BitString(long[] words, int length) {
        this.words = words;
        this.length = length;
    }

    /**
     * Construct a {@code BitString} that takes ownership of the supplied packed-bits array.
     *
     * <p>Ownership transfer: the caller must not retain a reference to {@code words} or mutate
     * it after this call returns. This contract avoids a defensive copy on the hot path.
     *
     * <p>{@code words.length} must be at least {@code ((length + 63) >>> 6) + 1} — one slack
     * guard word past the last data word. Bits beyond {@code length} (within the last data word
     * and within the slack guard) must already be zero; callers building a packed buffer should
     * clear trailing padding bits before invoking this factory.
     *
     * @param words  packed bits, MSB-first within each long
     * @param length logical length in bits
     * @return a new {@code BitString} backed by the supplied array
     * @throws IllegalArgumentException if {@code length} is negative or {@code words} is too short
     */
    public static BitString of(long[] words, int length) {
        if (words == null) {
            throw new IllegalArgumentException("words cannot be null");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length cannot be negative: " + length);
        }
        int required = ((length + 63) >>> 6) + 1;
        if (words.length < required) {
            throw new IllegalArgumentException(
                    "words array too short: have " + words.length + ", need at least " + required + " (incl. slack guard)");
        }
        if (length == 0) return EMPTY;
        return new BitString(words, length);
    }

    /**
     * Construct a {@code BitString} from a literal binary string of '0' and '1' characters.
     * Intended primarily for tests and constants.
     *
     * @param binaryString a sequence of '0' and '1' characters
     * @return the packed bit string
     * @throws IllegalArgumentException if the string contains any character other than '0' or '1'
     */
    public static BitString ofBitString(String binaryString) {
        if (binaryString == null) {
            throw new IllegalArgumentException("binaryString cannot be null");
        }
        int length = binaryString.length();
        if (length == 0) return EMPTY;
        long[] words = new long[((length + 63) >>> 6) + 1];
        for (int i = 0; i < length; i++) {
            char c = binaryString.charAt(i);
            if (c == '1') {
                words[i >>> 6] |= 1L << (63 - (i & 63));
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid binary character at index " + i + ": '" + c + "'");
            }
        }
        return new BitString(words, length);
    }

    /**
     * @return the number of bits in this bit string
     */
    public int length() {
        return length;
    }

    /**
     * Return whether the bit at the given begin index is 1.
     *
     * <p>Following the historical decoder semantics, only the bit at {@code begin} matters;
     * {@code end} is accepted for grep-compatibility with the other typed accessors but ignored.
     *
     * @param begin the bit index (inclusive)
     * @param end   the bit index (exclusive); ignored
     * @return {@code true} if the bit at {@code begin} is set
     */
    public boolean getBoolean(int begin, int end) {
        if (begin >= length) return false;
        return (words[begin >>> 6] & (1L << (63 - (begin & 63)))) != 0L;
    }

    /**
     * Read an unsigned integer from the bit range {@code [begin, end)}.
     *
     * @param begin the starting bit index (inclusive)
     * @param end   the ending bit index (exclusive); {@code end - begin} must be in 0..32
     * @return the unsigned integer value
     */
    public int getUnsignedInt(int begin, int end) {
        int width = end - begin;
        if (width == 0) return 0;
        if (begin >= length) return 0;
        int wordIdx = begin >>> 6;
        int shift = begin & 63;
        long hi = words[wordIdx] << shift;
        long lo = (shift == 0) ? 0L : (words[wordIdx + 1] >>> (64 - shift));
        return (int) ((hi | lo) >>> (64 - width));
    }

    /**
     * Read a signed two's-complement integer from the bit range {@code [begin, end)}.
     * Sign extension is applied within the field width.
     *
     * @param begin the starting bit index (inclusive)
     * @param end   the ending bit index (exclusive); {@code end - begin} must be in 0..32
     * @return the signed integer value
     */
    public int getSignedInt(int begin, int end) {
        int width = end - begin;
        if (width == 0) return 0;
        int v = getUnsignedInt(begin, end);
        int s = 32 - width;
        return (v << s) >> s;
    }

    /**
     * Read an unsigned long integer from the bit range {@code [begin, end)}.
     *
     * @param begin the starting bit index (inclusive)
     * @param end   the ending bit index (exclusive); {@code end - begin} must be in 0..64
     * @return the unsigned long value
     */
    public long getUnsignedLong(int begin, int end) {
        int width = end - begin;
        if (width == 0) return 0L;
        if (begin >= length) return 0L;
        int wordIdx = begin >>> 6;
        int shift = begin & 63;
        long hi = words[wordIdx] << shift;
        long lo = (shift == 0) ? 0L : (words[wordIdx + 1] >>> (64 - shift));
        long combined = hi | lo;
        return (width == 64) ? combined : (combined >>> (64 - width));
    }

    /**
     * Read an unsigned integer from the bit range and return it as a float.
     */
    public float getUnsignedFloat(int begin, int end) {
        return (float) getUnsignedInt(begin, end);
    }

    /**
     * Read a signed integer from the bit range and return it as a float.
     */
    public float getSignedFloat(int begin, int end) {
        return (float) getSignedInt(begin, end);
    }

    /**
     * Extract the raw bits in {@code [begin, end)} as a string of '0' and '1' characters.
     * Useful for spare bits, regional reserved fields, and diagnostics.
     *
     * @param begin the starting bit index (inclusive)
     * @param end   the ending bit index (exclusive)
     * @return the bit pattern as a string
     */
    public String getBits(int begin, int end) {
        int width = end - begin;
        if (width <= 0) return "";
        char[] chars = new char[width];
        for (int i = 0; i < width; i++) {
            int bit = begin + i;
            if (bit >= length) {
                chars[i] = '0';
            } else {
                chars[i] = (words[bit >>> 6] & (1L << (63 - (bit & 63)))) != 0L ? '1' : '0';
            }
        }
        return new String(chars);
    }

    /**
     * Return a fresh {@code BitString} containing the bits in {@code [begin, end)}.
     *
     * @param begin the starting bit index (inclusive)
     * @param end   the ending bit index (exclusive); must not exceed {@link #length()}
     * @return a new {@code BitString} of length {@code end - begin}
     * @throws IllegalArgumentException if {@code begin < 0}, {@code end < begin}, or {@code end > length}
     */
    public BitString slice(int begin, int end) {
        if (begin < 0 || end < begin || end > length) {
            throw new IllegalArgumentException("invalid slice range [" + begin + ", " + end + ") for length " + length);
        }
        int newLength = end - begin;
        if (newLength == 0) return EMPTY;
        long[] newWords = new long[((newLength + 63) >>> 6) + 1];
        int shift = begin & 63;
        int wordIdx = begin >>> 6;
        int newWordsToFill = (newLength + 63) >>> 6;
        for (int i = 0; i < newWordsToFill; i++) {
            long hi = words[wordIdx + i] << shift;
            long lo = (shift == 0) ? 0L : (words[wordIdx + i + 1] >>> (64 - shift));
            newWords[i] = hi | lo;
        }
        clearTrailingPaddingBits(newWords, newLength);
        return new BitString(newWords, newLength);
    }

    /**
     * Return a {@code BitString} of length {@code newLength} containing this bit string's
     * bits followed by zero-bit padding. If {@code newLength == length()} this instance is returned.
     *
     * @param newLength the target length in bits; must be {@code >= length()}
     * @return a {@code BitString} padded to {@code newLength}
     * @throws IllegalArgumentException if {@code newLength < length()}
     */
    public BitString withLengthPaddedTo(int newLength) {
        if (newLength < length) {
            throw new IllegalArgumentException("newLength " + newLength + " < current length " + length);
        }
        if (newLength == length) return this;
        long[] newWords = new long[((newLength + 63) >>> 6) + 1];
        // Only the data words need copying — slack stays zero.
        int dataWords = (length + 63) >>> 6;
        System.arraycopy(words, 0, newWords, 0, dataWords);
        return new BitString(newWords, newLength);
    }

    /**
     * Zero out any bits in {@code words} past the logical length.
     * Assumes {@code words.length >= ((length + 63) >>> 6) + 1} (slack guard present).
     */
    private static void clearTrailingPaddingBits(long[] words, int length) {
        int validBitsInLastWord = length & 63;
        int lastWordIdx = length >>> 6;
        if (validBitsInLastWord > 0) {
            long paddingMask = ~((1L << (64 - validBitsInLastWord)) - 1);
            words[lastWordIdx] &= paddingMask;
        }
        // Slack guard word and any words past lastWordIdx are zero by allocation.
    }

    /**
     * @return the bit string rendered as a sequence of '0' and '1' characters.
     * <strong>O(n) allocation</strong> — for debug, logging, and diagnostics only; not for hot paths.
     */
    @Override
    public String toString() {
        if (length == 0) return "";
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = (words[i >>> 6] & (1L << (63 - (i & 63)))) != 0L ? '1' : '0';
        }
        return new String(chars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BitString other)) return false;
        return length == other.length && Arrays.equals(words, other.words);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(words) * 31 + length;
    }
}
