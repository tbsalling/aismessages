package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InterrogationTest {

    @Test
    public void canDecodeShortVariant() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,?h3Ovk1GOPph000,2*53");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.Interrogation, aisMessage.getMessageType());
        Interrogation message = (Interrogation) aisMessage;
        assertEquals(3, message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(3669708), message.getSourceMmsi());
        assertEquals(MMSI.valueOf(366969740), message.getInterrogatedMmsi1());
        assertEquals(0, message.getType1_1());
        assertEquals(0, message.getOffset1_1());
        assertEquals(-1, message.getType1_2());
        assertEquals(-1, message.getOffset1_2());
        assertNull(message.getInterrogatedMmsi2());
        assertEquals(-1, message.getType2_1());
        assertEquals(-1, message.getOffset2_1());
    }
}
