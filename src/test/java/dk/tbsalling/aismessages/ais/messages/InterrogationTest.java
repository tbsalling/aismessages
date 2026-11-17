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
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.Interrogation, aisMessage.getMessageType());
        Interrogation message = (Interrogation) aisMessage;
        assertEquals(3, message.getRepeatIndicator());
        assertEquals(new MMSI(3669708), message.getSourceMmsi());
        assertEquals(new MMSI(366969740), message.getInterrogatedMmsi1());
        assertEquals(0, message.getType1_1());
        assertEquals(0, message.getOffset1_1());
        assertEquals(-1, message.getType1_2());
        assertEquals(-1, message.getOffset1_2());
        assertNull(message.getInterrogatedMmsi2());
        assertEquals(-1, message.getType2_1());
        assertEquals(-1, message.getOffset2_1());
    }

    /**
     * One station interrogated for two messages (ITU-R M.1371-6 Table 64, 110 bits padded to 112).
     * Synthetic sentence built from the spec layout; independently verified with the pyais decoder.
     */
    @Test
    public void canDecodeOneStationTwoMessages() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,?h2R3p@l=o?T<P@59a0,4*32");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        assertEquals(AISMessageType.Interrogation, aisMessage.getMessageType());
        Interrogation message = (Interrogation) aisMessage;
        assertEquals(3, message.getRepeatIndicator());
        assertEquals(new MMSI(2655201), message.getSourceMmsi());
        assertEquals(new MMSI(219012345), message.getInterrogatedMmsi1());
        assertEquals(3, message.getType1_1());
        assertEquals(516, message.getOffset1_1());
        assertEquals(5, message.getType1_2());
        assertEquals(617, message.getOffset1_2());
        assertNull(message.getInterrogatedMmsi2());
        assertEquals(-1, message.getType2_1());
        assertEquals(-1, message.getOffset2_1());
    }

    /**
     * Two stations interrogated, 160 bits (ITU-R M.1371-6 Table 64): the second station's fields are at bits
     * 110-140 (MMSI), 140-146 (message id) and 146-158 (12-bit slot offset).
     * Synthetic sentence built from the spec layout; independently verified with the pyais decoder.
     */
    @Test
    public void canDecodeTwoStations() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,?02R3p0jKFFPD6@H2F3@ndh@hj0,2*24");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        assertEquals(AISMessageType.Interrogation, aisMessage.getMessageType());
        Interrogation message = (Interrogation) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2655200), message.getSourceMmsi());
        assertEquals(new MMSI(211507560), message.getInterrogatedMmsi1());
        assertEquals(5, message.getType1_1());
        assertEquals(100, message.getOffset1_1());
        assertEquals(24, message.getType1_2());
        assertEquals(150, message.getOffset1_2());
        assertEquals(new MMSI(219000001), message.getInterrogatedMmsi2());
        assertEquals(3, message.getType2_1());
        assertEquals(200, message.getOffset2_1());
    }
}
