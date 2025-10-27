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

import lombok.Getter;

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
@Getter
public final class BitStringParser {

    /**
     * The binary string representation of the AIS message payload (string of 0's and 1's).
     */
    private final String bitString;
    
    /**
     * Cached zero-bit-stuffed string. Computed lazily on first access and reused thereafter.
     * Once set, this contains the bitString padded with zeros to at least the maximum requested length.
     */
    private String cachedZeroBitStuffedString;

    public BitStringParser(String bitString) {
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
     * Extracts an unsigned integer from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded unsigned integer value
     */
    public int getUnsignedInt(int beginIndex, int endIndex) {
        return BitDecoder.INSTANCE.decodeUnsignedInt(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a signed integer from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded signed integer value
     */
    public int getSignedInt(int beginIndex, int endIndex) {
        return BitDecoder.INSTANCE.decodeSignedInt(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts an unsigned float from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded unsigned float value
     */
    public float getUnsignedFloat(int beginIndex, int endIndex) {
        return BitDecoder.INSTANCE.decodeUnsignedFloat(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a signed float from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded signed float value
     */
    public float getSignedFloat(int beginIndex, int endIndex) {
        return BitDecoder.INSTANCE.decodeFloat(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a boolean value from the specified bit range.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded boolean value
     */
    public boolean getBoolean(int beginIndex, int endIndex) {
        return BitDecoder.INSTANCE.decodeBoolean(getBits(beginIndex, endIndex));
    }

    /**
     * Extracts a string from the specified bit range using AIS 6-bit ASCII encoding.
     *
     * @param beginIndex the starting bit index (inclusive)
     * @param endIndex   the ending bit index (exclusive)
     * @return the decoded string value
     */
    public String getString(int beginIndex, int endIndex) {
        return BitDecoder.INSTANCE.decodeString(getBits(beginIndex, endIndex));
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
        return BitDecoder.INSTANCE.getBitString(getBits(beginIndex, endIndex));
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
     * Returns a zero bit-stuffed string based on the given endIndex.
     * <p>
     * If the bitString is shorter than endIndex, it is padded with zeros.
     * The result is cached to avoid recomputation on subsequent calls.
     *
     * @param endIndex the index where the string should end
     * @return the zero bit-stuffed string
     */
    private String getZeroBitStuffedString(int endIndex) {
        // Fast path: no padding needed
        if (endIndex <= bitString.length()) {
            return bitString;
        }
        
        // Check if we have a cached padded string that's long enough
        if (cachedZeroBitStuffedString != null && endIndex <= cachedZeroBitStuffedString.length()) {
            return cachedZeroBitStuffedString;
        }
        
        // Need to create a new padded string (or extend the existing cache)
        int deficit = endIndex - bitString.length();
        cachedZeroBitStuffedString = bitString + "0".repeat(deficit);
        return cachedZeroBitStuffedString;
    }

}
