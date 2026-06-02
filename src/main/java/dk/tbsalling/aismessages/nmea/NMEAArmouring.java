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

package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.ais.SixBitAsciiCodec;

/**
 * NMEA six-bit ASCII armouring (IEC 61162-1 §5.3.1).
 *
 * <p>The NMEA 0183 {@code !AIVDM}/{@code !AIVDO} sentence format carries binary payloads as
 * printable ASCII characters using a fixed armouring alphabet: each six-bit value 0..63 maps
 * to one character in the ranges {@code '0'..'W'} (codes 0..39) and {@code '`'..'w'}
 * (codes 40..63). This class binds that alphabet to a {@link SixBitAsciiCodec} and exposes the
 * two operations the NMEA pipeline needs.
 *
 * <p>The last character of an armoured payload may carry padding bits; the count is
 * transmitted in the NMEA sentence's padding-bits field and supplied to {@link #decode}.
 */
public final class NMEAArmouring {

    /**
     * IEC 61162-1 §5.3.1 — 0..39 → '0'..'W', 40..63 → '`'..'w'.
     */
    private static final char[] ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', // 0..9   (ASCII 48..57)
            ':', ';', '<', '=', '>', '?',                 // 10..15 (ASCII 58..63)
            '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', // 16..25 (ASCII 64..73)
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', // 26..35 (ASCII 74..83)
            'T', 'U', 'V', 'W',                         // 36..39 (ASCII 84..87)
            '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', // 40..49 (ASCII 96..105)
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', // 50..59 (ASCII 106..115)
            't', 'u', 'v', 'w'                          // 60..63 (ASCII 116..119)
    };

    private static final SixBitAsciiCodec CODEC = new SixBitAsciiCodec(ALPHABET);

    private NMEAArmouring() {
        // utility class
    }

    /**
     * Decode an armoured ASCII string directly into a {@link BitString}.
     *
     * @param armoured    the armoured string (characters in {@code '0'..'W'} and {@code '`'..'w'})
     * @param paddingBits number of trailing pad bits to discard from the last character (0..5)
     * @return the unpacked bit string of length {@code armoured.length() * 6 - paddingBits}
     * @throws IllegalArgumentException if {@code armoured} is null, contains a character outside
     *                                  the armouring range, or {@code paddingBits} is out of range
     */
    public static BitString decode(String armoured, int paddingBits) {
        return CODEC.decode(armoured, paddingBits);
    }

    /**
     * Encode a {@link BitString} as an armoured ASCII string. If {@code bits.length()} is not a
     * multiple of 6 the final six-bit chunk is zero-padded; the number of padding bits used is
     * {@code (6 - bits.length() % 6) % 6}.
     *
     * @param bits the bit string to encode
     * @return the armoured ASCII string of length {@code (bits.length() + 5) / 6}
     * @throws IllegalArgumentException if {@code bits} is null
     */
    public static String encode(BitString bits) {
        return CODEC.encode(bits);
    }
}
