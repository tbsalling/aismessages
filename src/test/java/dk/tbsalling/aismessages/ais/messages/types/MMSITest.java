package dk.tbsalling.aismessages.ais.messages.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MMSITest {

    @Test
    void intValue() {
        assertEquals(123456789, new MMSI(123456789).getMmsi());
        assertEquals(999999999, new MMSI(999999999).getMmsi());
        assertEquals(0, new MMSI(0).getMmsi());
    }

    @Test
    void as9DigitString() {
        assertEquals("000000000", new MMSI(0).as9DigitString());
        assertEquals("000123456", new MMSI(123456).as9DigitString());
        assertThrows(IllegalStateException.class, () -> new MMSI(1234567890).as9DigitString());
    }

    @Test
    void as9DigitStringLenient() {
        assertEquals("000000000", new MMSI(0).as9DigitStringLenient());
        assertEquals("000123456", new MMSI(123456).as9DigitStringLenient());
        assertEquals("1234567890", new MMSI(1234567890).as9DigitStringLenient());
    }

}