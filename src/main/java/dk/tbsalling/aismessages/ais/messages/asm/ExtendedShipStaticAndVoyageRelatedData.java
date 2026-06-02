package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendedShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {
    public ExtendedShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.messageLinkageId = getBinaryData().getUnsignedInt(0, 10);
        this.airDraught = getBinaryData().getUnsignedInt(10, 23) / 10f;
        this.lastPortOfCall = AISText.decode(getBinaryData(), 23, 53);
        this.nextPortOfCall = AISText.decode(getBinaryData(), 53, 83);
        this.secondPortOfCall = AISText.decode(getBinaryData(), 83, 113);
        this.solasEquipmentStatusAISClassA = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(113, 115));
        this.solasEquipmentStatusAutomaticTrackingAid = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(115, 117));
        this.solasEquipmentStatusBridgeNavigationalWatchAlarmSystem = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(117, 119));
        this.solasEquipmentStatusECDISBackup = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(119, 121));
        this.solasEquipmentStatusECDISPaperNauticalChart = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(121, 123));
        this.solasEquipmentStatusEchoSounder = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(123, 125));
        this.solasEquipmentStatusElectronicPlottingAid = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(125, 127));
        this.solasEquipmentStatusEmergencySteeringGear = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(127, 129));
        this.solasEquipmentStatusNavigationSystem = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(129, 131));
        this.solasEquipmentStatusGyroCompass = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(131, 133));
        this.solasEquipmentStatusLRIT = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(133, 135));
        this.solasEquipmentStatusMagneticCompass = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(135, 137));
        this.solasEquipmentStatusNAVTEX = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(137, 139));
        this.solasEquipmentStatusRadarARPA = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(139, 141));
        this.solasEquipmentStatusRadarSBand = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(141, 143));
        this.solasEquipmentStatusRadarXBand = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(143, 145));
        this.solasEquipmentStatusRadioHF = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(145, 147));
        this.solasEquipmentStatusRadioINMARSAT = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(147, 149));
        this.solasEquipmentStatusRadioMF = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(149, 151));
        this.solasEquipmentStatusRadioVHF = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(151, 153));
        this.solasEquipmentStatusSpeedLogOverGround = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(153, 155));
        this.solasEquipmentStatusSpeedLogThroughWater = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(155, 157));
        this.solasEquipmentStatusTransmittingHeadingDevice = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(157, 159));
        this.solasEquipmentStatusTrackControlSystem = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(159, 161));
        this.solasEquipmentStatusVDR = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(161, 163));
        this.solasEquipmentStatusFuture = SolasEquipmentStatus.valueOf(getBinaryData().getUnsignedInt(163, 165));
        this.iceClass = IceClass.valueOf(getBinaryData().getUnsignedInt(165, 169));
        this.shaftHorsePower = getBinaryData().getUnsignedInt(169, 187);
        this.vhfWorkingChannel = AISText.decode(getBinaryData(), 187, 199);
        this.lloydsShipType = AISText.decode(getBinaryData(), 199, 241);
        this.grossTonnage = getBinaryData().getUnsignedInt(241, 259);
        this.ladenOrBallast = BallastStatus.valueOf(getBinaryData().getUnsignedInt(259, 261));
        this.heavyFuelOil = BunkerOilStatus.valueOf(getBinaryData().getUnsignedInt(261, 263));
        this.lightFuelOil = BunkerOilStatus.valueOf(getBinaryData().getUnsignedInt(263, 265));
        this.diesel = BunkerOilStatus.valueOf(getBinaryData().getUnsignedInt(265, 267));
        this.totalAmountOfBunkerOil = getBinaryData().getUnsignedInt(267, 281);
        this.numberOfPersons = getBinaryData().getUnsignedInt(281, 294);
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
