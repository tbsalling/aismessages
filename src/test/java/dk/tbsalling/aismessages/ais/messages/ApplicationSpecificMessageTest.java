package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.*;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationSpecificMessageTest {

    @Test
    public void canDecodeTextDescription() {
        // Arrange
        String binaryData = "";

        // Act
        ApplicationSpecificMessage asm0 = ApplicationSpecificMessage.create(1, 0, binaryData);
        ApplicationSpecificMessage asm1 = ApplicationSpecificMessage.create(1, 1, binaryData);

        // Assert
        assertTrue(asm0 instanceof TextDescription);
        if (asm0 instanceof TextDescription textDescription) {
            assertEquals(1, textDescription.getDesignatedAreaCode());
            assertEquals(0, textDescription.getFunctionalId());
        }

        assertTrue(asm1 instanceof TextDescription);
        if (asm1 instanceof TextDescription textDescription) {
            assertEquals(1, textDescription.getDesignatedAreaCode());
            assertEquals(1, textDescription.getFunctionalId());
        }
    }

    @Test
    public void canDecodeUtcDateInquiry() {
        // Arrange
        String binaryData = "";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 10, binaryData);

        // Assert
        assertTrue(asm instanceof UtcDateInquiry);
        if (asm instanceof UtcDateInquiry inquiry) {
            assertEquals(1, inquiry.getDesignatedAreaCode());
            assertEquals(10, inquiry.getFunctionalId());
        }
    }

    @Test
    public void canDecodeUtcDateResponse() {
        // Arrange
        String binaryData = "00000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 11, binaryData);

        // Assert
        assertTrue(asm instanceof UtcDateResponse);
        if (asm instanceof UtcDateResponse response) {
            assertEquals(1, response.getDesignatedAreaCode());
            assertEquals(11, response.getFunctionalId());
            assertEquals(0, response.getYear());
            assertEquals(0, response.getMonth());
            assertEquals(0, response.getDay());
            assertEquals(0, response.getHour());
            assertEquals(0, response.getMinute());
            assertEquals(0, response.getSecond());
        }
    }

    @Test
    public void canDecodeTidalWindow() {
        // Arrange
        String binaryData = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 14, binaryData);

        // Assert
        assertTrue(asm instanceof TidalWindow);
        if (asm instanceof TidalWindow tidalWindow) {
            assertEquals(1, tidalWindow.getDesignatedAreaCode());
            assertEquals(14, tidalWindow.getFunctionalId());
        }
    }

    @Test
    public void canDecodeVtsGeneratedSyntheticTargets() {
        // Arrange
        String binaryData = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 17, binaryData);

        // Assert
        assertTrue(asm instanceof VtsGeneratedSyntheticTargets);
        if (asm instanceof VtsGeneratedSyntheticTargets vtsTargets) {
            assertEquals(1, vtsTargets.getDesignatedAreaCode());
            assertEquals(17, vtsTargets.getFunctionalId());
        }
    }

    @Test
    public void canDecodeMarineTrafficSignal() {
        // Arrange
        String binaryData = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm18 = ApplicationSpecificMessage.create(1, 18, binaryData);
        ApplicationSpecificMessage asm19 = ApplicationSpecificMessage.create(1, 19, binaryData);

        // Assert
        assertTrue(asm18 instanceof MarineTrafficSignal);
        if (asm18 instanceof MarineTrafficSignal signal18) {
            assertEquals(1, signal18.getDesignatedAreaCode());
            assertEquals(18, signal18.getFunctionalId());
        }

        assertTrue(asm19 instanceof MarineTrafficSignal);
        if (asm19 instanceof MarineTrafficSignal signal19) {
            assertEquals(1, signal19.getDesignatedAreaCode());
            assertEquals(19, signal19.getFunctionalId());
        }
    }

    @Test
    public void canDecodeWeatherObservation() {
        // Arrange
        String binaryData = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 21, binaryData);

        // Assert
        assertTrue(asm instanceof WeatherObservation);
        if (asm instanceof WeatherObservation weather) {
            assertEquals(1, weather.getDesignatedAreaCode());
            assertEquals(21, weather.getFunctionalId());
        }
    }

    @Test
    public void canDecodeAreaNotice() {
        // Arrange
        String binaryData = "000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm22 = ApplicationSpecificMessage.create(1, 22, binaryData);
        ApplicationSpecificMessage asm23 = ApplicationSpecificMessage.create(1, 23, binaryData);

        // Assert
        assertTrue(asm22 instanceof AreaNotice);
        if (asm22 instanceof AreaNotice notice22) {
            assertEquals(1, notice22.getDesignatedAreaCode());
            assertEquals(22, notice22.getFunctionalId());
        }

        assertTrue(asm23 instanceof AreaNotice);
        if (asm23 instanceof AreaNotice notice23) {
            assertEquals(1, notice23.getDesignatedAreaCode());
            assertEquals(23, notice23.getFunctionalId());
        }
    }

    @Test
    public void canDecodeDangerousCargoIndication() {
        // Arrange
        String binaryData = "000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 25, binaryData);

        // Assert
        assertTrue(asm instanceof DangerousCargoIndication);
        if (asm instanceof DangerousCargoIndication cargo) {
            assertEquals(1, cargo.getDesignatedAreaCode());
            assertEquals(25, cargo.getFunctionalId());
        }
    }

    @Test
    public void canDecodeEnvironmental() {
        // Arrange
        String binaryData = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 26, binaryData);

        // Assert
        assertTrue(asm instanceof Environmental);
        if (asm instanceof Environmental environmental) {
            assertEquals(1, environmental.getDesignatedAreaCode());
            assertEquals(26, environmental.getFunctionalId());
        }
    }

    @Test
    public void canDecodeRouteInformation() {
        // Arrange
        String binaryData = "000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm27 = ApplicationSpecificMessage.create(1, 27, binaryData);
        ApplicationSpecificMessage asm28 = ApplicationSpecificMessage.create(1, 28, binaryData);

        // Assert
        assertTrue(asm27 instanceof RouteInformation);
        if (asm27 instanceof RouteInformation route27) {
            assertEquals(1, route27.getDesignatedAreaCode());
            assertEquals(27, route27.getFunctionalId());
        }

        assertTrue(asm28 instanceof RouteInformation);
        if (asm28 instanceof RouteInformation route28) {
            assertEquals(1, route28.getDesignatedAreaCode());
            assertEquals(28, route28.getFunctionalId());
        }
    }

    @Test
    public void canDecodeMeteorologicalAndHydrographicalData() {
        // Arrange
        String binaryData = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 31, binaryData);

        // Assert
        assertTrue(asm instanceof MeteorologicalAndHydrographicalData);
        if (asm instanceof MeteorologicalAndHydrographicalData metHydro) {
            assertEquals(1, metHydro.getDesignatedAreaCode());
            assertEquals(31, metHydro.getFunctionalId());
        }
    }

    @Test
    public void canDecodeExistingMessages() {
        // Arrange
        String binaryDataFi20 = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String binaryDataFi24 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String binaryDataFi40 = "0000000000000";

        // Act
        ApplicationSpecificMessage asm20 = ApplicationSpecificMessage.create(1, 20, binaryDataFi20);
        ApplicationSpecificMessage asm24 = ApplicationSpecificMessage.create(1, 24, binaryDataFi24);
        ApplicationSpecificMessage asm40 = ApplicationSpecificMessage.create(1, 40, binaryDataFi40);

        // Assert
        assertTrue(asm20 instanceof BerthingData);
        if (asm20 instanceof BerthingData berthingData) {
            assertEquals(1, berthingData.getDesignatedAreaCode());
            assertEquals(20, berthingData.getFunctionalId());
        }

        assertTrue(asm24 instanceof ExtendedShipStaticAndVoyageRelatedData);
        if (asm24 instanceof ExtendedShipStaticAndVoyageRelatedData extendedData) {
            assertEquals(1, extendedData.getDesignatedAreaCode());
            assertEquals(24, extendedData.getFunctionalId());
        }

        assertTrue(asm40 instanceof NumberOfPersonsOnBoard);
        if (asm40 instanceof NumberOfPersonsOnBoard personsOnBoard) {
            assertEquals(1, personsOnBoard.getDesignatedAreaCode());
            assertEquals(40, personsOnBoard.getFunctionalId());
        }
    }

    @Test
    public void canDecodeUnknownMessage() {
        // Arrange
        String binaryData = "0000000000";

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 99, binaryData);

        // Assert
        assertTrue(asm instanceof UnknownApplicationSpecificMessage);
        if (asm instanceof UnknownApplicationSpecificMessage unknown) {
            assertEquals(1, unknown.getDesignatedAreaCode());
            assertEquals(99, unknown.getFunctionalId());
        }
    }

}
