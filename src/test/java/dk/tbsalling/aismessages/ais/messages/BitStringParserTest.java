package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.BitStringParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitStringParserTest {

    @Test
    public void canCreateParser() {
        // Arrange
        String bitString = "000001000010000011";

        // Act
        BitStringParser parser = new BitStringParser(bitString);

        // Assert
        assertNotNull(parser);
        assertEquals(18, parser.getLength());
    }

    @Test
    public void canGetBits() {
        // Arrange
        String bitString = "000001000010000011";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        String bits = parser.getBits(0, 6);

        // Assert
        assertEquals("000001", bits);
    }

    @Test
    public void canGetUnsignedInt() {
        // Arrange
        // Binary: 000001 = 1
        String bitString = "000001000010000011";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        int value = parser.getUnsignedInt(0, 6);

        // Assert
        assertEquals(1, value);
    }

    @Test
    public void canGetSignedInt() {
        // Arrange
        // Test with a negative value in two's complement
        // Using 8 bits: 11111111 = -1 in two's complement
        String bitString = "11111111";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        int value = parser.getSignedInt(0, 8);

        // Assert
        assertEquals(-1, value);
    }

    @Test
    public void canGetBoolean() {
        // Arrange
        String bitString = "1";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        boolean value = parser.getBoolean(0, 1);

        // Assert
        assertTrue(value);
    }

    @Test
    public void canGetFloat() {
        // Arrange
        // Binary: 000001 = 1
        String bitString = "000001";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        float value = parser.getUnsignedFloat(0, 6);

        // Assert
        assertEquals(1.0f, value);
    }

    @Test
    public void canGetString() {
        // Arrange
        // AIS 6-bit ASCII for "TEST": T=20(010100), E=5(000101), S=19(010011), T=20(010100)
        String bitString = "010100000101010011010100";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        String value = parser.getString(0, 24);

        // Assert
        assertEquals("TEST", value);
    }

    @Test
    public void handlesBitStuffingForShortBitString() {
        // Arrange
        String bitString = "0001";
        BitStringParser parser = new BitStringParser(bitString);

        // Act - request more bits than available
        String bits = parser.getBits(0, 10);

        // Assert - should pad with zeros
        assertEquals("0001000000", bits);
    }

    @Test
    public void canGetBitPattern() {
        // Arrange
        String bitString = "10101010";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        String pattern = parser.getBitPattern(0, 8);

        // Assert
        assertEquals("10101010", pattern);
    }

    @Test
    public void canGetBitStringBackFromParser() {
        // Arrange
        String originalBitString = "000001000010000011";
        BitStringParser parser = new BitStringParser(originalBitString);

        // Act
        String returnedBitString = parser.getBitString().toString();

        // Assert
        assertEquals(originalBitString, returnedBitString);
    }

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
    public void getBits_emptyRangeReturnsEmptyString() {
        BitStringParser parser = new BitStringParser("101010");
        assertEquals("", parser.getBits(2, 2));
    }

    @Test
    public void getBits_middleRange() {
        BitStringParser parser = new BitStringParser("1100110011");
        // indices 2..8 -> 001100
        assertEquals("001100", parser.getBits(2, 8));
    }

    @Test
    public void getBits_zeroPaddingFromMiddle() {
        // Original has 3 bits; request 1..6 should pad to length 6 then slice
        BitStringParser parser = new BitStringParser("101"); // pad -> 101000
        assertEquals("01000", parser.getBits(1, 6));
    }

    @Test
    public void getUnsignedInt_variousSlices() {
        BitStringParser parser = new BitStringParser("1011100101");
        // 0..4 -> 1011(11?) actually 0..4 is 4 bits: 1011 = 11
        assertEquals(11, parser.getUnsignedInt(0, 4));
        // 4..10 -> 100101 = 37
        assertEquals(37, parser.getUnsignedInt(4, 10));
        // whole -> 10 bits 1011100101 = 741
        assertEquals(741, parser.getUnsignedInt(0, 10));
    }

    @Test
    public void getSignedInt_variousWidths() {
        // 4-bit signed values
        BitStringParser p4 = new BitStringParser("1111");
        assertEquals(-1, p4.getSignedInt(0, 4));
        BitStringParser p8 = new BitStringParser("10000000");
        assertEquals(-128, p8.getSignedInt(0, 8));
        BitStringParser pMix = new BitStringParser("011111110001010");
        // 0..8 -> 01111111 = 127
        assertEquals(127, pMix.getSignedInt(0, 8));
        // 8..15 -> 0001010 (7 bits) = +10
        assertEquals(10, pMix.getSignedInt(8, 15));
    }

    @Test
    public void floats_signedAndUnsignedFollowIntSemantics() {
        BitStringParser parser = new BitStringParser("11110001");
        // 0..4 -> 1111 => -1 as signed float
        assertEquals(-1.0f, parser.getSignedFloat(0, 4));
        // 4..8 -> 0001 => 1.0 as unsigned float
        assertEquals(1.0f, parser.getUnsignedFloat(4, 8));
    }

    @Test
    public void boolean_firstBitOnlyEvenWithLongerSlice() {
        BitStringParser parser = new BitStringParser("0110");
        // slice 1..3 gives "11" -> true (first bit is 1)
        assertTrue(parser.getBoolean(1, 3));
        // slice 2..4 gives "10" -> true
        assertTrue(parser.getBoolean(2, 4));
        // slice 0..2 gives "01" -> false
        assertFalse(parser.getBoolean(0, 2));
    }

    @Test
    public void getString_atSignsTrimAndSpacesPreservedBetweenWords() {
        // @ -> 0; H=8; E=5; L=12; O=15
        String bits = sixBits(0, 8, 5, 12, 12, 15, 0); // "@HELLO@" -> "HELLO"
        BitStringParser parser = new BitStringParser(bits);
        assertEquals("HELLO", parser.getString(0, bits.length()));

        // Embedding @ as space between words should be preserved
        String bits2 = sixBits(8, 5, 12, 0, 12, 15); // H E L @ L O -> "HEL LO"
        BitStringParser parser2 = new BitStringParser(bits2);
        assertEquals("HEL LO", parser2.getString(0, bits2.length()));
    }

    @Test
    public void getString_ignoresLeftoverBitsNotMultipleOf6() {
        // "AB" = 1,2 -> 12 bits; add three leftover bits "101" which should be ignored
        String bits = sixBits(1, 2) + "101";
        BitStringParser parser = new BitStringParser(bits);
        assertEquals("AB", parser.getString(0, bits.length()));
    }

    @Test
    public void integration_readsAcrossDifferentFields() {
        // signed -1 (4b), string "ABC" (18b), unsigned int 1 (4b), boolean (1b)
        String combo = "1111" + sixBits(1, 2, 3) + "0001" + "1";
        BitStringParser parser = new BitStringParser(combo);
        assertEquals(-1, parser.getSignedInt(0, 4));
        assertEquals("ABC", parser.getString(4, 22));
        assertEquals(1, parser.getUnsignedInt(22, 26));
        assertTrue(parser.getBoolean(26, 27));
        assertEquals("1111", parser.getBitPattern(0, 4));
        assertEquals(combo.length(), parser.getLength());
    }

}
