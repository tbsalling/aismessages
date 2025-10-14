package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BinaryAcknowledgeMessageTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,702;bCSdToR`,0*34");

        // Act
        AISMessage aisMessage = AISMessage.create(nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryAcknowledge, aisMessage.getMessageType());
        BinaryAcknowledge message = (BinaryAcknowledge) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(2288206), message.getSourceMmsi());
        assertEquals((Integer) 1, message.getNumOfAcks());
        assertEquals((Integer) 0, message.getSequence1());
        assertEquals(MMSI.valueOf(992271914), message.getMmsi1());
        assertNull(message.getSequence2());
        assertNull(message.getMmsi2());
        assertNull(message.getSequence3());
        assertNull(message.getMmsi3());
        assertNull(message.getSequence4());
        assertNull(message.getMmsi4());
    }

}
