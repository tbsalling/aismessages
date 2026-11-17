package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.message;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.sentence;
import static org.junit.jupiter.api.Assertions.*;

/** The public {@link AISInputStreamReader} entry point. Sentences are synthetic. */
public class AISInputStreamReaderTest {

    private static final String POSITION = sentence(message(1, 168));
    private static final String BASE_STATION = sentence(message(4, 168));

    @Test
    public void decodesFromList() {
        List<AISMessage> decoded = new ArrayList<>();

        new AISInputStreamReader(new ArrayList<>(List.of(POSITION, BASE_STATION)), decoded::add).run();

        assertEquals(List.of(AISMessageType.PositionReportClassAScheduled, AISMessageType.BaseStationReport), decoded.stream().map(AISMessage::getMessageType).toList());
    }

    @Test
    public void decodesFromInputStream() {
        List<AISMessage> decoded = new ArrayList<>();
        String lines = POSITION + "\n" + BASE_STATION + "\n";

        new AISInputStreamReader(new ByteArrayInputStream(lines.getBytes(StandardCharsets.UTF_8)), decoded::add).run();

        assertEquals(2, decoded.size());
    }

    @Test
    public void appliesPreProcessor() {
        List<AISMessage> decoded = new ArrayList<>();
        String lines = "rx1 " + POSITION + "\nrx2 " + BASE_STATION + "\n";

        new AISInputStreamReader(new ByteArrayInputStream(lines.getBytes(StandardCharsets.UTF_8)), s -> s.substring(4), decoded::add).run();

        assertEquals(2, decoded.size());
    }

    @Test
    public void stopRequestedBeforeRunProcessesNothing() {
        List<AISMessage> decoded = new ArrayList<>();
        AISInputStreamReader reader = new AISInputStreamReader(new ArrayList<>(List.of(POSITION, BASE_STATION)), decoded::add);

        reader.requestStop();
        reader.run();

        assertTrue(reader.isStopRequested());
        assertTrue(decoded.isEmpty());
    }
}
