package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendedShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {
    public ExtendedShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.messageLinkageId = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 10));
        this.airDraught = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(10, 23)) / 10f;
        this.lastPortOfCall = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(23, 53));
        this.nextPortOfCall = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(53, 83));
        this.secondPortOfCall = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(83, 113));
        this.solasEquipmentStatusAISClassA = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(113, 115)));
        this.solasEquipmentStatusAutomaticTrackingAid = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(115, 117)));
        this.solasEquipmentStatusBridgeNavigationalWatchAlarmSystem = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(117, 119)));
        this.solasEquipmentStatusECDISBackup = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(119, 121)));
        this.solasEquipmentStatusECDISPaperNauticalChart = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(121, 123)));
        this.solasEquipmentStatusEchoSounder = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(123, 125)));
        this.solasEquipmentStatusElectronicPlottingAid = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(125, 127)));
        this.solasEquipmentStatusEmergencySteeringGear = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(127, 129)));
        this.solasEquipmentStatusNavigationSystem = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(129, 131)));
        this.solasEquipmentStatusGyroCompass = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(131, 133)));
        this.solasEquipmentStatusLRIT = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(133, 135)));
        this.solasEquipmentStatusMagneticCompass = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(135, 137)));
        this.solasEquipmentStatusNAVTEX = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(137, 139)));
        this.solasEquipmentStatusRadarARPA = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(139, 141)));
        this.solasEquipmentStatusRadarSBand = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(141, 143)));
        this.solasEquipmentStatusRadarXBand = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(143, 145)));
        this.solasEquipmentStatusRadioHF = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(145, 147)));
        this.solasEquipmentStatusRadioINMARSAT = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(147, 149)));
        this.solasEquipmentStatusRadioMF = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(149, 151)));
        this.solasEquipmentStatusRadioVHF = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(151, 153)));
        this.solasEquipmentStatusSpeedLogOverGround = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(153, 155)));
        this.solasEquipmentStatusSpeedLogThroughWater = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(155, 157)));
        this.solasEquipmentStatusTransmittingHeadingDevice = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(157, 159)));
        this.solasEquipmentStatusTrackControlSystem = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(159, 161)));
        this.solasEquipmentStatusVDR = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(161, 163)));
        this.solasEquipmentStatusFuture = SolasEquipmentStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(163, 165)));
        this.iceClass = IceClass.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(165, 169)));
        this.shaftHorsePower = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(169, 187));
        this.vhfWorkingChannel = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(187, 199));
        this.lloydsShipType = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(199, 241));
        this.grossTonnage = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(241, 259));
        this.ladenOrBallast = BallastStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(259, 261)));
        this.heavyFuelOil = BunkerOilStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(261, 263)));
        this.lightFuelOil = BunkerOilStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(263, 265)));
        this.diesel = BunkerOilStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(265, 267)));
        this.totalAmountOfBunkerOil = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(267, 281));
        this.numberOfPersons = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(281, 294));
    }

    Integer messageLinkageId;
    Float airDraught;
    String lastPortOfCall;
    String nextPortOfCall;
    String secondPortOfCall;
    SolasEquipmentStatus solasEquipmentStatusAISClassA;
    SolasEquipmentStatus solasEquipmentStatusAutomaticTrackingAid;
    SolasEquipmentStatus solasEquipmentStatusBridgeNavigationalWatchAlarmSystem;
    SolasEquipmentStatus solasEquipmentStatusECDISBackup;
    SolasEquipmentStatus solasEquipmentStatusECDISPaperNauticalChart;
    SolasEquipmentStatus solasEquipmentStatusEchoSounder;
    SolasEquipmentStatus solasEquipmentStatusElectronicPlottingAid;
    SolasEquipmentStatus solasEquipmentStatusEmergencySteeringGear;
    SolasEquipmentStatus solasEquipmentStatusNavigationSystem;
    SolasEquipmentStatus solasEquipmentStatusGyroCompass;
    SolasEquipmentStatus solasEquipmentStatusLRIT;
    SolasEquipmentStatus solasEquipmentStatusMagneticCompass;
    SolasEquipmentStatus solasEquipmentStatusNAVTEX;
    SolasEquipmentStatus solasEquipmentStatusRadarARPA;
    SolasEquipmentStatus solasEquipmentStatusRadarSBand;
    SolasEquipmentStatus solasEquipmentStatusRadarXBand;
    SolasEquipmentStatus solasEquipmentStatusRadioHF;
    SolasEquipmentStatus solasEquipmentStatusRadioINMARSAT;
    SolasEquipmentStatus solasEquipmentStatusRadioMF;
    SolasEquipmentStatus solasEquipmentStatusRadioVHF;
    SolasEquipmentStatus solasEquipmentStatusSpeedLogOverGround;
    SolasEquipmentStatus solasEquipmentStatusSpeedLogThroughWater;
    SolasEquipmentStatus solasEquipmentStatusTransmittingHeadingDevice;
    SolasEquipmentStatus solasEquipmentStatusTrackControlSystem;
    SolasEquipmentStatus solasEquipmentStatusVDR;
    SolasEquipmentStatus solasEquipmentStatusFuture;
    IceClass iceClass;
    Integer shaftHorsePower;
    String vhfWorkingChannel;
    String lloydsShipType;
    Integer grossTonnage;
    BallastStatus ladenOrBallast;
    BunkerOilStatus heavyFuelOil;
    BunkerOilStatus lightFuelOil;
    BunkerOilStatus diesel;
    Integer totalAmountOfBunkerOil;
    Integer numberOfPersons;

    public enum SolasEquipmentStatus {
        EQUIPMENT_NOT_AVAILABLE_OR_REQUESTED(0),
        EQUIPMENT_OPERATIONAL(1),
        EQUIPMENT_NOT_OPERATIONAL(2),
        NO_DATA(3);

        int value;

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

        int value;

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

        int value;

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

        int value;

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
