package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
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

    protected Environmental(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 10));
        this.longitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(10, 35)) / 60000f;
        this.latitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(35, 59)) / 60000f;
        this.day = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(59, 64));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(64, 69));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(69, 75));
        this.averageWindSpeed = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(75, 82));
        this.windGust = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(82, 89));
        this.windDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(89, 98));
        this.windGustDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(98, 107));
        this.airTemperature = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(107, 118)) / 10f;
        this.relativeHumidity = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(118, 125));
        this.dewPoint = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(125, 135)) / 10f;
        this.airPressure = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(135, 144));
        this.horizontalVisibility = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(144, 152)) / 10f;
        this.waterLevel = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(152, 161)) / 10f;
        this.surfaceCurrentSpeed = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(161, 169)) / 10f;
        this.surfaceCurrentDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(169, 178));
        this.waveHeight = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(178, 186)) / 10f;
        this.wavePeriod = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(186, 192));
        this.waveDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(192, 201));
        this.swellHeight = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(201, 209)) / 10f;
        this.swellPeriod = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(209, 215));
        this.swellDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(215, 224));
        this.waterTemperature = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(224, 234)) / 10f;
        this.salinity = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(234, 243)) / 10f;
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
