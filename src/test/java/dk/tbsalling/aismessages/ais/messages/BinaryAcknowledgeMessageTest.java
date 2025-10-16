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
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,702;bCSdToR`,0*34");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryAcknowledge, aisMessage.getMessageType());
        BinaryAcknowledge message = (BinaryAcknowledge) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2288206), message.getSourceMmsi());
        assertEquals(1, message.getNumOfAcks());
        assertEquals(0, message.getSequence1());
        assertEquals(new MMSI(992271914), message.getMmsi1());
        assertNull(message.getSequence2());
        assertNull(message.getMmsi2());
        assertNull(message.getSequence3());
        assertNull(message.getMmsi3());
        assertNull(message.getSequence4());
        assertNull(message.getMmsi4());
    }

}
