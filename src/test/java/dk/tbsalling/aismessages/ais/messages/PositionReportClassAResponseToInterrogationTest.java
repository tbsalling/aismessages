package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class PositionReportClassAResponseToInterrogationTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,34RjBV0028o:pnNEBeU<pJF>0PT@,0*3F"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.PositionReportClassAResponseToInterrogation, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) aisMessage;
        assertEquals(MMSI.valueOf(304911000), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals(0, message.getRateOfTurn());
        assertEquals(13.6f, message.getSpeedOverGround());
        assertEquals(136, message.getRawSpeedOverGround());
        assertTrue(message.getPositionAccuracy());
        assertEquals(37.21113f, message.getLatitude());
        assertEquals(22326676, message.getRawLatitude());
        assertEquals(-123.45053f, message.getLongitude());
        assertEquals(-74070321, message.getRawLongitude());
        assertEquals(329.7f, message.getCourseOverGround());
        assertEquals(3297, message.getRawCourseOverGround());
        assertEquals(331, message.getTrueHeading());
        assertEquals(7, message.getSecond());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.getRaimFlag());
    }

    @Test
    public void canDecode2() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,3:U70chP@7LrG1SjrgmKF8uh00vP,0*56"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.PositionReportClassAResponseToInterrogation, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) aisMessage;
        assertEquals(MMSI.valueOf(710000815), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals(-720, message.getRateOfTurn());
        assertEquals(0.7f, message.getSpeedOverGround());
        assertEquals(7, message.getRawSpeedOverGround());
        assertFalse(message.getPositionAccuracy());
        assertEquals(-22.862577f, message.getLatitude());
        assertEquals(-13717547, message.getRawLatitude());
        assertEquals(-43.175175f, message.getLongitude());
        assertEquals(-25905103, message.getRawLongitude());
        assertEquals(290.4f, message.getCourseOverGround());
        assertEquals(2904, message.getRawCourseOverGround());
        assertEquals(286, message.getTrueHeading());
        assertEquals(56, message.getSecond());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.getRaimFlag());
    }

    @Test
    public void digest() throws NoSuchAlgorithmException {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,34RjBV0028o:pnNEBeU<pJF>0PT@,0*3F"));
        byte[] digest = aisMessage.digest();
        String digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));
        assertEquals("673ac3b20886868cafe7376e05092bf625f00b75", digestAsString);
    }
}