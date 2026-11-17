package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;

public class BinaryAcknowledgeMessageTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,702;bCSdToR`,0*34");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryAcknowledge, aisMessage.getMessageType());
        BinaryAcknowledge message = (BinaryAcknowledge) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2288206), message.getSourceMmsi());
        assertEquals(1, message.getNumOfAcks());
        assertEquals(0, message.getSequence1());
        assertEquals(new MMSI(992271914), message.getMmsi1());
        assertNull(message.getSequence2());
        assertNull(message.getMmsi2());
        assertNull(message.getSequence3());
        assertNull(message.getMmsi3());
        assertNull(message.getSequence4());
        assertNull(message.getMmsi4());
    }

    /** Message 7 with 2 acknowledgements (104 bits, ITU-R M.1371-6 Table 54). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecode2Acknowledgements() {
        Bits b = bits().u(7, 6).u(0, 2).u(219000001, 30).u(0, 2);
        for (int i = 1; i <= 2; i++) b.u(211000000 + i, 30).u(i % 4, 2);

        BinaryAcknowledge message = (BinaryAcknowledge) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(2, message.getNumOfAcks());
        assertEquals(new MMSI(211000001), message.getMmsi1());
        assertEquals(1, message.getSequence1());
        assertEquals(new MMSI(211000002), message.getMmsi2());
        assertEquals(2, message.getSequence2().intValue());
        assertNull(message.getMmsi3());
        assertNull(message.getSequence3());
        assertNull(message.getMmsi4());
        assertNull(message.getSequence4());
    }

    /** Message 7 with 3 acknowledgements (136 bits, ITU-R M.1371-6 Table 54). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecode3Acknowledgements() {
        Bits b = bits().u(7, 6).u(0, 2).u(219000001, 30).u(0, 2);
        for (int i = 1; i <= 3; i++) b.u(211000000 + i, 30).u(i % 4, 2);

        BinaryAcknowledge message = (BinaryAcknowledge) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(3, message.getNumOfAcks());
        assertEquals(new MMSI(211000001), message.getMmsi1());
        assertEquals(1, message.getSequence1());
        assertEquals(new MMSI(211000002), message.getMmsi2());
        assertEquals(2, message.getSequence2().intValue());
        assertEquals(new MMSI(211000003), message.getMmsi3());
        assertEquals(3, message.getSequence3().intValue());
        assertNull(message.getMmsi4());
        assertNull(message.getSequence4());
    }

    /** Message 7 with 4 acknowledgements (168 bits, ITU-R M.1371-6 Table 54). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecode4Acknowledgements() {
        Bits b = bits().u(7, 6).u(0, 2).u(219000001, 30).u(0, 2);
        for (int i = 1; i <= 4; i++) b.u(211000000 + i, 30).u(i % 4, 2);

        BinaryAcknowledge message = (BinaryAcknowledge) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(4, message.getNumOfAcks());
        assertEquals(new MMSI(211000001), message.getMmsi1());
        assertEquals(1, message.getSequence1());
        assertEquals(new MMSI(211000002), message.getMmsi2());
        assertEquals(2, message.getSequence2().intValue());
        assertEquals(new MMSI(211000003), message.getMmsi3());
        assertEquals(3, message.getSequence3().intValue());
        assertEquals(new MMSI(211000004), message.getMmsi4());
        assertEquals(0, message.getSequence4().intValue());
    }
}
