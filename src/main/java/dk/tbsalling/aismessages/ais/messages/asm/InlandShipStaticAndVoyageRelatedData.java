package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InlandShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {

    protected InlandShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.uniqueEuropeanVesselIdentificationNumber = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(0, 48));
        this.lengthOfShip = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(48, 61)) / 10f;
        this.beamOfShip = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(61, 71)) / 10f;
        this.shipOrCombinationType = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(71, 85));
        this.hazardousCargo = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(85, 88));
        this.draught = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(88, 99)) / 100f;
        this.loaded = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(99, 101));
        this.qualityOfSpeedInformation = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(101, 102));
        this.qualityOfCourseInformation = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(102, 103));
        this.qualityOfHeadingInformation = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(103, 104));
    }

    String uniqueEuropeanVesselIdentificationNumber;
    float lengthOfShip;
    float beamOfShip;
    int shipOrCombinationType;
    int hazardousCargo;
    float draught;
    int loaded;
    int qualityOfSpeedInformation;
    int qualityOfCourseInformation;
    int qualityOfHeadingInformation;
}
