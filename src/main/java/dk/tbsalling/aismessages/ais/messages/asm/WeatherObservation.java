package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
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

    protected WeatherObservation(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields according to ITU-R M.1371-5
        this.longitude = getBinaryData().getSignedInt(0, 25) / 60000.0;
        this.latitude = getBinaryData().getSignedInt(25, 49) / 60000.0;
        this.day = getBinaryData().getUnsignedInt(49, 54);
        this.hour = getBinaryData().getUnsignedInt(54, 59);
        this.minute = getBinaryData().getUnsignedInt(59, 65);
        this.averageWindSpeed = getBinaryData().getUnsignedInt(65, 72);
        this.windGust = getBinaryData().getUnsignedInt(72, 79);
        this.windDirection = getBinaryData().getUnsignedInt(79, 88);
        this.windGustDirection = getBinaryData().getUnsignedInt(88, 97);
        this.airTemperature = getBinaryData().getSignedFloat(97, 108) / 10f;
        this.relativeHumidity = getBinaryData().getUnsignedInt(108, 115);
        this.dewPoint = getBinaryData().getSignedFloat(115, 125) / 10f;
        this.airPressure = getBinaryData().getUnsignedInt(125, 134);
        this.airPressureTendency = AirPressureTendency.valueOf(getBinaryData().getUnsignedInt(134, 136));
        this.horizontalVisibility = getBinaryData().getSignedFloat(136, 144) / 10f;
        this.waterLevel = getBinaryData().getSignedFloat(144, 153) / 10f;
        this.waterLevelTrend = WaterLevelTrend.valueOf(getBinaryData().getUnsignedInt(153, 155));
        this.surfaceCurrentSpeed = getBinaryData().getSignedFloat(155, 163) / 10f;
        this.surfaceCurrentDirection = getBinaryData().getUnsignedInt(163, 172);
        this.currentSpeed2 = getBinaryData().getSignedFloat(172, 180) / 10f;
        this.currentDirection2 = getBinaryData().getUnsignedInt(180, 189);
        this.currentDepth2 = getBinaryData().getUnsignedInt(189, 194);
        this.currentSpeed3 = getBinaryData().getSignedFloat(194, 202) / 10f;
        this.currentDirection3 = getBinaryData().getUnsignedInt(202, 211);
        this.currentDepth3 = getBinaryData().getUnsignedInt(211, 216);
        this.waveHeight = getBinaryData().getSignedFloat(216, 224) / 10f;
        this.wavePeriod = getBinaryData().getUnsignedInt(224, 230);
        this.waveDirection = getBinaryData().getUnsignedInt(230, 239);
        this.swellHeight = getBinaryData().getSignedFloat(239, 247) / 10f;
        this.swellPeriod = getBinaryData().getUnsignedInt(247, 253);
        this.swellDirection = getBinaryData().getUnsignedInt(253, 262);
        this.seaState = SeaState.valueOf(getBinaryData().getUnsignedInt(262, 266));
        this.waterTemperature = getBinaryData().getSignedFloat(266, 276) / 10f;
        this.precipitation = Precipitation.valueOf(getBinaryData().getUnsignedInt(276, 279));
        this.salinity = getBinaryData().getSignedFloat(279, 288) / 10f;
        this.ice = Ice.valueOf(getBinaryData().getUnsignedInt(288, 290));
    }

    /** Bits 0-25 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): longitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    double longitude;
    /** Bits 25-49 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): latitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    double latitude;
    /** Bits 49-54 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): day. */
    int day;
    /** Bits 54-59 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): hour. */
    int hour;
    /** Bits 59-65 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): minute. */
    int minute;
    /** Bits 65-72 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): average wind speed. */
    int averageWindSpeed;
    /** Bits 72-79 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): wind gust. */
    int windGust;
    /** Bits 79-88 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): wind direction. */
    int windDirection;
    /** Bits 88-97 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): wind gust direction. */
    int windGustDirection;
    /** Bits 97-108 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): air temperature (transmitted value ÷ 10), signed. */
    float airTemperature;
    /** Bits 108-115 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): relative humidity. */
    int relativeHumidity;
    /** Bits 115-125 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): dew point (transmitted value ÷ 10), signed. */
    float dewPoint;
    /** Bits 125-134 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): air pressure. */
    int airPressure;
    /** Bits 134-136 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): air pressure tendency code, decoded as {@link AirPressureTendency}. */
    AirPressureTendency airPressureTendency;
    /** Bits 136-144 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): horizontal visibility (transmitted value ÷ 10), signed. */
    float horizontalVisibility;
    /** Bits 144-153 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): water level (transmitted value ÷ 10), signed. */
    float waterLevel;
    /** Bits 153-155 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): water level trend code, decoded as {@link WaterLevelTrend}. */
    WaterLevelTrend waterLevelTrend;
    /** Bits 155-163 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): surface current speed (transmitted value ÷ 10), signed. */
    float surfaceCurrentSpeed;
    /** Bits 163-172 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): surface current direction. */
    int surfaceCurrentDirection;
    /** Bits 172-180 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): current speed2 (transmitted value ÷ 10), signed. */
    float currentSpeed2;
    /** Bits 180-189 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): current direction2. */
    int currentDirection2;
    /** Bits 189-194 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): current depth2. */
    int currentDepth2;
    /** Bits 194-202 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): current speed3 (transmitted value ÷ 10), signed. */
    float currentSpeed3;
    /** Bits 202-211 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): current direction3. */
    int currentDirection3;
    /** Bits 211-216 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): current depth3. */
    int currentDepth3;
    /** Bits 216-224 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): wave height (transmitted value ÷ 10), signed. */
    float waveHeight;
    /** Bits 224-230 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): wave period. */
    int wavePeriod;
    /** Bits 230-239 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): wave direction. */
    int waveDirection;
    /** Bits 239-247 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): swell height (transmitted value ÷ 10), signed. */
    float swellHeight;
    /** Bits 247-253 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): swell period. */
    int swellPeriod;
    /** Bits 253-262 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): swell direction. */
    int swellDirection;
    /** Bits 262-266 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): sea state code, decoded as {@link SeaState}. */
    SeaState seaState;
    /** Bits 266-276 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): water temperature (transmitted value ÷ 10), signed. */
    float waterTemperature;
    /** Bits 276-279 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): precipitation code, decoded as {@link Precipitation}. */
    Precipitation precipitation;
    /** Bits 279-288 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): salinity (transmitted value ÷ 10), signed. */
    float salinity;
    /** Bits 288-290 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21): ice code, decoded as {@link Ice}. */
    Ice ice;

    /** Codes of the air pressure tendency field, bits 134-136 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21). */
    public enum AirPressureTendency {
        /** 0 = steady. */
        STEADY(0),
        /** 1 = decreasing. */
        DECREASING(1),
        /** 2 = increasing. */
        INCREASING(2),
        /** 3 = not available. */
        NOT_AVAILABLE(3);

        /** The code as transmitted. */
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

    /** Codes of the water level trend field, bits 153-155 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21). */
    public enum WaterLevelTrend {
        /** 0 = steady. */
        STEADY(0),
        /** 1 = decreasing. */
        DECREASING(1),
        /** 2 = increasing. */
        INCREASING(2),
        /** 3 = not available. */
        NOT_AVAILABLE(3);

        /** The code as transmitted. */
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

    /** Codes of the sea state field, bits 262-266 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21). */
    public enum SeaState {
        /** 0 = calm glassy. */
        CALM_GLASSY(0),
        /** 1 = calm rippled. */
        CALM_RIPPLED(1),
        /** 2 = smooth. */
        SMOOTH(2),
        /** 3 = slight. */
        SLIGHT(3),
        /** 4 = moderate. */
        MODERATE(4),
        /** 5 = rough. */
        ROUGH(5),
        /** 6 = very rough. */
        VERY_ROUGH(6),
        /** 7 = high. */
        HIGH(7),
        /** 8 = very high. */
        VERY_HIGH(8),
        /** 9 = phenomenal. */
        PHENOMENAL(9),
        /** 15 = not available. */
        NOT_AVAILABLE(15);

        /** The code as transmitted. */
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

    /** Codes of the precipitation field, bits 276-279 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21). */
    public enum Precipitation {
        /** 0 = reserved. */
        RESERVED(0),
        /** 1 = rain. */
        RAIN(1),
        /** 2 = thunderstorm. */
        THUNDERSTORM(2),
        /** 3 = freezing rain. */
        FREEZING_RAIN(3),
        /** 4 = mixed ice. */
        MIXED_ICE(4),
        /** 5 = snow. */
        SNOW(5),
        /** 6 = reserved 6. */
        RESERVED_6(6),
        /** 7 = not available. */
        NOT_AVAILABLE(7);

        /** The code as transmitted. */
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

    /** Codes of the ice field, bits 288-290 of the application data (IMO SN.1/Circ.289, DAC=1 FI=21). */
    public enum Ice {
        /** 0 = no. */
        NO(0),
        /** 1 = yes. */
        YES(1),
        /** 2 = reserved 2. */
        RESERVED_2(2),
        /** 3 = not available. */
        NOT_AVAILABLE(3);

        /** The code as transmitted. */
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
