package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

public class GNSSBinaryBroadcastMessageTest {

    @Test
    public void canDecodeMinimal() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,A02VqLPA4I6C07h5Ed1h,0*43");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GNSSBinaryBroadcastMessage, aisMessage.getMessageType());
        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2734450), message.getSourceMmsi());
        assertEquals(0, message.getSpare1());
        assertEquals(1747.8f, message.getLongitude(), 0.0f);
        assertEquals(3599.2f, message.getLatitude(), 0.0f);
        assertEquals(0, message.getSpare2());
        assertEquals(31, message.getMType());
        assertEquals(5, message.getStationId());
        assertEquals(2776, message.getZCount());
        assertEquals(0, message.getSequenceNumber());
        assertEquals(14, message.getNumOfWords());
        assertEquals(0, message.getHealth());
        assertEquals("0111110000000101010101101100000001110000", message.getBinaryData().toString());
    }

    @Test
    public void canDecodeWithBinaryData() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,A02VqLPA4I6C07h5Ed1h<OrsubtH0000000000S@gN10kGJp,0*0E");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GNSSBinaryBroadcastMessage, aisMessage.getMessageType());
        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2734450), message.getSourceMmsi());
        assertEquals(0, message.getSpare1());
        assertEquals(1747.8f, message.getLongitude(), 0.0f);
        assertEquals(3599.2f, message.getLatitude(), 0.0f);
        assertEquals(0, message.getSpare2());
        assertEquals(31, message.getMType());
        assertEquals(5, message.getStationId());
        assertEquals(2776, message.getZCount());
        assertEquals(0, message.getSequenceNumber());
        assertEquals(14, message.getNumOfWords());
        assertEquals(0, message.getHealth());
        assertTrue(message.getBinaryData().length() > 40);
    }

    /** No correction data (80 bits, ITU-R M.1371-6 Table 66): the data header fields are null and the data empty. */
    @Test
    public void canDecodeWithoutCorrectionData() {
        Bits b = bits().u(17, 6).u(0, 2).u(2190047, 30).u(0, 2).s(1200 * 10, 18).s(-330 * 10, 17).u(0, 5);

        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(1200f, message.getLongitude(), 0.0f);
        assertEquals(-330f, message.getLatitude(), 0.0f);
        assertNull(message.getMType());
        assertNull(message.getStationId());
        assertNull(message.getZCount());
        assertNull(message.getSequenceNumber());
        assertNull(message.getNumOfWords());
        assertNull(message.getHealth());
        assertEquals(0, message.getBinaryData().length());
    }

    /** Correction data with a two-word header and N = 2 data words (168 bits, Table 67). Synthetic sentence. */
    @Test
    public void canDecodeWithCorrectionData() {
        Bits b = bits().u(17, 6).u(0, 2).u(2190047, 30).u(0, 2).s(120, 18).s(556, 17).u(0, 5)
                .u(9, 6).u(321, 10).u(4567, 13).u(5, 3).u(2, 5).u(3, 3).u(0xABCDEF, 24).u(0x123456, 24);

        GNSSBinaryBroadcastMessage message = (GNSSBinaryBroadcastMessage) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(9, message.getMType());
        assertEquals(321, message.getStationId());
        assertEquals(4567, message.getZCount());
        assertEquals(5, message.getSequenceNumber());
        assertEquals(2, message.getNumOfWords());
        assertEquals(3, message.getHealth());
        assertEquals(88, message.getBinaryData().length());
    }
}
