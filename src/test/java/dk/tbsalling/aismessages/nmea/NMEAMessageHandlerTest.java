package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class NMEAMessageHandlerTest {

    @Mock
    private Consumer<AISMessage> aisMessageHandler;
    
    private NMEAMessageHandler aisMessageReceiver;

    @BeforeEach
    public void setUp() {
        aisMessageReceiver = new NMEAMessageHandler("TEST", aisMessageHandler);
    }

    @Test
    public void a_canHandleUnfragmentedMessageReceived() {
        // Arrange
        NMEAMessage unfragmentedNMEAMessage = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        ArgumentCaptor<AISMessage> aisMessage = ArgumentCaptor.forClass(AISMessage.class);

        // Act
        aisMessageReceiver.accept(unfragmentedNMEAMessage);

        // Assert
        verify(aisMessageHandler, times(1)).accept(aisMessage.capture());
        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getValue().getMessageType());
    }

    @Test
    public void b_canHandleFragmentedMessageReceived() {
        // Arrange
        NMEAMessage fragmentedNMEAMessage1 = new NMEAMessage("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");
        NMEAMessage fragmentedNMEAMessage2 = new NMEAMessage("!AIVDM,2,2,3,B,p=Mh00000000000,2*4C");
        ArgumentCaptor<AISMessage> aisMessage = ArgumentCaptor.forClass(AISMessage.class);

        // Act
        aisMessageReceiver.accept(fragmentedNMEAMessage1);
        aisMessageReceiver.accept(fragmentedNMEAMessage2);

        // Assert
        verify(aisMessageHandler, times(1)).accept(aisMessage.capture());
        assertEquals(AISMessageType.ShipAndVoyageRelatedData, aisMessage.getValue().getMessageType());
    }

    @Test
    public void c_canFlushEmpty() {
        // Arrange
        NMEAMessage unfragmentedNMEAMessage = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        NMEAMessage fragmentedNMEAMessage1 = new NMEAMessage("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");
        NMEAMessage fragmentedNMEAMessage2 = new NMEAMessage("!AIVDM,2,2,3,B,p=Mh00000000000,2*4C");

        // Act
        aisMessageReceiver.accept(unfragmentedNMEAMessage);
        aisMessageReceiver.accept(fragmentedNMEAMessage1);
        aisMessageReceiver.accept(fragmentedNMEAMessage2);
        List<NMEAMessage> flush = aisMessageReceiver.flush();

        // Assert
        verify(aisMessageHandler, times(2)).accept(any(AISMessage.class));
        assertNotNull(flush);
        assertEquals(0, flush.size());
    }

    @Test
    public void d_canFlushUnhandled() {
        // Arrange
        NMEAMessage unfragmentedNMEAMessage = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        NMEAMessage fragmentedNMEAMessage1 = new NMEAMessage("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");

        // Act
        aisMessageReceiver.accept(unfragmentedNMEAMessage);
        aisMessageReceiver.accept(fragmentedNMEAMessage1);
        List<NMEAMessage> flush = aisMessageReceiver.flush();

        // Assert
        verify(aisMessageHandler, times(1)).accept(any(AISMessage.class));
        assertNotNull(flush);
        assertEquals(1, flush.size());
        assertEquals(fragmentedNMEAMessage1, flush.get(0));
    }

    @Test
    public void canHandleInvalidFragmentMessageReceived() {
        // Arrange
        NMEAMessage invalidFragmentNMEAMessage = new NMEAMessage("!AIVDM,,1,,B,13cpFJ0P0100`lE4IIvW8@Ow`052p,0*53");

        // Act
        aisMessageReceiver.accept(invalidFragmentNMEAMessage);
        List<NMEAMessage> flush = aisMessageReceiver.flush();

        // Assert
        verify(aisMessageHandler, never()).accept(any(AISMessage.class));
        assertNotNull(flush);
        assertEquals(0, flush.size());
        assertDoesNotThrow(() -> {});
    }
}
