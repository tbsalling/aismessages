package dk.tbsalling.aismessages.ais;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the BitString class.
 */
public class BitStringTest {

    @Test
    public void canCreateBitStringFromString() {
        // Arrange
        String bitStr = "10101010";

        // Act
        BitString bitString = new BitString(bitStr);

        // Assert
        assertNotNull(bitString);
        assertEquals(8, bitString.length());
    }

    @Test
    public void canConvertBitStringToString() {
        // Arrange
        String original = "11001100";
        BitString bitString = new BitString(original);

        // Act
        String result = bitString.toString();

        // Assert
        assertEquals(original, result);
    }

    @Test
    public void canGetBitAtIndex() {
        // Arrange
        String bitStr = "10101010";
        BitString bitString = new BitString(bitStr);

        // Act & Assert
        assertTrue(bitString.get(0));
        assertFalse(bitString.get(1));
        assertTrue(bitString.get(2));
        assertFalse(bitString.get(3));
        assertTrue(bitString.get(4));
        assertFalse(bitString.get(5));
        assertTrue(bitString.get(6));
        assertFalse(bitString.get(7));
    }

    @Test
    public void canGetCharAtIndex() {
        // Arrange
        String bitStr = "10101010";
        BitString bitString = new BitString(bitStr);

        // Act & Assert
        assertEquals('1', bitString.charAt(0));
        assertEquals('0', bitString.charAt(1));
        assertEquals('1', bitString.charAt(2));
        assertEquals('0', bitString.charAt(3));
    }

    @Test
    public void canExtractSubstring() {
        // Arrange
        String bitStr = "11110000";
        BitString bitString = new BitString(bitStr);

        // Act
        BitString substring = bitString.substring(0, 4);

        // Assert
        assertEquals(4, substring.length());
        assertEquals("1111", substring.toString());
    }

    @Test
    public void substringPadsWithZerosWhenNeeded() {
        // Arrange
        String bitStr = "111";
        BitString bitString = new BitString(bitStr);

        // Act
        BitString substring = bitString.substring(0, 6);

        // Assert
        assertEquals(6, substring.length());
        assertEquals("111000", substring.toString());
    }

    @Test
    public void emptySubstringWorks() {
        // Arrange
        BitString bitString = new BitString("10101010");

        // Act
        BitString empty = bitString.substring(2, 2);

        // Assert
        assertEquals(0, empty.length());
        assertEquals("", empty.toString());
    }

    @Test
    public void substringFromMiddle() {
        // Arrange
        String bitStr = "1100110011";
        BitString bitString = new BitString(bitStr);

        // Act
        BitString substring = bitString.substring(2, 8);

        // Assert
        assertEquals(6, substring.length());
        assertEquals("001100", substring.toString());
    }

    @Test
    public void rejectsInvalidCharacters() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new BitString("10201010"));
        
        assertTrue(ex.getMessage().contains("Invalid character"));
    }

    @Test
    public void rejectsNullInput() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new BitString(null));
        
        assertTrue(ex.getMessage().contains("cannot be null"));
    }

    @Test
    public void equalityWorks() {
        // Arrange
        BitString bs1 = new BitString("10101010");
        BitString bs2 = new BitString("10101010");
        BitString bs3 = new BitString("10101011");

        // Act & Assert
        assertEquals(bs1, bs2);
        assertNotEquals(bs1, bs3);
        assertEquals(bs1.hashCode(), bs2.hashCode());
    }

    @Test
    public void handlesEmptyString() {
        // Arrange
        BitString bitString = new BitString("");

        // Act & Assert
        assertEquals(0, bitString.length());
        assertEquals("", bitString.toString());
    }

    @Test
    public void handlesLongBitStrings() {
        // Arrange - Create a long bit string (common in AIS messages which can be 168+ bits)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append(i % 2 == 0 ? '1' : '0');
        }
        String longBits = sb.toString();

        // Act
        BitString bitString = new BitString(longBits);

        // Assert
        assertEquals(200, bitString.length());
        assertEquals(longBits, bitString.toString());
    }

    @Test
    public void getReturnsZeroForOutOfBoundsIndex() {
        // Arrange
        BitString bitString = new BitString("111");

        // Act & Assert
        assertFalse(bitString.get(10)); // Out of bounds returns false (0)
        assertFalse(bitString.get(-1)); // Negative index returns false (0)
    }
}
