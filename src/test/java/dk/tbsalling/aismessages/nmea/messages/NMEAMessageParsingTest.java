package dk.tbsalling.aismessages.nmea.messages;

import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import dk.tbsalling.aismessages.nmea.exceptions.UnsupportedMessageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedHashMap;
import java.util.Map;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Parsing of the IEC 61162-1 {@code !AIVDM}/{@code !AIVDO} sentence envelope by {@link NMEAMessage}. Sentences are
 * synthetic.
 */
public class NMEAMessageParsingTest {

    private static final BitString PAYLOAD = message(1, 168);
    private static final String ARMOURED = dk.tbsalling.aismessages.nmea.NMEAArmouring.encode(PAYLOAD);

    @Test
    public void parsesEveryField() {
        NMEAMessage m = new NMEAMessage(withChecksum("AIVDO,2,1,7,B," + ARMOURED + ",0"));

        assertEquals("AIVDO", m.getMessageType());
        assertEquals(2, m.getNumberOfFragments());
        assertEquals(1, m.getFragmentNumber());
        assertEquals(7, m.getSequenceNumber());
        assertEquals("B", m.getRadioChannelCode());
        assertEquals(ARMOURED, m.getEncodedPayload());
        assertEquals(0, m.getFillBits());
        assertTrue(m.isChecksumValid());
        assertNull(m.getTagBlock());
    }

    @Test
    public void blankFieldsBecomeSentinels() {
        NMEAMessage m = new NMEAMessage(withChecksum("AIVDM,,,,,,"));

        assertEquals(-1, m.getNumberOfFragments());
        assertEquals(-1, m.getFragmentNumber());
        assertEquals(-1, m.getSequenceNumber());
        assertNull(m.getRadioChannelCode());
        assertNull(m.getEncodedPayload());
        assertEquals(-1, m.getFillBits());
    }

    @Test
    public void parsesAndStripsTagBlock() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("c", "1700000000");
        params.put("s", "station1");
        String sentence = sentence(PAYLOAD);

        NMEAMessage m = new NMEAMessage(tagBlock(params) + sentence);

        assertNotNull(m.getTagBlock());
        assertEquals(1700000000L, m.getTagBlock().getTimestamp());
        assertEquals("station1", m.getTagBlock().getSourceId());
        assertEquals(sentence, m.getRawMessage());
    }

    @ParameterizedTest(name = "\"{0}\"")
    @ValueSource(strings = {
            "AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*26",   // no leading '!'
            "!AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0",      // no checksum
            "!AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*2",    // one-digit checksum
            "!AIVDM,1,1,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*26",    // six fields
            "!AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*3*26", // extra '*' in the last field
            ""})
    public void rejectsMalformedEnvelope(String input) {
        assertThrows(NMEAParseException.class, () -> new NMEAMessage(input));
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"AIVDX", "GPABC", "AIVD", "AIVDMX"})
    public void rejectsUnsupportedSentenceTypes(String type) {
        String sentence = withChecksum(type + ",1,1,,A," + ARMOURED + ",0");

        assertThrows(UnsupportedMessageType.class, () -> new NMEAMessage(sentence));
    }

    @ParameterizedTest(name = "\"{0}\"")
    @ValueSource(strings = {"AIVDM,X,1,,A,%s,0", "AIVDM,1,Y,,A,%s,0", "AIVDM,1,1,Z,A,%s,0", "AIVDM,1,1,,A,%s,F"})
    public void rejectsNonNumericNumericFields(String template) {
        String sentence = withChecksum(template.formatted(ARMOURED));

        NMEAParseException e = assertThrows(NMEAParseException.class, () -> new NMEAMessage(sentence));
        assertTrue(e.getMessage().contains("not a number"), e.getMessage());
    }
}
