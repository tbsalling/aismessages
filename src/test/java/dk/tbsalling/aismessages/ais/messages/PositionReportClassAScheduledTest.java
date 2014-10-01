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

public class PositionReportClassAScheduledTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,13@nePh01>PjcO4PGReoJEmL0HJg,0*67"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        PositionReportClassAScheduled message = (PositionReportClassAScheduled) aisMessage;
        assertEquals(MMSI.valueOf(219000195L), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals((Integer) 0, message.getRateOfTurn());
        assertEquals((Float) 7.8f, message.getSpeedOverGround());
        assertTrue(message.getPositionAccurate());
        assertEquals(Float.valueOf(56.56692f), message.getLatitude());
        assertEquals(Float.valueOf(11.071096f), message.getLongitude());
        assertEquals(Float.valueOf(189.7f), message.getCourseOverGround());
        assertEquals((Integer) 46, message.getSecond());
        assertEquals((Integer) 186, message.getTrueHeading());
        assertEquals(ManeuverIndicator.NotAvailable, message.getManeuverIndicator());
        assertFalse(message.getRaimFlag());
    }
}