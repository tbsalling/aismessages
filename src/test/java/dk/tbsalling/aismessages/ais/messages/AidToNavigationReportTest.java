package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.ais.messages.types.TimeStampIndicator;
import org.junit.jupiter.api.Test;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

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
        assertNull(message.getSecond());
        assertEquals(TimeStampIndicator.NotAvailable, message.getTimeStampIndicator());
        assertEquals(0, message.getToBow());
        assertEquals(0, message.getToPort());
        assertEquals(0, message.getToStern());
        assertEquals(0, message.getToStarboard());
        assertEquals(true, message.isVirtualAid());
        assertEquals(false, message.isPositionAccurate());
        assertEquals(-14362016 / 600000.0, message.getLatitude(), 0.0f); // lat = 111001001001101101001100000b = -23,9366933333
        assertEquals(90866068 / 600000.0, message.getLongitude(), 0.0f);
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
        assertNull(message.getSecond());
        assertEquals(TimeStampIndicator.NotAvailable, message.getTimeStampIndicator());
        assertEquals(0, message.getToBow());
        assertEquals(0, message.getToPort());
        assertEquals(0, message.getToStern());
        assertEquals(0, message.getToStarboard());
        assertEquals(true, message.isVirtualAid());
        assertEquals(false, message.isPositionAccurate());
        assertEquals(-14350431 / 600000.0, message.getLatitude(), 0.0f);
        assertEquals(90898742 / 600000.0, message.getLongitude(), 0.0f);
        assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
        assertFalse(message.isRaimFlag());
        assertFalse(message.isNameExtensionLabel());
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
        assertTrue(message.isNameExtensionLabel());
        assertEquals("LB1", message.getNameExtension());
    }

    /** The 272 fixed bits of Message 21 (ITU-R M.1371-6 Table 71) for a port hand mark named "MARK". */
    private static Bits aidToNavigationHeader() {
        return bits().u(21, 6).u(0, 2).u(992191001, 30).u(24, 5).text("MARK", 20).b(true)
                .s(6450000, 28).s(33300000, 27).u(1, 9).u(1, 9).u(1, 6).u(1, 6).u(7, 4).u(60, 6)
                .b(false).u(0, 8).b(false).b(false).b(false).u(0, 1);
    }

    /**
     * Name extensions of 1, 2, 3 and 14 characters followed by 2, 4, 6 and 4 spare bits respectively, which pad the
     * message to a byte boundary (Table 71). Two and four spare bits are set to non-zero values to check their
     * extraction. Six spare bits cannot be told apart from one more six-bit character; Table 71 requires them to be zero,
     * which decodes as the padding character "@" and is trimmed, so that case uses zero.
     */
    @Test
    public void canDecodeNameExtensionWithSpareBits() {
        record Case(String extension, int spareBits, int spareValue, int totalBits) {}
        for (Case c : new Case[]{new Case("A", 2, 2, 280), new Case("AB", 4, 5, 288), new Case("ABC", 6, 0, 296), new Case("ABCDEFGHIJKLMN", 4, 9, 360)}) {
            Bits b = aidToNavigationHeader().text(c.extension(), c.extension().length()).u(c.spareValue(), c.spareBits());
            assertEquals(c.totalBits(), b.length());

            AidToNavigationReport message = (AidToNavigationReport) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

            assertEquals("MARK", message.getName());
            assertEquals(c.extension(), message.getNameExtension());
            assertEquals(c.spareValue(), message.getSpare2());
            assertFalse(message.isNameExtensionLabel());
        }
    }
}
