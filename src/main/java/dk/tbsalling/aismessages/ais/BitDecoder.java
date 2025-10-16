package dk.tbsalling.aismessages.ais;

import java.util.Map;
import java.util.TreeMap;

public class BitDecoder {

    public static final BitDecoder INSTANCE = new BitDecoder();

    public int decodeUnsignedInt(String bitString) {
        return Integer.parseUnsignedInt(bitString, 2);
    }

    public int decodeSignedInt(String bitString) {
        int n = bitString.length();
        int x = Integer.parseUnsignedInt(bitString, 2);
        if ((x & (1 << (n - 1))) != 0) {
            x -= (1 << n); // two's complement sign extension within n bits
        }
        return x;
    }

    public float decodeFloat(String bitString) {
        return (float) decodeSignedInt(bitString);
    }

    public boolean decodeBoolean(String bitString) {
        return bitString.charAt(0) == '1';
    }

    public long decodeUnsignedLong(String bitString) {
        return Long.parseUnsignedLong(bitString, 2);
    }

    public float decodeUnsignedFloat(String bitString) {
        return (float) decodeUnsignedInt(bitString);
    }

    @Deprecated
    public String decodeTime(String bitString) {
        Integer month = decodeUnsignedInt(bitString.substring(0, 4));
        Integer day = decodeUnsignedInt(bitString.substring(4, 9));
        Integer hour = decodeUnsignedInt(bitString.substring(9, 14));
        Integer minute = decodeUnsignedInt(bitString.substring(14, 20));
        return "%02d-%02d %02d:%02d".formatted(day, month, hour, minute);
    }

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

    public String decodeBits(String bitString) {
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
