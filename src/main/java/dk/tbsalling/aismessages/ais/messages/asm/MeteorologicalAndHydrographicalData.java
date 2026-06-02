package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Meteorological and Hydrographical Data (DAC=1, FI=31)
 * Meteorological and hydrographical observation data
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MeteorologicalAndHydrographicalData extends ApplicationSpecificMessage {

    protected MeteorologicalAndHydrographicalData(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.longitude = getBinaryData().getSignedFloat(0, 25) / 60000f;
        this.latitude = getBinaryData().getSignedFloat(25, 49) / 60000f;
        this.accuracy = getBinaryData().getBoolean(49, 50);
        this.day = getBinaryData().getUnsignedInt(50, 55);
        this.hour = getBinaryData().getUnsignedInt(55, 60);
        this.minute = getBinaryData().getUnsignedInt(60, 66);
        this.windSpeed = getBinaryData().getUnsignedInt(66, 73);
        this.windGust = getBinaryData().getUnsignedInt(73, 80);
        this.windDirection = getBinaryData().getUnsignedInt(80, 89);
        this.windGustDirection = getBinaryData().getUnsignedInt(89, 98);
        this.airTemperature = getBinaryData().getSignedFloat(98, 109) / 10f;
        this.relativeHumidity = getBinaryData().getUnsignedInt(109, 116);
        this.dewPoint = getBinaryData().getSignedFloat(116, 126) / 10f;
        this.airPressure = getBinaryData().getUnsignedInt(126, 135);
        this.airPressureTendency = AirPressureTendency.valueOf(getBinaryData().getUnsignedInt(135, 137));
        this.horizontalVisibility = getBinaryData().getSignedFloat(137, 145) / 10f;
        this.waterLevel = getBinaryData().getSignedFloat(145, 157) / 100f;
        this.waterLevelTrend = WaterLevelTrend.valueOf(getBinaryData().getUnsignedInt(157, 159));
        this.surfaceCurrentSpeed = getBinaryData().getSignedFloat(159, 167) / 10f;
        this.surfaceCurrentDirection = getBinaryData().getUnsignedInt(167, 176);
        this.currentSpeed2 = getBinaryData().getSignedFloat(176, 184) / 10f;
        this.currentDirection2 = getBinaryData().getUnsignedInt(184, 193);
        this.currentDepth2 = getBinaryData().getUnsignedInt(193, 198);
        this.currentSpeed3 = getBinaryData().getSignedFloat(198, 206) / 10f;
        this.currentDirection3 = getBinaryData().getUnsignedInt(206, 215);
        this.currentDepth3 = getBinaryData().getUnsignedInt(215, 220);
        this.waveHeight = getBinaryData().getSignedFloat(220, 228) / 10f;
        this.wavePeriod = getBinaryData().getUnsignedInt(228, 234);
        this.waveDirection = getBinaryData().getUnsignedInt(234, 243);
        this.swellHeight = getBinaryData().getSignedFloat(243, 251) / 10f;
        this.swellPeriod = getBinaryData().getUnsignedInt(251, 257);
        this.swellDirection = getBinaryData().getUnsignedInt(257, 266);
        this.seaState = SeaState.valueOf(getBinaryData().getUnsignedInt(266, 270));
        this.waterTemperature = getBinaryData().getSignedFloat(270, 280) / 10f;
        this.precipitation = Precipitation.valueOf(getBinaryData().getUnsignedInt(280, 283));
        this.salinity = getBinaryData().getSignedFloat(283, 292) / 10f;
        this.ice = Ice.valueOf(getBinaryData().getUnsignedInt(292, 294));
    }

    float longitude;
    float latitude;
    boolean accuracy;
    int day;
    int hour;
    int minute;
    int windSpeed;
    int windGust;
    int windDirection;
    int windGustDirection;
    float airTemperature;
    int relativeHumidity;
    float dewPoint;
    int airPressure;
    AirPressureTendency airPressureTendency;
    float horizontalVisibility;
    float waterLevel;
    WaterLevelTrend waterLevelTrend;
    float surfaceCurrentSpeed;
    int surfaceCurrentDirection;
    float currentSpeed2;
    int currentDirection2;
    int currentDepth2;
    float currentSpeed3;
    int currentDirection3;
    int currentDepth3;
    float waveHeight;
    int wavePeriod;
    int waveDirection;
    float swellHeight;
    int swellPeriod;
    int swellDirection;
    SeaState seaState;
    float waterTemperature;
    Precipitation precipitation;
    float salinity;
    Ice ice;

    public enum AirPressureTendency {
        STEADY(0),
        DECREASING(1),
        INCREASING(2),
        NOT_AVAILABLE(3);

        int value;

        AirPressureTendency(int value) {
            this.value = value;
        }

        public static AirPressureTendency valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

    public enum WaterLevelTrend {
        STEADY(0),
        DECREASING(1),
        INCREASING(2),
        NOT_AVAILABLE(3);

        int value;

        WaterLevelTrend(int value) {
            this.value = value;
        }

        public static WaterLevelTrend valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

    public enum SeaState {
        CALM_GLASSY(0),
        CALM_RIPPLED(1),
        SMOOTH(2),
        SLIGHT(3),
        MODERATE(4),
        ROUGH(5),
        VERY_ROUGH(6),
        HIGH(7),
        VERY_HIGH(8),
        PHENOMENAL(9),
        NOT_AVAILABLE(15);

        int value;

        SeaState(int value) {
            this.value = value;
        }

        public static SeaState valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .orElse(NOT_AVAILABLE);
        }
    }

    public enum Precipitation {
        RESERVED(0),
        RAIN(1),
        THUNDERSTORM(2),
        FREEZING_RAIN(3),
        MIXED_ICE(4),
        SNOW(5),
        RESERVED_6(6),
        NOT_AVAILABLE(7);

        int value;

        Precipitation(int value) {
            this.value = value;
        }

        public static Precipitation valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

    public enum Ice {
        NO(0),
        YES(1),
        RESERVED_2(2),
        NOT_AVAILABLE(3);

        int value;

        Ice(int value) {
            this.value = value;
        }

        public static Ice valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

}
