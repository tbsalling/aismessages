package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UTCAndDateInquiryTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,:5AKhr1GORMH,0*57"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.UTCAndDateInquiry, aisMessage.getMessageType());
        UTCAndDateInquiry message = (UTCAndDateInquiry) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(353825000), message.getSourceMmsi());
        assertEquals(MMSI.valueOf(366971350), message.getDestinationMmsi());
    }

    @Test
    public void canDecodeAlternative() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,:6TMCD1GOS60,0*11"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.UTCAndDateInquiry, aisMessage.getMessageType());
        UTCAndDateInquiry message = (UTCAndDateInquiry) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(440882000), message.getSourceMmsi());
        assertEquals(MMSI.valueOf(366972000), message.getDestinationMmsi());
    }
}
