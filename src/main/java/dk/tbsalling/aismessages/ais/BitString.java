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

import java.util.BitSet;

/**
 * BitString provides an efficient representation of a string of bits using BitSet internally.
 * This class replaces the previous String-based representation which was inefficient for
 * storing and manipulating binary data.
 * <p>
 * The class is immutable and provides methods for extracting substrings of bits and
 * converting to various data types.
 *
 * @author tbsalling
 */
public final class BitString {

    private final BitSet bits;
    private final int length;

    /**
     * Creates a BitString from a string of '0' and '1' characters.
     *
     * @param bitString a string consisting only of '0' and '1' characters
     * @throws IllegalArgumentException if the string contains invalid characters
     */
    public BitString(String bitString) {
        if (bitString == null) {
            throw new IllegalArgumentException("bitString cannot be null");
        }
        this.length = bitString.length();
        this.bits = new BitSet(length);
        
        for (int i = 0; i < length; i++) {
            char c = bitString.charAt(i);
            if (c == '1') {
                bits.set(i);
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid character in bit string: '" + c + "' at position " + i);
            }
        }
    }

    /**
     * Creates a BitString from a BitSet and length.
     *
     * @param bits   the BitSet containing the bit data
     * @param length the number of bits
     */
    private BitString(BitSet bits, int length) {
        this.bits = (BitSet) bits.clone();
        this.length = length;
    }

    /**
     * Returns the length of the bit string in bits.
     *
     * @return the number of bits
     */
    public int length() {
        return length;
    }

    /**
     * Extracts a substring of bits from the specified range.
     * If the requested range extends beyond the available bits, the result is padded with zeros.
     *
     * @param beginIndex the starting index (inclusive)
     * @param endIndex   the ending index (exclusive)
     * @return a new BitString containing the extracted bits
     */
    public BitString substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IllegalArgumentException("beginIndex cannot be negative: " + beginIndex);
        }
        if (endIndex < beginIndex) {
            throw new IllegalArgumentException("endIndex must be >= beginIndex: " + endIndex + " < " + beginIndex);
        }

        int newLength = endIndex - beginIndex;
        if (newLength == 0) {
            return new BitString(new BitSet(0), 0);
        }

        BitSet newBits = new BitSet(newLength);
        int copyLength = Math.min(length - beginIndex, newLength);
        
        for (int i = 0; i < copyLength; i++) {
            if (bits.get(beginIndex + i)) {
                newBits.set(i);
            }
        }
        
        return new BitString(newBits, newLength);
    }

    /**
     * Converts the bit string to a String representation (sequence of '0' and '1').
     * This method is provided for backward compatibility and debugging.
     *
     * @return a string of '0' and '1' characters
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(bits.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    /**
     * Returns the bit at the specified index.
     *
     * @param index the bit index
     * @return true if the bit is set (1), false otherwise (0)
     */
    public boolean get(int index) {
        if (index < 0 || index >= length) {
            return false; // Return 0 for out-of-bounds (padding)
        }
        return bits.get(index);
    }

    /**
     * Returns the character at the specified index ('0' or '1').
     *
     * @param index the character index
     * @return '1' if the bit is set, '0' otherwise
     */
    public char charAt(int index) {
        return get(index) ? '1' : '0';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitString bitString = (BitString) o;
        if (length != bitString.length) return false;
        
        for (int i = 0; i < length; i++) {
            if (bits.get(i) != bitString.bits.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = length;
        for (int i = 0; i < length; i++) {
            if (bits.get(i)) {
                result = 31 * result + 1;
            } else {
                result = 31 * result;
            }
        }
        return result;
    }
}
