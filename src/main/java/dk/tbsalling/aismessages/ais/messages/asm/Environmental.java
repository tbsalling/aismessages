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

    int linkageId;
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
    float horizontalVisibility;
    float waterLevel;
    float surfaceCurrentSpeed;
    int surfaceCurrentDirection;
    float waveHeight;
    int wavePeriod;
    int waveDirection;
    float swellHeight;
    int swellPeriod;
    int swellDirection;
    float waterTemperature;
    float salinity;

}
