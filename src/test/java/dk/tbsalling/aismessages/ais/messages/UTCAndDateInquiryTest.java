package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UTCAndDateInquiryTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,:5AKhr1GORMH,0*57");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.UTCAndDateInquiry, aisMessage.getMessageType());
        UTCAndDateInquiry message = (UTCAndDateInquiry) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(353825000), message.getSourceMmsi());
        assertEquals(MMSI.valueOf(366971350), message.getDestinationMmsi());
    }

    @Test
    public void canDecodeAlternative() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,:6TMCD1GOS60,0*11");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.UTCAndDateInquiry, aisMessage.getMessageType());
        UTCAndDateInquiry message = (UTCAndDateInquiry) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(440882000), message.getSourceMmsi());
        assertEquals(MMSI.valueOf(366972000), message.getDestinationMmsi());
    }
}
