package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UTCAndDateResponseTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,;4R33:1uUK2F`q?mOt@@GoQ00000,0*5D");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.UTCAndDateResponse, aisMessage.getMessageType());
        UTCAndDateResponse message = (UTCAndDateResponse) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(304137000), message.getSourceMmsi());
        assertEquals(2009, message.getYear());
        assertEquals(5, message.getMonth());
        assertEquals(22, message.getDay());
        assertEquals(2, message.getHour());
        assertEquals(22, message.getMinute());
        assertEquals(40, message.getSecond());
        assertTrue(message.isPositionAccurate());
        assertEquals(17045470 / 600000.0, message.getLatitude(), 0.0f);
        assertEquals(-56644610 / 600000.0, message.getLongitude(), 0.0f);
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertFalse(message.isRaimFlag());
        assertFalse(message.isSatelliteBroadcastTransmissionControl());
        assertNotNull(message.getCommunicationState());
        assertEquals(0, message.getCommunicationState().getSlotTimeout());
        assertEquals(0, message.getCommunicationState().getSlotOffset());
    }

    @Test
    public void canDecodeAlternative() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,;5MtFGQua>0:=I25ih?rVc100000,0*69");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.UTCAndDateResponse, aisMessage.getMessageType());
        UTCAndDateResponse message = (UTCAndDateResponse) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(366941790), message.getSourceMmsi());
        assertEquals(2010, message.getYear());
        assertEquals(4, message.getMonth());
        assertEquals(28, message.getDay());
        assertEquals(0, message.getHour());
        assertEquals(10, message.getMinute());
        assertEquals(13, message.getSecond());
        assertFalse(message.isPositionAccurate());
        assertEquals(16688812 / 600000.0, message.getLatitude(), 0.0f);
        assertEquals(-58446280 / 600000.0, message.getLongitude(), 0.0f);
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertFalse(message.isRaimFlag());
        assertFalse(message.isSatelliteBroadcastTransmissionControl());
        assertNotNull(message.getCommunicationState());
        assertEquals(0, message.getCommunicationState().getSlotTimeout());
        assertEquals(0, message.getCommunicationState().getSlotOffset());
    }
}
