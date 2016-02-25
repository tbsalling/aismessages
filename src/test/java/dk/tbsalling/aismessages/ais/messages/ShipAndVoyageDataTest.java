package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.IMO;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class ShipAndVoyageDataTest {

    @Test
    public void canDecode1() {
        AISMessage aisMessage = AISMessage.create(
            NMEAMessage.fromString("!AIVDM,2,1,3,A,55MuUD02;EFUL@CO;W@lU=<U=<U10V1HuT4LE:1DC@T>B4kC0DliSp=t,0*14"),
            NMEAMessage.fromString("!AIVDM,2,2,3,A,888888888888880,2*27")
        );

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.ShipAndVoyageRelatedData, aisMessage.getMessageType());
        ShipAndVoyageData message = (ShipAndVoyageData) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(366962000), message.getSourceMmsi());
        assertEquals(IMO.valueOf(9131369), message.getImo());
        assertEquals("WDD7294", message.getCallsign());
        assertEquals("MISSISSIPPI VOYAGER", message.getShipName());
        assertEquals(ShipType.TankerHazardousD, message.getShipType());
        assertEquals(Integer.valueOf(154), message.getToBow());
        assertEquals(Integer.valueOf(36), message.getToStern());
        assertEquals(Integer.valueOf(18), message.getToStarboard());
        assertEquals(Integer.valueOf(14), message.getToPort());
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertEquals(Float.valueOf("8.3"), message.getDraught());
        assertEquals("06-03 19:00", message.getEta());
        assertEquals("SFO 70", message.getDestination());
        assertFalse(message.getDataTerminalReady());
    }

    @Test
    public void canDecode2() {
        AISMessage aisMessage = AISMessage.create(
            NMEAMessage.fromString("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34"),
            NMEAMessage.fromString("!AIVDM,2,2,0,B,00000000000,2*27")
        );

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.ShipAndVoyageRelatedData, aisMessage.getMessageType());
        ShipAndVoyageData message = (ShipAndVoyageData) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(211339980), message.getSourceMmsi());
        assertEquals(IMO.valueOf(0), message.getImo());
        assertEquals("J050A", message.getCallsign());
        assertEquals("HHLA 3         B", message.getShipName());
        assertEquals(ShipType.LawEnforcement, message.getShipType());
        assertEquals(Integer.valueOf(12), message.getToBow());
        assertEquals(Integer.valueOf(38), message.getToStern());
        assertEquals(Integer.valueOf(2), message.getToStarboard());
        assertEquals(Integer.valueOf(23), message.getToPort());
        assertNull(message.getPositionFixingDevice());
        assertEquals(Float.valueOf("0"), message.getDraught());
        assertEquals("14-05 20:10", message.getEta());
        assertEquals("", message.getDestination());
        assertFalse(message.getDataTerminalReady());
    }

    @Test
    public void digest() throws NoSuchAlgorithmException {
        String expectedDigest = "2ca6350a33d7b19f0ef49799aa96dd61da9e081e";

        AISMessage aisMessage = AISMessage.create(
            NMEAMessage.fromString("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34"),
            NMEAMessage.fromString("!AIVDM,2,2,0,B,00000000000,2*27")
        );
        byte[] digest = aisMessage.digest();
        String digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));
        assertEquals(expectedDigest, digestAsString);

        // Change line 1
        aisMessage = AISMessage.create(
            NMEAMessage.fromString("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000001,0*34"),
            NMEAMessage.fromString("!AIVDM,2,2,0,B,00000000000,2*27")
        );
        digest = aisMessage.digest();
        digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));
        assertNotEquals(expectedDigest, digestAsString);

        // Change line 2
        aisMessage = AISMessage.create(
            NMEAMessage.fromString("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34"),
            NMEAMessage.fromString("!AIVDM,2,2,0,B,00000000001,2*27")
        );
        digest = aisMessage.digest();
        digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));
        assertNotEquals(expectedDigest, digestAsString);


    }

}
