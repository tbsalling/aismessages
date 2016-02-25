package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataLinkManagementTest {

    @Test
    public void canDecodeShortVariant1() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,Dh3Ovk1UAN>4,0*0A"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.DataLinkManagement, aisMessage.getMessageType());
        DataLinkManagement message = (DataLinkManagement) aisMessage;
        assertEquals(Integer.valueOf(3), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(3669708), message.getSourceMmsi());
        assertEquals((Integer) 1620, message.getOffsetNumber1());
        assertEquals((Integer) 5, message.getReservedSlots1());
        assertEquals((Integer) 7, message.getTimeout1());
        assertEquals((Integer) 225, message.getIncrement1());
        assertNull(message.getOffsetNumber2());
        assertNull(message.getReservedSlots2());
        assertNull(message.getTimeout2());
        assertNull(message.getIncrement2());
        assertNull(message.getOffsetNumber3());
        assertNull(message.getReservedSlots3());
        assertNull(message.getTimeout3());
        assertNull(message.getIncrement3());
    }

    @Test
    public void canDecodeShortVariant2() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,Dh3Ovk1cEN>4,0*3B"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.DataLinkManagement, aisMessage.getMessageType());
        DataLinkManagement message = (DataLinkManagement) aisMessage;
        assertEquals(Integer.valueOf(3), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(3669708), message.getSourceMmsi());
        assertEquals((Integer) 1717, message.getOffsetNumber1());
        assertEquals((Integer) 5, message.getReservedSlots1());
        assertEquals((Integer) 7, message.getTimeout1());
        assertEquals((Integer) 225, message.getIncrement1());
        assertNull(message.getOffsetNumber2());
        assertNull(message.getReservedSlots2());
        assertNull(message.getTimeout2());
        assertNull(message.getIncrement2());
        assertNull(message.getOffsetNumber3());
        assertNull(message.getReservedSlots3());
        assertNull(message.getTimeout3());
        assertNull(message.getIncrement3());
    }
}