package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardSARAircraftPositionReportTest {

    @Test
    void canDecode10DigitMMSI() {
        // Arrange
        String nmea = "!AIVDM,1,1,,A,9>rAUn00GiU7gi<COH913Pu:0@6:,0*4B";

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, new NMEAMessage(nmea));
        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardSARAircraftPositionReport, aisMessage.getMessageType());
        StandardSARAircraftPositionReport message = (StandardSARAircraftPositionReport) aisMessage;

        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(1000629720), message.getSourceMmsi());
        assertEquals(Integer.valueOf(1), message.getAltitude());
        assertEquals(Float.valueOf(497.0f), message.getSpeedOverGround());
        assertTrue(message.isPositionAccuracy());
        assertEquals(Float.valueOf(71.6f), message.getLongitude(), 0.1f);
        assertEquals(Float.valueOf(34.1f), message.getLatitude(), 0.1f);
        assertEquals(Float.valueOf(27.0f), message.getCourseOverGround());
        assertEquals(Integer.valueOf(3), message.getSecond());
        assertEquals("11010010", message.getRegionalReserved());
        assertTrue(message.isDataTerminalReady());
        assertFalse(message.isAssigned());
        assertEquals(Boolean.FALSE, message.isRaimFlag());
        assertEquals("00010000000110001010", message.getRadioStatus());
    }

}
