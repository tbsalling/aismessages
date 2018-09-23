package dk.tbsalling.aismessages.ais.messages.asm;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

public class InlandShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {

    protected InlandShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }

    public String getUniqueEuropeanVesselIdentificationNumber() {
        return getDecodedValue(() -> uniqueEuropeanVesselIdentificationNumber, value -> uniqueEuropeanVesselIdentificationNumber = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBinaryData().substring(0,48)));
    }

    public Float getLengthOfShip() {
        return getDecodedValue(() -> lengthOfShip, value -> lengthOfShip = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(48, 61)) / 10f);
    }

    public Float getBeamOfShip() {
        return getDecodedValue(() -> beamOfShip, value -> beamOfShip = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(61, 71)) / 10f);
    }

    public Integer getShipOrCombinationType() {
        return getDecodedValue(() -> shipOrCombinationType, value -> shipOrCombinationType = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(71, 85)));
    }

    public Integer getHarzardousCargo() {
        return getDecodedValue(() -> harzardousCargo, value -> harzardousCargo = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(85, 88)));
    }

    public Float getDraught() {
        return getDecodedValue(() -> draught, value -> draught = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(88, 99)) / 100f);
    }

    public Integer getLoaded() {
        return getDecodedValue(() -> loaded, value -> loaded = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(99, 101)));
    }

    public Integer getQualityOfSpeedInformation() {
        return getDecodedValue(() -> qualityOfSpeedInformation, value -> qualityOfSpeedInformation = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(101, 102)));
    }

    public Integer getQualityOfCourseInformation() {
        return getDecodedValue(() -> qualityOfCourseInformation, value -> qualityOfCourseInformation = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(102, 103)));
    }

    public Integer getQualityOfHeadingInformation() {
        return getDecodedValue(() -> qualityOfHeadingInformation, value -> qualityOfHeadingInformation = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(103, 104)));
    }

    private transient String uniqueEuropeanVesselIdentificationNumber;
    private transient Float lengthOfShip;
    private transient Float beamOfShip;
    private transient Integer shipOrCombinationType;
    private transient Integer harzardousCargo;
    private transient Float draught;
    private transient Integer loaded;
    private transient Integer qualityOfSpeedInformation;
    private transient Integer qualityOfCourseInformation;
    private transient Integer qualityOfHeadingInformation;
}
