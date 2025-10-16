package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GNSSBinaryBroadcastMessageTest {

    @Test
    public void canDecodeMinimal() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,A02VqLPA4I6C07h5Ed1h,0*43");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GNSSBinaryBroadcastMessage, aisMessage.getMessageType());
        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2734450), message.getSourceMmsi());
        assertEquals(0, message.getSpare1());
        assertEquals(1747.8f, message.getLongitude(), 0.0f);
        assertEquals(3599.2f, message.getLatitude(), 0.0f);
        assertEquals(0, message.getSpare2());
        assertEquals(31, message.getMType());
        assertEquals(5, message.getStationId());
        assertEquals(2776, message.getZCount());
        assertEquals(0, message.getSequenceNumber());
        assertEquals(14, message.getNumOfWords());
        assertEquals(0, message.getHealth());
        assertEquals("0111110000000101010101101100000001110000", message.getBinaryData());
    }

    @Test
    public void canDecodeWithBinaryData() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,A02VqLPA4I6C07h5Ed1h<OrsubtH0000000000S@gN10kGJp,0*0E");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GNSSBinaryBroadcastMessage, aisMessage.getMessageType());
        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2734450), message.getSourceMmsi());
        assertEquals(0, message.getSpare1());
        assertEquals(1747.8f, message.getLongitude(), 0.0f);
        assertEquals(3599.2f, message.getLatitude(), 0.0f);
        assertEquals(0, message.getSpare2());
        assertEquals(31, message.getMType());
        assertEquals(5, message.getStationId());
        assertEquals(2776, message.getZCount());
        assertEquals(0, message.getSequenceNumber());
        assertEquals(14, message.getNumOfWords());
        assertEquals(0, message.getHealth());
        assertTrue(message.getBinaryData().length() > 40);
    }
}
