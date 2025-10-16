package dk.tbsalling.aismessages.ais;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitDecoderTest {

    private final BitDecoder decoder = BitDecoder.INSTANCE;

    // Helper to build a 6-bit ASCII bitstring from integer codes
    private static String sixBits(int... codes) {
        StringBuilder sb = new StringBuilder();
        for (int c : codes) {
            String bits = Integer.toBinaryString(c & 0x3F);
            sb.append("0".repeat(6 - bits.length())).append(bits);
        }
        return sb.toString();
    }

    @Test
    public void decodeUnsignedInt_basicAndBounds() {
        assertEquals(0, decoder.decodeUnsignedInt("0"));
        assertEquals(1, decoder.decodeUnsignedInt("1"));
        assertEquals(2, decoder.decodeUnsignedInt("10"));
        assertEquals(3, decoder.decodeUnsignedInt("11"));
        // 8-bit max
        assertEquals(255, decoder.decodeUnsignedInt("11111111"));
        // 16-bit sample
        assertEquals(0xBEEF, decoder.decodeUnsignedInt("1011111011101111"));
    }

    @Test
    public void decodeSignedInt_twoComplementVariousWidths() {
        // 4-bit range: -8..7
        assertEquals(-8, decoder.decodeSignedInt("1000"));
        assertEquals(-1, decoder.decodeSignedInt("1111"));
        assertEquals(0, decoder.decodeSignedInt("0000"));
        assertEquals(7, decoder.decodeSignedInt("0111"));
        // 8-bit range: -128..127
        assertEquals(-128, decoder.decodeSignedInt("10000000"));
        assertEquals(-1, decoder.decodeSignedInt("11111111"));
        assertEquals(127, decoder.decodeSignedInt("01111111"));
        assertEquals(42, decoder.decodeSignedInt("00101010"));
    }

    @Test
    public void decodeUnsignedLong_basicAndLarge() {
        assertEquals(0L, decoder.decodeUnsignedLong("0"));
        assertEquals(1L, decoder.decodeUnsignedLong("1"));
        assertEquals(2L, decoder.decodeUnsignedLong("10"));
        assertEquals(3L, decoder.decodeUnsignedLong("11"));
        // 32-bit sample
        assertEquals(0xCAFEBABEL, decoder.decodeUnsignedLong("11001010111111101011101010111110"));
    }

    @Test
    public void decodeFloat_andUnsignedFloat_matchIntSemantics() {
        // Signed float is just signed int cast to float
        assertEquals(0.0f, decoder.decodeFloat("0000"));
        assertEquals(1.0f, decoder.decodeFloat("0001"));
        assertEquals(-1.0f, decoder.decodeFloat("1111"));
        assertEquals(-8.0f, decoder.decodeFloat("1000"));

        // Unsigned float mirrors unsigned int
        assertEquals(0.0f, decoder.decodeUnsignedFloat("0000"));
        assertEquals(1.0f, decoder.decodeUnsignedFloat("0001"));
        assertEquals(15.0f, decoder.decodeUnsignedFloat("1111"));
    }

    @Test
    public void decodeBoolean_firstBitOnly() {
        assertTrue(decoder.decodeBoolean("1"));
        assertFalse(decoder.decodeBoolean("0"));
        // Longer strings: only first bit matters
        assertTrue(decoder.decodeBoolean("10"));
        assertFalse(decoder.decodeBoolean("01"));
    }

    @Test
    public void getBitString_identity() {
        assertEquals("", decoder.getBitString(""));
        assertEquals("101010", decoder.getBitString("101010"));
    }

    @Test
    public void decodeString_6bitAscii_basic() {
        // A=1, B=2, C=3 -> "ABC"
        assertEquals("ABC", decoder.decodeString(sixBits(1, 2, 3)));
        // Digits 0..9 are 48..57
        assertEquals("0123456789", decoder.decodeString(sixBits(48, 49, 50, 51, 52, 53, 54, 55, 56, 57)));
        // Punctuation sample
        assertEquals("-./:", decoder.decodeString(sixBits(45, 46, 47, 58)));
    }

    @Test
    public void decodeString_stripsAtSignsAndTrims() {
        // Single '@' becomes space then trimmed -> empty string
        assertEquals("", decoder.decodeString(sixBits(0)));
        // Leading and trailing '@' are turned into spaces and then trimmed
        assertEquals("HELLO", decoder.decodeString(sixBits(0, 8, 5, 12, 12, 15, 0))); // @HELLO@
        // Embedded '@' becomes a space which is preserved between words
        assertEquals("HEL LO", decoder.decodeString(sixBits(8, 5, 12, 0, 12, 15)));
        // Multiple internal '@' become single spaces respectively
        assertEquals("A  B", decoder.decodeString(sixBits(1, 0, 0, 2)));
    }

    @Test
    public void decodeTime_parsesComponents() {
        // month(4) day(5) hour(5) minute(6)
        int month = 12;   // 1100
        int day = 31;     // 11111
        int hour = 23;    // 10111
        int minute = 59;  // 111011
        String bits =
                String.format("%4s", Integer.toBinaryString(month)).replace(' ', '0') +
                        String.format("%5s", Integer.toBinaryString(day)).replace(' ', '0') +
                        String.format("%5s", Integer.toBinaryString(hour)).replace(' ', '0') +
                        String.format("%6s", Integer.toBinaryString(minute)).replace(' ', '0');
        assertEquals("31-12 23:59", decoder.decodeTime(bits));
    }

    @Test
    public void integration_withBitStringParser_accessors() {
        // Build "ABC" (1,2,3) and some numbers around it
        String bits = "1111" + sixBits(1, 2, 3) + "0001"; // signed -1, string ABC, unsigned 1
        BitStringParser parser = new BitStringParser(bits);
        assertEquals(-1, parser.getSignedInt(0, 4));
        assertEquals("ABC", parser.getString(4, 22));
        assertEquals(1.0f, parser.getUnsignedFloat(22, 26));
        assertEquals("1111", parser.getBitPattern(0, 4));
        assertEquals(bits.length(), parser.getLength());
        assertEquals(bits, parser.getBitString().toString());
    }
}
