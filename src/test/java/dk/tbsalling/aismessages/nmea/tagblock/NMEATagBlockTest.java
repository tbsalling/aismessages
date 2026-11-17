package dk.tbsalling.aismessages.nmea.tagblock;

import dk.tbsalling.aismessages.nmea.exceptions.InvalidTagBlock;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedHashMap;
import java.util.Map;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.tagBlock;
import static org.junit.jupiter.api.Assertions.*;

/** IEC 61162-1 tag blocks preceding an NMEA sentence, parsed by {@link NMEATagBlock}. */
public class NMEATagBlockTest {

    private static Map<String, String> allParameters() {
        Map<String, String> p = new LinkedHashMap<>();
        p.put("c", "1700000000");
        p.put("d", "DEST1");
        p.put("g", "1-2-34");
        p.put("n", "42");
        p.put("r", "1700000001");
        p.put("s", "STATION1");
        p.put("t", "Hello");
        return p;
    }

    @Test
    public void parsesEveryParameterCode() {
        NMEATagBlock t = NMEATagBlock.fromString(tagBlock(allParameters()));

        assertEquals(1700000000L, t.getTimestamp());
        assertEquals("DEST1", t.getDestinationId());
        assertEquals("1-2-34", t.getSentenceGrouping());
        assertEquals(42, t.getLineCount());
        assertEquals(1700000001L, t.getRelativeTime());
        assertEquals("STATION1", t.getSourceId());
        assertEquals("Hello", t.getText());
        assertTrue(t.getValid());
        assertEquals(7, t.getParameterMap().size());
    }

    @Test
    public void absentParametersAreNull() {
        NMEATagBlock t = NMEATagBlock.fromString(tagBlock(Map.of("s", "ONLY")));

        assertEquals("ONLY", t.getSourceId());
        assertNull(t.getTimestamp());
        assertNull(t.getDestinationId());
        assertNull(t.getSentenceGrouping());
        assertNull(t.getLineCount());
        assertNull(t.getRelativeTime());
        assertNull(t.getText());
    }

    @Test
    public void ignoresUnknownParameterCodes() {
        Map<String, String> p = new LinkedHashMap<>();
        p.put("x", "unknown");
        p.put("s", "STATION1");

        NMEATagBlock t = NMEATagBlock.fromString(tagBlock(p));

        assertEquals("STATION1", t.getSourceId());
        assertEquals(1, t.getParameterMap().size());
    }

    @Test
    public void toStringListsTheParameters() {
        String s = NMEATagBlock.fromString(tagBlock(allParameters())).toString();

        assertTrue(s.contains("1700000000"), s);
        assertTrue(s.contains("STATION1"), s);
        assertTrue(s.contains("Hello"), s);
    }

    @Test
    public void rejectsWrongChecksum() {
        String good = tagBlock(Map.of("s", "STATION1"));
        String bad = good.substring(0, good.length() - 3) + (good.charAt(good.length() - 3) == '0' ? '1' : '0') + good.charAt(good.length() - 2) + "\\";

        assertThrows(InvalidTagBlock.class, () -> NMEATagBlock.fromString(bad));
    }

    @ParameterizedTest(name = "\"{0}\"")
    @ValueSource(strings = {
            "\\s:STATION1*1F",         // no closing backslash
            "s:STATION1*1F\\",         // no opening backslash
            "\\s:STATION1\\",          // no checksum
            "\\s:A*12*34\\",           // two checksum separators
            "\\sSTATION1*00\\"})       // parameter without colon
    public void rejectsMalformedTagBlocks(String input) {
        assertThrows(NMEAParseException.class, () -> NMEATagBlock.fromString(input));
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"c", "n", "r"})
    public void rejectsNonNumericNumericParameters(String code) {
        String input = tagBlock(Map.of(code, "abc"));

        NMEAParseException e = assertThrows(NMEAParseException.class, () -> NMEATagBlock.fromString(input));
        assertTrue(e.getMessage().contains("not a number"), e.getMessage());
    }
}
