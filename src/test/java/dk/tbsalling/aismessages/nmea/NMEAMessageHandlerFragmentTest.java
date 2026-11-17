package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.ShipAndVoyageData;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Fragment reassembly in {@link NMEAMessageHandler} and checksum handling in {@link NMEAMessageHandlerStrict}.
 * Multi-fragment messages are synthetic Message 5 payloads (424 bits, ITU-R M.1371-6 Table 50).
 */
public class NMEAMessageHandlerFragmentTest {

    private final List<AISMessage> received = new ArrayList<>();
    private final NMEAMessageHandler handler = new NMEAMessageHandler("TEST", received::add);

    private static BitString shipAndVoyageData(String name) {
        return bits().u(5, 6).u(0, 2).u(219000501, 30).u(3, 2).u(9123456, 30).text("OXYZ", 7).text(name, 20).u(70, 8)
                .u(100, 9).u(20, 9).u(5, 6).u(5, 6).u(1, 4).u(6, 4).u(15, 5).u(12, 5).u(30, 6).u(85, 8).text("AARHUS", 20)
                .b(false).u(0, 1).toBitString();
    }

    private static List<NMEAMessage> nmea(List<String> sentences) {
        return sentences.stream().map(NMEAMessage::new).toList();
    }

    @Test
    public void reassemblesThreeFragments() {
        List<NMEAMessage> fragments = nmea(fragments(shipAndVoyageData("THREE PARTS"), 1, 30));
        assertEquals(3, fragments.size());

        fragments.forEach(handler);

        assertEquals(1, received.size());
        ShipAndVoyageData message = (ShipAndVoyageData) received.get(0);
        assertEquals("THREE PARTS", message.getShipName());
        assertEquals("AARHUS", message.getDestination());
        assertEquals(dk.tbsalling.aismessages.ais.messages.types.AisVersionIndicator.ITU_R_M1371_6, message.getAisVersionIndicator());
        assertTrue(handler.flush().isEmpty());
    }

    @Test
    public void outOfOrderFragmentDiscardsBufferThenRecovers() {
        List<NMEAMessage> fragments = nmea(fragments(shipAndVoyageData("IN ORDER"), 2, 50));

        handler.accept(fragments.get(1));   // fragment 2 before fragment 1: discarded
        assertTrue(handler.flush().isEmpty());
        fragments.forEach(handler);

        assertEquals(1, received.size());
    }

    @Test
    public void fragmentNumberAboveCountDiscardsBuffer() {
        List<NMEAMessage> fragments = nmea(fragments(shipAndVoyageData("TOO HIGH"), 3, 50));
        handler.accept(fragments.get(0));

        handler.accept(new NMEAMessage(withChecksum("AIVDM,2,3,3,A,0000,0")));

        assertTrue(handler.flush().isEmpty());
        assertTrue(received.isEmpty());
    }

    @Test
    public void blankFragmentNumberDiscardsBuffer() {
        List<NMEAMessage> fragments = nmea(fragments(shipAndVoyageData("BLANK"), 4, 50));
        handler.accept(fragments.get(0));

        handler.accept(new NMEAMessage(withChecksum("AIVDM,2,,4,A,0000,0")));

        assertTrue(handler.flush().isEmpty());
        assertTrue(received.isEmpty());
    }

    @Test
    public void unfragmentedSentenceBetweenFragmentsDiscardsPendingFragments() {
        // Known limitation: fragments are not keyed by sequence id or channel, so they must arrive consecutively.
        List<NMEAMessage> fragments = nmea(fragments(shipAndVoyageData("INTERLEAVED"), 5, 50));

        handler.accept(fragments.get(0));
        handler.accept(dk.tbsalling.aismessages.testsupport.NmeaSentences.nmea(message(1, 168)));
        handler.accept(fragments.get(1));

        assertEquals(1, received.size());
        assertTrue(handler.flush().isEmpty());
    }

    @Test
    public void failedDecodeOfCompleteMessageDoesNotPoisonTheNextOne() {
        BitString tooShort = message(5, 420);
        List<NMEAMessage> undecodable = nmea(fragments(tooShort, 6, 50));
        List<NMEAMessage> valid = nmea(fragments(shipAndVoyageData("AFTER FAILURE"), 7, 50));

        handler.accept(undecodable.get(0));
        assertThrows(InvalidMessage.class, () -> handler.accept(undecodable.get(1)));
        valid.forEach(handler);

        assertEquals(1, received.size());
        assertEquals("AFTER FAILURE", ((ShipAndVoyageData) received.get(0)).getShipName());
    }

    @Test
    public void failedDecodeOfSingleSentenceDoesNotPoisonTheNextFragmentedMessage() {
        List<NMEAMessage> abandoned = nmea(fragments(shipAndVoyageData("ABANDONED"), 8, 50));
        List<NMEAMessage> valid = nmea(fragments(shipAndVoyageData("NEXT"), 9, 50));

        handler.accept(abandoned.get(0));
        assertThrows(InvalidMessage.class, () -> handler.accept(dk.tbsalling.aismessages.testsupport.NmeaSentences.nmea(message(1, 163))));
        valid.forEach(handler);

        assertEquals(1, received.size());
        assertEquals("NEXT", ((ShipAndVoyageData) received.get(0)).getShipName());
    }

    @Test
    public void deliversToEveryReceiver() {
        List<AISMessage> second = new ArrayList<>();
        List<AISMessage> third = new ArrayList<>();
        NMEAMessageHandler h = new NMEAMessageHandler("TEST", received::add, second::add);
        h.addAisMessageReceiver(third::add);

        h.accept(dk.tbsalling.aismessages.testsupport.NmeaSentences.nmea(message(1, 168)));

        assertEquals(1, received.size());
        assertEquals(1, second.size());
        assertEquals(1, third.size());
    }

    @Test
    public void lenientHandlerDecodesSentenceWithWrongChecksumButStrictHandlerDropsIt() {
        String good = sentence(message(1, 168));
        String bad = good.substring(0, good.length() - 2) + (good.endsWith("00") ? "01" : "00");
        List<AISMessage> strictReceived = new ArrayList<>();

        handler.accept(new NMEAMessage(bad));
        new NMEAMessageHandlerStrict("TEST", strictReceived::add).accept(new NMEAMessage(bad));

        assertEquals(1, received.size());
        assertTrue(strictReceived.isEmpty());
    }

    @Test
    public void strictHandlerReassemblesValidFragments() {
        List<AISMessage> strictReceived = new ArrayList<>();
        NMEAMessageHandlerStrict strict = new NMEAMessageHandlerStrict("TEST", strictReceived::add);

        nmea(fragments(shipAndVoyageData("STRICT"), 8, 40)).forEach(strict);

        assertEquals(1, strictReceived.size());
    }
}
