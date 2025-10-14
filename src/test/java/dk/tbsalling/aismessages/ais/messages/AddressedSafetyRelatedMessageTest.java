package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressedSafetyRelatedMessageTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,<42Lati0W:Ov=C7P6B?=Pjoihhjhqq,2*2B"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.AddressedSafetyRelatedMessage, aisMessage.getMessageType());
        AddressedSafetyRelatedMessage message = (AddressedSafetyRelatedMessage) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(271002099), message.getSourceMmsi());
        assertEquals(Integer.valueOf(0), message.getSequenceNumber());
        assertEquals(MMSI.valueOf(271002111), message.getDestinationMmsi());
        assertTrue(message.getRetransmit());
        assertEquals(Integer.valueOf(0), message.getSpare());
        assertEquals("MSG FROM 27100209", message.getText());
    }

    @Test
    public void canDecodeWithLongerText() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,<5?Per18=HB1U:1@E=B0m<L,2*51"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.AddressedSafetyRelatedMessage, aisMessage.getMessageType());
        AddressedSafetyRelatedMessage message = (AddressedSafetyRelatedMessage) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(351809000), message.getSourceMmsi());
        assertEquals(Integer.valueOf(0), message.getSequenceNumber());
        assertEquals(MMSI.valueOf(302866720), message.getDestinationMmsi());
        assertFalse(message.getRetransmit());
        assertEquals(Integer.valueOf(1), message.getSpare());
        assertEquals("%JAPUMR 5L", message.getText());
    }
}
