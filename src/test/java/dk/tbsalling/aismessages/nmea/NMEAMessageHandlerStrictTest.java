package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NMEAMessageHandlerStrictTest {

    @Mock
    private Consumer<AISMessage> strictHandler;

    @Test
    public void strictHandlerRejectsInvalidChecksum() {
        // Arrange
        NMEAMessageHandlerStrict strictReceiver = new NMEAMessageHandlerStrict("TEST", strictHandler);
        NMEAMessage messageWithInvalidChecksum = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3B");

        // Act
        strictReceiver.accept(messageWithInvalidChecksum);

        // Assert - message should not be processed
        verify(strictHandler, never()).accept(any(AISMessage.class));
    }

    @Test
    public void strictHandlerProcessesValidChecksum() {
        // Arrange
        NMEAMessageHandlerStrict strictReceiver = new NMEAMessageHandlerStrict("TEST", strictHandler);
        NMEAMessage messageWithValidChecksum = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        ArgumentCaptor<AISMessage> aisMessage = ArgumentCaptor.forClass(AISMessage.class);

        // Act
        strictReceiver.accept(messageWithValidChecksum);

        // Assert
        verify(strictHandler, times(1)).accept(aisMessage.capture());
        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getValue().getMessageType());
    }
}
