package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
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

    protected MeteorologicalAndHydrographicalData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.longitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(0, 25)) / 60000f;
        this.latitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(25, 49)) / 60000f;
        this.accuracy = BitDecoder.INSTANCE.decodeBoolean(getBinaryData().substring(49, 50));
        this.day = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(50, 55));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(55, 60));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(60, 66));
        this.windSpeed = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(66, 73));
        this.windGust = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(73, 80));
        this.windDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(80, 89));
        this.windGustDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(89, 98));
        this.airTemperature = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(98, 109)) / 10f;
        this.relativeHumidity = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(109, 116));
        this.dewPoint = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(116, 126)) / 10f;
        this.airPressure = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(126, 135));
        this.airPressureTendency = AirPressureTendency.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(135, 137)));
        this.horizontalVisibility = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(137, 145)) / 10f;
        this.waterLevel = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(145, 157)) / 100f;
        this.waterLevelTrend = WaterLevelTrend.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(157, 159)));
        this.surfaceCurrentSpeed = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(159, 167)) / 10f;
        this.surfaceCurrentDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(167, 176));
        this.currentSpeed2 = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(176, 184)) / 10f;
        this.currentDirection2 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(184, 193));
        this.currentDepth2 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(193, 198));
        this.currentSpeed3 = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(198, 206)) / 10f;
        this.currentDirection3 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(206, 215));
        this.currentDepth3 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(215, 220));
        this.waveHeight = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(220, 228)) / 10f;
        this.wavePeriod = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(228, 234));
        this.waveDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(234, 243));
        this.swellHeight = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(243, 251)) / 10f;
        this.swellPeriod = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(251, 257));
        this.swellDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(257, 266));
        this.seaState = SeaState.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(266, 270)));
        this.waterTemperature = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(270, 280)) / 10f;
        this.precipitation = Precipitation.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(280, 283)));
        this.salinity = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(283, 292)) / 10f;
        this.ice = Ice.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(292, 294)));
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
