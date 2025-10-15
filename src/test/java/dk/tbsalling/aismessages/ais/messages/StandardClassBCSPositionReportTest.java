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
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(367430530), message.getSourceMmsi());
        assertEquals("00000000", message.getRegionalReserved1());
        assertEquals((Float) 0.0f, message.getSpeedOverGround());
        assertEquals((Integer) 0, message.getRawSpeedOverGround());
        assertFalse(message.isPositionAccuracy());
        assertEquals(Float.valueOf(37.785034f), message.getLatitude());
        assertEquals((Integer)22671021, message.getRawLatitude());
        assertEquals(Float.valueOf(-122.26732f), message.getLongitude());
        assertEquals((Integer)(-73360392), message.getRawLongitude());
        assertEquals(Float.valueOf(0.0f), message.getCourseOverGround());
        assertEquals((Integer) 0, message.getRawCourseOverGround());
        assertEquals((Integer) 511, message.getTrueHeading());
        assertEquals((Integer) 55, message.getSecond());
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
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(941204826), message.getSourceMmsi());
        assertEquals("00000000", message.getRegionalReserved1());
        assertEquals((Float) 0.3f, message.getSpeedOverGround());
        assertTrue(message.isPositionAccuracy());
        assertEquals(Float.valueOf(42.020855f), message.getLatitude());
        assertEquals(Float.valueOf(-87.70006f), message.getLongitude());
        assertEquals(Float.valueOf(186.2f), message.getCourseOverGround());
        assertEquals((Integer) 511, message.getTrueHeading());
        assertEquals((Integer) 60, message.getSecond());
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
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(423302100), message.getSourceMmsi());
        assertEquals("00001111", message.getRegionalReserved1());
        assertEquals((Float) 1.4f, message.getSpeedOverGround());
        assertTrue(message.isPositionAccuracy());
        assertEquals(Float.valueOf(40.005283f), message.getLatitude());
        assertEquals(Float.valueOf(53.010998f), message.getLongitude());
        assertEquals(Float.valueOf(177f), message.getCourseOverGround());
        assertEquals((Integer) 177, message.getTrueHeading());
        assertEquals((Integer) 34, message.getSecond());
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
        assertEquals(Integer.valueOf(0), itdmaCommunicationState.getSlotIncrement());
        assertEquals(Integer.valueOf(3), itdmaCommunicationState.getNumberOfSlots());
        assertFalse(itdmaCommunicationState.getKeepFlag());
    }
}
