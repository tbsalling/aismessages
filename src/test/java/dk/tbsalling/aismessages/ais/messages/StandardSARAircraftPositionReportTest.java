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

        assertEquals(0, message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(1000629720), message.getSourceMmsi());
        assertEquals(1, message.getAltitude());
        assertEquals(497.0f, message.getSpeedOverGround(), 0.0f);
        assertTrue(message.isPositionAccuracy());
        assertEquals(71.6f, message.getLongitude(), 0.1f);
        assertEquals(34.1f, message.getLatitude(), 0.1f);
        assertEquals(27.0f, message.getCourseOverGround(), 0.0f);
        assertEquals(3, message.getSecond());
        assertEquals("11010010", message.getRegionalReserved());
        assertTrue(message.isDataTerminalReady());
        assertFalse(message.isAssigned());
        assertEquals(Boolean.FALSE, message.isRaimFlag());
        assertEquals("00010000000110001010", message.getRadioStatus());
    }

}
