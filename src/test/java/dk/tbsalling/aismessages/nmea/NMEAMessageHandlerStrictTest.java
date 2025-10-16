package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.test.helpers.ArgumentCaptor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class NMEAMessageHandlerStrictTest {

    @Test
    @SuppressWarnings("unchecked")
    public void strictHandlerRejectsInvalidChecksum() {
        // Arrange
        Mockery strictContext = new JUnit5Mockery();
        Consumer<AISMessage> strictHandler = (Consumer<AISMessage>) strictContext.mock(Consumer.class);
        NMEAMessageHandlerStrict strictReceiver = new NMEAMessageHandlerStrict("TEST", strictHandler);
        NMEAMessage messageWithInvalidChecksum = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3B");
        strictContext.checking(new Expectations() {{
            never(strictHandler).accept(with(any(AISMessage.class)));
        }});

        // Act
        strictReceiver.accept(messageWithInvalidChecksum);

        // Assert - message should not be processed (expectation is verified by jmock)
        strictContext.assertIsSatisfied();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void strictHandlerProcessesValidChecksum() {
        // Arrange
        Mockery strictContext = new JUnit5Mockery();
        Consumer<AISMessage> strictHandler = (Consumer<AISMessage>) strictContext.mock(Consumer.class);
        NMEAMessageHandlerStrict strictReceiver = new NMEAMessageHandlerStrict("TEST", strictHandler);
        NMEAMessage messageWithValidChecksum = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        final ArgumentCaptor<AISMessage> aisMessage = new ArgumentCaptor<>();
        strictContext.checking(new Expectations() {{
            oneOf(strictHandler).accept(with(aisMessage.getMatcher()));
        }});

        // Act
        strictReceiver.accept(messageWithValidChecksum);

        // Assert
        strictContext.assertIsSatisfied();
        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getCapturedObject().getMessageType());
    }
}
