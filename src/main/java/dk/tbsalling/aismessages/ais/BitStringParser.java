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

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

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

    private static final Map<Integer, String> SIX_BIT_ASCII = new TreeMap<>();

    static {
        SIX_BIT_ASCII.put(0, "@"); // 0
        SIX_BIT_ASCII.put(1, "A"); // 1
        SIX_BIT_ASCII.put(2, "B"); // 2
        SIX_BIT_ASCII.put(3, "C"); // 3
        SIX_BIT_ASCII.put(4, "D"); // 4
        SIX_BIT_ASCII.put(5, "E"); // 5
        SIX_BIT_ASCII.put(6, "F"); // 6
        SIX_BIT_ASCII.put(7, "G"); // 7
        SIX_BIT_ASCII.put(8, "H"); // 8
        SIX_BIT_ASCII.put(9, "I"); // 9
        SIX_BIT_ASCII.put(10, "J"); // 10
        SIX_BIT_ASCII.put(11, "K"); // 11
        SIX_BIT_ASCII.put(12, "L"); // 12
        SIX_BIT_ASCII.put(13, "M"); // 13
        SIX_BIT_ASCII.put(14, "N"); // 14
        SIX_BIT_ASCII.put(15, "O"); // 15
        SIX_BIT_ASCII.put(16, "P"); // 16
        SIX_BIT_ASCII.put(17, "Q"); // 17
        SIX_BIT_ASCII.put(18, "R"); // 18
        SIX_BIT_ASCII.put(19, "S"); // 19
        SIX_BIT_ASCII.put(20, "T"); // 20
        SIX_BIT_ASCII.put(21, "U"); // 21
        SIX_BIT_ASCII.put(22, "V"); // 22
        SIX_BIT_ASCII.put(23, "W"); // 23
        SIX_BIT_ASCII.put(24, "X"); // 24
        SIX_BIT_ASCII.put(25, "Y"); // 25
        SIX_BIT_ASCII.put(26, "Z"); // 26
        SIX_BIT_ASCII.put(27, "["); // 27
        SIX_BIT_ASCII.put(28, "\\"); // 28
        SIX_BIT_ASCII.put(29, "]"); // 29
        SIX_BIT_ASCII.put(30, "^"); // 30
        SIX_BIT_ASCII.put(31, "_"); // 31
        SIX_BIT_ASCII.put(32, " "); // 32
        SIX_BIT_ASCII.put(33, "!"); // 33
        SIX_BIT_ASCII.put(34, "\""); // 34
        SIX_BIT_ASCII.put(35, "#"); // 35
        SIX_BIT_ASCII.put(36, "$"); // 36
        SIX_BIT_ASCII.put(37, "%"); // 37
        SIX_BIT_ASCII.put(38, "&"); // 38
        SIX_BIT_ASCII.put(39, "'"); // 39
        SIX_BIT_ASCII.put(40, "("); // 40
        SIX_BIT_ASCII.put(41, ")"); // 41
        SIX_BIT_ASCII.put(42, "*"); // 42
        SIX_BIT_ASCII.put(43, "+"); // 43
        SIX_BIT_ASCII.put(44, ","); // 44
        SIX_BIT_ASCII.put(45, "-"); // 45
        SIX_BIT_ASCII.put(46, "."); // 46
        SIX_BIT_ASCII.put(47, "/"); // 47
        SIX_BIT_ASCII.put(48, "0"); // 48
        SIX_BIT_ASCII.put(49, "1"); // 49
        SIX_BIT_ASCII.put(50, "2"); // 50
        SIX_BIT_ASCII.put(51, "3"); // 51
        SIX_BIT_ASCII.put(52, "4"); // 52
        SIX_BIT_ASCII.put(53, "5"); // 53
        SIX_BIT_ASCII.put(54, "6"); // 54
        SIX_BIT_ASCII.put(55, "7"); // 55
        SIX_BIT_ASCII.put(56, "8"); // 56
        SIX_BIT_ASCII.put(57, "9"); // 57
        SIX_BIT_ASCII.put(58, ":"); // 58
        SIX_BIT_ASCII.put(59, ";"); // 59
        SIX_BIT_ASCII.put(60, "<"); // 60
        SIX_BIT_ASCII.put(61, "="); // 61
        SIX_BIT_ASCII.put(62, ">"); // 62
        SIX_BIT_ASCII.put(63, "?"); // 63
    }

    private static final Boolean STRIP_ALPHA_SIGNS = true;

    public static final Function<String, Integer> UNSIGNED_INTEGER_DECODER = bitString -> Integer.parseUnsignedInt(bitString, 2);

    public static final Function<String, Integer> INTEGER_DECODER = bitString -> {
        Integer value;
        String signBit = bitString.substring(0, 1);
        String valueBits = bitString.substring(1);
        if ("0".equals(signBit))
            value = UNSIGNED_INTEGER_DECODER.apply(valueBits);
        else {
            valueBits = valueBits.replaceAll("0", "x");
            valueBits = valueBits.replaceAll("1", "0");
            valueBits = valueBits.replaceAll("x", "1");
            value = -1 - UNSIGNED_INTEGER_DECODER.apply(valueBits);
        }
        return value;
    };

    public static final Function<String, Float> FLOAT_DECODER = bitString -> Float.valueOf(INTEGER_DECODER.apply(bitString));

    public static final Function<String, Boolean> BOOLEAN_DECODER = bitString -> "1".equals(bitString.substring(0, 1));

    public static final Function<String, Long> UNSIGNED_LONG_DECODER = bitString -> Long.parseUnsignedLong(bitString, 2);

    public static final Function<String, Float> UNSIGNED_FLOAT_DECODER = bitString -> Float.valueOf(UNSIGNED_INTEGER_DECODER.apply(bitString));

    @Deprecated
    public static final Function<String, String> TIME_DECODER = bitString -> {
        Integer month = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(0, 4));
        Integer day = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(4, 9));
        Integer hour = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(9, 14));
        Integer minute = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(14, 20));

        return "%02d-%02d %02d:%02d".formatted(day, month, hour, minute);
    };

    public static final Function<String, String> STRING_DECODER = bitString -> {
        StringBuilder stringBuffer = new StringBuilder();
        String remainingBits = bitString;
        while (remainingBits.length() >= 6) {
            String b = remainingBits.substring(0, 6);
            remainingBits = remainingBits.substring(6);
            Integer i = UNSIGNED_INTEGER_DECODER.apply(b);
            String c = SIX_BIT_ASCII.get(i);
            stringBuffer.append(c);
        }
        String string = stringBuffer.toString();
        if (STRIP_ALPHA_SIGNS) {
            string = string.replaceAll("@", " ").trim();
        }
        return string;
    };

    public static final Function<String, String> BIT_DECODER = bitString -> bitString;

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
