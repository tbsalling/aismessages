package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChannelManagementTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,F030p:j2N2P5aJR0r;6f3rj10000,0*11"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.ChannelManagement, aisMessage.getMessageType());
        ChannelManagement message = (ChannelManagement) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(3160107), message.getSourceMmsi());
        assertFalse(message.getPower());
        assertEquals((Integer) 2087, message.getChannelA());
        assertEquals((Integer) 2088, message.getChannelB());
        assertFalse(message.getPower());
        // Jurisdiction area
        assertEquals(Float.valueOf(-7710.0f), message.getNorthEastLongitude());
        assertEquals(Float.valueOf(3300.0f), message.getNorthEastLatitude());
        assertEquals(Float.valueOf(-8020.0f), message.getSouthWestLongitude());
        assertEquals(Float.valueOf(3210), message.getSouthWestLatitude());
        // Bands
        assertFalse(message.getBandA());
        assertFalse(message.getBandB());
        assertEquals((Integer) 2, message.getZoneSize());
        // Not addressed and therefore destination mmsis should be null
        assertFalse(message.getAddressed());
        assertNull(message.getDestinationMmsi1());
        assertNull(message.getDestinationMmsi2());
    }


}