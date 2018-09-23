package dk.tbsalling.aismessages.ais.messages.asm;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.util.Arrays.stream;

public class ExtendedShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {
    public ExtendedShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }

    public Integer getMessageLinkageId() {
        return getDecodedValue(() -> messageLinkageId, value -> messageLinkageId = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(0,10)));
    }

    public Float getAirDraught() {
        return getDecodedValue(() -> airDraught, value -> airDraught = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(10,23))/10f);
    }

    public String getLastPortOfCall() {
        return getDecodedValue(() -> lastPortOfCall, value -> lastPortOfCall = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBinaryData().substring(23,53)));
    }

    public String getNextPortOfCall() {
        return getDecodedValue(() -> nextPortOfCall, value -> nextPortOfCall = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBinaryData().substring(53,83)));
    }

    public String getSecondPortOfCall() {
        return getDecodedValue(() -> secondPortOfCall, value -> secondPortOfCall = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBinaryData().substring(83,113)));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusAISClassA() {
        return getDecodedValue(() -> solasEquipmentStatusAISClassA, value -> solasEquipmentStatusAISClassA = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(113,115))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusAutomaticTrackingAid() {
        return getDecodedValue(() -> solasEquipmentStatusAutomaticTrackingAid, value -> solasEquipmentStatusAutomaticTrackingAid = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(115,117))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusBridgeNavigationalWatchAlarmSystem() {
        return getDecodedValue(() -> solasEquipmentStatusBridgeNavigationalWatchAlarmSystem, value -> solasEquipmentStatusBridgeNavigationalWatchAlarmSystem = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(117,119))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusECDISBackup() {
        return getDecodedValue(() -> solasEquipmentStatusECDISBackup, value -> solasEquipmentStatusECDISBackup = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(119,121))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusECDISPaperNauticalChart() {
        return getDecodedValue(() -> solasEquipmentStatusECDISPaperNauticalChart, value -> solasEquipmentStatusECDISPaperNauticalChart = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(121,123))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusEchoSounder() {
        return getDecodedValue(() -> solasEquipmentStatusEchoSounder, value -> solasEquipmentStatusEchoSounder = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(123,125))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusElectronicPlottingAid() {
        return getDecodedValue(() -> solasEquipmentStatusElectronicPlottingAid, value -> solasEquipmentStatusElectronicPlottingAid = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(125,127))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusEmergencySteeringGear() {
        return getDecodedValue(() -> solasEquipmentStatusEmergencySteeringGear, value -> solasEquipmentStatusEmergencySteeringGear = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(127,129))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusNavigationSystem() {
        return getDecodedValue(() -> solasEquipmentStatusNavigationSystem, value -> solasEquipmentStatusNavigationSystem = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(129,131))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusGyroCompass() {
        return getDecodedValue(() -> solasEquipmentStatusGyroCompass, value -> solasEquipmentStatusGyroCompass = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(131,133))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusLRIT() {
        return getDecodedValue(() -> solasEquipmentStatusLRIT, value -> solasEquipmentStatusLRIT = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(133,135))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusMagneticCompass() {
        return getDecodedValue(() -> solasEquipmentStatusMagneticCompass, value -> solasEquipmentStatusMagneticCompass = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(135,137))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusNAVTEX() {
        return getDecodedValue(() -> solasEquipmentStatusNAVTEX, value -> solasEquipmentStatusNAVTEX = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(137,139))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadarARPA() {
        return getDecodedValue(() -> solasEquipmentStatusRadarARPA, value -> solasEquipmentStatusRadarARPA = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(139,141))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadarSBand() {
        return getDecodedValue(() -> solasEquipmentStatusRadarSBand, value -> solasEquipmentStatusRadarSBand = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(141,143))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadarXBand() {
        return getDecodedValue(() -> solasEquipmentStatusRadarXBand, value -> solasEquipmentStatusRadarXBand = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(143,145))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioHF() {
        return getDecodedValue(() -> solasEquipmentStatusRadioHF, value -> solasEquipmentStatusRadioHF = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(145,147))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioINMARSAT() {
        return getDecodedValue(() -> solasEquipmentStatusRadioINMARSAT, value -> solasEquipmentStatusRadioINMARSAT = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(147,149))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioMF() {
        return getDecodedValue(() -> solasEquipmentStatusRadioMF, value -> solasEquipmentStatusRadioMF = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(149,151))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusRadioVHF() {
        return getDecodedValue(() -> solasEquipmentStatusRadioVHF, value -> solasEquipmentStatusRadioVHF = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(151,153))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusSpeedLogOverGround() {
        return getDecodedValue(() -> solasEquipmentStatusSpeedLogOverGround, value -> solasEquipmentStatusSpeedLogOverGround = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(153,155))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusSpeedLogThroughWater() {
        return getDecodedValue(() -> solasEquipmentStatusSpeedLogThroughWater, value -> solasEquipmentStatusSpeedLogThroughWater = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(155,157))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusTransmittingHeadingDevice() {
        return getDecodedValue(() -> solasEquipmentStatusTransmittingHeadingDevice, value -> solasEquipmentStatusTransmittingHeadingDevice = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(157,159))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusTrackControlSystem() {
        return getDecodedValue(() -> solasEquipmentStatusTrackControlSystem, value -> solasEquipmentStatusTrackControlSystem = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(159,161))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusVDR() {
        return getDecodedValue(() -> solasEquipmentStatusVDR, value -> solasEquipmentStatusVDR = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(161,163))));
    }

    public SolasEquipmentStatus getSolasEquipmentStatusFuture() {
        return getDecodedValue(() -> solasEquipmentStatusFuture, value -> solasEquipmentStatusFuture = value, () -> Boolean.TRUE, () -> SolasEquipmentStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(163,165))));
    }

    public IceClass getIceClass() {
        return getDecodedValue(() -> iceClass, value -> iceClass = value, () -> Boolean.TRUE, () -> IceClass.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(165,169))));
    }

    public Integer getShaftHorsePower() {
        return getDecodedValue(() -> shaftHorsePower, value -> shaftHorsePower = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(169,187)));
    }

    public String getVhfWorkingChannel() {
        return getDecodedValue(() -> vhfWorkingChannel, value -> vhfWorkingChannel = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBinaryData().substring(187,199)));
    }

    public String getLloydsShipType() {
        return getDecodedValue(() -> lloydsShipType, value -> lloydsShipType = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBinaryData().substring(199,241)));
    }

    public Integer getGrossTonnage() {
        return getDecodedValue(() -> grossTonnage, value -> grossTonnage = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(241,259)));
    }

    public BallastStatus getLadenOrBallast() {
        return getDecodedValue(() -> ladenOrBallast, value -> ladenOrBallast = value, () -> Boolean.TRUE, () -> BallastStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(259,261))));
    }

    public BunkerOilStatus getHeavyFuelOil() {
        return getDecodedValue(() -> heavyFuelOil, value -> heavyFuelOil = value, () -> Boolean.TRUE, () -> BunkerOilStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(261,263))));
    }

    public BunkerOilStatus getLightFuelOil() {
        return getDecodedValue(() -> lightFuelOil, value -> lightFuelOil = value, () -> Boolean.TRUE, () -> BunkerOilStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(263,265))));
    }

    public BunkerOilStatus getDiesel() {
        return getDecodedValue(() -> diesel, value -> diesel = value, () -> Boolean.TRUE, () -> BunkerOilStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(265,267))));
    }

    public Integer getTotalAmountOfBunkerOil() {
        return getDecodedValue(() -> totalAmountOfBunkerOil, value -> totalAmountOfBunkerOil = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(267,281)));
    }

    public Integer getNumberOfPersons() {
        return getDecodedValue(() -> numberOfPersons, value -> numberOfPersons = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(281,294)));
    }

    private transient Integer messageLinkageId;
    private transient Float airDraught;
    private transient String lastPortOfCall;
    private transient String nextPortOfCall;
    private transient String secondPortOfCall;
    private transient SolasEquipmentStatus solasEquipmentStatusAISClassA;
    private transient SolasEquipmentStatus solasEquipmentStatusAutomaticTrackingAid;
    private transient SolasEquipmentStatus solasEquipmentStatusBridgeNavigationalWatchAlarmSystem;
    private transient SolasEquipmentStatus solasEquipmentStatusECDISBackup;
    private transient SolasEquipmentStatus solasEquipmentStatusECDISPaperNauticalChart;
    private transient SolasEquipmentStatus solasEquipmentStatusEchoSounder;
    private transient SolasEquipmentStatus solasEquipmentStatusElectronicPlottingAid;
    private transient SolasEquipmentStatus solasEquipmentStatusEmergencySteeringGear;
    private transient SolasEquipmentStatus solasEquipmentStatusNavigationSystem;
    private transient SolasEquipmentStatus solasEquipmentStatusGyroCompass;
    private transient SolasEquipmentStatus solasEquipmentStatusLRIT;
    private transient SolasEquipmentStatus solasEquipmentStatusMagneticCompass;
    private transient SolasEquipmentStatus solasEquipmentStatusNAVTEX;
    private transient SolasEquipmentStatus solasEquipmentStatusRadarARPA;
    private transient SolasEquipmentStatus solasEquipmentStatusRadarSBand;
    private transient SolasEquipmentStatus solasEquipmentStatusRadarXBand;
    private transient SolasEquipmentStatus solasEquipmentStatusRadioHF;
    private transient SolasEquipmentStatus solasEquipmentStatusRadioINMARSAT;
    private transient SolasEquipmentStatus solasEquipmentStatusRadioMF;
    private transient SolasEquipmentStatus solasEquipmentStatusRadioVHF;
    private transient SolasEquipmentStatus solasEquipmentStatusSpeedLogOverGround;
    private transient SolasEquipmentStatus solasEquipmentStatusSpeedLogThroughWater;
    private transient SolasEquipmentStatus solasEquipmentStatusTransmittingHeadingDevice;
    private transient SolasEquipmentStatus solasEquipmentStatusTrackControlSystem;
    private transient SolasEquipmentStatus solasEquipmentStatusVDR;
    private transient SolasEquipmentStatus solasEquipmentStatusFuture;
    private transient IceClass iceClass;
    private transient Integer shaftHorsePower;
    private transient String vhfWorkingChannel;
    private transient String lloydsShipType;
    private transient Integer grossTonnage;
    private transient BallastStatus ladenOrBallast;
    private transient BunkerOilStatus heavyFuelOil;
    private transient BunkerOilStatus lightFuelOil;
    private transient BunkerOilStatus diesel;
    private transient Integer totalAmountOfBunkerOil;
    private transient Integer numberOfPersons;

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
