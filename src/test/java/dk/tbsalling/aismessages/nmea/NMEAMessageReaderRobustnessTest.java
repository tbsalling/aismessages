package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link NMEAMessageInputStreamReader}: constructors, stopping, I/O failure, and that no kind of rejected sentence
 * stops the reader. Sentences are synthetic.
 */
public class NMEAMessageReaderRobustnessTest {

    private static final String VALID = sentence(message(1, 168));

    /** One sentence of every kind the reader must log and skip. */
    private static List<String> rejectedSentences() {
        String wrongChecksumTagBlock = "\\s:STATION1*00\\" + VALID;
        return List.of(
                "garbage",                                           // NMEAParseException
                withChecksum("GPGGA,1,1,,A,0000,0"),                 // UnsupportedMessageType
                sentence(message(1, 163)),                           // InvalidMessage
                withChecksum("AIVDM,X,1,,A,0000,0"),                 // non-numeric fragment count
                wrongChecksumTagBlock,                               // tag block with wrong checksum
                tagBlock(Map.of("c", "abc")) + VALID,               // tag block with non-numeric timestamp
                tagBlock(Map.of("n", "2147483648")) + VALID);       // tag block with a line count beyond the int range
    }

    private static List<String> mixed() {
        List<String> input = new ArrayList<>();
        input.add(VALID);
        for (String rejected : rejectedSentences()) {
            input.add(rejected);
            input.add(VALID);
        }
        return input;
    }

    @Test
    public void skipsEveryKindOfRejectedSentenceFromList() {
        List<AISMessage> decoded = new ArrayList<>();

        new NMEAMessageInputStreamReader(mixed(), new NMEAMessageHandler("TEST", decoded::add)).run();

        assertEquals(rejectedSentences().size() + 1, decoded.size());
    }

    @Test
    public void skipsEveryKindOfRejectedSentenceFromStream() {
        List<AISMessage> decoded = new ArrayList<>();
        InputStream in = new ByteArrayInputStream(String.join("\n", mixed()).getBytes(StandardCharsets.UTF_8));

        new NMEAMessageInputStreamReader(in, new NMEAMessageHandler("TEST", decoded::add)).run();

        assertEquals(rejectedSentences().size() + 1, decoded.size());
    }

    @Test
    public void appliesPreProcessorToStreamLines() {
        List<NMEAMessage> received = new ArrayList<>();
        String lines = "2026-11-17T10:00:00Z " + VALID + "\n2026-11-17T10:00:01Z " + VALID + "\n";
        Function<String, String> stripPrefix = s -> s.substring(s.indexOf('!'));

        new NMEAMessageInputStreamReader(new ByteArrayInputStream(lines.getBytes(StandardCharsets.UTF_8)), stripPrefix, received::add).run();

        assertEquals(2, received.size());
    }

    @Test
    public void stopsAfterStopIsRequested() {
        List<NMEAMessage> received = new ArrayList<>();
        AtomicReference<NMEAMessageInputStreamReader> reader = new AtomicReference<>();
        reader.set(new NMEAMessageInputStreamReader(new ArrayList<>(List.of(VALID, VALID, VALID)), m -> {
            received.add(m);
            reader.get().requestStop();
        }));

        reader.get().run();

        assertEquals(1, received.size());
        assertTrue(reader.get().isStopRequested());
    }

    @Test
    public void readFailureSurfacesAsRuntimeException() {
        InputStream failing = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("connection reset");
            }
        };

        RuntimeException e = assertThrows(RuntimeException.class, () -> new NMEAMessageInputStreamReader(failing, m -> {}).run());
        assertInstanceOf(IOException.class, e.getCause());
    }

    @Test
    public void listConstructorRejectsNullArguments() {
        List<String> lines = List.of(VALID);

        assertThrows(NullPointerException.class, () -> new NMEAMessageInputStreamReader((List<String>) null, m -> {}));
        assertThrows(NullPointerException.class, () -> new NMEAMessageInputStreamReader(lines, null, m -> {}));
        assertThrows(NullPointerException.class, () -> new NMEAMessageInputStreamReader(lines, Function.identity(), null));
    }
}
