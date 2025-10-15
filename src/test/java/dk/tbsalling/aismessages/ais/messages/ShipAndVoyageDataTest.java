package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ShipAndVoyageDataTest {

    @Test
    public void canDecode1() {
        // Arrange
        NMEAMessage nmeaMessage1 = new NMEAMessage("!AIVDM,2,1,3,A,55MuUD02;EFUL@CO;W@lU=<U=<U10V1HuT4LE:1DC@T>B4kC0DliSp=t,0*14");
        NMEAMessage nmeaMessage2 = new NMEAMessage("!AIVDM,2,2,3,A,888888888888880,2*27");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage1, nmeaMessage2);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.ShipAndVoyageRelatedData, aisMessage.getMessageType());
        ShipAndVoyageData message = (ShipAndVoyageData) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(366962000), message.getSourceMmsi());
        assertEquals(IMO.valueOf(9131369), message.getImo());
        assertEquals("WDD7294", message.getCallsign());
        assertEquals("MISSISSIPPI VOYAGER", message.getShipName());
        assertEquals(ShipType.TankerHazardousD, message.getShipType());
        assertEquals(154, message.getToBow());
        assertEquals(36, message.getToStern());
        assertEquals(18, message.getToStarboard());
        assertEquals(14, message.getToPort());
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertEquals(8.3f, message.getDraught(), 0.0f);
        assertEquals(83, message.getRawDraught());
        assertEquals(String.format("%02d-%02d %02d:%02d", message.getEtaDay(), message.getEtaMonth(), message.getEtaHour(), message.getEtaMinute()), "06-03 19:00");
        assertEquals(3, message.getEtaMonth());
        assertEquals(6, message.getEtaDay());
        assertEquals(19, message.getEtaHour());
        assertEquals(0, message.getEtaMinute());
        assertEquals(Optional.empty(), message.getEtaAfterReceived());
        assertEquals("SFO 70", message.getDestination());
        assertFalse(message.isDataTerminalReady());
    }

    @Test
    public void canDecode2() {
        // Arrange
        ZonedDateTime now = ZonedDateTime.of(2010, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);
        NMEATagBlock tag = NMEATagBlock.fromString("\\c:1609841515,s:my dearest AIS base station*6E\\");
        NMEAMessage nmeaMessage1 = new NMEAMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34");
        NMEAMessage nmeaMessage2 = new NMEAMessage("!AIVDM,2,2,0,B,00000000000,2*27");

        // Act
        AISMessage aisMessage = AISMessage.create(now.toInstant(), "Test", tag, nmeaMessage1, nmeaMessage2);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.ShipAndVoyageRelatedData, aisMessage.getMessageType());
        ShipAndVoyageData message = (ShipAndVoyageData) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(211339980), message.getSourceMmsi());
        assertEquals(IMO.valueOf(0), message.getImo());
        assertEquals("J050A", message.getCallsign());
        assertEquals("HHLA 3         B", message.getShipName());
        assertEquals(ShipType.LawEnforcement, message.getShipType());
        assertEquals(12, message.getToBow());
        assertEquals(38, message.getToStern());
        assertEquals(2, message.getToStarboard());
        assertEquals(23, message.getToPort());
        assertNull(message.getPositionFixingDevice());
        assertEquals(0.0f, message.getDraught(), 0.0f);
        assertEquals(String.format("%02d-%02d %02d:%02d", message.getEtaDay(), message.getEtaMonth(), message.getEtaHour(), message.getEtaMinute()), "14-05 20:10");
        assertEquals(5, message.getEtaMonth());
        assertEquals(14, message.getEtaDay());
        assertEquals(20, message.getEtaHour());
        assertEquals(10, message.getEtaMinute());
        assertEquals(Optional.of(ZonedDateTime.of(2011, 5, 14, 20, 10, 0, 0, ZoneOffset.UTC)), message.getEtaAfterReceived());
        assertEquals("", message.getDestination());
        assertFalse(message.isDataTerminalReady());
    }

    @Test
    public void digest() throws NoSuchAlgorithmException {
        // Arrange
        String expectedDigest = "2ca6350a33d7b19f0ef49799aa96dd61da9e081e";
        NMEAMessage nmeaMessage1 = new NMEAMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34");
        NMEAMessage nmeaMessage2 = new NMEAMessage("!AIVDM,2,2,0,B,00000000000,2*27");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage1, nmeaMessage2);
        byte[] digest = aisMessage.digest();
        String digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));

        // Assert
        assertEquals(expectedDigest, digestAsString);

        // Change line 1
        NMEAMessage nmeaMessage1b = new NMEAMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000001,0*34");
        aisMessage = AISMessage.create(null, null, null, nmeaMessage1b, nmeaMessage2);
        digest = aisMessage.digest();
        digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));
        assertNotEquals(expectedDigest, digestAsString);

        // Change line 2
        NMEAMessage nmeaMessage2b = new NMEAMessage("!AIVDM,2,2,0,B,00000000001,2*27");
        aisMessage = AISMessage.create(null, null, null, nmeaMessage1, nmeaMessage2b);
        digest = aisMessage.digest();
        digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));
        assertNotEquals(expectedDigest, digestAsString);


    }

}
