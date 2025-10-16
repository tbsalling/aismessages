package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.*;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationSpecificMessageTest {

    @Test
    public void canDecodeTextDescription() {
        // Test that TextDescription messages are created for FI 0 and FI 1
        ApplicationSpecificMessage asm0 = ApplicationSpecificMessage.create(1, 0, "");
        assertInstanceOf(TextDescription.class, asm0);
        assertEquals(1, asm0.getDesignatedAreaCode());
        assertEquals(0, asm0.getFunctionalId());

        ApplicationSpecificMessage asm1 = ApplicationSpecificMessage.create(1, 1, "");
        assertInstanceOf(TextDescription.class, asm1);
        assertEquals(1, asm1.getDesignatedAreaCode());
        assertEquals(1, asm1.getFunctionalId());
    }

    @Test
    public void canDecodeUtcDateInquiry() {
        // Test that UtcDateInquiry is created for FI 10
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 10, "");
        assertInstanceOf(UtcDateInquiry.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(10, asm.getFunctionalId());
    }

    @Test
    public void canDecodeUtcDateResponse() {
        // Test that UtcDateResponse is created for FI 11
        // Just verify the message type is decoded correctly with basic data
        String binaryData = "00000000000000000000000000000000000000000000000000";
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 11, binaryData);
        assertInstanceOf(UtcDateResponse.class, asm);
        
        UtcDateResponse response = (UtcDateResponse) asm;
        assertEquals(1, response.getDesignatedAreaCode());
        assertEquals(11, response.getFunctionalId());
        assertEquals(0, response.getYear());
        assertEquals(0, response.getMonth());
        assertEquals(0, response.getDay());
        assertEquals(0, response.getHour());
        assertEquals(0, response.getMinute());
        assertEquals(0, response.getSecond());
    }

    @Test
    public void canDecodeTidalWindow() {
        // Test that TidalWindow is created for FI 14
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 14, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(TidalWindow.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(14, asm.getFunctionalId());
    }

    @Test
    public void canDecodeVtsGeneratedSyntheticTargets() {
        // Test that VtsGeneratedSyntheticTargets is created for FI 17
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 17, "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(VtsGeneratedSyntheticTargets.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(17, asm.getFunctionalId());
    }

    @Test
    public void canDecodeMarineTrafficSignal() {
        // Test that MarineTrafficSignal is created for FI 18 and 19
        ApplicationSpecificMessage asm18 = ApplicationSpecificMessage.create(1, 18, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(MarineTrafficSignal.class, asm18);
        assertEquals(1, asm18.getDesignatedAreaCode());
        assertEquals(18, asm18.getFunctionalId());

        ApplicationSpecificMessage asm19 = ApplicationSpecificMessage.create(1, 19, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(MarineTrafficSignal.class, asm19);
        assertEquals(1, asm19.getDesignatedAreaCode());
        assertEquals(19, asm19.getFunctionalId());
    }

    @Test
    public void canDecodeWeatherObservation() {
        // Test that WeatherObservation is created for FI 21
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 21, "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(WeatherObservation.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(21, asm.getFunctionalId());
    }

    @Test
    public void canDecodeAreaNotice() {
        // Test that AreaNotice is created for FI 22 and 23
        ApplicationSpecificMessage asm22 = ApplicationSpecificMessage.create(1, 22, "000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(AreaNotice.class, asm22);
        assertEquals(1, asm22.getDesignatedAreaCode());
        assertEquals(22, asm22.getFunctionalId());

        ApplicationSpecificMessage asm23 = ApplicationSpecificMessage.create(1, 23, "000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(AreaNotice.class, asm23);
        assertEquals(1, asm23.getDesignatedAreaCode());
        assertEquals(23, asm23.getFunctionalId());
    }

    @Test
    public void canDecodeDangerousCargoIndication() {
        // Test that DangerousCargoIndication is created for FI 25
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 25, "000000000000000000000000000000000000000000000000");
        assertInstanceOf(DangerousCargoIndication.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(25, asm.getFunctionalId());
    }

    @Test
    public void canDecodeEnvironmental() {
        // Test that Environmental is created for FI 26
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 26, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(Environmental.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(26, asm.getFunctionalId());
    }

    @Test
    public void canDecodeRouteInformation() {
        // Test that RouteInformation is created for FI 27 and 28
        ApplicationSpecificMessage asm27 = ApplicationSpecificMessage.create(1, 27, "000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(RouteInformation.class, asm27);
        assertEquals(1, asm27.getDesignatedAreaCode());
        assertEquals(27, asm27.getFunctionalId());

        ApplicationSpecificMessage asm28 = ApplicationSpecificMessage.create(1, 28, "000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(RouteInformation.class, asm28);
        assertEquals(1, asm28.getDesignatedAreaCode());
        assertEquals(28, asm28.getFunctionalId());
    }

    @Test
    public void canDecodeMeteorologicalAndHydrographicalData() {
        // Test that MeteorologicalAndHydrographicalData is created for FI 31
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 31, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(MeteorologicalAndHydrographicalData.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(31, asm.getFunctionalId());
    }

    @Test
    public void canDecodeExistingMessages() {
        // Test that existing messages still work
        ApplicationSpecificMessage asm20 = ApplicationSpecificMessage.create(1, 20, "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(BerthingData.class, asm20);
        assertEquals(1, asm20.getDesignatedAreaCode());
        assertEquals(20, asm20.getFunctionalId());

        ApplicationSpecificMessage asm24 = ApplicationSpecificMessage.create(1, 24, "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertInstanceOf(ExtendedShipStaticAndVoyageRelatedData.class, asm24);
        assertEquals(1, asm24.getDesignatedAreaCode());
        assertEquals(24, asm24.getFunctionalId());

        ApplicationSpecificMessage asm40 = ApplicationSpecificMessage.create(1, 40, "0000000000000");
        assertInstanceOf(NumberOfPersonsOnBoard.class, asm40);
        assertEquals(1, asm40.getDesignatedAreaCode());
        assertEquals(40, asm40.getFunctionalId());
    }

    @Test
    public void canDecodeUnknownMessage() {
        // Test that unknown messages fall back to UnknownApplicationSpecificMessage
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 99, "0000000000");
        assertInstanceOf(UnknownApplicationSpecificMessage.class, asm);
        assertEquals(1, asm.getDesignatedAreaCode());
        assertEquals(99, asm.getFunctionalId());
    }

}
