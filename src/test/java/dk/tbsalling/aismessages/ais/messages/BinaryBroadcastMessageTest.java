package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.asm.InlandShipStaticAndVoyageRelatedData;
import dk.tbsalling.aismessages.ais.messages.asm.UnknownApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryBroadcastMessageTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,85MwpKiKf:MPiQa:ofV@v2mQTfB26oEtbEVqh4j1QDQPHjhpkNJ3,0*11");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BinaryBroadcastMessage, aisMessage.getMessageType());
        BinaryBroadcastMessage message = (BinaryBroadcastMessage) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(366999663), message.getSourceMmsi());
        assertEquals(366, message.getDesignatedAreaCode());
        assertEquals(56, message.getFunctionalId());
        // TODO : check the binary value
        assertEquals("1010011101100000110001100001101001001010110111101110100110010000111110000010110101100001100100101110010010000010000110110111010101111100101010010101100110111001110000000100110010000001100001010100100001100000011000110010110000111000110011011110011010000011", message.getBinaryData());
    }

    @Test
    public void canDecodeUnknownApplicationSpecificMessage() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,8@30oni?1j020@00,0*23");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(316, ((BinaryBroadcastMessage) aisMessage).getDesignatedAreaCode());
        assertEquals(7, ((BinaryBroadcastMessage) aisMessage).getFunctionalId());
        assertEquals(UnknownApplicationSpecificMessage.class, ((BinaryBroadcastMessage) aisMessage).getApplicationSpecificMessage().getClass());
    }

    @Test
    public void canDecodeMultiSentenceUnknownApplicationSpecificMessage() {
        // Arrange
        NMEAMessage nmeaMessage1 = new NMEAMessage("!AIVDM,2,1,8,A,803Iw60F14m1CPH4mDT4RDi@000003RP9iHb@001irBQ0@4gAaI00000261Q,0*04");
        NMEAMessage nmeaMessage2 = new NMEAMessage("!AIVDM,2,2,8,A,pGp07IiTPi@BkU5pSwrrbs8219RW=R19RV=R19RVER19RVKtDb>jq20000>4,0*47");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage1, nmeaMessage2);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(88, ((BinaryBroadcastMessage) aisMessage).getDesignatedAreaCode());
        assertEquals(4, ((BinaryBroadcastMessage) aisMessage).getFunctionalId());
        assertEquals(UnknownApplicationSpecificMessage.class, ((BinaryBroadcastMessage) aisMessage).getApplicationSpecificMessage().getClass());
    }


    @Test
    public void canDecodeDac200Fi10InlandShipStaticAndVoyageRelatedData1() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,839udkPj2d<dteLMt1T0a?bP01L0,0*79");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertTrue(aisMessage instanceof BinaryBroadcastMessage);
        BinaryBroadcastMessage binaryBroadcastMessage = (BinaryBroadcastMessage) aisMessage;
        assertEquals(200, binaryBroadcastMessage.getDesignatedAreaCode());
        assertEquals(10, binaryBroadcastMessage.getFunctionalId());

        ApplicationSpecificMessage asm = binaryBroadcastMessage.getApplicationSpecificMessage();
        assertEquals(200, asm.getDesignatedAreaCode());
        assertEquals(10, asm.getFunctionalId());

        assertTrue(asm instanceof InlandShipStaticAndVoyageRelatedData);
        InlandShipStaticAndVoyageRelatedData inlandMessage = (InlandShipStaticAndVoyageRelatedData) asm;

        assertEquals("02325170", inlandMessage.getUniqueEuropeanVesselIdentificationNumber());
        assertEquals(80.0f, inlandMessage.getLengthOfShip(), 0.0f);
        assertEquals(8.2f, inlandMessage.getBeamOfShip(), 0.0f);
        assertEquals(8020, inlandMessage.getShipOrCombinationType());
        assertEquals(0, inlandMessage.getHazardousCargo());
        assertEquals(0.0f, inlandMessage.getDraught(), 0.0f);
        assertEquals(2, inlandMessage.getLoaded());
        assertEquals(1, inlandMessage.getQualityOfSpeedInformation());
        assertEquals(1, inlandMessage.getQualityOfCourseInformation());
        assertEquals(1, inlandMessage.getQualityOfHeadingInformation());
    }

    @Test
    public void canDecodeDac200Fi10InlandShipStaticAndVoyageRelatedData2() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,83aDCr@j2P000000029Pt?cm0000,0*5F");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertTrue(aisMessage instanceof BinaryBroadcastMessage);
        BinaryBroadcastMessage binaryBroadcastMessage = (BinaryBroadcastMessage) aisMessage;
        assertEquals(200, binaryBroadcastMessage.getDesignatedAreaCode());
        assertEquals(10, binaryBroadcastMessage.getFunctionalId());

        ApplicationSpecificMessage asm = binaryBroadcastMessage.getApplicationSpecificMessage();
        assertEquals(200, asm.getDesignatedAreaCode());
        assertEquals(10, asm.getFunctionalId());

        assertTrue(asm instanceof InlandShipStaticAndVoyageRelatedData);
        InlandShipStaticAndVoyageRelatedData inlandMessage = (InlandShipStaticAndVoyageRelatedData) asm;

        assertEquals("", inlandMessage.getUniqueEuropeanVesselIdentificationNumber());
        assertEquals(110.0f, inlandMessage.getLengthOfShip(), 0.0f);
        assertEquals(12.0f, inlandMessage.getBeamOfShip(), 0.0f);
        assertEquals(8030, inlandMessage.getShipOrCombinationType());
        assertEquals(5, inlandMessage.getHazardousCargo());
        assertEquals(0.0f, inlandMessage.getDraught(), 0.0f);
        assertEquals(0, inlandMessage.getLoaded());
        assertEquals(0, inlandMessage.getQualityOfSpeedInformation());
        assertEquals(0, inlandMessage.getQualityOfCourseInformation());
        assertEquals(0, inlandMessage.getQualityOfHeadingInformation());
    }

    @Test
    public void canDecodeDac265Fi1EmptyPayload() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,83n30vi2@@,4*69");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertTrue(aisMessage instanceof BinaryBroadcastMessage);
        BinaryBroadcastMessage binaryBroadcastMessage = (BinaryBroadcastMessage) aisMessage;
        assertEquals(265, binaryBroadcastMessage.getDesignatedAreaCode());
        assertEquals(1, binaryBroadcastMessage.getFunctionalId());
        assertEquals("", binaryBroadcastMessage.getBinaryData());
    }

    @Test
    public void failsWithInvalidMessageWhenDecodingShortMessage() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,83aDCr@,0*5F");

        // Act & Assert
        assertThrows(InvalidMessage.class, () -> AISMessage.create(null, null, null, nmeaMessage));
    }

}
