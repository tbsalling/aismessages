package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class AidToNavigationReportTest {

    @Test
    public void canDecode1() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDO,1,1,,A,E>lt;Lqaps0h3V:@;4a:@0b7W005J`6Dq9e<000003v010,4*7E"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.AidToNavigationReport, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        AidToNavigationReport message = (AidToNavigationReport) aisMessage;
        assertEquals(MMSI.valueOf(995036019), message.getSourceMmsi());
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
        assertEquals(MMSI.valueOf(995036021), message.getSourceMmsi());
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

    @Test
    public void testDataFields() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDO,1,1,,A,E>lt;MIas0h3V:@;4a::h0b7W005Jh4nq:3l800003v010,4*08"));
        Map<String, Object> dataFields = aisMessage.dataFields();

        assertNotNull(dataFields);
        assertEquals(23, dataFields.size());

        assertEquals("AidToNavigationReport", dataFields.get("messageType"));
        assertEquals(0, dataFields.get("repeatIndicator"));
        assertEquals(995036021, dataFields.get("sourceMmsi.MMSI"));
        assertEquals("BeaconSpecialMark", dataFields.get("aidType"));
        assertEquals("S6A GLT VIRTU ATON", dataFields.get("name"));
        assertEquals(false, dataFields.get("positionAccurate"));
        assertEquals(151.49791f, dataFields.get("longitude"));
        assertEquals(-23.917383f, dataFields.get("latitude"));
        assertEquals(0, dataFields.get("toStern"));
        assertEquals(0, dataFields.get("toBow"));
        assertEquals(0, dataFields.get("toPort"));
        assertEquals(0, dataFields.get("toStarboard"));
        assertEquals("Surveyed", dataFields.get("positionFixingDevice"));
        assertEquals(60, dataFields.get("second"));
        assertEquals(false, dataFields.get("offPosition"));
        assertEquals("00000000", dataFields.get("atoNStatus"));
        assertEquals(false, dataFields.get("raimFlag"));
        assertEquals(true, dataFields.get("virtualAid"));
        assertEquals(false, dataFields.get("assignedMode"));
        assertEquals(0, dataFields.get("spare1"));
        assertEquals(0, dataFields.get("spare2"));
        assertEquals(true, dataFields.get("valid"));

        assertFalse(dataFields.containsKey("nameExtension"));
        assertFalse(dataFields.containsKey("class"));
    }

}
