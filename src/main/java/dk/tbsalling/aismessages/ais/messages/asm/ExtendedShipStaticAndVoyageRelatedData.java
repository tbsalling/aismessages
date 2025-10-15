package dk.tbsalling.aismessages.ais.messages.asm;

import static dk.tbsalling.aismessages.ais.BitStringParser.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.BitStringParser.UNSIGNED_INTEGER_DECODER;
import static java.util.Arrays.stream;

public class ExtendedShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {
    public ExtendedShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.messageLinkageId = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(0, 10));
        this.airDraught = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(10, 23)) / 10f;
        this.lastPortOfCall = STRING_DECODER.apply(getBinaryData().substring(23, 53));
        this.nextPortOfCall = STRING_DECODER.apply(getBinaryData().substring(53, 83));
        this.secondPortOfCall = STRING_DECODER.apply(getBinaryData().substring(83, 113));
        this.solasEquipmentStatusAISClassA = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(113, 115)));
        this.solasEquipmentStatusAutomaticTrackingAid = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(115, 117)));
        this.solasEquipmentStatusBridgeNavigationalWatchAlarmSystem = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(117, 119)));
        this.solasEquipmentStatusECDISBackup = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(119, 121)));
        this.solasEquipmentStatusECDISPaperNauticalChart = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(121, 123)));
        this.solasEquipmentStatusEchoSounder = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(123, 125)));
        this.solasEquipmentStatusElectronicPlottingAid = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(125, 127)));
        this.solasEquipmentStatusEmergencySteeringGear = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(127, 129)));
        this.solasEquipmentStatusNavigationSystem = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(129, 131)));
        this.solasEquipmentStatusGyroCompass = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(131, 133)));
        this.solasEquipmentStatusLRIT = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(133, 135)));
        this.solasEquipmentStatusMagneticCompass = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(135, 137)));
        this.solasEquipmentStatusNAVTEX = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(137, 139)));
        this.solasEquipmentStatusRadarARPA = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(139, 141)));
        this.solasEquipmentStatusRadarSBand = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(141, 143)));
        this.solasEquipmentStatusRadarXBand = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(143, 145)));
        this.solasEquipmentStatusRadioHF = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(145, 147)));
        this.solasEquipmentStatusRadioINMARSAT = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(147, 149)));
        this.solasEquipmentStatusRadioMF = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(149, 151)));
        this.solasEquipmentStatusRadioVHF = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(151, 153)));
        this.solasEquipmentStatusSpeedLogOverGround = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(153, 155)));
        this.solasEquipmentStatusSpeedLogThroughWater = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(155, 157)));
        this.solasEquipmentStatusTransmittingHeadingDevice = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(157, 159)));
        this.solasEquipmentStatusTrackControlSystem = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(159, 161)));
        this.solasEquipmentStatusVDR = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(161, 163)));
        this.solasEquipmentStatusFuture = SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(163, 165)));
        this.iceClass = IceClass.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(165, 169)));
        this.shaftHorsePower = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(169, 187));
        this.vhfWorkingChannel = STRING_DECODER.apply(getBinaryData().substring(187, 199));
        this.lloydsShipType = STRING_DECODER.apply(getBinaryData().substring(199, 241));
        this.grossTonnage = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(241, 259));
        this.ladenOrBallast = BallastStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(259, 261)));
        this.heavyFuelOil = BunkerOilStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(261, 263)));
        this.lightFuelOil = BunkerOilStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(263, 265)));
        this.diesel = BunkerOilStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(265, 267)));
        this.totalAmountOfBunkerOil = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(267, 281));
        this.numberOfPersons = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(281, 294));
    }

    public Integer getMessageLinkageId() {
        return messageLinkageId;
    }

    public Float getAirDraught() {
        return airDraught;
    }

    public String getLastPortOfCall() {
        return lastPortOfCall;
    }

    public String getNextPortOfCall() {
        return nextPortOfCall;
    }

    public String getSecondPortOfCall() {
        return secondPortOfCall;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusAISClassA() {
        return solasEquipmentStatusAISClassA;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusAutomaticTrackingAid() {
        return solasEquipmentStatusAutomaticTrackingAid;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusBridgeNavigationalWatchAlarmSystem() {
        return solasEquipmentStatusBridgeNavigationalWatchAlarmSystem;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusECDISBackup() {
        return solasEquipmentStatusECDISBackup;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusECDISPaperNauticalChart() {
        return solasEquipmentStatusECDISPaperNauticalChart;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusEchoSounder() {
        return solasEquipmentStatusEchoSounder;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusElectronicPlottingAid() {
        return solasEquipmentStatusElectronicPlottingAid;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusEmergencySteeringGear() {
        return solasEquipmentStatusEmergencySteeringGear;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusNavigationSystem() {
        return solasEquipmentStatusNavigationSystem;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusGyroCompass() {
        return solasEquipmentStatusGyroCompass;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusLRIT() {
        return solasEquipmentStatusLRIT;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusMagneticCompass() {
        return solasEquipmentStatusMagneticCompass;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusNAVTEX() {
        return solasEquipmentStatusNAVTEX;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadarARPA() {
        return solasEquipmentStatusRadarARPA;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadarSBand() {
        return solasEquipmentStatusRadarSBand;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadarXBand() {
        return solasEquipmentStatusRadarXBand;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioHF() {
        return solasEquipmentStatusRadioHF;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioINMARSAT() {
        return solasEquipmentStatusRadioINMARSAT;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioMF() {
        return solasEquipmentStatusRadioMF;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioVHF() {
        return solasEquipmentStatusRadioVHF;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusSpeedLogOverGround() {
        return solasEquipmentStatusSpeedLogOverGround;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusSpeedLogThroughWater() {
        return solasEquipmentStatusSpeedLogThroughWater;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusTransmittingHeadingDevice() {
        return solasEquipmentStatusTransmittingHeadingDevice;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusTrackControlSystem() {
        return solasEquipmentStatusTrackControlSystem;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusVDR() {
        return solasEquipmentStatusVDR;
    }

    public SolasEquipmentStatus getSolasEquipmentStatusFuture() {
        return solasEquipmentStatusFuture;
    }

    public IceClass getIceClass() {
        return iceClass;
    }

    public Integer getShaftHorsePower() {
        return shaftHorsePower;
    }

    public String getVhfWorkingChannel() {
        return vhfWorkingChannel;
    }

    public String getLloydsShipType() {
        return lloydsShipType;
    }

    public Integer getGrossTonnage() {
        return grossTonnage;
    }

    public BallastStatus getLadenOrBallast() {
        return ladenOrBallast;
    }

    public BunkerOilStatus getHeavyFuelOil() {
        return heavyFuelOil;
    }

    public BunkerOilStatus getLightFuelOil() {
        return lightFuelOil;
    }

    public BunkerOilStatus getDiesel() {
        return diesel;
    }

    public Integer getTotalAmountOfBunkerOil() {
        return totalAmountOfBunkerOil;
    }

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    private final Integer messageLinkageId;
    private final Float airDraught;
    private final String lastPortOfCall;
    private final String nextPortOfCall;
    private final String secondPortOfCall;
    private final SolasEquipmentStatus solasEquipmentStatusAISClassA;
    private final SolasEquipmentStatus solasEquipmentStatusAutomaticTrackingAid;
    private final SolasEquipmentStatus solasEquipmentStatusBridgeNavigationalWatchAlarmSystem;
    private final SolasEquipmentStatus solasEquipmentStatusECDISBackup;
    private final SolasEquipmentStatus solasEquipmentStatusECDISPaperNauticalChart;
    private final SolasEquipmentStatus solasEquipmentStatusEchoSounder;
    private final SolasEquipmentStatus solasEquipmentStatusElectronicPlottingAid;
    private final SolasEquipmentStatus solasEquipmentStatusEmergencySteeringGear;
    private final SolasEquipmentStatus solasEquipmentStatusNavigationSystem;
    private final SolasEquipmentStatus solasEquipmentStatusGyroCompass;
    private final SolasEquipmentStatus solasEquipmentStatusLRIT;
    private final SolasEquipmentStatus solasEquipmentStatusMagneticCompass;
    private final SolasEquipmentStatus solasEquipmentStatusNAVTEX;
    private final SolasEquipmentStatus solasEquipmentStatusRadarARPA;
    private final SolasEquipmentStatus solasEquipmentStatusRadarSBand;
    private final SolasEquipmentStatus solasEquipmentStatusRadarXBand;
    private final SolasEquipmentStatus solasEquipmentStatusRadioHF;
    private final SolasEquipmentStatus solasEquipmentStatusRadioINMARSAT;
    private final SolasEquipmentStatus solasEquipmentStatusRadioMF;
    private final SolasEquipmentStatus solasEquipmentStatusRadioVHF;
    private final SolasEquipmentStatus solasEquipmentStatusSpeedLogOverGround;
    private final SolasEquipmentStatus solasEquipmentStatusSpeedLogThroughWater;
    private final SolasEquipmentStatus solasEquipmentStatusTransmittingHeadingDevice;
    private final SolasEquipmentStatus solasEquipmentStatusTrackControlSystem;
    private final SolasEquipmentStatus solasEquipmentStatusVDR;
    private final SolasEquipmentStatus solasEquipmentStatusFuture;
    private final IceClass iceClass;
    private final Integer shaftHorsePower;
    private final String vhfWorkingChannel;
    private final String lloydsShipType;
    private final Integer grossTonnage;
    private final BallastStatus ladenOrBallast;
    private final BunkerOilStatus heavyFuelOil;
    private final BunkerOilStatus lightFuelOil;
    private final BunkerOilStatus diesel;
    private final Integer totalAmountOfBunkerOil;
    private final Integer numberOfPersons;

    public enum SolasEquipmentStatus {
        EQUIPMENT_NOT_AVAILABLE_OR_REQUESTED(0),
        EQUIPMENT_OPERATIONAL(1),
        EQUIPMENT_NOT_OPERATIONAL(2),
        NO_DATA(3);

        private final int value;

        SolasEquipmentStatus(int value) {
            this.value = value;
        }

        public static SolasEquipmentStatus valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

    public enum IceClass {
        NOT_CLASSIFIED(0),
        IACS_PC_1(1),
        IACS_PC_2(2),
        IACS_PC_3(3),
        IACS_PC_4(4),
        IACS_PC_5(5),
        IACS_PC_6(6),
        IACS_PC_7(7),
        FSICR_IB_RS_ICE3(8),
        FSICR_IC_RS_ICE2(9),
        RS_ICE1(10),
        FUTURE_1(11),
        FUTURE_2(12),
        FUTURE_3(13),
        FUTURE_4(14),
        NOT_AVAILABLE(15);

        private final int value;

        IceClass(int value) {
            this.value = value;
        }

        public static IceClass valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

    public enum BallastStatus {
        NOT_AVAILABLE(0),
        LADEN(1),
        BALLAST(2),
        NOT_IN_USE(3);

        private final int value;

        BallastStatus(int value) {
            this.value = value;
        }

        public static BallastStatus valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

    public enum BunkerOilStatus {
        NOT_AVAILABLE(0),
        NO(1),
        YES(2),
        NOT_IN_USE(3);

        private final int value;

        BunkerOilStatus(int value) {
            this.value = value;
        }

        public static BunkerOilStatus valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

}
