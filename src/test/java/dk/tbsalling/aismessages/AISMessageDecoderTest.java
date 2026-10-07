package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.PositionReportClassAScheduled;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    public void decodeSingleSentenceRejectsUnparseableInput() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> AISMessageDecoder.decode("INVALID"));

        assertTrue(exception.getMessage().contains("No AIS message could be decoded"));
    }
}
