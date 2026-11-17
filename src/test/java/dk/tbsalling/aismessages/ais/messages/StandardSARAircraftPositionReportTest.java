package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.SyncState;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;
import dk.tbsalling.aismessages.ais.messages.types.*;

import static org.junit.jupiter.api.Assertions.*;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;

class StandardSARAircraftPositionReportTest {

    @Test
    void canDecode10DigitMMSI() {
        // Arrange
        String nmea = "!AIVDM,1,1,,A,9>rAUn00GiU7gi<COH913Pu:0@6:,0*4B";

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, new NMEAMessage(nmea));
        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardSARAircraftPositionReport, aisMessage.getMessageType());
        StandardSARAircraftPositionReport message = (StandardSARAircraftPositionReport) aisMessage;

        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(1000629720), message.getSourceMmsi());
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
        assertTrue(message.isBarometricAltitude());
        assertFalse(message.isCommStateSelectorFlag());
        SOTDMACommunicationState communicationState = (SOTDMACommunicationState) message.getCommunicationState();
        assertEquals(SyncState.UTCDirect, communicationState.getSyncState());
        assertEquals(4, communicationState.getSlotTimeout());
        assertEquals(394, communicationState.getSlotNumber());
    }

    /** ITDMA communication state (selector 1) and GNSS altitude. ITU-R M.1371-6 Table 57. Synthetic sentence built from the spec layout. */
    @Test
    public void canDecodeWithItdmaCommunicationState() {
        Bits b = bits().u(9, 6).u(0, 2).u(111219001, 30).u(1500, 12).u(140, 10).b(false).s(6450000, 28).s(33300000, 27).u(2700, 12).u(30, 6)
                .b(false).u(0, 7).b(false).u(0, 3).b(true).b(false).b(true)
                .u(1, 2).u(4500, 13).u(2, 3).b(true);
        assertEquals(168, b.length());

        StandardSARAircraftPositionReport message = (StandardSARAircraftPositionReport) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(1500, message.getAltitude());
        assertFalse(message.isBarometricAltitude());
        assertTrue(message.isAssigned());
        assertTrue(message.isCommStateSelectorFlag());
        ITDMACommunicationState communicationState = (ITDMACommunicationState) message.getCommunicationState();
        assertEquals(SyncState.UTCIndirect, communicationState.getSyncState());
        assertEquals(4500, communicationState.getSlotIncrement());
        assertEquals(2, communicationState.getNumberOfSlots());
        assertTrue(communicationState.getKeepFlag());
    }
}
