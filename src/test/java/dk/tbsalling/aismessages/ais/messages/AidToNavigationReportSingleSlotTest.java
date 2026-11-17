package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.AtoNDimensionsType;
import dk.tbsalling.aismessages.ais.messages.types.AtoNOnStationStatus;
import dk.tbsalling.aismessages.ais.messages.types.AtoNStationType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.RestrictedUseIndicator;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Message 28, single-slot aid-to-navigation report (ITU-R M.1371-6 Table 84). No real-world Message 28 traffic was
 * publicly available when these tests were written, so both vectors are synthetic.
 */
public class AidToNavigationReportSingleSlotTest {

    /**
     * Fixed physical AtoN with height-and-structural-area dimensions, authenticated. Synthetic sentence built from the
     * Table 84 layout; independently verified with the pyais decoder.
     */
    @Test
    public void canDecodeFixedAuthenticatedAtoN() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,L>j>S0P@HVe@?p?40@ph>A5E>L@1,0*68");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.AidToNavigationReportSingleSlot, aisMessage.getMessageType());
        AidToNavigationReportSingleSlot message = (AidToNavigationReportSingleSlot) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(992191234), message.getSourceMmsi());
        assertEquals(1, message.getSecond());
        assertEquals(10.75f, message.getLongitude(), 0.0f);
        assertEquals(55.5f, message.getLatitude(), 0.0f);
        assertEquals(RestrictedUseIndicator.UnrestrictedUse, message.getRestrictedUse());
        assertEquals(AtoNStationType.PhysicalFixed, message.getStationType());
        assertEquals(AidType.LeadingLightFront, message.getAidType());
        assertEquals(12345, message.getIalaMrn());
        assertEquals(AtoNDimensionsType.HeightAndStructuralArea, message.getDimensionsType());
        assertEquals(42, message.getDimensionA());
        assertEquals(1337, message.getDimensionB());
        assertTrue(message.isMultipleDimensionTypes());
        assertTrue(message.isCharted());
        assertEquals(AtoNOnStationStatus.OnStationOrOnCourse, message.getOnStationStatus());
        assertEquals(0, message.getAtonStatusBits());
        assertTrue(message.isAuthenticated());
    }

    /**
     * Mobile AtoN (SAR datum mark) with a vector dimension type, EEZ-restricted use, maximum MRN, status bits set
     * and not authenticated. Synthetic sentence built from the Table 84 layout; independently verified with pyais.
     */
    @Test
    public void canDecodeMobileAtoNVector() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,L>k`s@DKWWbP=Q;65EgwwkQh3<JD,0*77");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        AidToNavigationReportSingleSlot message = (AidToNavigationReportSingleSlot) aisMessage;
        assertEquals(new MMSI(993672001), message.getSourceMmsi());
        assertEquals(17, message.getSecond());
        assertEquals(-122.5f, message.getLongitude(), 0.0f);
        assertEquals(47.25f, message.getLatitude(), 0.0f);
        assertEquals(RestrictedUseIndicator.ExclusiveEconomicZone, message.getRestrictedUse());
        assertEquals(AtoNStationType.Mobile, message.getStationType());
        assertEquals(AidType.SearchAndRescueDatumMark, message.getAidType());
        assertEquals(131071, message.getIalaMrn());
        assertEquals(AtoNDimensionsType.MobileAtoNVector, message.getDimensionsType());
        assertEquals(270, message.getDimensionA());
        assertEquals(12, message.getDimensionB());
        assertTrue(message.isMultipleDimensionTypes());
        assertTrue(message.isCharted());
        assertEquals(AtoNOnStationStatus.OnStationOrOnCourse, message.getOnStationStatus());
        assertEquals(0b10100101, message.getAtonStatusBits());
        assertFalse(message.isAuthenticated());
    }
}
