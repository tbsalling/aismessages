package dk.tbsalling.aismessages.ais;

import java.util.Map;
import java.util.TreeMap;

/**
 * BitDecoder provides low-level decoders for interpreting AIS payload bit strings.
 * It converts fixed-width binary substrings to Java primitives and AIS 6-bit ASCII text.
 * <p>
 * All methods expect inputs to be strings consisting only of '0' and '1'. Range handling for
 * signed values follows two's complement over the width of the provided bitString.
 * <p>
 * This class is stateless and exposes a singleton INSTANCE for convenience. It is used by
 * BitStringParser and the various AIS message parsers.
 *
 * @author tbsalling
 */
public class BitDecoder {

    /**
     * Singleton instance of the BitDecoder.
     */
    public static final BitDecoder INSTANCE = new BitDecoder();

    /**
     * Decodes the given bit string as an unsigned binary integer.
     *
     * @param bitString a sequence of '0' and '1' characters (most significant bit first)
     * @return the value parsed as an unsigned int
     * @throws NumberFormatException if the string is empty or contains characters other than '0' or '1'
     */
    public int decodeUnsignedInt(String bitString) {
        return Integer.parseUnsignedInt(bitString, 2);
    }

    /**
     * Decodes the given bit string as a signed two's-complement integer with the width equal to
     * the length of the provided bitString.
     * <p>
     * If the most significant bit is 1, the value is interpreted as negative by subtracting 2^n,
     * where n is the number of bits in the string.
     *
     * @param bitString a sequence of '0' and '1' characters
     * @return the decoded signed int value
     * @throws NumberFormatException if the string is empty or contains invalid characters
     */
    public int decodeSignedInt(String bitString) {
        int n = bitString.length();
        int x = Integer.parseUnsignedInt(bitString, 2);
        if ((x & (1 << (n - 1))) != 0) {
            x -= (1 << n); // two's complement sign extension within n bits
        }
        return x;
    }

    /**
     * Decodes the given bit string as a signed integer and returns it as a float.
     * <p>
     * This is a convenience for fields that are defined as signed integers but are
     * later scaled to fractional values.
     *
     * @param bitString a sequence of '0' and '1' characters
     * @return the signed value as a float
     */
    public float decodeFloat(String bitString) {
        return (float) decodeSignedInt(bitString);
    }

    /**
     * Decodes the first bit as a boolean value.
     *
     * @param bitString a sequence of '0' and '1' characters; only the first bit is considered
     * @return true if the first bit is '1', otherwise false
     */
    public boolean decodeBoolean(String bitString) {
        return bitString.charAt(0) == '1';
    }

    /**
     * Decodes the given bit string as an unsigned long integer.
     *
     * @param bitString a sequence of '0' and '1' characters (up to 64 bits)
     * @return the value parsed as an unsigned long
     * @throws NumberFormatException if the string is empty or contains invalid characters
     */
    public long decodeUnsignedLong(String bitString) {
        return Long.parseUnsignedLong(bitString, 2);
    }

    /**
     * Decodes the given bit string as an unsigned integer and returns it as a float.
     *
     * @param bitString a sequence of '0' and '1' characters
     * @return the unsigned value as a float
     */
    public float decodeUnsignedFloat(String bitString) {
        return (float) decodeUnsignedInt(bitString);
    }

    /**
     * Decodes a 20-bit timestamp field into a human-readable string "dd-MM HH:mm".
     * <p>
     * Layout: MMMM DDDDD HHHHH MMMMM (month 4b, day 5b, hour 5b, minute 6b).
     * Note: This helper is deprecated in favor of parsing via dedicated message fields/time classes.
     *
     * @param bitString a 20-bit string (or longer, only the first 20 bits are used)
     * @return formatted time string in the form "dd-MM HH:mm"
     */
    @Deprecated
    public String decodeTime(String bitString) {
        Integer month = decodeUnsignedInt(bitString.substring(0, 4));
        Integer day = decodeUnsignedInt(bitString.substring(4, 9));
        Integer hour = decodeUnsignedInt(bitString.substring(9, 14));
        Integer minute = decodeUnsignedInt(bitString.substring(14, 20));
        return "%02d-%02d %02d:%02d".formatted(day, month, hour, minute);
    }

    /**
     * Decodes an AIS 6-bit ASCII encoded string from the given bit string.
     * <p>
     * The input is processed in 6-bit chunks. Each chunk is mapped using the AIS 6-bit ASCII table.
     * If STRIP_ALPHA_SIGNS is enabled, the '@' filler characters are turned into spaces and the
     * result is trimmed.
     *
     * @param bitString a sequence of '0' and '1' characters; extra bits beyond a multiple of 6 are ignored
     * @return the decoded textual representation
     */
    public String decodeString(String bitString) {
        int len = bitString.length();
        StringBuilder stringBuffer = new StringBuilder(len / 6 + 1);
        for (int i = 0; i + 6 <= len; i += 6) {
            String b = bitString.substring(i, i + 6);
            Integer v = decodeUnsignedInt(b);
            String c = SIX_BIT_ASCII.get(v);
            stringBuffer.append(c);
        }
        String string = stringBuffer.toString();
        if (STRIP_ALPHA_SIGNS) {
            string = string.replace("@", " ").trim();
        }
        return string;
    }

    /**
     * Returns the bitString untouched. Useful for fields that must be carried as raw bits
     * (e.g., spare bits or regional reserved fields).
     *
     * @param bitString a sequence of '0' and '1' characters
     * @return the input bitString
     */
    public String getBitString(String bitString) {
        return bitString;
    }

    private static final boolean STRIP_ALPHA_SIGNS = true;
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

}
