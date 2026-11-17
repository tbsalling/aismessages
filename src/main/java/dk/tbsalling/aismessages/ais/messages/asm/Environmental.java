package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - Environmental (DAC=1, FI=26)
 * Environmental data including wind, wave, and water information
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Environmental extends ApplicationSpecificMessage {

    protected Environmental(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = getBinaryData().getUnsignedInt(0, 10);
        this.longitude = getBinaryData().getSignedFloat(10, 35) / 60000f;
        this.latitude = getBinaryData().getSignedFloat(35, 59) / 60000f;
        this.day = getBinaryData().getUnsignedInt(59, 64);
        this.hour = getBinaryData().getUnsignedInt(64, 69);
        this.minute = getBinaryData().getUnsignedInt(69, 75);
        this.averageWindSpeed = getBinaryData().getUnsignedInt(75, 82);
        this.windGust = getBinaryData().getUnsignedInt(82, 89);
        this.windDirection = getBinaryData().getUnsignedInt(89, 98);
        this.windGustDirection = getBinaryData().getUnsignedInt(98, 107);
        this.airTemperature = getBinaryData().getSignedFloat(107, 118) / 10f;
        this.relativeHumidity = getBinaryData().getUnsignedInt(118, 125);
        this.dewPoint = getBinaryData().getSignedFloat(125, 135) / 10f;
        this.airPressure = getBinaryData().getUnsignedInt(135, 144);
        this.horizontalVisibility = getBinaryData().getSignedFloat(144, 152) / 10f;
        this.waterLevel = getBinaryData().getSignedFloat(152, 161) / 10f;
        this.surfaceCurrentSpeed = getBinaryData().getSignedFloat(161, 169) / 10f;
        this.surfaceCurrentDirection = getBinaryData().getUnsignedInt(169, 178);
        this.waveHeight = getBinaryData().getSignedFloat(178, 186) / 10f;
        this.wavePeriod = getBinaryData().getUnsignedInt(186, 192);
        this.waveDirection = getBinaryData().getUnsignedInt(192, 201);
        this.swellHeight = getBinaryData().getSignedFloat(201, 209) / 10f;
        this.swellPeriod = getBinaryData().getUnsignedInt(209, 215);
        this.swellDirection = getBinaryData().getUnsignedInt(215, 224);
        this.waterTemperature = getBinaryData().getSignedFloat(224, 234) / 10f;
        this.salinity = getBinaryData().getSignedFloat(234, 243) / 10f;
    }

    /** Bits 0-10 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): linkage ID. */
    int linkageId;
    /** Bits 10-35 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): longitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    float longitude;
    /** Bits 35-59 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): latitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    float latitude;
    /** Bits 59-64 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): day. */
    int day;
    /** Bits 64-69 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): hour. */
    int hour;
    /** Bits 69-75 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): minute. */
    int minute;
    /** Bits 75-82 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): average wind speed. */
    int averageWindSpeed;
    /** Bits 82-89 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): wind gust. */
    int windGust;
    /** Bits 89-98 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): wind direction. */
    int windDirection;
    /** Bits 98-107 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): wind gust direction. */
    int windGustDirection;
    /** Bits 107-118 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): air temperature (transmitted value ÷ 10), signed. */
    float airTemperature;
    /** Bits 118-125 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): relative humidity. */
    int relativeHumidity;
    /** Bits 125-135 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): dew point (transmitted value ÷ 10), signed. */
    float dewPoint;
    /** Bits 135-144 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): air pressure. */
    int airPressure;
    /** Bits 144-152 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): horizontal visibility (transmitted value ÷ 10), signed. */
    float horizontalVisibility;
    /** Bits 152-161 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): water level (transmitted value ÷ 10), signed. */
    float waterLevel;
    /** Bits 161-169 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): surface current speed (transmitted value ÷ 10), signed. */
    float surfaceCurrentSpeed;
    /** Bits 169-178 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): surface current direction. */
    int surfaceCurrentDirection;
    /** Bits 178-186 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): wave height (transmitted value ÷ 10), signed. */
    float waveHeight;
    /** Bits 186-192 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): wave period. */
    int wavePeriod;
    /** Bits 192-201 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): wave direction. */
    int waveDirection;
    /** Bits 201-209 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): swell height (transmitted value ÷ 10), signed. */
    float swellHeight;
    /** Bits 209-215 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): swell period. */
    int swellPeriod;
    /** Bits 215-224 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): swell direction. */
    int swellDirection;
    /** Bits 224-234 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): water temperature (transmitted value ÷ 10), signed. */
    float waterTemperature;
    /** Bits 234-243 of the application data (IMO SN.1/Circ.289, DAC=1 FI=26): salinity (transmitted value ÷ 10), signed. */
    float salinity;

}
