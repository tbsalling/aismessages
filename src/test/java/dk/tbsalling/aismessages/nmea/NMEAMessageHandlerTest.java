package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.test.helpers.ArgumentCaptor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class NMEAMessageHandlerTest {
    private final static Mockery context = new JUnit5Mockery();

    private static Consumer<AISMessage> aisMessageHandler;
    private static NMEAMessageHandler aisMessageReceiver;

    @SuppressWarnings("unchecked")
    @BeforeAll
    public static void setUp() {
        aisMessageHandler = (Consumer<AISMessage>) context.mock(Consumer.class);
        aisMessageReceiver = new NMEAMessageHandler("TEST", aisMessageHandler);
    }

    @Test
    public void a_canHandleUnfragmentedMessageReceived() {
        NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");

        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();

        context.checking(new Expectations() {{
            oneOf(aisMessageHandler).accept(with(aisMessage.getMatcher()));
        }});

        aisMessageReceiver.accept(unfragmentedNMEAMessage);

        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getCapturedObject().getMessageType());
    }

    @Test
    public void b_canHandleFragmentedMessageReceived() {
        NMEAMessage fragmentedNMEAMessage1 = NMEAMessage.fromString("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");
        NMEAMessage fragmentedNMEAMessage2 = NMEAMessage.fromString("!AIVDM,2,2,3,B,p=Mh00000000000,2*4C");

        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();

        context.checking(new Expectations() {{
            oneOf(aisMessageHandler).accept(with(aisMessage.getMatcher()));
        }});

        aisMessageReceiver.accept(fragmentedNMEAMessage1);
        aisMessageReceiver.accept(fragmentedNMEAMessage2);

        assertEquals(AISMessageType.ShipAndVoyageRelatedData, aisMessage.getCapturedObject().getMessageType());
    }

    @Test
    public void c_canFlushEmpty() {
        NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        NMEAMessage fragmentedNMEAMessage1 = NMEAMessage.fromString("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");
        NMEAMessage fragmentedNMEAMessage2 = NMEAMessage.fromString("!AIVDM,2,2,3,B,p=Mh00000000000,2*4C");

        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();

        context.checking(new Expectations() {{
            exactly(3).of(aisMessageHandler).accept(with(aisMessage.getMatcher()));
        }});

        aisMessageReceiver.accept(unfragmentedNMEAMessage);
        aisMessageReceiver.accept(fragmentedNMEAMessage1);
        aisMessageReceiver.accept(fragmentedNMEAMessage2);

        List<NMEAMessage> flush = aisMessageReceiver.flush();

        assertNotNull(flush);
        assertEquals(0, flush.size());
    }

    @Test
    public void d_canFlushUnhandled() {
        NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        NMEAMessage fragmentedNMEAMessage1 = NMEAMessage.fromString("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");

        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();

        context.checking(new Expectations() {{
            exactly(2).of(aisMessageHandler).accept(with(aisMessage.getMatcher()));
        }});

        aisMessageReceiver.accept(unfragmentedNMEAMessage);
        aisMessageReceiver.accept(fragmentedNMEAMessage1);

        List<NMEAMessage> flush = aisMessageReceiver.flush();

        assertNotNull(flush);
        assertEquals(1, flush.size());
        assertEquals(fragmentedNMEAMessage1, flush.get(0));
    }

    @Test
    public void canHandleInvalidFragmentMessageReceived() {
        NMEAMessage invalidFragmentNMEAMessage = NMEAMessage.fromString("!AIVDM,,1,,B,13cpFJ0P0100`lE4IIvW8@Ow`052p,0*53");

        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();

        context.checking(new Expectations() {{
            oneOf(aisMessageHandler).accept(with(aisMessage.getMatcher()));
        }});

        aisMessageReceiver.accept(invalidFragmentNMEAMessage);

        List<NMEAMessage> flush = aisMessageReceiver.flush();

        assertNotNull(flush);
        assertEquals(0, flush.size());
        assertDoesNotThrow(() -> {});
    }
}
