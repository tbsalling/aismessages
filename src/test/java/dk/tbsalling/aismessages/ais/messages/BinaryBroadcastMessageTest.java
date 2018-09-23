package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.InlandShipStaticAndVoyageRelatedData;
import dk.tbsalling.aismessages.ais.messages.asm.UnknownApplicationSpecificMessage;
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
        BinaryBroadcastMessage binaryBroadcastMessage = (BinaryBroadcastMessage) aisMessage;

        System.out.println(aisMessage.toString());

        assertEquals(200, binaryBroadcastMessage.getDesignatedAreaCode().intValue());
        assertEquals(10, binaryBroadcastMessage.getFunctionalId().intValue());
        assertEquals(InlandShipStaticAndVoyageRelatedData.class, binaryBroadcastMessage.getApplicationSpecificMessage().getClass());

        InlandShipStaticAndVoyageRelatedData asm = (InlandShipStaticAndVoyageRelatedData) binaryBroadcastMessage.getApplicationSpecificMessage();

        assertEquals(200, asm.getDesignatedAreaCode());
        assertEquals(10, asm.getFunctionalId());
        assertEquals("02325170", asm.getUniqueEuropeanVesselIdentificationNumber());
        assertEquals(Float.valueOf(80.0f), asm.getLengthOfShip());
        assertEquals(Float.valueOf(8.2f), asm.getBeamOfShip());
        assertEquals(Integer.valueOf(8020), asm.getShipOrCombinationType());
        assertEquals(Integer.valueOf(0), asm.getHarzardousCargo());
        assertEquals(Float.valueOf(0.0f), asm.getDraught());
        assertEquals(Integer.valueOf(2), asm.getLoaded());
        assertEquals(Integer.valueOf(1), asm.getQualityOfSpeedInformation());
        assertEquals(Integer.valueOf(1), asm.getQualityOfCourseInformation());
        assertEquals(Integer.valueOf(1), asm.getQualityOfHeadingInformation());
    }

    @Test
    public void canDecodeDac200Fi10InlandShipStaticAndVoyageRelatedData2() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,83aDCr@j2P000000029Pt?cm0000,0*5F"));
        BinaryBroadcastMessage binaryBroadcastMessage = (BinaryBroadcastMessage) aisMessage;

        System.out.println(aisMessage.toString());

        InlandShipStaticAndVoyageRelatedData asm = (InlandShipStaticAndVoyageRelatedData) binaryBroadcastMessage.getApplicationSpecificMessage();

        assertEquals("", asm.getUniqueEuropeanVesselIdentificationNumber());
        assertEquals(Float.valueOf(110.0f), asm.getLengthOfShip());
        assertEquals(Float.valueOf(12.0f), asm.getBeamOfShip());
        assertEquals(Integer.valueOf(8030), asm.getShipOrCombinationType());
        assertEquals(Integer.valueOf(5), asm.getHarzardousCargo());
        assertEquals(Float.valueOf(0.0f), asm.getDraught());
        assertEquals(Integer.valueOf(0), asm.getLoaded());
        assertEquals(Integer.valueOf(0), asm.getQualityOfSpeedInformation());
        assertEquals(Integer.valueOf(0), asm.getQualityOfCourseInformation());
        assertEquals(Integer.valueOf(0), asm.getQualityOfHeadingInformation());
    }

}
