package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Weather Observation Report from Ship (DAC=1, FI=21)
 * Weather and sea state information reported by ships
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WeatherObservation extends ApplicationSpecificMessage {

    protected WeatherObservation(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields according to ITU-R M.1371-5
        this.longitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(0, 25)) / 60000f;
        this.latitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(25, 49)) / 60000f;
        this.day = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(49, 54));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(54, 59));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(59, 65));
        this.averageWindSpeed = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(65, 72));
        this.windGust = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(72, 79));
        this.windDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(79, 88));
        this.windGustDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(88, 97));
        this.airTemperature = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(97, 108)) / 10f;
        this.relativeHumidity = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(108, 115));
        this.dewPoint = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(115, 125)) / 10f;
        this.airPressure = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(125, 134));
        this.airPressureTendency = AirPressureTendency.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(134, 136)));
        this.horizontalVisibility = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(136, 144)) / 10f;
        this.waterLevel = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(144, 153)) / 10f;
        this.waterLevelTrend = WaterLevelTrend.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(153, 155)));
        this.surfaceCurrentSpeed = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(155, 163)) / 10f;
        this.surfaceCurrentDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(163, 172));
        this.currentSpeed2 = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(172, 180)) / 10f;
        this.currentDirection2 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(180, 189));
        this.currentDepth2 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(189, 194));
        this.currentSpeed3 = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(194, 202)) / 10f;
        this.currentDirection3 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(202, 211));
        this.currentDepth3 = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(211, 216));
        this.waveHeight = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(216, 224)) / 10f;
        this.wavePeriod = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(224, 230));
        this.waveDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(230, 239));
        this.swellHeight = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(239, 247)) / 10f;
        this.swellPeriod = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(247, 253));
        this.swellDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(253, 262));
        this.seaState = SeaState.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(262, 266)));
        this.waterTemperature = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(266, 276)) / 10f;
        this.precipitation = Precipitation.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(276, 279)));
        this.salinity = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(279, 288)) / 10f;
        this.ice = Ice.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(288, 290)));
    }

    float longitude;
    float latitude;
    int day;
    int hour;
    int minute;
    int averageWindSpeed;
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
