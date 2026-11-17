package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AidToNavigationReportTest {

    @Test
    public void canDecode1() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDO,1,1,,A,E>lt;Lqaps0h3V:@;4a:@0b7W005J`6Dq9e<000003v010,4*7E");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.AidToNavigationReport, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        AidToNavigationReport message = (AidToNavigationReport) aisMessage;
        assertEquals(new MMSI(995036019), message.getSourceMmsi());
        assertEquals(AidType.BeaconSpecialMark, message.getAidType());
        assertEquals(false, message.isAssignedMode());
        assertEquals("S16A GLT VIRT ATON", message.getName());
        assertEquals(null, message.getNameExtension());
        assertEquals(false, message.isOffPosition());
        assertEquals(60, message.getSecond());
        assertEquals(0, message.getToBow());
        assertEquals(0, message.getToPort());
        assertEquals(0, message.getToStern());
        assertEquals(0, message.getToStarboard());
        assertEquals(true, message.isVirtualAid());
        assertEquals(false, message.isPositionAccurate());
        assertEquals(-23.936693f, message.getLatitude(), 0.0f); // lat = 111001001001101101001100000b = -23,9366933333
        assertEquals(151.44344f, message.getLongitude(), 0.0f);
        assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
        assertFalse(message.isRaimFlag());
    }

    @Test
    public void canDecode2() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDO,1,1,,A,E>lt;MIas0h3V:@;4a::h0b7W005Jh4nq:3l800003v010,4*08");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.AidToNavigationReport, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        AidToNavigationReport message = (AidToNavigationReport) aisMessage;
        assertEquals(new MMSI(995036021), message.getSourceMmsi());
        assertEquals(AidType.BeaconSpecialMark, message.getAidType());
        assertEquals(false, message.isAssignedMode());
        assertEquals("S6A GLT VIRTU ATON", message.getName());
        assertEquals(null, message.getNameExtension());
        assertEquals(false, message.isOffPosition());
        assertEquals(60, message.getSecond());
        assertEquals(0, message.getToBow());
        assertEquals(0, message.getToPort());
        assertEquals(0, message.getToStern());
        assertEquals(0, message.getToStarboard());
        assertEquals(true, message.isVirtualAid());
        assertEquals(false, message.isPositionAccurate());
        assertEquals(-23.917385f, message.getLatitude(), 0.0f);
        assertEquals(151.49791f, message.getLongitude(), 0.0f);
        assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
        assertFalse(message.isRaimFlag());
        assertFalse(message.isNameExtensionIsLabel());
    }

    /**
     * Name extension preceded by "@@@", which ITU-R M.1371-6 Table 71 defines as the AtoN's label (here "LB1") rather
     * than a continuation of the name. Synthetic sentence built from the spec layout; verified with pyais, which
     * reports the raw extension "@@@LB1".
     */
    @Test
    public void canDecodeNameExtensionLabel() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,E>jHC6L64ST:2R@1:WdhHP000000LW3P?tl201088;v0000030d@,4*13");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        AidToNavigationReport message = (AidToNavigationReport) aisMessage;
        assertEquals(new MMSI(992351001), message.getSourceMmsi());
        assertEquals(AidType.PortHandMark, message.getAidType());
        assertEquals("LIGHTED BUOY 1", message.getName());
        assertEquals(12.5f, message.getLongitude(), 0.0f);
        assertEquals(55.75f, message.getLatitude(), 0.0f);
        assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
        assertTrue(message.isNameExtensionIsLabel());
        assertEquals("LB1", message.getNameExtension());
    }

}
