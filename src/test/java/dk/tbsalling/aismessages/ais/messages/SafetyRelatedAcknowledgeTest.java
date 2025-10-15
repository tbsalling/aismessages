package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SafetyRelatedAcknowledgeTest {

    @Test
    public void canDecodeWithOneAck() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,A,=39UOj0jFs9R,0*17");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.SafetyRelatedAcknowledge, aisMessage.getMessageType());
        SafetyRelatedAcknowledge message = (SafetyRelatedAcknowledge) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(211378120), message.getSourceMmsi());
        assertEquals(Integer.valueOf(0), message.getSpare());
        assertEquals(MMSI.valueOf(211217560), message.getMmsi1());
        assertEquals(Integer.valueOf(2), message.getSequence1());
        assertNull(message.getMmsi2());
        assertNull(message.getSequence2());
        assertNull(message.getMmsi3());
        assertNull(message.getSequence3());
        assertNull(message.getMmsi4());
        assertNull(message.getSequence4());
        assertEquals(Integer.valueOf(1), message.getNumOfAcks());
    }

    @Test
    public void canDecodeWithMultipleAcks() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,A,=8157oQGOv9f,0*0F");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.SafetyRelatedAcknowledge, aisMessage.getMessageType());
        SafetyRelatedAcknowledge message = (SafetyRelatedAcknowledge) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(538003422), message.getSourceMmsi());
        assertEquals(Integer.valueOf(0), message.getSpare());
        assertEquals(MMSI.valueOf(366999707), message.getMmsi1());
        assertEquals(Integer.valueOf(2), message.getSequence1());
        assertEquals(Integer.valueOf(1), message.getNumOfAcks());
    }
}
