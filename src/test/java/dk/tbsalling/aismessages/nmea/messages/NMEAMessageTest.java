package dk.tbsalling.aismessages.nmea.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NMEAMessageTest {

    @Test
    public void testValidChecksumAccepted() {
        // Valid NMEA message with correct checksum
        NMEAMessage msg = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
        assertTrue(msg.isChecksumValid());
    }

    @Test
    public void testInvalidChecksumRejected() {
        // Same message but with incorrect checksum (changed from 3A to 3B)
        NMEAMessage msg = new NMEAMessage("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3B");
        assertFalse(msg.isChecksumValid());
    }

    @Test
    public void testAnotherValidChecksum() {
        // Another valid message from test data
        NMEAMessage msg = new NMEAMessage("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");
        assertTrue(msg.isChecksumValid());
    }

    @Test
    public void testAnotherInvalidChecksum() {
        // Same message with wrong checksum (changed from 27 to 28)
        NMEAMessage msg = new NMEAMessage("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*28");
        assertFalse(msg.isChecksumValid());
    }

    @Test
    public void testFragmentedMessageWithValidChecksum() {
        // Second fragment with valid checksum
        NMEAMessage msg = new NMEAMessage("!AIVDM,2,2,3,B,p=Mh00000000000,2*4C");
        assertTrue(msg.isChecksumValid());
    }

    @Test
    public void testFragmentedMessageWithInvalidChecksum() {
        // Second fragment with invalid checksum (changed from 4C to 4D)
        NMEAMessage msg = new NMEAMessage("!AIVDM,2,2,3,B,p=Mh00000000000,2*4D");
        assertFalse(msg.isChecksumValid());
    }

    @Test
    public void testVDOMessageWithValidChecksum() {
        // VDO message type with valid checksum
        NMEAMessage msg = new NMEAMessage("!AIVDO,1,1,,A,15MqdBP000G@qoLEi69PVGaN0D0=,0*3B");
        assertTrue(msg.isChecksumValid());
    }

    @Test
    public void testVDOMessageWithInvalidChecksum() {
        // VDO message type with invalid checksum (changed from 3B to 3C)
        NMEAMessage msg = new NMEAMessage("!AIVDO,1,1,,A,15MqdBP000G@qoLEi69PVGaN0D0=,0*3C");
        assertFalse(msg.isChecksumValid());
    }

    @Test
    public void testChecksumWithEmptyFields() {
        // Message with empty fields (from test data)
        NMEAMessage msg = new NMEAMessage("!AIVDM,,1,,B,13cpFJ0P0100`lE4IIvW8@Ow`052p,0*53");
        assertTrue(msg.isChecksumValid());
    }
}
