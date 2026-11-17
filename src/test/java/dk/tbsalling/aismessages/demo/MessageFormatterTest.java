package dk.tbsalling.aismessages.demo;

import dk.tbsalling.aismessages.AISMessageDecoder;
import org.junit.jupiter.api.Test;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

/** The human-readable output of {@link WebSocketDemoApp}. Sentences are synthetic. */
public class MessageFormatterTest {

    @Test
    public void humanizesEnumNamesKeepingAcronyms() {
        assertEquals("Position report class A scheduled", MessageFormatter.humanize("PositionReportClassAScheduled"));
        assertEquals("Class BCS static data report", MessageFormatter.humanize("ClassBCSStaticDataReport"));
        assertEquals("Underway using engine", MessageFormatter.humanize("UnderwayUsingEngine"));
    }

    @Test
    public void formatsAPositionReport() {
        // Message 1 (Table 46): under way using engine, 12.3 kn, 55.5°N 11.25°E, course 123.4°, heading 120°.
        String sentence = sentence(bits().u(1, 6).u(0, 2).u(219000123, 30).u(0, 4).s(0, 8).u(123, 10).b(true).s(6750000, 28).s(33300000, 27).u(1234, 12).u(120, 9).u(30, 6)
                .u(0, 2).u(0, 3).b(false).u(0, 19).toBitString());

        String line = MessageFormatter.format(AISMessageDecoder.decode(sentence));

        assertTrue(line.startsWith("MMSI 219000123  Position report class A scheduled"), line);
        assertTrue(line.contains("55.50000°N 11.25000°E"), line);
        assertTrue(line.contains("12.3 kn"), line);
        assertTrue(line.contains("course 123.4°"), line);
        assertTrue(line.contains("heading 120°"), line);
        assertTrue(line.endsWith("Underway using engine"), line);
    }

    @Test
    public void leavesOutValuesThatAreNotAvailable() {
        // Message 1 with position, speed, course and heading not available.
        String sentence = sentence(bits().u(1, 6).u(0, 2).u(219000124, 30).u(15, 4).s(-128, 8).u(1023, 10).b(false).s(181 * 600000, 28).s(91 * 600000, 27).u(3600, 12).u(511, 9).u(60, 6)
                .u(0, 2).u(0, 3).b(false).u(0, 19).toBitString());

        String line = MessageFormatter.format(AISMessageDecoder.decode(sentence));

        assertFalse(line.contains("°N") || line.contains("kn") || line.contains("course") || line.contains("heading") || line.contains("NaN") || line.contains("null"), line);
    }
}
