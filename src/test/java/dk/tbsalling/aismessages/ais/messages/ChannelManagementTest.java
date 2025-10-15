package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChannelManagementTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,F030p:j2N2P5aJR0r;6f3rj10000,0*11");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.ChannelManagement, aisMessage.getMessageType());
        ChannelManagement message = (ChannelManagement) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(3160107), message.getSourceMmsi());
        assertFalse(message.isPower());
        assertEquals(2087, message.getChannelA());
        assertEquals(2088, message.getChannelB());
        assertFalse(message.isPower());
        // Jurisdiction area
        assertEquals(-7710.0f, message.getNorthEastLongitude(), 0.0f);
        assertEquals(3300.0f, message.getNorthEastLatitude(), 0.0f);
        assertEquals(-8020.0f, message.getSouthWestLongitude(), 0.0f);
        assertEquals(3210.0f, message.getSouthWestLatitude(), 0.0f);
        // Bands
        assertFalse(message.isBandA());
        assertFalse(message.isBandB());
        assertEquals(2, message.getZoneSize());
        // Not addressed and therefore destination mmsis should be null
        assertFalse(message.isAddressed());
        assertNull(message.getDestinationMmsi1());
        assertNull(message.getDestinationMmsi2());
    }


}
