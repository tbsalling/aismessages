package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.AISInputStreamReader;
import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Malformed payloads must be rejected with {@link InvalidMessage}, never with an unchecked exception from deeper in the
 * decoder, so that stream readers can log and continue. Payload lengths that ITU-R M.1371-6 Annex 7 does not allow for
 * a message type are taken from each message class's length check. All sentences are synthetic.
 */
public class MalformedMessageTest {

    static Stream<Arguments> invalidLengths() {
        return Stream.of(
                Arguments.of(1, 162), Arguments.of(1, 174), Arguments.of(2, 162), Arguments.of(2, 174),
                Arguments.of(3, 162), Arguments.of(3, 174), Arguments.of(4, 162), Arguments.of(4, 174),
                Arguments.of(5, 420), Arguments.of(5, 428),
                Arguments.of(6, 72), Arguments.of(6, 1014),
                Arguments.of(7, 80), Arguments.of(7, 174),
                Arguments.of(8, 50), Arguments.of(8, 1014),
                Arguments.of(9, 162), Arguments.of(9, 174),
                Arguments.of(10, 78),
                Arguments.of(11, 162), Arguments.of(11, 174),
                Arguments.of(12, 66), Arguments.of(12, 1014),
                Arguments.of(13, 80), Arguments.of(13, 174),
                Arguments.of(14, 38), Arguments.of(14, 1014),
                Arguments.of(15, 100), Arguments.of(15, 168),
                Arguments.of(16, 120), Arguments.of(16, 150),
                Arguments.of(17, 74), Arguments.of(17, 822),
                Arguments.of(18, 162), Arguments.of(18, 174),
                Arguments.of(19, 300), Arguments.of(19, 318),
                Arguments.of(20, 66), Arguments.of(20, 166),
                Arguments.of(21, 266), Arguments.of(21, 366),
                Arguments.of(22, 162), Arguments.of(22, 174),
                Arguments.of(23, 154), Arguments.of(23, 166),
                Arguments.of(24, 162), Arguments.of(24, 174),
                Arguments.of(25, 38), Arguments.of(25, 174),
                Arguments.of(26, 58), Arguments.of(26, 1070),
                Arguments.of(27, 120), Arguments.of(27, 174),
                Arguments.of(28, 162), Arguments.of(28, 174));
    }

    @ParameterizedTest(name = "message {0} with {1} bits")
    @MethodSource("invalidLengths")
    public void rejectsPayloadOfInvalidLength(int type, int lengthBits) {
        BitString payload = message(type, lengthBits);
        List<NMEAMessage> sentences = new ArrayList<>();
        for (String s : fragments(payload, 1, 60)) sentences.add(new NMEAMessage(s));

        InvalidMessage e = assertThrows(InvalidMessage.class, () -> AISMessageFactory.create(null, null, null, sentences.toArray(new NMEAMessage[0])));

        assertTrue(e.getMessage().contains(AISMessageType.fromInteger(type).toString()), e.getMessage());
        assertTrue(e.getMessage().contains("219000001"), e.getMessage());
    }

    @Test
    public void rejectsFillBitsOutsideZeroToFive() {
        NMEAMessage sentence = new NMEAMessage(withChecksum("AIVDM,1,1,,A,15MqdBP000G@qoLEi69PVGaN0D0=,6"));

        InvalidMessage e = assertThrows(InvalidMessage.class, () -> AISMessageFactory.create(null, null, null, sentence));
        assertTrue(e.getMessage().contains("Cannot decode payload"), e.getMessage());
    }

    @Test
    public void rejectsPositionReportTooShortForItsCommunicationState() {
        // 163 bits: the 19-bit communication state at bits 149-168 cannot be sliced out.
        InvalidMessage e = assertThrows(InvalidMessage.class, () -> AISMessageFactory.create(null, null, null, nmea(message(1, 163))));
        assertTrue(e.getMessage().contains("PositionReportClassAScheduled"), e.getMessage());
    }

    @ParameterizedTest(name = "message ID {0}")
    @ValueSource(ints = {0, 29, 45, 59, 60, 63})
    public void rejectsUndefinedMessageIds(int type) {
        assertThrows(InvalidMessage.class, () -> AISMessageFactory.create(null, null, null, nmea(message(type, 168))));
    }

    @Test
    public void rejectsPayloadTooShortToHoldAMessageId() {
        assertThrows(InvalidMessage.class, () -> AISMessageFactory.create(null, null, null, nmea(bits().u(0, 4).toBitString())));
    }

    @Test
    public void streamReaderSkipsMalformedSentencesAndKeepsDecoding() {
        BitString valid = message(1, 168);
        List<String> input = new ArrayList<>(List.of(
                sentence(valid),
                sentence(message(1, 163)),
                withChecksum("AIVDM,1,1,,A,15MqdBP000G@qoLEi69PVGaN0D0=,6"),
                sentence(message(40, 168)),
                sentence(message(18, 174)),
                sentence(valid)));
        List<AISMessage> decoded = new ArrayList<>();

        new AISInputStreamReader(input, decoded::add).run();

        assertEquals(2, decoded.size());
        decoded.forEach(m -> assertEquals(AISMessageType.PositionReportClassAScheduled, m.getMessageType()));
    }
}
