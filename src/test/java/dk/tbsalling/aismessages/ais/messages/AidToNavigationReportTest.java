package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AidToNavigationReportTest {

    @Test
    public void canDecode1() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDO,1,1,,A,E>lt;Lqaps0h3V:@;4a:@0b7W005J`6Dq9e<000003v010,4*7E"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.AidToNavigationReport, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        AidToNavigationReport message = (AidToNavigationReport) aisMessage;
        assertEquals(MMSI.valueOf(995036019L), message.getSourceMmsi());
        assertEquals(AidType.BeaconSpecialMark, message.getAidType());
        assertEquals(false, message.getAssignedMode());
        assertEquals("S16A GLT VIRT ATON", message.getName());
        assertEquals(null, message.getNameExtension());
        assertEquals(false, message.getOffPosition());
        assertEquals(Integer.valueOf(60), message.getSecond());
        assertEquals(Integer.valueOf(0), message.getToBow());
        assertEquals(Integer.valueOf(0), message.getToPort());
        assertEquals(Integer.valueOf(0), message.getToStern());
        assertEquals(Integer.valueOf(0), message.getToStarboard());
        assertEquals(true, message.getVirtualAid());
        assertEquals(false, message.getPositionAccurate());
        assertEquals(Float.valueOf(-23.936691f), message.getLatitude());
        assertEquals(Float.valueOf(151.44344f), message.getLongitude());
        assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
        assertFalse(message.getRaimFlag());
    }

    @Test
    public void canDecode2() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDO,1,1,,A,E>lt;MIas0h3V:@;4a::h0b7W005Jh4nq:3l800003v010,4*08"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.AidToNavigationReport, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        AidToNavigationReport message = (AidToNavigationReport) aisMessage;
        assertEquals(MMSI.valueOf(995036021L), message.getSourceMmsi());
        assertEquals(AidType.BeaconSpecialMark, message.getAidType());
        assertEquals(false, message.getAssignedMode());
        assertEquals("S6A GLT VIRTU ATON", message.getName());
        assertEquals(null, message.getNameExtension());
        assertEquals(false, message.getOffPosition());
        assertEquals(Integer.valueOf(60), message.getSecond());
        assertEquals(Integer.valueOf(0), message.getToBow());
        assertEquals(Integer.valueOf(0), message.getToPort());
        assertEquals(Integer.valueOf(0), message.getToStern());
        assertEquals(Integer.valueOf(0), message.getToStarboard());
        assertEquals(true, message.getVirtualAid());
        assertEquals(false, message.getPositionAccurate());
        assertEquals(Float.valueOf(-23.917383f), message.getLatitude());
        assertEquals(Float.valueOf(151.49791f), message.getLongitude());
        assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
        assertFalse(message.getRaimFlag());
    }
}