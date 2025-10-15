package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StandardClassBCSPositionReportTest {

    @Test
    public void canDecode1() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,B5NJ;PP005l4ot5Isbl03wsUkP06,0*76");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(367430530), message.getSourceMmsi());
        assertEquals("00000000", message.getRegionalReserved1());
        assertEquals(0.0f, message.getSpeedOverGround(), 0.0f);
        assertEquals(0, message.getRawSpeedOverGround());
        assertFalse(message.isPositionAccuracy());
        assertEquals(37.785034f, message.getLatitude(), 0.0f);
        assertEquals(22671021, message.getRawLatitude());
        assertEquals(-122.26732f, message.getLongitude(), 0.0f);
        assertEquals(-73360392, message.getRawLongitude());
        assertEquals(0.0f, message.getCourseOverGround(), 0.0f);
        assertEquals(0, message.getRawCourseOverGround());
        assertEquals(511, message.getTrueHeading());
        assertEquals(55, message.getSecond());
        assertEquals("00", message.getRegionalReserved2());
        assertTrue(message.isCsUnit());
        assertFalse(message.isDisplay());
        assertTrue(message.isDsc());
        assertTrue(message.isBand());
        assertTrue(message.isMessage22());
        assertFalse(message.isAssigned());
        assertFalse(message.isRaimFlag());
        //assertEquals("11100000000000000110", message.getRadioStatus());
    }

    @Test
    public void canDecode2() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,B>1VUFP00vK`auV0eUulKwv0RJGT,0*09");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(941204826), message.getSourceMmsi());
        assertEquals("00000000", message.getRegionalReserved1());
        assertEquals(0.3f, message.getSpeedOverGround(), 0.0f);
        assertTrue(message.isPositionAccuracy());
        assertEquals(42.020855f, message.getLatitude(), 0.0f);
        assertEquals(-87.70006f, message.getLongitude(), 0.0f);
        assertEquals(186.2f, message.getCourseOverGround(), 0.0f);
        assertEquals(511, message.getTrueHeading());
        assertEquals(60, message.getSecond());
        assertEquals("00", message.getRegionalReserved2());
        assertFalse(message.isCsUnit());
        assertFalse(message.isDisplay());
        assertFalse(message.isDsc());
        assertTrue(message.isBand());
        assertFalse(message.isMessage22());
        assertFalse(message.isAssigned());
        assertFalse(message.isRaimFlag());
    }

    @Test
    public void canDecodeITDMACommunicationState() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,B6CdCm0t3`tba35f@V9faHi7kP06,0*58");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(423302100), message.getSourceMmsi());
        assertEquals("00001111", message.getRegionalReserved1());
        assertEquals(1.4f, message.getSpeedOverGround(), 0.0f);
        assertTrue(message.isPositionAccuracy());
        assertEquals(40.005283f, message.getLatitude(), 0.0f);
        assertEquals(53.010998f, message.getLongitude(), 0.0f);
        assertEquals(177f, message.getCourseOverGround(), 0.0f);
        assertEquals(177, message.getTrueHeading());
        assertEquals(34, message.getSecond());
        assertEquals("00", message.getRegionalReserved2());
        assertTrue(message.isCsUnit());
        assertTrue(message.isDisplay());
        assertTrue(message.isDsc());
        assertTrue(message.isBand());
        assertTrue(message.isMessage22());
        assertFalse(message.isAssigned());
        assertFalse(message.isRaimFlag());
        assertTrue(message.isCommStateSelectorFlag());

        CommunicationState communicationState = message.getCommunicationState();   // 1100000000000000110b = 3, slot incr = 6
        assertEquals(SyncState.BaseIndirect, communicationState.getSyncState());
        assertTrue(communicationState instanceof ITDMACommunicationState);
        ITDMACommunicationState itdmaCommunicationState = (ITDMACommunicationState) communicationState;
        assertEquals(0, itdmaCommunicationState.getSlotIncrement());
        assertEquals(3, itdmaCommunicationState.getNumberOfSlots());
        assertFalse(itdmaCommunicationState.getKeepFlag());
    }
}
