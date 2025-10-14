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

package dk.tbsalling.aismessages.ais.messages;

import static dk.tbsalling.aismessages.ais.Decoders.*;

/**
 * BitStringParser provides a clean separation between AIS message bitstring parsing
 * and the immutable AIS message value objects.
 * <p>
 * This class encapsulates all bitstring parsing logic, allowing AIS message classes
 * to be pure immutable value objects without parsing responsibilities.
 * <p>
 * The parser provides typed methods for extracting different data types from specific
 * bit ranges, using the standard AIS decoders.
 *
 * @author tbsalling
 */
public class BitStringParser {

    /**
     * The binary string representation of the AIS message payload (string of 0's and 1's).
     */
    private final String bitString;

    /**
     * Creates a new BitStringParser for the given binary string.
     *
     * @param bitString the binary string representation of an AIS message payload
     * @throws NullPointerException if bitString is null
     */
    public BitStringParser(String bitString) {
        if (bitString == null) {
            throw new NullPointerException("bitString cannot be null");
        }
        this.bitString = bitString;
    }

    /**
     * Retrieves a substring of the zero bit-stuffed string based on the given beginIndex and endIndex.
     * <p>
     * This method handles cases where the bit string is shorter than expected by padding with zeros.
     *
     * @param beginIndex the starting index (inclusive) of the substring
     * @param endIndex   the ending index (exclusive) of the substring
     * @return the substring of the zero bit-stuffed string
     */
    public String getBits(int beginIndex, int endIndex) {
        return getZeroBitStuffedString(endIndex).substring(beginIndex, endIndex);
    }

    /**
     * Returns a zero bit-stuffed string based on the given endIndex.
     * <p>
     * If the bitString is shorter than endIndex, it is padded with zeros.
     *
     * @param endIndex the index where the string should end
     * @return the zero bit-stuffed string
     */
    private String getZeroBitStuffedString(int endIndex) {
        String b = bitString;
        if (b.length() - endIndex < 0) {
            StringBuilder c = new StringBuilder(b);
            for (int i = b.length() - endIndex; i < 0; i++) {
                c.append("0");
            }
            b = c.toString();
        }
        return b;
    }

    /**
     * Extracts an unsigned integer from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded unsigned integer value
     */
    public int getUnsignedInt(int beginIndex, int endIndex) {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a signed integer from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded signed integer value
     */
    public int getSignedInt(int beginIndex, int endIndex) {
        return INTEGER_DECODER.apply(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts an unsigned float from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded unsigned float value
     */
    public float getUnsignedFloat(int beginIndex, int endIndex) {
        return UNSIGNED_FLOAT_DECODER.apply(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a signed float from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded signed float value
     */
    public float getSignedFloat(int beginIndex, int endIndex) {
        return FLOAT_DECODER.apply(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a boolean value from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded boolean value
     */
    public boolean getBoolean(int beginIndex, int endIndex) {
        return BOOLEAN_DECODER.apply(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a string from the specified bit range using AIS 6-bit ASCII encoding.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded string value
     */
    public String getString(int beginIndex, int endIndex) {
        return STRING_DECODER.apply(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a bit pattern as a string from the specified bit range.
     * Useful for spare bits, regional reserved bits, etc.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the bit pattern as a string
     */
    public String getBitPattern(int beginIndex, int endIndex) {
        return BIT_DECODER.apply(getBits(beginIndex, endIndex));
    }

    /**
     * Returns the length of the bit string in bits.
     *
     * @return the number of bits in the bit string
     */
    public int getLength() {
        return bitString.length();
    }

    /**
     * Returns the complete bit string.
     *
     * @return the bit string
     */
    public String getBitString() {
        return bitString;
    }

    @Override
    public String toString() {
        return "BitStringParser{length=" + bitString.length() + " bits}";
    }
}
