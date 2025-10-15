package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryMessageSingleSlotTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,A,I6SWo?8P00a3PKpEKEVj0?vNP<65,0*73");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryMessageSingleSlot, aisMessage.getMessageType());
        BinaryMessageSingleSlot message = (BinaryMessageSingleSlot) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(440006460), message.getSourceMmsi());
        assertTrue(message.getDestinationIndicator());
        assertFalse(message.getBinaryDataFlag());
        assertEquals(MMSI.valueOf(134218384), message.getDestinationMMSI());
        assertEquals("00100000000000000000101001000011100000011011111000010101011011010101100110110010000000001111111110011110100000001100000110000101", message.getBinaryData());
        assertFalse(message.getBinaryData().isEmpty());
    }

    @Test
    public void canDecodeAlternative() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,I6SW6D5P00a3PO8>KUV00?vNP<65,0*0E");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryMessageSingleSlot, aisMessage.getMessageType());
        BinaryMessageSingleSlot message = (BinaryMessageSingleSlot) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(439993936), message.getSourceMmsi());
        assertFalse(message.getDestinationIndicator());
        assertTrue(message.getBinaryDataFlag());
        assertEquals(MMSI.valueOf(402653840), message.getDestinationMMSI());
        assertEquals("01100000000000000000101001000011100000011111001000001110011011100101100110000000000000001111111110011110100000001100000110000101", message.getBinaryData());
    }
}
