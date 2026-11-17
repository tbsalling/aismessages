package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.UnknownApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Message 25 layout per ITU-R M.1371-6 Table 79. Note that the destination ID of an addressed message is followed by
 * 2 spare bits for byte alignment, so application data (or the application identifier) starts at bit 72, not 70.
 */
public class BinaryMessageSingleSlotTest {

    @Test
    public void canDecodeAddressedUnstructured() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,I6SWo?8P00a3PKpEKEVj0?vNP<65,0*73");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryMessageSingleSlot, aisMessage.getMessageType());
        BinaryMessageSingleSlot message = (BinaryMessageSingleSlot) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(440006460), message.getSourceMmsi());
        assertTrue(message.isDestinationIndicator());
        assertFalse(message.isBinaryDataFlag());
        assertEquals(new MMSI(134218384), message.getDestinationMMSI());
        assertEquals(-1, message.getDesignatedAreaCode());
        assertEquals(-1, message.getFunctionalId());
        assertNull(message.getApplicationSpecificMessage());
        assertEquals(96, message.getData().length());
        assertEquals("100000011011111000010101011011010101100110110010000000001111111110011110100000001100000110000101", message.getData().toString());
        assertEquals("00100000000000000000101001000011100000011011111000010101011011010101100110110010000000001111111110011110100000001100000110000101", message.getBinaryData());
    }

    @Test
    public void canDecodeBroadcastStructured() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,I6SW6D5P00a3PO8>KUV00?vNP<65,0*0E");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryMessageSingleSlot, aisMessage.getMessageType());
        BinaryMessageSingleSlot message = (BinaryMessageSingleSlot) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(439993936), message.getSourceMmsi());
        assertFalse(message.isDestinationIndicator());
        assertTrue(message.isBinaryDataFlag());
        assertNull(message.getDestinationMMSI());
        assertEquals(384, message.getDesignatedAreaCode());
        assertEquals(0, message.getFunctionalId());
        assertInstanceOf(UnknownApplicationSpecificMessage.class, message.getApplicationSpecificMessage());
        assertEquals(112, message.getData().length());
        assertEquals("0000101001000011100000011111001000001110011011100101100110000000000000001111111110011110100000001100000110000101", message.getData().toString());
    }

    /**
     * Broadcast, structured (DAC 235, FI 10). Synthetic sentence built from the spec layout; independently verified with
     * the pyais decoder.
     */
    @Test
    public void canDecodeBroadcastStructuredSynthetic() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,I3P7o2DrjP0UBVvDfMp3:4mjUskQ,0*54");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        BinaryMessageSingleSlot message = (BinaryMessageSingleSlot) aisMessage;
        assertEquals(new MMSI(235009801), message.getSourceMmsi());
        assertFalse(message.isDestinationIndicator());
        assertTrue(message.isBinaryDataFlag());
        assertNull(message.getDestinationMMSI());
        assertEquals(235, message.getDesignatedAreaCode());
        assertEquals(10, message.getFunctionalId());
        assertEquals(112, message.getData().length());
        assertEquals("0000000000100101010010100110111110010100101110011101111000000011001010000100110101110010100101111011110011100001", message.getData().toString());
    }

    /**
     * Broadcast, unstructured: all 128 bits after the flags are application data. Synthetic sentence built from the
     * spec layout; independently verified with the pyais decoder.
     */
    @Test
    public void canDecodeBroadcastUnstructuredSynthetic() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,I3@nu`@7?76Vni15NbwT6Dr3f>lR,0*21");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        BinaryMessageSingleSlot message = (BinaryMessageSingleSlot) aisMessage;
        assertEquals(new MMSI(219004321), message.getSourceMmsi());
        assertFalse(message.isDestinationIndicator());
        assertFalse(message.isBinaryDataFlag());
        assertNull(message.getDestinationMMSI());
        assertEquals(-1, message.getDesignatedAreaCode());
        assertEquals(-1, message.getFunctionalId());
        assertNull(message.getApplicationSpecificMessage());
        assertEquals(128, message.getData().length());
    }
}
