package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.PositionReportClassAScheduled;
import dk.tbsalling.aismessages.ais.messages.ShipAndVoyageData;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AISMessageDecoderTest {

    @Test
    public void decodeSingleSentenceReturnsMessage() {
        AISMessage aisMessage = AISMessageDecoder.decode("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");

        assertNotNull(aisMessage);
        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getMessageType());
        assertEquals(366898250, aisMessage.getSourceMmsi().getMmsi());
    }

    @Test
    public void decodeVarargsReturnsAllDecodedMessages() {
        List<AISMessage> decoded = AISMessageDecoder.decode(
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "!AIVDM,1,1,,B,58LAM242B9POUKWWW<0a>0<4E<58,0*6E"
        );

        assertEquals(2, decoded.size());
        assertTrue(decoded.stream().allMatch(message -> message.getMessageType() == AISMessageType.PositionReportClassAScheduled));
    }

    @Test
    public void decodeMultipleSentencesReturnsAllDecodedMessages() {
        List<AISMessage> decoded = AISMessageDecoder.decode(List.of(
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "!AIVDM,1,1,,B,58LAM242B9POUKWWW<0a>0<4E<58,0*6E"
        ));

        assertEquals(2, decoded.size());
        assertTrue(decoded.stream().allMatch(message -> message.getMessageType() == AISMessageType.PositionReportClassAScheduled));
    }

    @Test
    public void decodeStreamReadsAndDecodesEachLine() {
        String stream = String.join("\n",
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58",
                "not an ais sentence"
        );

        List<AISMessage> decoded = AISMessageDecoder.decode(new ByteArrayInputStream(stream.getBytes(StandardCharsets.UTF_8)));

        assertEquals(2, decoded.size());
        assertTrue(decoded.stream().allMatch(message -> message instanceof PositionReportClassAScheduled));
    }

    @Test
    public void tryDecodeSingleSentenceReturnsMessage() {
        Optional<AISMessage> decoded = AISMessageDecoder.tryDecode("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");

        assertTrue(decoded.isPresent());
        assertEquals(AISMessageType.PositionReportClassAScheduled, decoded.get().getMessageType());
        assertEquals(366898250, decoded.get().getSourceMmsi().getMmsi());
    }

    @Test
    public void tryDecodeUnparseableInputReturnsEmpty() {
        // The behavioural contrast with decodeSingleSentenceRejectsUnparseableInput.
        Optional<AISMessage> decoded = assertDoesNotThrow(() -> AISMessageDecoder.tryDecode("INVALID"));

        assertTrue(decoded.isEmpty());
    }

    @Test
    public void tryDecodeNullSentenceReturnsEmpty() {
        assertTrue(AISMessageDecoder.tryDecode((String) null).isEmpty());
    }

    @Test
    public void tryDecodeVarargsReturnsOnlyDecodableMessages() {
        Optional<List<AISMessage>> decoded = AISMessageDecoder.tryDecode(
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "INVALID",
                "!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58"
        );

        assertTrue(decoded.isPresent());
        assertEquals(2, decoded.get().size());
    }

    @Test
    public void tryDecodeVarargsToleratesNullElements() {
        // This is what the collection overloads buy over the (hardened, but still
        // null-rejecting) decode overloads: decode(a, null, b) throws NullPointerException.
        Optional<List<AISMessage>> decoded = AISMessageDecoder.tryDecode(
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                null,
                "!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58"
        );

        assertTrue(decoded.isPresent());
        assertEquals(2, decoded.get().size());
    }

    @Test
    public void tryDecodeNullListReturnsEmpty() {
        assertTrue(AISMessageDecoder.tryDecode((List<String>) null).isEmpty());
    }

    @Test
    public void tryDecodeReturnsEmptyWhenNothingDecodes() {
        assertTrue(AISMessageDecoder.tryDecode(List.of("INVALID", "ALSO INVALID")).isEmpty());
    }

    @Test
    public void tryDecodeEmptyListReturnsEmpty() {
        assertTrue(AISMessageDecoder.tryDecode(List.of()).isEmpty());
    }

    @Test
    public void tryDecodeStreamReturnsDecodedMessages() {
        String stream = String.join("\n",
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58",
                "not an ais sentence"
        );

        Optional<List<AISMessage>> decoded = AISMessageDecoder.tryDecode(
                new ByteArrayInputStream(stream.getBytes(StandardCharsets.UTF_8)));

        assertTrue(decoded.isPresent());
        assertEquals(2, decoded.get().size());
    }

    @Test
    public void tryDecodeStreamReturnsEmptyWhenStreamCannotBeRead() {
        InputStream failing = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }
        };

        Optional<List<AISMessage>> decoded = assertDoesNotThrow(() -> AISMessageDecoder.tryDecode(failing));

        assertTrue(decoded.isEmpty());
    }

    @Test
    public void tryDecodeStreamReturnsEmptyWhenStreamHoldsNoAisMessages() {
        // Pins the uniform rule: empty means "no AIS message", never Optional.of([]).
        // A readable-but-empty result must not be distinguishable from a read failure.
        Optional<List<AISMessage>> decoded = AISMessageDecoder.tryDecode(
                new ByteArrayInputStream("not an ais sentence".getBytes(StandardCharsets.UTF_8)));

        assertTrue(decoded.isEmpty());
    }

    @Test
    public void tryDecodeReassemblesMultiFragmentMessage() {
        // Both fragments must reach the same call: reassembly state is per-call.
        Optional<List<AISMessage>> decoded = AISMessageDecoder.tryDecode(List.of(
                "!AIVDM,2,1,1,A,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09",
                "!AIVDM,2,2,1,A,888888888888880,2*2E"
        ));

        assertTrue(decoded.isPresent());
        assertEquals(1, decoded.get().size());
        assertInstanceOf(ShipAndVoyageData.class, decoded.get().getFirst());
    }

    @Test
    public void tryDecodeSingleFragmentOfMultiFragmentMessageReturnsEmpty() {
        // Indistinguishable from malformed input, by design.
        assertTrue(AISMessageDecoder.tryDecode(
                "!AIVDM,2,1,1,A,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09").isEmpty());
    }

    @Test
    public void decodeIgnoresSentenceWhoseFieldsAreNotNumeric() {
        // "x" as fragment count passes NMEAMessage's regex and field-count checks,
        // but makes Integer.parseInt throw deeper in the pipeline.
        List<AISMessage> decoded = AISMessageDecoder.decode(List.of(
                "!AIVDM,x,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A",
                "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A"
        ));

        assertEquals(1, decoded.size());
        assertEquals(AISMessageType.PositionReportClassAScheduled, decoded.getFirst().getMessageType());
    }

    @Test
    public void decodeSingleSentenceRejectsUnparseableInput() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> AISMessageDecoder.decode("INVALID"));

        assertTrue(exception.getMessage().contains("No AIS message could be decoded"));
    }
}
