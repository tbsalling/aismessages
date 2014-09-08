package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StandardClassBCSPositionReportTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,B5NJ;PP005l4ot5Isbl03wsUkP06,0*76"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(367430530L), message.getSourceMmsi());
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
        assertEquals("11100000000000000110", message.getRadioStatus());
    }
}