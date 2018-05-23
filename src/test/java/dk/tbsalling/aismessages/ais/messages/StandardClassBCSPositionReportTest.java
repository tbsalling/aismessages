package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.CommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.ITDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.SyncState;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StandardClassBCSPositionReportTest {

    @Test
    public void canDecode1() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,B5NJ;PP005l4ot5Isbl03wsUkP06,0*76"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(367430530), message.getSourceMmsi());
        assertEquals("00000000", message.getRegionalReserved1());
        assertEquals((Float) 0.0f, message.getSpeedOverGround());
        assertFalse(message.getPositionAccurate());
        assertEquals(Float.valueOf(37.785034f), message.getLatitude());
        assertEquals(Float.valueOf(-122.26732f), message.getLongitude());
        assertEquals(Float.valueOf(0.0f), message.getCourseOverGround());
        assertEquals((Integer) 511, message.getTrueHeading());
        assertEquals((Integer) 55, message.getSecond());
        assertEquals("00", message.getRegionalReserved2());
        assertTrue(message.getCsUnit());
        assertFalse(message.getDisplay());
        assertTrue(message.getDsc());
        assertTrue(message.getBand());
        assertTrue(message.getMessage22());
        assertFalse(message.getAssigned());
        assertFalse(message.getRaimFlag());
        //assertEquals("11100000000000000110", message.getRadioStatus());
    }

    @Test
    public void canDecode2() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,B>1VUFP00vK`auV0eUulKwv0RJGT,0*09"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(941204826), message.getSourceMmsi());
        assertEquals("00000000", message.getRegionalReserved1());
        assertEquals((Float) 0.3f, message.getSpeedOverGround());
        assertTrue(message.getPositionAccurate());
        assertEquals(Float.valueOf(42.020855f), message.getLatitude());
        assertEquals(Float.valueOf(-87.70006f), message.getLongitude());
        assertEquals(Float.valueOf(186.2f), message.getCourseOverGround());
        assertEquals((Integer) 511, message.getTrueHeading());
        assertEquals((Integer) 60, message.getSecond());
        assertEquals("00", message.getRegionalReserved2());
        assertFalse(message.getCsUnit());
        assertFalse(message.getDisplay());
        assertFalse(message.getDsc());
        assertTrue(message.getBand());
        assertFalse(message.getMessage22());
        assertFalse(message.getAssigned());
        assertFalse(message.getRaimFlag());
    }

    @Test
    public void canDecodeITDMACommunicationState() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,B6CdCm0t3`tba35f@V9faHi7kP06,0*58"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(423302100), message.getSourceMmsi());
        assertEquals("00001111", message.getRegionalReserved1());
        assertEquals((Float) 1.4f, message.getSpeedOverGround());
        assertTrue(message.getPositionAccurate());
        assertEquals(Float.valueOf(40.005283f), message.getLatitude());
        assertEquals(Float.valueOf(53.010998f), message.getLongitude());
        assertEquals(Float.valueOf(177f), message.getCourseOverGround());
        assertEquals((Integer) 177, message.getTrueHeading());
        assertEquals((Integer) 34, message.getSecond());
        assertEquals("00", message.getRegionalReserved2());
        assertTrue(message.getCsUnit());
        assertTrue(message.getDisplay());
        assertTrue(message.getDsc());
        assertTrue(message.getBand());
        assertTrue(message.getMessage22());
        assertFalse(message.getAssigned());
        assertFalse(message.getRaimFlag());
        assertTrue(message.getCommunicationStateSelectorFlag());

        CommunicationState communicationState = message.getCommunicationState();   // 1100000000000000110b = 3, slot incr = 6
        assertEquals(SyncState.BaseIndirect, communicationState.getSyncState());
        assertTrue(communicationState instanceof ITDMACommunicationState);
        ITDMACommunicationState itdmaCommunicationState = (ITDMACommunicationState) communicationState;
        assertEquals(Integer.valueOf(0), itdmaCommunicationState.getSlotIncrement());
        assertEquals(Integer.valueOf(3), itdmaCommunicationState.getNumberOfSlots());
        assertFalse(itdmaCommunicationState.getKeepFlag());
    }
}