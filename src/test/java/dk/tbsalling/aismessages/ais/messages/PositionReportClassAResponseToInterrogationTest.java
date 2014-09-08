package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PositionReportClassAResponseToInterrogationTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,34RjBV0028o:pnNEBeU<pJF>0PT@,0*3F"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.PositionReportClassAResponseToInterrogation, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) aisMessage;
        assertEquals(MMSI.valueOf(304911000L), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals((Integer) 0, message.getRateOfTurn());
        assertEquals((Float) 13.6f, message.getSpeedOverGround());
        assertTrue(message.getPositionAccurate());
        assertEquals(Float.valueOf(37.21113f), message.getLatitude());
        assertEquals(Float.valueOf(-123.45053f), message.getLongitude());
        assertEquals(Float.valueOf(329.7f), message.getCourseOverGround());
        assertEquals((Integer) 331, message.getTrueHeading());
        assertEquals((Integer) 7, message.getSecond());
        assertEquals(ManeuverIndicator.NotAvailable, message.getManeuverIndicator());
        assertFalse(message.getRaimFlag());
    }
}