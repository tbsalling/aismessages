/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011- by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.ais;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Decoders {

    public static final Function<String, Integer> INTEGER_DECODER = new Function<String, Integer>() {
        @Override
        public Integer apply(String bitString) {
            Integer value;
            String signBit = bitString.substring(0, 1);
            String valueBits = bitString.substring(1);
            if ("0".equals(signBit))
                value = UNSIGNED_INTEGER_DECODER.apply(valueBits);
            else {
                valueBits = valueBits.replaceAll("0", "x");
                valueBits = valueBits.replaceAll("1", "0");
                valueBits = valueBits.replaceAll("x", "1");
                value = UNSIGNED_INTEGER_DECODER.apply("-" + valueBits);
            }
            return value;
        }
    };

    public static final Function<String, Float> FLOAT_DECODER = bitString -> Float.valueOf(INTEGER_DECODER.apply(bitString));

    public static final Function<String, Boolean> BOOLEAN_DECODER = bitString -> "1".equals(bitString.substring(0, 1));

    public static final Function<String, Integer> UNSIGNED_INTEGER_DECODER = bitString -> Integer.parseInt(bitString, 2);

    public static final Function<String, Long> UNSIGNED_LONG_DECODER = bitString -> Long.parseLong(bitString, 2);

    public static final Function<String, Float> UNSIGNED_FLOAT_DECODER = bitString -> Float.valueOf(UNSIGNED_INTEGER_DECODER.apply(bitString));

    public static final Function<String, String> TIME_DECODER = new Function<String, String>() {
        @Override
        public String apply(String bitString) {
            Integer month = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(0, 4));
            Integer day = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(4, 9));
            Integer hour = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(9, 14));
            Integer minute = UNSIGNED_INTEGER_DECODER.apply(bitString.substring(14, 20));

            String monthAsString = month<10 ? "0"+month : ""+month;
            String dayAsString = day<10 ? "0"+day : ""+day;
            String hourAsString = hour<10 ? "0"+hour : ""+hour;
            String minuteAsString = minute<10 ? "0"+minute : ""+minute;

            return dayAsString + "-" + monthAsString + " " + hourAsString + ":" + minuteAsString;
        }
    };

    public static final Function<String, String> STRING_DECODER = new Function<String, String>() {
        @Override
        public String apply(String bitString) {
            StringBuffer stringBuffer = new StringBuffer();
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
        }
    };

    public static final Function<String, String> BIT_DECODER = bitString -> bitString;

	private final static Map<Integer, String> SIX_BIT_ASCII = new TreeMap<Integer, String>();
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

}
