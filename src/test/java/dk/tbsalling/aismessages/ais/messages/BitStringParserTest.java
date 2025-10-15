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
    public void throwsNullPointerExceptionForNullBitString() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new BitStringParser(null));
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
        String returnedBitString = parser.getBitString();

        // Assert
        assertEquals(originalBitString, returnedBitString);
    }

    @Test
    public void toStringShowsLength() {
        // Arrange
        String bitString = "000001000010000011";
        BitStringParser parser = new BitStringParser(bitString);

        // Act
        String result = parser.toString();

        // Assert
        assertTrue(result.contains("18"));
        assertTrue(result.contains("bits"));
    }
}
