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

/**
 * AIS text-field decoder. Combines the AIS six-bit ASCII alphabet (ITU-R M.1371-5 Table 47)
 * with the AIS-specific filler convention: AIS text fields are fixed-width and pad with the
 * {@code '@'} character; on decode the filler is turned into spaces and the result is trimmed
 * of leading/trailing whitespace.
 *
 * <p>Bits beyond the largest multiple of 6 within the requested range are ignored.
 */
public final class AISText {

    /**
     * ITU-R M.1371-5 Table 47 — 0..31 → '@'..'_', 32..63 → ' '..'?'.
     */
    private static final char[] ALPHABET = {
            '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', //  0.. 9 (ASCII 64..73)
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', // 10..19 (ASCII 74..83)
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z',             // 20..26 (ASCII 84..90)
            '[', '\\', ']', '^', '_',                    // 27..31 (ASCII 91..95)
            ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', // 32..41 (ASCII 32..41)
            '*', '+', ',', '-', '.', '/',                 // 42..47 (ASCII 42..47)
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', // 48..57 (ASCII 48..57)
            ':', ';', '<', '=', '>', '?'                  // 58..63 (ASCII 58..63)
    };

    private static final SixBitAsciiCodec CODEC = new SixBitAsciiCodec(ALPHABET);

    private AISText() {
        // utility class
    }

    /**
     * Decode the bit range {@code [begin, end)} as AIS six-bit ASCII text.
     *
     * @param bits  the source bit string
     * @param begin the starting bit index (inclusive)
     * @param end   the ending bit index (exclusive)
     * @return the decoded textual representation, with filler {@code '@'} replaced by space
     * and leading/trailing whitespace trimmed
     */
    public static String decode(BitString bits, int begin, int end) {
        int width = end - begin;
        int numChars = width / 6;
        if (numChars == 0) return "";
        // Walk the bits inline so we can apply '@' → space + trim in a single allocation.
        char[] chars = new char[numChars];
        for (int i = 0; i < numChars; i++) {
            int code = bits.getUnsignedInt(begin + i * 6, begin + i * 6 + 6);
            char c = CODEC.charFor(code);
            chars[i] = (c == '@') ? ' ' : c;
        }
        int start = 0;
        while (start < numChars && chars[start] <= ' ') start++;
        int endPos = numChars;
        while (endPos > start && chars[endPos - 1] <= ' ') endPos--;
        if (start == 0 && endPos == numChars) return new String(chars);
        return new String(chars, start, endPos - start);
    }
}
