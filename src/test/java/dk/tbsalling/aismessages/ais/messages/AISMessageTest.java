package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AISMessageTest {

    @Test
    public void testEquals() {
          assertEquals(AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,13AkSB0000PhAmJPoTMoiQFT0D1:,0*5E")), AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,13AkSB0000PhAmJPoTMoiQFT0D1:,0*5E")));
          assertNotEquals(AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,13AkSB0000PhAmJPoTMoiQFT0D1:,0*5E")), AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,13AkSB0000PhAmHPoTNcp1Fp0D17,0*00")));
    }

    @Test
    public void testEqualsWithMetadataAndTagBlock() {
        String source = "test";
        Instant time = Instant.now();
        NMEAMessage nmea = NMEAMessage.fromString("!AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*26");
        NMEATagBlock tag = NMEATagBlock.fromString("\\c:1705510832,s:43574,i:<S>S</S><O>XEE</O><T>A:1705510832 G:1 I:1705510887 D:1705510892 F:1641</T><Q>32</Q>*38\\");

        Metadata meta1 = new Metadata(source, time);
        Metadata meta2 = new Metadata(source, time);
        Metadata meta3 = new Metadata(source, time.plusMillis(1000));

        AISMessage ais1 = AISMessage.create(meta1, tag, nmea);
        AISMessage ais2 = AISMessage.create(meta2, tag, nmea);
        AISMessage ais3 = AISMessage.create(meta3, tag, nmea);

        assertEquals(meta1, meta2);
        assertNotEquals(meta1, meta3);

        assertEquals(ais1, ais2);
        assertNotEquals(ais1, ais3);
    }

    @Test
    public void testEqualsWithMetadata() {
        String source = "test";
        Instant time = Instant.now();
        NMEAMessage nmea = NMEAMessage.fromString("!AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*26");

        Metadata meta1 = new Metadata(source, time);
        Metadata meta2 = new Metadata(source, time);
        Metadata meta3 = new Metadata(source, time.plusMillis(1000));

        AISMessage ais1 = AISMessage.create(meta1, nmea);
        AISMessage ais2 = AISMessage.create(meta2, nmea);
        AISMessage ais3 = AISMessage.create(meta3, nmea);

        assertEquals(meta1, meta2);
        assertNotEquals(meta1, meta3);

        assertEquals(ais1, ais2);
        assertNotEquals(ais1, ais3);
    }

    @Test
    public void canHandleEmptyMessage() {
        assertThrows(InvalidMessage.class, () -> AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,00,4*21")));
    }

    @Test
    public void canHandleUnparsableNMEAMessage() {
        assertThrows(NMEAParseException.class, () -> AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,13K6th002u9@8P0DEVv2M1up02Pl,0*740008,2*09")));
    }

    @Test
    public void isSerializable() {
        // Type 1
        assertTrue(isSerializable(AISMessage.create(
            NMEAMessage.fromString("!BSVDM,1,1,,A,1:02Ih001U0d=V:Op85<2aT>0<0F,0*3B")
        )));

        // Type 4
        assertTrue(isSerializable(AISMessage.create(
            NMEAMessage.fromString("!AIVDM,1,1,,B,4h3Ovk1udp6I9o>jPHEdjdW000S:,0*0C")
        )));

        // Type 5
        assertTrue(isSerializable(AISMessage.create(
            NMEAMessage.fromString("!BSVDM,2,1,5,A,5:02Ih01WrRsEH57J20H5P8u8N222222222222167H66663k085QBS1H,0*55"),
            NMEAMessage.fromString("!BSVDM,2,2,5,A,888888888888880,2*38")
        )));
    }

    @Test
    public void canReturnRawNmeaMessages() {
        // Test one-liner
        AISMessage aisMessage = AISMessage.create(
            NMEAMessage.fromString("!BSVDM,1,1,,A,1:02Ih001U0d=V:Op85<2aT>0<0F,0*3B")
        );

        NMEAMessage[] nmeaMessages = aisMessage.getNmeaMessages();
        assertNotNull(nmeaMessages);
        assertEquals(1, nmeaMessages.length);
        assertEquals("!BSVDM,1,1,,A,1:02Ih001U0d=V:Op85<2aT>0<0F,0*3B", nmeaMessages[0].getRawMessage());

        // Test two-liner
        aisMessage =AISMessage.create(
            NMEAMessage.fromString("!BSVDM,2,1,5,A,5:02Ih01WrRsEH57J20H5P8u8N222222222222167H66663k085QBS1H,0*55"),
            NMEAMessage.fromString("!BSVDM,2,2,5,A,888888888888880,2*38")
        );

        nmeaMessages = aisMessage.getNmeaMessages();
        assertNotNull(nmeaMessages);
        assertEquals(2, nmeaMessages.length);
        assertEquals("!BSVDM,2,1,5,A,5:02Ih01WrRsEH57J20H5P8u8N222222222222167H66663k085QBS1H,0*55", nmeaMessages[0].getRawMessage());
        assertEquals("!BSVDM,2,2,5,A,888888888888880,2*38", nmeaMessages[1].getRawMessage());
    }

    @Test
    public void checkDataFields() {
        // Arrange
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,702;bCSdToR`,0*34"));

        // Act
        Map<String, Object> dataFields = aisMessage.dataFields();

        // Assert
        assertEquals(16, dataFields.size());
        assertEquals("BinaryAcknowledge", dataFields.get("messageType"));
        assertNull(dataFields.get("metadata"));
        assertEquals(992271914, dataFields.get("mmsi1.MMSI"));
        assertNull(dataFields.get("mmsi2"));
        assertNull(dataFields.get("mmsi3"));
        assertNull(dataFields.get("mmsi4"));
        assertEquals(1, dataFields.get("numOfAcks"));
        assertEquals(0, dataFields.get("repeatIndicator"));
        assertEquals(0, dataFields.get("sequence1"));
        assertNull(dataFields.get("sequence2"));
        assertNull(dataFields.get("sequence3"));
        assertNull(dataFields.get("sequence4"));
        assertEquals(2288206, dataFields.get("sourceMmsi.MMSI"));
    }

    private boolean isSerializable(Object object) {
        assertTrue(object instanceof Serializable);

        // Deep validate that whole tree is serializable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(stream);
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
