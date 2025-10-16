package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessageFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AISMessageFactoryTest {

    @Test
    public void toBitString_basicMappingAndPaddingZero() {
        // Arrange
        String input = "01A"; // 0 -> 000000, 1 -> 000001, A -> 010001
        int padding = 0;

        // Act
        String result = AISMessageFactory.toBitString(input, padding);

        // Assert
        assertEquals("000000000001010001", result);
        assertEquals(18, result.length()); // 3 * 6 - 0 = 18
    }

    @Test
    public void toBitString_paddingRemovesTrailingBits() {
        // Arrange
        String input = "01A";
        int padding = 5;
        String expectedFull = "000000000001010001"; // from input above with 0 padding
        String expected = expectedFull.substring(0, expectedFull.length() - padding);

        // Act
        String padded = AISMessageFactory.toBitString(input, padding);

        // Assert
        assertEquals(expected, padded); // Padding 5 should remove last 5 bits
        assertEquals(13, padded.length());
    }

    @Test
    public void toBitString_paddingBounds() {
        // Arrange
        String input = "0";

        // Act + Assert: valid bounds 0..5
        for (int p = 0; p <= 5; p++) {
            String r = AISMessageFactory.toBitString(input, p);
            // Assert
            assertEquals(6 - p, r.length());
        }

        // Arrange for invalid padding values
        int negativePadding = -1;
        int tooLargePadding = 6;

        // Act + Assert: out of range negative
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> AISMessageFactory.toBitString(input, negativePadding));
        assertTrue(ex1.getMessage().contains("0..5"));

        // Act + Assert: out of range >5
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> AISMessageFactory.toBitString(input, tooLargePadding));
        assertTrue(ex2.getMessage().contains("0..5"));

    }

    @Test
    public void toBitString_invalidCharacterThrows() {
        // Arrange
        String input = "X"; // 'X' is not part of the supported 6-bit AIS alphabet in this mapping
        int padding = 0;

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> AISMessageFactory.toBitString(input, padding));

        // Assert
        assertTrue(ex.getMessage().contains("Invalid six-bit character"));
    }

    @Test
    public void toBitString_nullEncodedStringThrows() {
        // Arrange
        String input = null;
        int padding = 0;

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> AISMessageFactory.toBitString(input, padding));

        // Assert
        assertTrue(ex.getMessage().contains("cannot be null"));
    }

    @Test
    public void toBitString_variousKnownMappings() {
        // Arrange
        String colon = ":";
        String semicolon = ";";
        String upperO = "O";
        String upperP = "P";
        String lowerW = "w";

        // Act
        String colonBits = AISMessageFactory.toBitString(colon, 0);
        String semicolonBits = AISMessageFactory.toBitString(semicolon, 0);
        String upperOBits = AISMessageFactory.toBitString(upperO, 0);
        String upperPBits = AISMessageFactory.toBitString(upperP, 0);
        String lowerWBits = AISMessageFactory.toBitString(lowerW, 0);

        // Assert
        assertEquals("001010", colonBits);
        assertEquals("001011", semicolonBits);
        assertEquals("011111", upperOBits);
        assertEquals("100000", upperPBits);
        assertEquals("111111", lowerWBits);
    }
}