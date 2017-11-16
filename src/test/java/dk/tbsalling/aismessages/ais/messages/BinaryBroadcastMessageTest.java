package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinaryBroadcastMessageTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,85MwpKiKf:MPiQa:ofV@v2mQTfB26oEtbEVqh4j1QDQPHjhpkNJ3,0*11"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.BinaryBroadcastMessage, aisMessage.getMessageType());
        BinaryBroadcastMessage message = (BinaryBroadcastMessage) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(366999663), message.getSourceMmsi());
        assertEquals((Integer) 366, message.getDesignatedAreaCode());
        assertEquals((Integer) 56, message.getFunctionalId());
        // TODO : check the binary value
        assertEquals("1010011101100000110001100001101001001010110111101110100110010000111110000010110101100001100100101110010010000010000110110111010101111100101010010101100110111001110000000100110010000001100001010100100001100000011000110010110000111000110011011110011010000011", message.getBinaryData());
    }
}
