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
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,;4R33:1uUK2F`q?mOt@@GoQ00000,0*5D"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.UTCAndDateResponse, aisMessage.getMessageType());
        UTCAndDateResponse message = (UTCAndDateResponse) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(304137000), message.getSourceMmsi());
        assertEquals(Integer.valueOf(2009), message.getYear());
        assertEquals(Integer.valueOf(5), message.getMonth());
        assertEquals(Integer.valueOf(22), message.getDay());
        assertEquals(Integer.valueOf(2), message.getHour());
        assertEquals(Integer.valueOf(22), message.getMinute());
        assertEquals(Integer.valueOf(40), message.getSecond());
        assertTrue(message.getPositionAccurate());
        assertEquals(Float.valueOf(28.409117f), message.getLatitude());
        assertEquals(Float.valueOf(-94.40768f), message.getLongitude());
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertFalse(message.getRaimFlag());
    }

    @Test
    public void canDecodeAlternative() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,;5MtFGQua>0:=I25ih?rVc100000,0*69"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.UTCAndDateResponse, aisMessage.getMessageType());
        UTCAndDateResponse message = (UTCAndDateResponse) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(366941790), message.getSourceMmsi());
        assertEquals(Integer.valueOf(2010), message.getYear());
        assertEquals(Integer.valueOf(4), message.getMonth());
        assertEquals(Integer.valueOf(28), message.getDay());
        assertEquals(Integer.valueOf(0), message.getHour());
        assertEquals(Integer.valueOf(10), message.getMinute());
        assertEquals(Integer.valueOf(13), message.getSecond());
        assertFalse(message.getPositionAccurate());
        assertEquals(Float.valueOf(27.814686f), message.getLatitude());
        assertEquals(Float.valueOf(-97.41047f), message.getLongitude());
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertFalse(message.getRaimFlag());
    }
}
