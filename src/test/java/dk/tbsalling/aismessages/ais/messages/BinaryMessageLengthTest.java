package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.bits;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.nmea;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The minimum length of Messages 25 and 26 depends on their destination and binary data flags: a 32-bit destination ID
 * when addressed and a 16-bit application identifier when structured (ITU-R M.1371-6 Tables 79 and 81), plus Message
 * 26's 24-bit trailer. Sentences are synthetic.
 */
public class BinaryMessageLengthTest {

    private static AISMessage decode(int type, boolean addressed, boolean structured, int lengthBits) {
        return AISMessageFactory.create(null, null, null, nmea(bits().u(type, 6).u(0, 2).u(219000003, 30).b(addressed).b(structured).zeros(lengthBits - 40).toBitString()));
    }

    @ParameterizedTest(name = "Message {0}, addressed={1}, structured={2}: minimum {3} bits")
    @CsvSource({
            "25, false, false, 40",
            "25, true,  false, 72",
            "25, false, true,  56",
            "25, true,  true,  88",
            "26, false, false, 64",
            "26, true,  false, 96",
            "26, false, true,  80",
            "26, true,  true,  112"
    })
    public void minimumLengthDependsOnFlags(int type, boolean addressed, boolean structured, int minimum) {
        assertNotNull(decode(type, addressed, structured, minimum));
        if (minimum > 40) {
            InvalidMessage e = assertThrows(InvalidMessage.class, () -> decode(type, addressed, structured, minimum - 8));
            assertTrue(e.getMessage().contains("at least " + minimum) || e.getMessage().contains(minimum + "-1064"), e.getMessage());
        }
    }

    @ParameterizedTest(name = "addressed Message {0} of the minimum length decodes its destination")
    @CsvSource({"25, 72", "26, 96"})
    public void addressedMinimumDecodesDestination(int type, int lengthBits) {
        AISMessage m = AISMessageFactory.create(null, null, null, nmea(bits().u(type, 6).u(0, 2).u(219000003, 30).b(true).b(false).u(219000004, 30).zeros(lengthBits - 70).toBitString()));

        Object destination = m instanceof BinaryMessageSingleSlot s ? s.getDestinationMMSI() : ((BinaryMessageMultipleSlot) m).getDestinationMmsi();
        assertEquals(new dk.tbsalling.aismessages.ais.messages.types.MMSI(219000004), destination);
    }
}
