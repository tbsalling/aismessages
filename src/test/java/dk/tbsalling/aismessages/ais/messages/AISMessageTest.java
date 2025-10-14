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
        // Arrange
        NMEAMessage nmeaMessage1 = NMEAMessage.fromString("!AIVDM,1,1,,B,13AkSB0000PhAmJPoTMoiQFT0D1:,0*5E");
        NMEAMessage nmeaMessage2 = NMEAMessage.fromString("!AIVDM,1,1,,A,13AkSB0000PhAmHPoTNcp1Fp0D17,0*00");

        // Act
        AISMessage aisMessage1 = AISMessage.create(nmeaMessage1);
        AISMessage aisMessage2 = AISMessage.create(nmeaMessage1);
        AISMessage aisMessage3 = AISMessage.create(nmeaMessage2);

        // Assert
        assertEquals(aisMessage1, aisMessage2);
        assertNotEquals(aisMessage1, aisMessage3);
    }

    @Test
    public void testEqualsWithMetadataAndTagBlock() {
        // Arrange
        String source = "test";
        Instant time = Instant.now();
        NMEAMessage nmea = NMEAMessage.fromString("!AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*26");
        NMEATagBlock tag = NMEATagBlock.fromString("\\c:1705510832,s:43574,i:<S>S</S><O>XEE</O><T>A:1705510832 G:1 I:1705510887 D:1705510892 F:1641</T><Q>32</Q>*38\\");

        Metadata meta1 = new Metadata(source, time);
        Metadata meta2 = new Metadata(source, time);
        Metadata meta3 = new Metadata(source, time.plusMillis(1000));

        // Act
        AISMessage ais1 = AISMessage.create(meta1, tag, nmea);
        AISMessage ais2 = AISMessage.create(meta2, tag, nmea);
        AISMessage ais3 = AISMessage.create(meta3, tag, nmea);

        // Assert
        assertEquals(meta1, meta2);
        assertNotEquals(meta1, meta3);

        assertEquals(ais1, ais2);
        assertNotEquals(ais1, ais3);
    }

    @Test
    public void testEqualsWithMetadata() {
        // Arrange
        String source = "test";
        Instant time = Instant.now();
        NMEAMessage nmea = NMEAMessage.fromString("!AIVDM,1,1,,A,13aEOK?P00PD2wVMdLDRhgvL289?,0*26");

        Metadata meta1 = new Metadata(source, time);
        Metadata meta2 = new Metadata(source, time);
        Metadata meta3 = new Metadata(source, time.plusMillis(1000));

        // Act
        AISMessage ais1 = AISMessage.create(meta1, nmea);
        AISMessage ais2 = AISMessage.create(meta2, nmea);
        AISMessage ais3 = AISMessage.create(meta3, nmea);

        // Assert
        assertEquals(meta1, meta2);
        assertNotEquals(meta1, meta3);

        assertEquals(ais1, ais2);
        assertNotEquals(ais1, ais3);
    }

    @Test
    public void canHandleEmptyMessage() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,00,4*21");

        // Act & Assert
        assertThrows(InvalidMessage.class, () -> AISMessage.create(nmeaMessage));
    }

    @Test
    public void canHandleUnparsableNMEAMessage() {
        // Arrange & Act & Assert
        assertThrows(NMEAParseException.class, () -> {
            NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,13K6th002u9@8P0DEVv2M1up02Pl,0*740008,2*09");
            AISMessage.create(nmeaMessage);
        });
    }

    @Test
    public void isSerializable() {
        // Arrange & Act & Assert
        // Type 1
        NMEAMessage nmeaMessage1 = NMEAMessage.fromString("!BSVDM,1,1,,A,1:02Ih001U0d=V:Op85<2aT>0<0F,0*3B");
        assertTrue(isSerializable(AISMessage.create(nmeaMessage1)));

        // Type 4
        NMEAMessage nmeaMessage4 = NMEAMessage.fromString("!AIVDM,1,1,,B,4h3Ovk1udp6I9o>jPHEdjdW000S:,0*0C");
        assertTrue(isSerializable(AISMessage.create(nmeaMessage4)));

        // Type 5
        NMEAMessage nmeaMessage5a = NMEAMessage.fromString("!BSVDM,2,1,5,A,5:02Ih01WrRsEH57J20H5P8u8N222222222222167H66663k085QBS1H,0*55");
        NMEAMessage nmeaMessage5b = NMEAMessage.fromString("!BSVDM,2,2,5,A,888888888888880,2*38");
        assertTrue(isSerializable(AISMessage.create(nmeaMessage5a, nmeaMessage5b)));
    }

    @Test
    public void canReturnRawNmeaMessages() {
        // Arrange
        NMEAMessage nmeaMessage1 = NMEAMessage.fromString("!BSVDM,1,1,,A,1:02Ih001U0d=V:Op85<2aT>0<0F,0*3B");

        // Act
        // Test one-liner
        AISMessage aisMessage = AISMessage.create(nmeaMessage1);

        // Assert
        NMEAMessage[] nmeaMessages = aisMessage.getNmeaMessages();
        assertNotNull(nmeaMessages);
        assertEquals(1, nmeaMessages.length);
        assertEquals("!BSVDM,1,1,,A,1:02Ih001U0d=V:Op85<2aT>0<0F,0*3B", nmeaMessages[0].getRawMessage());

        // Arrange
        NMEAMessage nmeaMessage2a = NMEAMessage.fromString("!BSVDM,2,1,5,A,5:02Ih01WrRsEH57J20H5P8u8N222222222222167H66663k085QBS1H,0*55");
        NMEAMessage nmeaMessage2b = NMEAMessage.fromString("!BSVDM,2,2,5,A,888888888888880,2*38");

        // Act
        // Test two-liner
        aisMessage = AISMessage.create(nmeaMessage2a, nmeaMessage2b);

        // Assert
        nmeaMessages = aisMessage.getNmeaMessages();
        assertNotNull(nmeaMessages);
        assertEquals(2, nmeaMessages.length);
        assertEquals("!BSVDM,2,1,5,A,5:02Ih01WrRsEH57J20H5P8u8N222222222222167H66663k085QBS1H,0*55", nmeaMessages[0].getRawMessage());
        assertEquals("!BSVDM,2,2,5,A,888888888888880,2*38", nmeaMessages[1].getRawMessage());
    }

    @Test
    public void checkDataFields() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,702;bCSdToR`,0*34");

        // Act
        AISMessage aisMessage = AISMessage.create(nmeaMessage);
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
