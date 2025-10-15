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
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,KC5E2b@U19PFdLbMuc5=ROv62<7m,0*16");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.LongRangeBroadcastMessage, aisMessage.getMessageType());
        assertEquals(1, aisMessage.getRepeatIndicator());
        assertEquals(MMSI.valueOf(206914217), aisMessage.getSourceMmsi());

        LongRangeBroadcastMessage message = (LongRangeBroadcastMessage) aisMessage;
        assertFalse(message.isPositionAccuracy());
        assertFalse(message.isRaim());
        assertEquals(NavigationStatus.NotUnderCommand, message.getNavigationStatus());
        assertEquals(137.02333f, message.getLongitude(), 0.0f);
        assertEquals(4.84f, message.getLatitude(), 0.0f);
        assertEquals(57f, message.getSpeedOverGround(), 1e-5);
        assertEquals(57, message.getRawSpeedOverGround());
        assertEquals(167f, message.getCourseOverGround(), 1e-5);
        assertEquals(167, message.getRawCourseOverGround());
    }

    @Test
    public void canDecode2() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,K5DfMB9FLsM?P00d,0*70");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.LongRangeBroadcastMessage, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        assertEquals(MMSI.valueOf(357277000), aisMessage.getSourceMmsi());

        LongRangeBroadcastMessage message = (LongRangeBroadcastMessage) aisMessage;
        assertTrue(message.isPositionAccuracy());
        assertFalse(message.isRaim());
        assertEquals(NavigationStatus.Moored, message.getNavigationStatus());
        assertEquals(176.18167f, message.getLongitude(), 0.0f);
        assertEquals(-37.65333f, message.getLatitude(), 0.0f);
        assertEquals(0f, message.getSpeedOverGround(), 1e-5);
        assertEquals(0, message.getRawSpeedOverGround());
        assertEquals(11f, message.getCourseOverGround(), 1e-5);
        assertEquals(11, message.getRawCourseOverGround());
    }

}
