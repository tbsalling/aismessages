package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.asm.*;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationSpecificMessageTest {

    /**
     * Helper method to convert an integer to a binary string with specified bit length
     */
    private String toBinaryString(int value, int bitLength) {
        String binary = Integer.toBinaryString(value);
        return "0".repeat(Math.max(0, bitLength - binary.length())) + binary;
    }

    /**
     * Helper method to encode text to 6-bit ASCII binary string
     * Mapping: 0=@, 1=A, 2=B, ..., 26=Z, 32= (space), etc.
     */
    private String encodeText(String text, int numChars) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < numChars; i++) {
            if (i < text.length()) {
                char c = text.charAt(i);
                int value;
                if (c == ' ' || c == '@') {
                    value = 0;  // @ or space maps to 0
                } else if (c >= 'A' && c <= 'Z') {
                    value = c - 'A' + 1;  // A=1, B=2, ..., Z=26
                } else if (c >= '0' && c <= '9') {
                    value = c - '0' + 48;  // Numbers
                } else {
                    value = 0;  // Default to @ for unknown characters
                }
                binary.append(toBinaryString(value, 6));
            } else {
                binary.append("000000");  // Padding with @
            }
        }
        return binary.toString();
    }

    @Test
    public void canDecodeTextDescription() {
        // Arrange
        String testText = "SAFETY WARNING";
        String binaryData = encodeText(testText, 20); // 20 characters = 120 bits

        // Act
        ApplicationSpecificMessage asm0 = ApplicationSpecificMessage.create(1, 0, binaryData);
        ApplicationSpecificMessage asm1 = ApplicationSpecificMessage.create(1, 1, binaryData);

        // Assert
        assertTrue(asm0 instanceof TextDescription);
        if (asm0 instanceof TextDescription textDescription) {
            assertEquals(1, textDescription.getDesignatedAreaCode());
            assertEquals(0, textDescription.getFunctionalId());
            assertEquals(testText, textDescription.getText().trim());
        }

        assertTrue(asm1 instanceof TextDescription);
        if (asm1 instanceof TextDescription textDescription) {
            assertEquals(1, textDescription.getDesignatedAreaCode());
            assertEquals(1, textDescription.getFunctionalId());
            assertEquals(testText, textDescription.getText().trim());
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
        // Date: 2024-12-15 14:30:45
        String binaryData = toBinaryString(2024, 14)  // year: 2024
                + toBinaryString(12, 4)                // month: 12 (December)
                + toBinaryString(15, 5)                // day: 15
                + toBinaryString(14, 5)                // hour: 14
                + toBinaryString(30, 6)                // minute: 30
                + toBinaryString(45, 6);               // second: 45

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 11, binaryData);

        // Assert
        assertTrue(asm instanceof UtcDateResponse);
        if (asm instanceof UtcDateResponse response) {
            assertEquals(1, response.getDesignatedAreaCode());
            assertEquals(11, response.getFunctionalId());
            assertEquals(2024, response.getYear());
            assertEquals(12, response.getMonth());
            assertEquals(15, response.getDay());
            assertEquals(14, response.getHour());
            assertEquals(30, response.getMinute());
            assertEquals(45, response.getSecond());
        }
    }

    @Test
    public void canDecodeTidalWindow() {
        // Arrange
        // Tidal window: June 15, 08:30, tide to 12, tide from 6, current 2.5 knots at 90 degrees
        String binaryData = toBinaryString(6, 4)      // month: 6 (June)
                + toBinaryString(15, 5)                // day: 15
                + toBinaryString(8, 5)                 // hour: 8
                + toBinaryString(30, 6)                // minute: 30
                + toBinaryString(12, 5)                // tideTo: 12
                + toBinaryString(6, 5)                 // tideFrom: 6
                + toBinaryString(25, 7)                // currentSpeed: 25 (2.5 knots * 10)
                + toBinaryString(90, 9);               // currentDirection: 90 degrees

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 14, binaryData);

        // Assert
        assertTrue(asm instanceof TidalWindow);
        if (asm instanceof TidalWindow tidalWindow) {
            assertEquals(1, tidalWindow.getDesignatedAreaCode());
            assertEquals(14, tidalWindow.getFunctionalId());
            assertEquals(6, tidalWindow.getMonth());
            assertEquals(15, tidalWindow.getDay());
            assertEquals(8, tidalWindow.getHour());
            assertEquals(30, tidalWindow.getMinute());
            assertEquals(12, tidalWindow.getTideTo());
            assertEquals(6, tidalWindow.getTideFrom());
            assertEquals(2.5f, tidalWindow.getCurrentSpeed(), 0.01f);
            assertEquals(90, tidalWindow.getCurrentDirection());
        }
    }

    @Test
    public void canDecodeVtsGeneratedSyntheticTargets() {
        // Arrange
        // VTS target: linkage 456, name "TARGETALPHA", position 55.5N 12.5E, speed 12.5 knots, course 180 degrees
        String targetName = "TARGETALPHA";
        int longitude = (int) (12.5 * 600000);  // 12.5 degrees E
        int latitude = (int) (55.5 * 600000);   // 55.5 degrees N
        
        String binaryData = toBinaryString(456, 10)                    // linkageId: 456
                + encodeText(targetName, 20)                           // name: "TARGET01" (120 bits)
                + toBinaryString(1, 1)                                 // accuracy: true (high accuracy)
                + toBinaryString(longitude, 28)                        // longitude: 12.5E
                + toBinaryString(latitude, 27)                         // latitude: 55.5N
                + toBinaryString(125, 10)                              // speedOverGround: 125 (12.5 knots * 10)
                + toBinaryString(450, 9)                               // courseOverGround: 450 (45.0 degrees * 10)
                + toBinaryString(30, 6)                                // second: 30
                + toBinaryString(450, 9);                              // cog: 450 (45.0 degrees * 10)

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 17, binaryData);

        // Assert
        assertTrue(asm instanceof VtsGeneratedSyntheticTargets);
        if (asm instanceof VtsGeneratedSyntheticTargets vtsTargets) {
            assertEquals(1, vtsTargets.getDesignatedAreaCode());
            assertEquals(17, vtsTargets.getFunctionalId());
            assertEquals(456, vtsTargets.getLinkageId());
            assertTrue(vtsTargets.getName().trim().startsWith("TARGET"));
            assertTrue(vtsTargets.isAccuracy());
            assertEquals(12.5f, vtsTargets.getLongitude(), 0.01f);
            assertEquals(55.5f, vtsTargets.getLatitude(), 0.01f);
            assertEquals(12.5f, vtsTargets.getSpeedOverGround(), 0.1f);
            assertEquals(45.0f, vtsTargets.getCourseOverGround(), 0.1f);
            assertEquals(30, vtsTargets.getSecond());
            assertEquals(45.0f, vtsTargets.getCog(), 0.1f);
        }
    }

    @Test
    public void canDecodeMarineTrafficSignal() {
        // Arrange
        // Traffic signal: linkage 789, name "SIGNAL A", position 51.5N 0.0E, in operation, signal 5, time 09:15, next signal 10
        String signalName = "SIGNAL A";
        int longitude = 0;                              // 0.0 degrees (Greenwich)
        int latitude = (int) (51.5 * 600000);          // 51.5 degrees N (London)
        
        String binaryData = toBinaryString(789, 10)                    // linkageId: 789
                + encodeText(signalName, 20)                           // name: "SIGNAL A" (120 bits)
                + toBinaryString(longitude, 28)                        // longitude: 0.0E
                + toBinaryString(latitude, 27)                         // latitude: 51.5N
                + toBinaryString(1, 2)                                 // status: 1 (IN_OPERATION)
                + toBinaryString(5, 5)                                 // signal: 5
                + toBinaryString(9, 5)                                 // hour: 9
                + toBinaryString(15, 6)                                // minute: 15
                + toBinaryString(10, 5);                               // nextSignal: 10

        // Act
        ApplicationSpecificMessage asm18 = ApplicationSpecificMessage.create(1, 18, binaryData);
        ApplicationSpecificMessage asm19 = ApplicationSpecificMessage.create(1, 19, binaryData);

        // Assert
        assertTrue(asm18 instanceof MarineTrafficSignal);
        if (asm18 instanceof MarineTrafficSignal signal18) {
            assertEquals(1, signal18.getDesignatedAreaCode());
            assertEquals(18, signal18.getFunctionalId());
            assertEquals(789, signal18.getLinkageId());
            assertEquals(signalName, signal18.getName().trim());
            assertEquals(0.0f, signal18.getLongitude(), 0.01f);
            assertEquals(51.5f, signal18.getLatitude(), 0.01f);
            assertEquals(MarineTrafficSignal.SignalStatus.IN_OPERATION, signal18.getStatus());
            assertEquals(5, signal18.getSignal());
            assertEquals(9, signal18.getHour());
            assertEquals(15, signal18.getMinute());
            assertEquals(10, signal18.getNextSignal());
        }

        assertTrue(asm19 instanceof MarineTrafficSignal);
        if (asm19 instanceof MarineTrafficSignal signal19) {
            assertEquals(1, signal19.getDesignatedAreaCode());
            assertEquals(19, signal19.getFunctionalId());
            assertEquals(789, signal19.getLinkageId());
        }
    }

    @Test
    public void canDecodeWeatherObservation() {
        // Arrange
        // Weather from ship at 60.0N 5.0E on day 22 at 11:30
        // Wind: 18 knots from 225°, gusts 25 knots, temp 15.5°C, humidity 70%, pressure 1013 hPa
        // Waves: 2.0m height, 7s period from 240°, sea state MODERATE
        int longitude = (int) (5.0 * 60000);           // 5.0 degrees E
        int latitude = (int) (60.0 * 60000);           // 60.0 degrees N
        
        String binaryData = toBinaryString(longitude, 25)              // longitude: 5.0E
                + toBinaryString(latitude, 24)                         // latitude: 60.0N
                + toBinaryString(22, 5)                                // day: 22
                + toBinaryString(11, 5)                                // hour: 11
                + toBinaryString(30, 6)                                // minute: 30
                + toBinaryString(18, 7)                                // averageWindSpeed: 18 knots
                + toBinaryString(25, 7)                                // windGust: 25 knots
                + toBinaryString(225, 9)                               // windDirection: 225 degrees
                + toBinaryString(230, 9)                               // windGustDirection: 230 degrees
                + toBinaryString(155, 11)                              // airTemperature: 155 (15.5°C * 10)
                + toBinaryString(70, 7)                                // relativeHumidity: 70%
                + toBinaryString(105, 10)                              // dewPoint: 105 (10.5°C * 10)
                + toBinaryString(420, 9)                               // airPressure: 420 (example value within 9-bit range)
                + toBinaryString(0, 2)                                 // airPressureTendency: 0 (STEADY)
                + toBinaryString(80, 8)                                // horizontalVisibility: 80 (8.0 km * 10)
                + toBinaryString(22, 9)                                // waterLevel: 22 (2.2m * 10)
                + toBinaryString(0, 2)                                 // waterLevelTrend: 0 (STEADY)
                + toBinaryString(12, 8)                                // surfaceCurrentSpeed: 12 (1.2 knots * 10)
                + toBinaryString(180, 9)                               // surfaceCurrentDirection: 180 degrees
                + toBinaryString(8, 8)                                 // currentSpeed2: 8 (0.8 knots * 10)
                + toBinaryString(185, 9)                               // currentDirection2: 185 degrees
                + toBinaryString(10, 5)                                // currentDepth2: 10 meters
                + toBinaryString(5, 8)                                 // currentSpeed3: 5 (0.5 knots * 10)
                + toBinaryString(190, 9)                               // currentDirection3: 190 degrees
                + toBinaryString(20, 5)                                // currentDepth3: 20 meters
                + toBinaryString(20, 8)                                // waveHeight: 20 (2.0m * 10)
                + toBinaryString(7, 6)                                 // wavePeriod: 7 seconds
                + toBinaryString(240, 9)                               // waveDirection: 240 degrees
                + toBinaryString(15, 8)                                // swellHeight: 15 (1.5m * 10)
                + toBinaryString(9, 6)                                 // swellPeriod: 9 seconds
                + toBinaryString(250, 9)                               // swellDirection: 250 degrees
                + toBinaryString(4, 4)                                 // seaState: 4 (MODERATE)
                + toBinaryString(132, 10)                              // waterTemperature: 132 (13.2°C * 10)
                + toBinaryString(1, 3)                                 // precipitation: 1 (RAIN)
                + toBinaryString(240, 9)                               // salinity: 240 (24.0 PSU * 10)
                + toBinaryString(0, 2);                                // ice: 0 (NO)

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 21, binaryData);

        // Assert
        assertTrue(asm instanceof WeatherObservation);
        if (asm instanceof WeatherObservation weather) {
            assertEquals(1, weather.getDesignatedAreaCode());
            assertEquals(21, weather.getFunctionalId());
            assertEquals(5.0f, weather.getLongitude(), 0.01f);
            assertEquals(60.0f, weather.getLatitude(), 0.01f);
            assertEquals(22, weather.getDay());
            assertEquals(11, weather.getHour());
            assertEquals(30, weather.getMinute());
            assertEquals(18, weather.getAverageWindSpeed());
            assertEquals(25, weather.getWindGust());
            assertEquals(225, weather.getWindDirection());
            assertEquals(15.5f, weather.getAirTemperature(), 0.1f);
            assertEquals(70, weather.getRelativeHumidity());
            assertEquals(420, weather.getAirPressure());
            assertEquals(WeatherObservation.AirPressureTendency.STEADY, weather.getAirPressureTendency());
            assertEquals(2.0f, weather.getWaveHeight(), 0.1f);
            assertEquals(7, weather.getWavePeriod());
            assertEquals(240, weather.getWaveDirection());
            assertEquals(WeatherObservation.SeaState.MODERATE, weather.getSeaState());
            assertEquals(13.2f, weather.getWaterTemperature(), 0.1f);
            assertEquals(WeatherObservation.Precipitation.RAIN, weather.getPrecipitation());
            assertEquals(24.0f, weather.getSalinity(), 0.1f);
            assertEquals(WeatherObservation.Ice.NO, weather.getIce());
        }
    }

    @Test
    public void canDecodeAreaNotice() {
        // Arrange
        // Area notice: linkage 123, shallow water, July 20, 10:00, duration 180 minutes
        String binaryData = toBinaryString(123, 10)    // linkageId: 123
                + toBinaryString(19, 7)                // noticeType: 19 (SHALLOW_WATER)
                + toBinaryString(7, 4)                 // month: 7 (July)
                + toBinaryString(20, 5)                // day: 20
                + toBinaryString(10, 5)                // hour: 10
                + toBinaryString(0, 6)                 // minute: 0
                + toBinaryString(180, 18);             // durationMinutes: 180

        // Act
        ApplicationSpecificMessage asm22 = ApplicationSpecificMessage.create(1, 22, binaryData);
        ApplicationSpecificMessage asm23 = ApplicationSpecificMessage.create(1, 23, binaryData);

        // Assert
        assertTrue(asm22 instanceof AreaNotice);
        if (asm22 instanceof AreaNotice notice22) {
            assertEquals(1, notice22.getDesignatedAreaCode());
            assertEquals(22, notice22.getFunctionalId());
            assertEquals(123, notice22.getLinkageId());
            assertEquals(AreaNotice.NoticeType.SHALLOW_WATER, notice22.getNoticeType());
            assertEquals(7, notice22.getMonth());
            assertEquals(20, notice22.getDay());
            assertEquals(10, notice22.getHour());
            assertEquals(0, notice22.getMinute());
            assertEquals(180, notice22.getDurationMinutes());
        }

        assertTrue(asm23 instanceof AreaNotice);
        if (asm23 instanceof AreaNotice notice23) {
            assertEquals(1, notice23.getDesignatedAreaCode());
            assertEquals(23, notice23.getFunctionalId());
            assertEquals(123, notice23.getLinkageId());
            assertEquals(AreaNotice.NoticeType.SHALLOW_WATER, notice23.getNoticeType());
        }
    }

    @Test
    public void canDecodeDangerousCargoIndication() {
        // Arrange
        // Dangerous cargo: 50 tons (unit 1), cargo code 42, IMO Class 3, UN 1203, MARPOL category 1
        String binaryData = toBinaryString(1, 2)       // unit: 1 (tons)
                + toBinaryString(500, 10)              // amount: 500 (50.0 tons)
                + toBinaryString(42, 7)                // cargoCode: 42
                + toBinaryString(12, 4)                // imoClass: 12 (CLASS_3)
                + toBinaryString(1203, 13)             // unNumber: 1203 (gasoline)
                + toBinaryString(1, 1)                 // bc: true
                + toBinaryString(1, 1)                 // marpol: true
                + toBinaryString(1, 4);                // category: 1

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 25, binaryData);

        // Assert
        assertTrue(asm instanceof DangerousCargoIndication);
        if (asm instanceof DangerousCargoIndication cargo) {
            assertEquals(1, cargo.getDesignatedAreaCode());
            assertEquals(25, cargo.getFunctionalId());
            assertEquals(1, cargo.getUnit());
            assertEquals(500, cargo.getAmount());
            assertEquals(42, cargo.getCargoCode());
            assertEquals(DangerousCargoIndication.ImoClass.CLASS_3, cargo.getImoClass());
            assertEquals(1203, cargo.getUnNumber());
            assertTrue(cargo.isBc());
            assertTrue(cargo.isMarpol());
            assertEquals(1, cargo.getCategory());
        }
    }

    @Test
    public void canDecodeEnvironmental() {
        // Arrange
        // Environmental data: linkage 654, position 57.5N 10.5E, day 18, time 16:45
        // Wind 15 knots from 270°, temp 18.5°C, humidity 65%, pressure 1015 hPa
        // Water temp 14.2°C, salinity 35.0 PSU, wave height 1.5m, period 6s, from 280°
        int longitude = (int) (10.5 * 60000);          // 10.5 degrees E
        int latitude = (int) (57.5 * 60000);           // 57.5 degrees N
        
        String binaryData = toBinaryString(654, 10)                    // linkageId: 654
                + toBinaryString(longitude, 25)                        // longitude: 10.5E
                + toBinaryString(latitude, 24)                         // latitude: 57.5N
                + toBinaryString(18, 5)                                // day: 18
                + toBinaryString(16, 5)                                // hour: 16
                + toBinaryString(45, 6)                                // minute: 45
                + toBinaryString(15, 7)                                // averageWindSpeed: 15 knots
                + toBinaryString(20, 7)                                // windGust: 20 knots
                + toBinaryString(270, 9)                               // windDirection: 270 degrees
                + toBinaryString(275, 9)                               // windGustDirection: 275 degrees
                + toBinaryString(185, 11)                              // airTemperature: 185 (18.5°C * 10)
                + toBinaryString(65, 7)                                // relativeHumidity: 65%
                + toBinaryString(120, 10)                              // dewPoint: 120 (12.0°C * 10)
                + toBinaryString(400, 9)                               // airPressure: 400 (example value within 9-bit range)
                + toBinaryString(100, 8)                               // horizontalVisibility: 100 (10.0 km * 10)
                + toBinaryString(25, 9)                                // waterLevel: 25 (2.5m * 10)
                + toBinaryString(8, 8)                                 // surfaceCurrentSpeed: 8 (0.8 knots * 10)
                + toBinaryString(180, 9)                               // surfaceCurrentDirection: 180 degrees
                + toBinaryString(15, 8)                                // waveHeight: 15 (1.5m * 10)
                + toBinaryString(6, 6)                                 // wavePeriod: 6 seconds
                + toBinaryString(280, 9)                               // waveDirection: 280 degrees
                + toBinaryString(12, 8)                                // swellHeight: 12 (1.2m * 10)
                + toBinaryString(8, 6)                                 // swellPeriod: 8 seconds
                + toBinaryString(290, 9)                               // swellDirection: 290 degrees
                + toBinaryString(142, 10)                              // waterTemperature: 142 (14.2°C * 10)
                + toBinaryString(250, 9);                              // salinity: 250 (25.0 PSU * 10)

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 26, binaryData);

        // Assert
        assertTrue(asm instanceof Environmental);
        if (asm instanceof Environmental environmental) {
            assertEquals(1, environmental.getDesignatedAreaCode());
            assertEquals(26, environmental.getFunctionalId());
            assertEquals(654, environmental.getLinkageId());
            assertEquals(10.5f, environmental.getLongitude(), 0.01f);
            assertEquals(57.5f, environmental.getLatitude(), 0.01f);
            assertEquals(18, environmental.getDay());
            assertEquals(16, environmental.getHour());
            assertEquals(45, environmental.getMinute());
            assertEquals(15, environmental.getAverageWindSpeed());
            assertEquals(20, environmental.getWindGust());
            assertEquals(270, environmental.getWindDirection());
            assertEquals(18.5f, environmental.getAirTemperature(), 0.1f);
            assertEquals(65, environmental.getRelativeHumidity());
            assertEquals(400, environmental.getAirPressure());
            assertEquals(1.5f, environmental.getWaveHeight(), 0.1f);
            assertEquals(6, environmental.getWavePeriod());
            assertEquals(280, environmental.getWaveDirection());
            assertEquals(14.2f, environmental.getWaterTemperature(), 0.1f);
            assertEquals(25.0f, environmental.getSalinity(), 0.1f);
        }
    }

    @Test
    public void canDecodeRouteInformation() {
        // Arrange
        // Route: linkage 321, sender type 3, route type 2, start Aug 25 at 14:00, duration 360 minutes
        String binaryData = toBinaryString(321, 10)    // linkageId: 321
                + toBinaryString(3, 5)                 // senderType: 3
                + toBinaryString(2, 5)                 // routeType: 2
                + toBinaryString(8, 4)                 // month: 8 (August)
                + toBinaryString(25, 5)                // day: 25
                + toBinaryString(14, 5)                // hour: 14
                + toBinaryString(0, 6)                 // minute: 0
                + toBinaryString(360, 18);             // duration: 360 minutes (6 hours)

        // Act
        ApplicationSpecificMessage asm27 = ApplicationSpecificMessage.create(1, 27, binaryData);
        ApplicationSpecificMessage asm28 = ApplicationSpecificMessage.create(1, 28, binaryData);

        // Assert
        assertTrue(asm27 instanceof RouteInformation);
        if (asm27 instanceof RouteInformation route27) {
            assertEquals(1, route27.getDesignatedAreaCode());
            assertEquals(27, route27.getFunctionalId());
            assertEquals(321, route27.getLinkageId());
            assertEquals(3, route27.getSenderType());
            assertEquals(2, route27.getRouteType());
            assertEquals(8, route27.getMonth());
            assertEquals(25, route27.getDay());
            assertEquals(14, route27.getHour());
            assertEquals(0, route27.getMinute());
            assertEquals(360, route27.getDuration());
        }

        assertTrue(asm28 instanceof RouteInformation);
        if (asm28 instanceof RouteInformation route28) {
            assertEquals(1, route28.getDesignatedAreaCode());
            assertEquals(28, route28.getFunctionalId());
            assertEquals(321, route28.getLinkageId());
        }
    }

    @Test
    public void canDecodeMeteorologicalAndHydrographicalData() {
        // Arrange
        // Met/hydro data at 58.5N 11.0E on day 28 at 18:15, high accuracy
        // Wind 22 knots from 200°, temp 16.8°C, humidity 75%, pressure 1008 hPa, decreasing
        // Water level 3.2m increasing, waves 2.5m, period 8s from 210°
        int longitude = (int) (11.0 * 60000);          // 11.0 degrees E
        int latitude = (int) (58.5 * 60000);           // 58.5 degrees N
        
        String binaryData = toBinaryString(longitude, 25)              // longitude: 11.0E
                + toBinaryString(latitude, 24)                         // latitude: 58.5N
                + toBinaryString(1, 1)                                 // accuracy: true
                + toBinaryString(28, 5)                                // day: 28
                + toBinaryString(18, 5)                                // hour: 18
                + toBinaryString(15, 6)                                // minute: 15
                + toBinaryString(22, 7)                                // windSpeed: 22 knots
                + toBinaryString(28, 7)                                // windGust: 28 knots
                + toBinaryString(200, 9)                               // windDirection: 200 degrees
                + toBinaryString(205, 9)                               // windGustDirection: 205 degrees
                + toBinaryString(168, 11)                              // airTemperature: 168 (16.8°C * 10)
                + toBinaryString(75, 7)                                // relativeHumidity: 75%
                + toBinaryString(112, 10)                              // dewPoint: 112 (11.2°C * 10)
                + toBinaryString(410, 9)                               // airPressure: 410 (example value within 9-bit range)
                + toBinaryString(1, 2)                                 // airPressureTendency: 1 (DECREASING)
                + toBinaryString(65, 8)                                // horizontalVisibility: 65 (6.5 km * 10)
                + toBinaryString(320, 12)                              // waterLevel: 320 (3.2m * 100)
                + toBinaryString(2, 2)                                 // waterLevelTrend: 2 (INCREASING)
                + toBinaryString(15, 8)                                // surfaceCurrentSpeed: 15 (1.5 knots * 10)
                + toBinaryString(185, 9)                               // surfaceCurrentDirection: 185 degrees
                + toBinaryString(10, 8)                                // currentSpeed2: 10 (1.0 knots * 10)
                + toBinaryString(190, 9)                               // currentDirection2: 190 degrees
                + toBinaryString(12, 5)                                // currentDepth2: 12 meters
                + toBinaryString(6, 8)                                 // currentSpeed3: 6 (0.6 knots * 10)
                + toBinaryString(195, 9)                               // currentDirection3: 195 degrees
                + toBinaryString(25, 5)                                // currentDepth3: 25 meters
                + toBinaryString(25, 8)                                // waveHeight: 25 (2.5m * 10)
                + toBinaryString(8, 6)                                 // wavePeriod: 8 seconds
                + toBinaryString(210, 9)                               // waveDirection: 210 degrees
                + toBinaryString(18, 8)                                // swellHeight: 18 (1.8m * 10)
                + toBinaryString(10, 6)                                // swellPeriod: 10 seconds
                + toBinaryString(220, 9)                               // swellDirection: 220 degrees
                + toBinaryString(5, 4)                                 // seaState: 5 (ROUGH)
                + toBinaryString(145, 10)                              // waterTemperature: 145 (14.5°C * 10)
                + toBinaryString(2, 3)                                 // precipitation: 2 (THUNDERSTORM)
                + toBinaryString(238, 9)                               // salinity: 238 (23.8 PSU * 10)
                + toBinaryString(0, 2);                                // ice: 0 (NO)

        // Act
        ApplicationSpecificMessage asm = ApplicationSpecificMessage.create(1, 31, binaryData);

        // Assert
        assertTrue(asm instanceof MeteorologicalAndHydrographicalData);
        if (asm instanceof MeteorologicalAndHydrographicalData metHydro) {
            assertEquals(1, metHydro.getDesignatedAreaCode());
            assertEquals(31, metHydro.getFunctionalId());
            assertEquals(11.0f, metHydro.getLongitude(), 0.01f);
            assertEquals(58.5f, metHydro.getLatitude(), 0.01f);
            assertTrue(metHydro.isAccuracy());
            assertEquals(28, metHydro.getDay());
            assertEquals(18, metHydro.getHour());
            assertEquals(15, metHydro.getMinute());
            assertEquals(22, metHydro.getWindSpeed());
            assertEquals(200, metHydro.getWindDirection());
            assertEquals(16.8f, metHydro.getAirTemperature(), 0.1f);
            assertEquals(75, metHydro.getRelativeHumidity());
            assertEquals(410, metHydro.getAirPressure());
            assertEquals(MeteorologicalAndHydrographicalData.AirPressureTendency.DECREASING, metHydro.getAirPressureTendency());
            assertEquals(3.2f, metHydro.getWaterLevel(), 0.1f);
            assertEquals(MeteorologicalAndHydrographicalData.WaterLevelTrend.INCREASING, metHydro.getWaterLevelTrend());
            assertEquals(2.5f, metHydro.getWaveHeight(), 0.1f);
            assertEquals(8, metHydro.getWavePeriod());
            assertEquals(210, metHydro.getWaveDirection());
            assertEquals(MeteorologicalAndHydrographicalData.SeaState.ROUGH, metHydro.getSeaState());
            assertEquals(14.5f, metHydro.getWaterTemperature(), 0.1f);
            assertEquals(MeteorologicalAndHydrographicalData.Precipitation.THUNDERSTORM, metHydro.getPrecipitation());
            assertEquals(23.8f, metHydro.getSalinity(), 0.1f);
            assertEquals(MeteorologicalAndHydrographicalData.Ice.NO, metHydro.getIce());
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
