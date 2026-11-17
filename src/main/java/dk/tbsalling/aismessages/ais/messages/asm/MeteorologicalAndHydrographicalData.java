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
        this.longitude = getBinaryData().getSignedInt(0, 25) / 60000.0;
        this.latitude = getBinaryData().getSignedInt(25, 49) / 60000.0;
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

    /** Bits 0-25 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): longitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    double longitude;
    /** Bits 25-49 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): latitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    double latitude;
    /** Bit 49 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): accuracy flag. */
    boolean accuracy;
    /** Bits 50-55 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): day. */
    int day;
    /** Bits 55-60 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): hour. */
    int hour;
    /** Bits 60-66 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): minute. */
    int minute;
    /** Bits 66-73 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): wind speed. */
    int windSpeed;
    /** Bits 73-80 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): wind gust. */
    int windGust;
    /** Bits 80-89 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): wind direction. */
    int windDirection;
    /** Bits 89-98 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): wind gust direction. */
    int windGustDirection;
    /** Bits 98-109 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): air temperature (transmitted value ÷ 10), signed. */
    float airTemperature;
    /** Bits 109-116 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): relative humidity. */
    int relativeHumidity;
    /** Bits 116-126 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): dew point (transmitted value ÷ 10), signed. */
    float dewPoint;
    /** Bits 126-135 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): air pressure. */
    int airPressure;
    /** Bits 135-137 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): air pressure tendency code, decoded as {@link AirPressureTendency}. */
    AirPressureTendency airPressureTendency;
    /** Bits 137-145 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): horizontal visibility (transmitted value ÷ 10), signed. */
    float horizontalVisibility;
    /** Bits 145-157 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): water level (transmitted value ÷ 100), signed. */
    float waterLevel;
    /** Bits 157-159 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): water level trend code, decoded as {@link WaterLevelTrend}. */
    WaterLevelTrend waterLevelTrend;
    /** Bits 159-167 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): surface current speed (transmitted value ÷ 10), signed. */
    float surfaceCurrentSpeed;
    /** Bits 167-176 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): surface current direction. */
    int surfaceCurrentDirection;
    /** Bits 176-184 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): current speed2 (transmitted value ÷ 10), signed. */
    float currentSpeed2;
    /** Bits 184-193 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): current direction2. */
    int currentDirection2;
    /** Bits 193-198 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): current depth2. */
    int currentDepth2;
    /** Bits 198-206 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): current speed3 (transmitted value ÷ 10), signed. */
    float currentSpeed3;
    /** Bits 206-215 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): current direction3. */
    int currentDirection3;
    /** Bits 215-220 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): current depth3. */
    int currentDepth3;
    /** Bits 220-228 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): wave height (transmitted value ÷ 10), signed. */
    float waveHeight;
    /** Bits 228-234 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): wave period. */
    int wavePeriod;
    /** Bits 234-243 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): wave direction. */
    int waveDirection;
    /** Bits 243-251 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): swell height (transmitted value ÷ 10), signed. */
    float swellHeight;
    /** Bits 251-257 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): swell period. */
    int swellPeriod;
    /** Bits 257-266 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): swell direction. */
    int swellDirection;
    /** Bits 266-270 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): sea state code, decoded as {@link SeaState}. */
    SeaState seaState;
    /** Bits 270-280 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): water temperature (transmitted value ÷ 10), signed. */
    float waterTemperature;
    /** Bits 280-283 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): precipitation code, decoded as {@link Precipitation}. */
    Precipitation precipitation;
    /** Bits 283-292 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): salinity (transmitted value ÷ 10), signed. */
    float salinity;
    /** Bits 292-294 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31): ice code, decoded as {@link Ice}. */
    Ice ice;

    /** Codes of the air pressure tendency field, bits 135-137 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31). */
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

    /** Codes of the water level trend field, bits 157-159 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31). */
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

    /** Codes of the sea state field, bits 266-270 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31). */
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

    /** Codes of the precipitation field, bits 280-283 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31). */
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

    /** Codes of the ice field, bits 292-294 of the application data (IMO SN.1/Circ.289, DAC=1 FI=31). */
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
