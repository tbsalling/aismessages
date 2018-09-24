package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.asm.NumberOfPersonsOnBoard;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddressedBinaryMessageTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!ABVDM,1,1,,B,63M@g840SJL`01lSk09w1IMK?00100803Pp03g8p001pTaIK00,4*56"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.AddressedBinaryMessage, aisMessage.getMessageType());
        AddressedBinaryMessage message = (AddressedBinaryMessage) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(232009504), message.getSourceMmsi());
        assertEquals((Integer) 0, message.getDesignatedAreaCode());
        assertEquals((Integer) 29, message.getFunctionalId());
        // TODO : check the binary value
        assertEquals("0010001111001100000000100111111100000101100101110101101100111100000000000000000100000000000000100000000000001110000011100000000000001110111100100011100000000000000000000111100010010010100101100101101100000000", message.getBinaryData());
    }

    @Test
    public void canDecodeAsmNumberOfPersonsOnboard() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,63bump80OEGr06P060,4*79"));
        System.out.println(aisMessage.toString());

        assertTrue(aisMessage instanceof AddressedBinaryMessage);
        AddressedBinaryMessage addressedBinaryMessage = (AddressedBinaryMessage) aisMessage;
        assertEquals(1, addressedBinaryMessage.getDesignatedAreaCode().intValue());
        assertEquals(40, addressedBinaryMessage.getFunctionalId().intValue());

        ApplicationSpecificMessage asm = addressedBinaryMessage.getApplicationSpecificMessage();
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(40, asm.getFunctionalId());

        assertTrue(asm instanceof NumberOfPersonsOnBoard);
        NumberOfPersonsOnBoard numberOfPersonsOnBoard = (NumberOfPersonsOnBoard) asm;
        assertEquals("0000000000011000", numberOfPersonsOnBoard.getBinaryData());
        assertEquals(Integer.valueOf(3), numberOfPersonsOnBoard.getNumberOfPersons());
   }

}