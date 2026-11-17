package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.TxRxMode;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChannelManagementTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,F030p:j2N2P5aJR0r;6f3rj10000,0*11");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.ChannelManagement, aisMessage.getMessageType());
        ChannelManagement message = (ChannelManagement) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(3160107), message.getSourceMmsi());
        assertFalse(message.isPower());
        assertEquals(2087, message.getChannelA());
        assertEquals(2088, message.getChannelB());
        assertFalse(message.isPower());
        // Jurisdiction area
        assertEquals(-7710.0f, message.getNorthEastLongitude(), 0.0f);
        assertEquals(3300.0f, message.getNorthEastLatitude(), 0.0f);
        assertEquals(-8020.0f, message.getSouthWestLongitude(), 0.0f);
        assertEquals(3210.0f, message.getSouthWestLatitude(), 0.0f);
        // Bands
        assertFalse(message.isBandA());
        assertFalse(message.isBandB());
        assertEquals(2, message.getZoneSize());
        // Not addressed and therefore destination mmsis should be null
        assertFalse(message.isAddressed());
        assertNull(message.getDestinationMmsi1());
        assertNull(message.getDestinationMmsi2());
    }

    /**
     * Addressed variant (ITU-R M.1371-6 Table 73): the area fields carry two station IDs instead of coordinates, so the
     * coordinate getters return null. Synthetic sentence built from the spec layout; independently verified with the
     * pyais decoder. Decoding an addressed Message 22 used to throw a NullPointerException.
     */
    @Test
    public void canDecodeAddressed() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,FP25bdB2N2PA`KHj@3tfbKPGP000,0*1A");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        ChannelManagement message = (ChannelManagement) aisMessage;
        assertEquals(2, message.getRepeatIndicator());
        assertEquals(new MMSI(2190001), message.getSourceMmsi());
        assertEquals(2087, message.getChannelA());
        assertEquals(2088, message.getChannelB());
        assertEquals(TxRxMode.TxARxAB, message.getTransmitReceiveMode());
        assertFalse(message.isPower());
        assertTrue(message.isAddressed());
        assertEquals(new MMSI(219001234), message.getDestinationMmsi1());
        assertEquals(new MMSI(265005678), message.getDestinationMmsi2());
        assertNull(message.getNorthEastLongitude());
        assertNull(message.getNorthEastLatitude());
        assertNull(message.getSouthWestLongitude());
        assertNull(message.getSouthWestLatitude());
        assertFalse(message.isBandA());
        assertTrue(message.isBandB());
        assertEquals(7, message.getZoneSize());
    }

}
