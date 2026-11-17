package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataLinkManagementTest {

    @Test
    public void canDecodeShortVariant1() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,Dh3Ovk1UAN>4,0*0A");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.DataLinkManagement, aisMessage.getMessageType());
        DataLinkManagement message = (DataLinkManagement) aisMessage;
        assertEquals(3, message.getRepeatIndicator());
        assertEquals(new MMSI(3669708), message.getSourceMmsi());
        assertEquals(1620, message.getOffsetNumber1());
        assertEquals(5, message.getReservedSlots1());
        assertEquals(7, message.getTimeout1());
        assertEquals(225, message.getIncrement1());
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
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,Dh3Ovk1cEN>4,0*3B");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.DataLinkManagement, aisMessage.getMessageType());
        DataLinkManagement message = (DataLinkManagement) aisMessage;
        assertEquals(3, message.getRepeatIndicator());
        assertEquals(new MMSI(3669708), message.getSourceMmsi());
        assertEquals(1717, message.getOffsetNumber1());
        assertEquals(5, message.getReservedSlots1());
        assertEquals(7, message.getTimeout1());
        assertEquals(225, message.getIncrement1());
        assertNull(message.getOffsetNumber2());
        assertNull(message.getReservedSlots2());
        assertNull(message.getTimeout2());
        assertNull(message.getIncrement2());
        assertNull(message.getOffsetNumber3());
        assertNull(message.getReservedSlots3());
        assertNull(message.getTimeout3());
        assertNull(message.getIncrement3());
    }

    /**
     * Three reservation blocks (ITU-R M.1371-6 Table 70: each block is 12 + 4 + 3 + 11 bits, starting at bit 40, 70, 100).
     * Increments of 1125 do not fit in 7 bits, so this also guards the 11-bit increment width. Synthetic sentence built
     * from the spec layout (130 bits plus 6 spare bits); independently verified with the pyais decoder.
     */
    @Test
    public void canDecodeThreeReservationBlocks() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,D025boh6@fGMMhK6DpAO6D0,2*08");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        // Assert
        assertEquals(AISMessageType.DataLinkManagement, aisMessage.getMessageType());
        DataLinkManagement message = (DataLinkManagement) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2190047), message.getSourceMmsi());
        assertEquals(100, message.getOffsetNumber1());
        assertEquals(2, message.getReservedSlots1());
        assertEquals(7, message.getTimeout1());
        assertEquals(375, message.getIncrement1());
        assertEquals(1500, message.getOffsetNumber2());
        assertEquals(1, message.getReservedSlots2());
        assertEquals(5, message.getTimeout2());
        assertEquals(1125, message.getIncrement2());
        assertEquals(900, message.getOffsetNumber3());
        assertEquals(5, message.getReservedSlots3());
        assertEquals(7, message.getTimeout3());
        assertEquals(1125, message.getIncrement3());
        assertNull(message.getOffsetNumber4());
        assertNull(message.getReservedSlots4());
        assertNull(message.getTimeout4());
        assertNull(message.getIncrement4());
    }

    /** Four reservation blocks (160 bits, ITU-R M.1371-6 Table 70). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecodeFourReservationBlocks() {
        Bits b = bits().u(20, 6).u(0, 2).u(2190047, 30).u(0, 2);
        for (int i = 1; i <= 4; i++) b.u(100 * i, 12).u(i, 4).u(i + 2, 3).u(250 * i, 11);

        DataLinkManagement message = (DataLinkManagement) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(100, message.getOffsetNumber1());
        assertEquals(1, message.getReservedSlots1());
        assertEquals(3, message.getTimeout1());
        assertEquals(250, message.getIncrement1());
        assertEquals(200, message.getOffsetNumber2());
        assertEquals(2, message.getReservedSlots2());
        assertEquals(4, message.getTimeout2());
        assertEquals(500, message.getIncrement2());
        assertEquals(300, message.getOffsetNumber3());
        assertEquals(3, message.getReservedSlots3());
        assertEquals(5, message.getTimeout3());
        assertEquals(750, message.getIncrement3());
        assertEquals(400, message.getOffsetNumber4());
        assertEquals(4, message.getReservedSlots4());
        assertEquals(6, message.getTimeout4());
        assertEquals(1000, message.getIncrement4());
    }
}
