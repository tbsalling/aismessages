package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LongRangeBroadcastMessageTest {

    @Test
    public void canDecode1() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,KC5E2b@U19PFdLbMuc5=ROv62<7m,0*16");

        // Act
        AISMessage aisMessage = AISMessage.create(nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.LongRangeBroadcastMessage, aisMessage.getMessageType());
        assertEquals((Integer) 1, aisMessage.getRepeatIndicator());
        assertEquals(MMSI.valueOf(206914217), aisMessage.getSourceMmsi());

        LongRangeBroadcastMessage message = (LongRangeBroadcastMessage) aisMessage;
        assertFalse(message.getPositionAccuracy());
        assertFalse(message.getRaim());
        assertEquals(NavigationStatus.NotUnderCommand, message.getNavigationalStatus());
        assertEquals(Float.valueOf(137.02333f), message.getLongitude());
        assertEquals(Float.valueOf(4.84f), message.getLatitude());
        assertEquals(Float.valueOf(57f), message.getSpeedOverGround(), 1e-5);
        assertEquals((Integer)57, message.getRawSpeedOverGround());
        assertEquals(Float.valueOf(167f), message.getCourseOverGround(), 1e-5);
        assertEquals((Integer)167, message.getRawCourseOverGround());
    }

    @Test
    public void canDecode2() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,K5DfMB9FLsM?P00d,0*70");

        // Act
        AISMessage aisMessage = AISMessage.create(nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.LongRangeBroadcastMessage, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        assertEquals(MMSI.valueOf(357277000), aisMessage.getSourceMmsi());

        LongRangeBroadcastMessage message = (LongRangeBroadcastMessage) aisMessage;
        assertTrue(message.getPositionAccuracy());
        assertFalse(message.getRaim());
        assertEquals(NavigationStatus.Moored, message.getNavigationalStatus());
        assertEquals(Float.valueOf(176.18167f), message.getLongitude());
        assertEquals(Float.valueOf(-37.65333f), message.getLatitude());
        assertEquals(Float.valueOf(0f), message.getSpeedOverGround(), 1e-5);
        assertEquals((Integer)0, message.getRawSpeedOverGround());
        assertEquals(Float.valueOf(11f), message.getCourseOverGround(), 1e-5);
        assertEquals((Integer)11, message.getRawCourseOverGround());
    }

}
