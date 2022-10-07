package dk.tbsalling.aismessages.ais.messages.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MMSITest {

    @Test
    void intValue() {
        assertEquals(123456789, MMSI.valueOf(123456789).intValue());
        assertEquals(999999999, MMSI.valueOf(999999999).intValue());
        assertEquals(0, MMSI.valueOf(0).intValue());
    }

    @Test
    void as9DigitString() {
        assertEquals("000000000", MMSI.valueOf(0).as9DigitString());
        assertEquals("000123456", MMSI.valueOf(123456).as9DigitString());
        assertThrows(IllegalStateException.class, () -> MMSI.valueOf(1234567890).as9DigitString());
    }

}