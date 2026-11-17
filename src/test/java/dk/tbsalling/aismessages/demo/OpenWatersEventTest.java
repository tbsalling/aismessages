package dk.tbsalling.aismessages.demo;

import dk.tbsalling.aismessages.AISMessageDecoder;
import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.ShipAndVoyageData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

/** The JSON frame reader of {@link WebSocketDemoApp}. Frames and sentences are synthetic. */
public class OpenWatersEventTest {

    /** A JSON string literal for {@code s}, escaping backslashes and quotes as the stream does. */
    private static String json(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static String event(List<String> sentences) {
        String array = String.join(", ", sentences.stream().map(OpenWatersEventTest::json).toList());
        return "{\"type\": \"event\", \"id\": \"15f3\", \"source\": \"synthetic\", \"msg_type\": \"PositionReport\", \"nmea\": [" + array + "], \"message\": {\"type\": \"nested\"}}";
    }

    @Test
    public void readsTheSentenceOfAnEventAndDecodesIt() {
        String sentence = sentence(message(1, 168));

        List<String> sentences = OpenWatersEvent.nmeaSentences(event(List.of(sentence)));

        assertEquals(List.of(sentence), sentences);
        assertEquals(1, AISMessageDecoder.decode(sentences).size());
    }

    @Test
    public void unescapesTagBlocks() {
        String withTagBlock = "\\s:2573010,c:1787234980*03\\" + sentence(message(3, 168));

        assertEquals(List.of(withTagBlock), OpenWatersEvent.nmeaSentences(event(List.of(withTagBlock))));
    }

    @Test
    public void keepsAllFragmentsOfAMultiSentenceMessageInOrder() {
        List<String> fragments = fragments(bits().u(5, 6).u(0, 2).u(219000501, 30).u(3, 2).u(9123456, 30).text("OXYZ", 7).text("FROM THE STREAM", 20).u(70, 8)
                .u(100, 9).u(20, 9).u(5, 6).u(5, 6).u(1, 4).u(6, 4).u(15, 5).u(12, 5).u(30, 6).u(85, 8).text("AARHUS", 20).b(false).u(0, 1).toBitString(), 7, 40);
        assertTrue(fragments.size() > 1);

        List<AISMessage> decoded = AISMessageDecoder.decode(OpenWatersEvent.nmeaSentences(event(fragments)));

        assertEquals("FROM THE STREAM", ((ShipAndVoyageData) decoded.get(0)).getShipName());
    }

    @Test
    public void handlesUnicodeAndSlashEscapes() {
        assertEquals(List.of("a/bå"), OpenWatersEvent.nmeaSentences("{\"type\":\"event\",\"nmea\":[\"a\\/b\\u00e5\"]}"));
    }

    @Test
    public void otherFramesAndMissingArraysGiveNoSentences() {
        assertEquals("welcome", OpenWatersEvent.type("{\"type\":\"welcome\",\"nmea\":[\"x\"]}"));
        assertTrue(OpenWatersEvent.nmeaSentences("{\"type\":\"welcome\",\"nmea\":[\"x\"]}").isEmpty());
        assertTrue(OpenWatersEvent.nmeaSentences("{\"type\":\"event\",\"mmsi\":1}").isEmpty());
        assertTrue(OpenWatersEvent.nmeaSentences("{\"type\":\"event\",\"nmea\":[]}").isEmpty());
        assertNull(OpenWatersEvent.type("{}"));
    }

    @Test
    public void boxesAreValidatedAgainstTheAnonymousAreaLimit() {
        assertArrayEquals(new double[]{54.5, 8.0, 58.0, 13.0}, WebSocketDemoApp.parseBox("54.5,8.0,58.0,13.0"));
        assertThrows(IllegalArgumentException.class, () -> WebSocketDemoApp.parseBox("50,0,61,10"));
        assertThrows(IllegalArgumentException.class, () -> WebSocketDemoApp.parseBox("58,8,54,13"));
        assertThrows(IllegalArgumentException.class, () -> WebSocketDemoApp.parseBox("1,2,3"));
    }
}
