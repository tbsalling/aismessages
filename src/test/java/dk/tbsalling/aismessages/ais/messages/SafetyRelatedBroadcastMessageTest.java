package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SafetyRelatedBroadcastMessageTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,>5?Per18=HB1U:1@E=B0m<L,2*51");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.SafetyRelatedBroadcastMessage, aisMessage.getMessageType());
        SafetyRelatedBroadcastMessage message = (SafetyRelatedBroadcastMessage) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(351809000), message.getSourceMmsi());
        assertEquals(0, message.getSpare());
        assertEquals("RCVD YR TEST MSG", message.getText());
    }

    @Test
    public void canDecodeShortMessage() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,>3R1p10E3;;R0USCR0HO>0@gN10kGJp,0*37");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.SafetyRelatedBroadcastMessage, aisMessage.getMessageType());
        SafetyRelatedBroadcastMessage message = (SafetyRelatedBroadcastMessage) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(237008900), message.getSourceMmsi());
        assertEquals(0, message.getSpare());
        assertEquals("EP228 IX48 FG3 DK7 PL56.", message.getText());
    }
}
