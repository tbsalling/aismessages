package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.ITDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.SyncState;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Message 26 layout per ITU-R M.1371-6 Table 81: optional destination ID (30 bits + 2 spare), optional 16-bit
 * application identifier, application data, then 4 spare bits, a communication state selector and a 19-bit
 * communication state at the very end of the message.
 */
public class BinaryMessageMultipleSlotTest {

    @Test
    public void canDecodeBroadcastUnstructured() {
        // Arrange
        NMEAMessage nmeaMessage1 = new NMEAMessage("!AIVDM,2,1,4,A,J6SUcDh0000000000000000000000000000000000000000000000000000,0*0E");
        NMEAMessage nmeaMessage2 = new NMEAMessage("!AIVDM,2,2,4,A,00000000000,2*23");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage1, nmeaMessage2);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryMessageMultipleSlot, aisMessage.getMessageType());
        BinaryMessageMultipleSlot message = (BinaryMessageMultipleSlot) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(439970643), message.getSourceMmsi());
        assertFalse(message.isAddressed());
        assertFalse(message.isStructured());
        assertNull(message.getDestinationMmsi());
        assertEquals(-1, message.getApplicationId());
        assertEquals(-1, message.getDesignatedAreaCode());
        assertEquals(-1, message.getFunctionalId());
        assertNull(message.getApplicationSpecificMessage());
        assertEquals(418 - 40 - 24, message.getData().length());
        assertEquals("0".repeat(354), message.getData().toString());
        assertFalse(message.isCommStateSelectorFlag());
        assertInstanceOf(SOTDMACommunicationState.class, message.getCommunicationState());
    }

    @Test
    public void canDecodeAddressedUnstructured() {
        // Arrange
        NMEAMessage nmeaMessage1 = new NMEAMessage("!AIVDM,2,1,1,A,J5NJP<82<mN1<tn4GfOl1lTp8h6000000000000000000000000000000000,0*38");
        NMEAMessage nmeaMessage2 = new NMEAMessage("!AIVDM,2,2,1,A,00000000000,2*26");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage1, nmeaMessage2);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryMessageMultipleSlot, aisMessage.getMessageType());
        BinaryMessageMultipleSlot message = (BinaryMessageMultipleSlot) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(367435824), message.getSourceMmsi());
        assertTrue(message.isAddressed());
        assertFalse(message.isStructured());
        assertEquals(new MMSI(9229792), message.getDestinationMmsi());
        assertEquals(-1, message.getApplicationId());
        assertNull(message.getApplicationSpecificMessage());
        assertEquals(424 - 72 - 24, message.getData().length());
        assertEquals("0011001111001101100001000101111011100111111101000000011101001001001110000010001100000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", message.getData().toString());
        assertFalse(message.isCommStateSelectorFlag());
        SOTDMACommunicationState communicationState = (SOTDMACommunicationState) message.getCommunicationState();
        assertEquals(SyncState.UTCDirect, communicationState.getSyncState());
        assertEquals(0, communicationState.getSlotTimeout());
        assertEquals(0, communicationState.getSlotOffset());
    }

    /**
     * Addressed, structured (DAC 366, FI 56), repeat indicator 1, SOTDMA communication state with slot time-out 2.
     * Synthetic sentence built from the Table 81 layout.
     */
    @Test
    public void canDecodeAddressedStructuredSynthetic() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,JEM:Ih<jG;hLFsP383mJMqBikfd89D9OO9Vnlw00Q=8,2*45");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        BinaryMessageMultipleSlot message = (BinaryMessageMultipleSlot) aisMessage;
        assertEquals(1, message.getRepeatIndicator());
        assertEquals(new MMSI(366123456), message.getSourceMmsi());
        assertTrue(message.isAddressed());
        assertTrue(message.isStructured());
        assertEquals(new MMSI(211234567), message.getDestinationMmsi());
        assertEquals(366, message.getDesignatedAreaCode());
        assertEquals(56, message.getFunctionalId());
        assertEquals(366 * 64 + 56, message.getApplicationId());
        assertNotNull(message.getApplicationSpecificMessage());
        assertEquals(256 - 88 - 24, message.getData().length());
        assertEquals("000000110010000000111101010110100111011110010100101100011100111011101011000010000010010101000010010111110111110010011001101101101101001111110000", message.getData().toString());
        assertFalse(message.isCommStateSelectorFlag());
        SOTDMACommunicationState communicationState = (SOTDMACommunicationState) message.getCommunicationState();
        assertEquals(2, communicationState.getSlotTimeout());
        assertEquals(1234, communicationState.getSlotNumber());
    }

    /**
     * Addressed, unstructured, with an ITDMA communication state (selector flag 1). Synthetic sentence built from the
     * Table 81 layout.
     */
    @Test
    public void canDecodeAddressedUnstructuredWithItdmaCommunicationState() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,J3m674`wCs?P2m:Ip2MfeOi3Re4HGrKe=7g22E2GoREd39C3,0*11");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        BinaryMessageMultipleSlot message = (BinaryMessageMultipleSlot) aisMessage;
        assertEquals(new MMSI(257001234), message.getSourceMmsi());
        assertTrue(message.isAddressed());
        assertFalse(message.isStructured());
        assertEquals(new MMSI(265547000), message.getDestinationMmsi());
        assertEquals(-1, message.getApplicationId());
        assertEquals(288 - 72 - 24, message.getData().length());
        assertTrue(message.isCommStateSelectorFlag());
        ITDMACommunicationState communicationState = (ITDMACommunicationState) message.getCommunicationState();
        assertEquals(SyncState.BaseDirect, communicationState.getSyncState());
        assertEquals(2380, communicationState.getSlotIncrement());
        assertEquals(1, communicationState.getNumberOfSlots());
        assertTrue(communicationState.getKeepFlag());
    }
}
