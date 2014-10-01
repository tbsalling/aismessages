package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.test.helpers.ArgumentCaptor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NMEAMessageHandlerTest {
    private final static Mockery context = new JUnit4Mockery();

    private static Consumer<AISMessage> aisMessageHandler;
    private static NMEAMessageHandler aisMessageReceiver;

    @BeforeClass
    public static void setUp() {
        aisMessageHandler = (Consumer<AISMessage>) context.mock(Consumer.class);
        aisMessageReceiver = new NMEAMessageHandler("TEST", aisMessageHandler);
    }

    @Test
    public void canHandleUnfragmentedMessageReceived() {
        NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");

        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();

        context.checking(new Expectations() {{
            oneOf(aisMessageHandler).accept(with(aisMessage.getMatcher()));
        }});

        aisMessageReceiver.accept(unfragmentedNMEAMessage);

        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getCapturedObject().getMessageType());
    }

    @Test
    public void canHandleFragmentedMessageReceived() {
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
    public void canFlushEmpty() {
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

        ArrayList<NMEAMessage> flush = aisMessageReceiver.flush();

        assertNotNull(flush);
        assertEquals(0, flush.size());
    }

    @Test
    public void canFlushUnhandled() {
        NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        NMEAMessage fragmentedNMEAMessage1 = NMEAMessage.fromString("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");

        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();

        context.checking(new Expectations() {{
            exactly(2).of(aisMessageHandler).accept(with(aisMessage.getMatcher()));
        }});

        aisMessageReceiver.accept(unfragmentedNMEAMessage);
        aisMessageReceiver.accept(fragmentedNMEAMessage1);

        ArrayList<NMEAMessage> flush = aisMessageReceiver.flush();

        assertNotNull(flush);
        assertEquals(1, flush.size());
        assertEquals(fragmentedNMEAMessage1, flush.get(0));
    }
}