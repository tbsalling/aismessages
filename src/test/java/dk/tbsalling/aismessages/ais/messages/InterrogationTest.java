package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InterrogationTest {

    @Test
    public void canDecodeShortVariant() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,?h3Ovk1GOPph000,2*53"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.Interrogation, aisMessage.getMessageType());
        Interrogation message = (Interrogation) aisMessage;
        assertEquals(Integer.valueOf(3), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(3669708), message.getSourceMmsi());
        assertEquals(MMSI.valueOf(366969740), message.getInterrogatedMmsi1());
        assertEquals((Integer) 0, message.getType1_1());
        assertEquals((Integer) 0, message.getOffset1_1());
        assertNull(message.getType1_2());
        assertNull(message.getOffset1_2());
        assertNull(message.getInterrogatedMmsi2());
        assertNull(message.getType2_1());
        assertNull(message.getOffset2_1());
    }
}
