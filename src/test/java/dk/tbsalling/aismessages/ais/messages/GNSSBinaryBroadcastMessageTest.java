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
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GNSSBinaryBroadcastMessage, aisMessage.getMessageType());
        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(2734450), message.getSourceMmsi());
        assertEquals(Integer.valueOf(0), message.getSpare1());
        assertEquals(Float.valueOf(1747.8f), message.getLongitude());
        assertEquals(Float.valueOf(3599.2f), message.getLatitude());
        assertEquals(Integer.valueOf(0), message.getSpare2());
        assertEquals(Integer.valueOf(31), message.getMType());
        assertEquals(Integer.valueOf(5), message.getStationId());
        assertEquals(Integer.valueOf(2776), message.getZCount());
        assertEquals(Integer.valueOf(0), message.getSequenceNumber());
        assertEquals(Integer.valueOf(14), message.getNumOfWords());
        assertEquals(Integer.valueOf(0), message.getHealth());
        assertEquals("0111110000000101010101101100000001110000", message.getBinaryData());
    }

    @Test
    public void canDecodeWithBinaryData() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,A02VqLPA4I6C07h5Ed1h<OrsubtH0000000000S@gN10kGJp,0*0E");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GNSSBinaryBroadcastMessage, aisMessage.getMessageType());
        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(2734450), message.getSourceMmsi());
        assertEquals(Integer.valueOf(0), message.getSpare1());
        assertEquals(Float.valueOf(1747.8f), message.getLongitude());
        assertEquals(Float.valueOf(3599.2f), message.getLatitude());
        assertEquals(Integer.valueOf(0), message.getSpare2());
        assertEquals(Integer.valueOf(31), message.getMType());
        assertEquals(Integer.valueOf(5), message.getStationId());
        assertEquals(Integer.valueOf(2776), message.getZCount());
        assertEquals(Integer.valueOf(0), message.getSequenceNumber());
        assertEquals(Integer.valueOf(14), message.getNumOfWords());
        assertEquals(Integer.valueOf(0), message.getHealth());
        assertTrue(message.getBinaryData().length() > 40);
    }
}
