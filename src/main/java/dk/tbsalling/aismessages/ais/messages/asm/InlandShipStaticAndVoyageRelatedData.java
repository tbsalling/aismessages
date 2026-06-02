package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InlandShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {

    protected InlandShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.uniqueEuropeanVesselIdentificationNumber = AISText.decode(getBinaryData(), 0, 48);
        this.lengthOfShip = getBinaryData().getUnsignedInt(48, 61) / 10f;
        this.beamOfShip = getBinaryData().getUnsignedInt(61, 71) / 10f;
        this.shipOrCombinationType = getBinaryData().getUnsignedInt(71, 85);
        this.hazardousCargo = getBinaryData().getUnsignedInt(85, 88);
        this.draught = getBinaryData().getUnsignedInt(88, 99) / 100f;
        this.loaded = getBinaryData().getUnsignedInt(99, 101);
        this.qualityOfSpeedInformation = getBinaryData().getUnsignedInt(101, 102);
        this.qualityOfCourseInformation = getBinaryData().getUnsignedInt(102, 103);
        this.qualityOfHeadingInformation = getBinaryData().getUnsignedInt(103, 104);
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
