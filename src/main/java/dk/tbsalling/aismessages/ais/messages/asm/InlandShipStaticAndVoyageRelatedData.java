package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;

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

    public String getUniqueEuropeanVesselIdentificationNumber() {
        return uniqueEuropeanVesselIdentificationNumber;
    }

    public float getLengthOfShip() {
        return lengthOfShip;
    }

    public float getBeamOfShip() {
        return beamOfShip;
    }

    public int getShipOrCombinationType() {
        return shipOrCombinationType;
    }

    public int getHazardousCargo() {
        return hazardousCargo;
    }

    public float getDraught() {
        return draught;
    }

    public int getLoaded() {
        return loaded;
    }

    public int getQualityOfSpeedInformation() {
        return qualityOfSpeedInformation;
    }

    public int getQualityOfCourseInformation() {
        return qualityOfCourseInformation;
    }

    public int getQualityOfHeadingInformation() {
        return qualityOfHeadingInformation;
    }

    private final String uniqueEuropeanVesselIdentificationNumber;
    private final float lengthOfShip;
    private final float beamOfShip;
    private final int shipOrCombinationType;
    private final int hazardousCargo;
    private final float draught;
    private final int loaded;
    private final int qualityOfSpeedInformation;
    private final int qualityOfCourseInformation;
    private final int qualityOfHeadingInformation;
}
