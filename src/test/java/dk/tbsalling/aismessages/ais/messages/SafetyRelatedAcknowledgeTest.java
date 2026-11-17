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

public class SafetyRelatedAcknowledgeTest {

    @Test
    public void canDecodeWithOneAck() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,=39UOj0jFs9R,0*17");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.SafetyRelatedAcknowledge, aisMessage.getMessageType());
        SafetyRelatedAcknowledge message = (SafetyRelatedAcknowledge) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(211378120), message.getSourceMmsi());
        assertEquals(0, message.getSpare());
        assertEquals(new MMSI(211217560), message.getMmsi1());
        assertEquals(2, message.getSequence1());
        assertNull(message.getMmsi2());
        assertEquals(-1, message.getSequence2());
        assertNull(message.getMmsi3());
        assertEquals(-1, message.getSequence3());
        assertNull(message.getMmsi4());
        assertEquals(-1, message.getSequence4());
        assertEquals(1, message.getNumOfAcks());
    }

    @Test
    public void canDecodeWithMultipleAcks() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,=8157oQGOv9f,0*0F");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.SafetyRelatedAcknowledge, aisMessage.getMessageType());
        SafetyRelatedAcknowledge message = (SafetyRelatedAcknowledge) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(538003422), message.getSourceMmsi());
        assertEquals(0, message.getSpare());
        assertEquals(new MMSI(366999707), message.getMmsi1());
        assertEquals(2, message.getSequence1());
        assertEquals(1, message.getNumOfAcks());
    }

    /** Message 13 with 2 acknowledgements (104 bits, ITU-R M.1371-6 Table 54). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecode2Acknowledgements() {
        Bits b = bits().u(13, 6).u(0, 2).u(219000001, 30).u(0, 2);
        for (int i = 1; i <= 2; i++) b.u(211000000 + i, 30).u(i % 4, 2);

        SafetyRelatedAcknowledge message = (SafetyRelatedAcknowledge) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(2, message.getNumOfAcks());
        assertEquals(new MMSI(211000001), message.getMmsi1());
        assertEquals(1, message.getSequence1());
        assertEquals(new MMSI(211000002), message.getMmsi2());
        assertEquals(2, message.getSequence2());
        assertNull(message.getMmsi3());
        assertEquals(-1, message.getSequence3());
        assertNull(message.getMmsi4());
        assertEquals(-1, message.getSequence4());
    }

    /** Message 13 with 3 acknowledgements (136 bits, ITU-R M.1371-6 Table 54). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecode3Acknowledgements() {
        Bits b = bits().u(13, 6).u(0, 2).u(219000001, 30).u(0, 2);
        for (int i = 1; i <= 3; i++) b.u(211000000 + i, 30).u(i % 4, 2);

        SafetyRelatedAcknowledge message = (SafetyRelatedAcknowledge) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(3, message.getNumOfAcks());
        assertEquals(new MMSI(211000001), message.getMmsi1());
        assertEquals(1, message.getSequence1());
        assertEquals(new MMSI(211000002), message.getMmsi2());
        assertEquals(2, message.getSequence2());
        assertEquals(new MMSI(211000003), message.getMmsi3());
        assertEquals(3, message.getSequence3());
        assertNull(message.getMmsi4());
        assertEquals(-1, message.getSequence4());
    }

    /** Message 13 with 4 acknowledgements (168 bits, ITU-R M.1371-6 Table 54). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecode4Acknowledgements() {
        Bits b = bits().u(13, 6).u(0, 2).u(219000001, 30).u(0, 2);
        for (int i = 1; i <= 4; i++) b.u(211000000 + i, 30).u(i % 4, 2);

        SafetyRelatedAcknowledge message = (SafetyRelatedAcknowledge) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(4, message.getNumOfAcks());
        assertEquals(new MMSI(211000001), message.getMmsi1());
        assertEquals(1, message.getSequence1());
        assertEquals(new MMSI(211000002), message.getMmsi2());
        assertEquals(2, message.getSequence2());
        assertEquals(new MMSI(211000003), message.getMmsi3());
        assertEquals(3, message.getSequence3());
        assertEquals(new MMSI(211000004), message.getMmsi4());
        assertEquals(0, message.getSequence4());
    }
}
