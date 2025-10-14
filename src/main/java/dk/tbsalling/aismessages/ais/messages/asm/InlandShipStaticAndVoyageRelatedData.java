package dk.tbsalling.aismessages.ais.messages.asm;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

public class InlandShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {

    protected InlandShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.uniqueEuropeanVesselIdentificationNumber = STRING_DECODER.apply(getBinaryData().substring(0, 48));
        this.lengthOfShip = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(48, 61)) / 10f;
        this.beamOfShip = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(61, 71)) / 10f;
        this.shipOrCombinationType = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(71, 85));
        this.hazardousCargo = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(85, 88));
        this.draught = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(88, 99)) / 100f;
        this.loaded = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(99, 101));
        this.qualityOfSpeedInformation = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(101, 102));
        this.qualityOfCourseInformation = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(102, 103));
        this.qualityOfHeadingInformation = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(103, 104));
    }

    public String getUniqueEuropeanVesselIdentificationNumber() {
        return uniqueEuropeanVesselIdentificationNumber;
    }

    public Float getLengthOfShip() {
        return lengthOfShip;
    }

    public Float getBeamOfShip() {
        return beamOfShip;
    }

    public Integer getShipOrCombinationType() {
        return shipOrCombinationType;
    }

    public Integer getHazardousCargo() {
        return hazardousCargo;
    }

    public Float getDraught() {
        return draught;
    }

    public Integer getLoaded() {
        return loaded;
    }

    public Integer getQualityOfSpeedInformation() {
        return qualityOfSpeedInformation;
    }

    public Integer getQualityOfCourseInformation() {
        return qualityOfCourseInformation;
    }

    public Integer getQualityOfHeadingInformation() {
        return qualityOfHeadingInformation;
    }

    private final String uniqueEuropeanVesselIdentificationNumber;
    private final Float lengthOfShip;
    private final Float beamOfShip;
    private final Integer shipOrCombinationType;
    private final Integer hazardousCargo;
    private final Float draught;
    private final Integer loaded;
    private final Integer qualityOfSpeedInformation;
    private final Integer qualityOfCourseInformation;
    private final Integer qualityOfHeadingInformation;
}
