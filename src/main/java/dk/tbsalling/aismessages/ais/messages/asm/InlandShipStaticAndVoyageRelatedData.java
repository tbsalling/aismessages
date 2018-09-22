package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.messages.AISMessage;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

public class InlandShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {

    protected InlandShipStaticAndVoyageRelatedData(AISMessage owner) {
        super(owner);
    }

    public String getUniqueEuropeanVesselIdentificationNumber() {
        return getDecodedValue(() -> uniqueEuropeanVesselIdentificationNumber, value -> uniqueEuropeanVesselIdentificationNumber = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBits(56, 104)));
    }

    public Float getLengthOfShip() {
        return getDecodedValue(() -> lengthOfShip, value -> lengthOfShip = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(104, 117)) / 10f);
    }

    public Float getBeamOfShip() {
        return getDecodedValue(() -> beamOfShip, value -> beamOfShip = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(117, 127)) / 10f);
    }

    public Integer getShipOrCombinationType() {
        return getDecodedValue(() -> shipOrCombinationType, value -> shipOrCombinationType = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(127, 141)));
    }

    public Integer getHarzardousCargo() {
        return getDecodedValue(() -> harzardousCargo, value -> harzardousCargo = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(141, 144)));
    }

    public Float getDraught() {
        return getDecodedValue(() -> draught, value -> draught = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(144, 155)) / 100f);
    }

    public Integer getLoaded() {
        return getDecodedValue(() -> loaded, value -> loaded = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(155, 157)));
    }

    public Integer getQualityOfSpeedInformation() {
        return getDecodedValue(() -> qualityOfSpeedInformation, value -> qualityOfSpeedInformation = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(157, 158)));
    }

    public Integer getQualityOfCourseInformation() {
        return getDecodedValue(() -> qualityOfCourseInformation, value -> qualityOfCourseInformation = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(158, 159)));
    }

    public Integer getQualityOfHeadingInformation() {
        return getDecodedValue(() -> qualityOfHeadingInformation, value -> qualityOfHeadingInformation = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(159, 160)));
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
