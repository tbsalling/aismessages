package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.asm.InlandShipStaticAndVoyageRelatedData;
import dk.tbsalling.aismessages.ais.messages.asm.UnknownApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void canDecodeUnknownApplicationSpecificMessage() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,8@30oni?1j020@00,0*23"));

        System.out.println(aisMessage.toString());

        assertEquals(316, ((BinaryBroadcastMessage) aisMessage).getDesignatedAreaCode().intValue());
        assertEquals(7, ((BinaryBroadcastMessage) aisMessage).getFunctionalId().intValue());
        assertEquals(UnknownApplicationSpecificMessage.class, ((BinaryBroadcastMessage) aisMessage).getApplicationSpecificMessage().getClass());
    }

    @Test
    public void canDecodeMultiSentenceUnknownApplicationSpecificMessage() {
        AISMessage aisMessage = AISMessage.create(
                NMEAMessage.fromString("!AIVDM,2,1,8,A,803Iw60F14m1CPH4mDT4RDi@000003RP9iHb@001irBQ0@4gAaI00000261Q,0*04"),
                NMEAMessage.fromString("!AIVDM,2,2,8,A,pGp07IiTPi@BkU5pSwrrbs8219RW=R19RV=R19RVER19RVKtDb>jq20000>4,0*47")
        );

        System.out.println(aisMessage.toString());

        assertEquals(88, ((BinaryBroadcastMessage) aisMessage).getDesignatedAreaCode().intValue());
        assertEquals(4, ((BinaryBroadcastMessage) aisMessage).getFunctionalId().intValue());
        assertEquals(UnknownApplicationSpecificMessage.class, ((BinaryBroadcastMessage) aisMessage).getApplicationSpecificMessage().getClass());
    }


    @Test
    public void canDecodeDac200Fi10InlandShipStaticAndVoyageRelatedData1() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,839udkPj2d<dteLMt1T0a?bP01L0,0*79"));
        System.out.println(aisMessage.toString());

        assertTrue(aisMessage instanceof BinaryBroadcastMessage);
        BinaryBroadcastMessage binaryBroadcastMessage = (BinaryBroadcastMessage) aisMessage;
        assertEquals(200, binaryBroadcastMessage.getDesignatedAreaCode().intValue());
        assertEquals(10, binaryBroadcastMessage.getFunctionalId().intValue());

        ApplicationSpecificMessage asm = binaryBroadcastMessage.getApplicationSpecificMessage();
        assertEquals(200, asm.getDesignatedAreaCode());
        assertEquals(10, asm.getFunctionalId());

        assertTrue(asm instanceof InlandShipStaticAndVoyageRelatedData);
        InlandShipStaticAndVoyageRelatedData inlandMessage = (InlandShipStaticAndVoyageRelatedData) asm;

        assertEquals("02325170", inlandMessage.getUniqueEuropeanVesselIdentificationNumber());
        assertEquals(Float.valueOf(80.0f), inlandMessage.getLengthOfShip());
        assertEquals(Float.valueOf(8.2f), inlandMessage.getBeamOfShip());
        assertEquals(Integer.valueOf(8020), inlandMessage.getShipOrCombinationType());
        assertEquals(Integer.valueOf(0), inlandMessage.getHarzardousCargo());
        assertEquals(Float.valueOf(0.0f), inlandMessage.getDraught());
        assertEquals(Integer.valueOf(2), inlandMessage.getLoaded());
        assertEquals(Integer.valueOf(1), inlandMessage.getQualityOfSpeedInformation());
        assertEquals(Integer.valueOf(1), inlandMessage.getQualityOfCourseInformation());
        assertEquals(Integer.valueOf(1), inlandMessage.getQualityOfHeadingInformation());
    }

    @Test
    public void canDecodeDac200Fi10InlandShipStaticAndVoyageRelatedData2() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,83aDCr@j2P000000029Pt?cm0000,0*5F"));
        System.out.println(aisMessage.toString());

        assertTrue(aisMessage instanceof BinaryBroadcastMessage);
        BinaryBroadcastMessage binaryBroadcastMessage = (BinaryBroadcastMessage) aisMessage;
        assertEquals(200, binaryBroadcastMessage.getDesignatedAreaCode().intValue());
        assertEquals(10, binaryBroadcastMessage.getFunctionalId().intValue());

        ApplicationSpecificMessage asm = binaryBroadcastMessage.getApplicationSpecificMessage();
        assertEquals(200, asm.getDesignatedAreaCode());
        assertEquals(10, asm.getFunctionalId());

        assertTrue(asm instanceof InlandShipStaticAndVoyageRelatedData);
        InlandShipStaticAndVoyageRelatedData inlandMessage = (InlandShipStaticAndVoyageRelatedData) asm;

        assertEquals("", inlandMessage.getUniqueEuropeanVesselIdentificationNumber());
        assertEquals(Float.valueOf(110.0f), inlandMessage.getLengthOfShip());
        assertEquals(Float.valueOf(12.0f), inlandMessage.getBeamOfShip());
        assertEquals(Integer.valueOf(8030), inlandMessage.getShipOrCombinationType());
        assertEquals(Integer.valueOf(5), inlandMessage.getHarzardousCargo());
        assertEquals(Float.valueOf(0.0f), inlandMessage.getDraught());
        assertEquals(Integer.valueOf(0), inlandMessage.getLoaded());
        assertEquals(Integer.valueOf(0), inlandMessage.getQualityOfSpeedInformation());
        assertEquals(Integer.valueOf(0), inlandMessage.getQualityOfCourseInformation());
        assertEquals(Integer.valueOf(0), inlandMessage.getQualityOfHeadingInformation());
    }

}
