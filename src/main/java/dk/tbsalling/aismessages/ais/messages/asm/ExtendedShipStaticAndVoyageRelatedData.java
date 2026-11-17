package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Extended Ship Static and Voyage Related Data (DAC=1, FI=24)
 * Supplements Message 5 with air draught, ports of call, SOLAS equipment status, ice class, propulsion power, VHF
 * working channel, Lloyd's ship type, gross tonnage, loading condition, bunker oil and persons on board.
 */
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

    /** Bits 0-10 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): message linkage ID. */
    Integer messageLinkageId;
    /** Bits 10-23 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): air draught (transmitted value ÷ 10). */
    Float airDraught;
    /** Bits 23-53 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): last port of call, up to 5 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String lastPortOfCall;
    /** Bits 53-83 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): next port of call, up to 5 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String nextPortOfCall;
    /** Bits 83-113 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): second port of call, up to 5 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String secondPortOfCall;
    /** Bits 113-115 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status aisclass a code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusAISClassA;
    /** Bits 115-117 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status automatic tracking aid code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusAutomaticTrackingAid;
    /** Bits 117-119 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status bridge navigational watch alarm system code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusBridgeNavigationalWatchAlarmSystem;
    /** Bits 119-121 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status ecdisbackup code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusECDISBackup;
    /** Bits 121-123 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status ecdispaper nautical chart code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusECDISPaperNauticalChart;
    /** Bits 123-125 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status echo sounder code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusEchoSounder;
    /** Bits 125-127 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status electronic plotting aid code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusElectronicPlottingAid;
    /** Bits 127-129 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status emergency steering gear code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusEmergencySteeringGear;
    /** Bits 129-131 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status navigation system code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusNavigationSystem;
    /** Bits 131-133 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status gyro compass code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusGyroCompass;
    /** Bits 133-135 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status lrit code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusLRIT;
    /** Bits 135-137 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status magnetic compass code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusMagneticCompass;
    /** Bits 137-139 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status navtex code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusNAVTEX;
    /** Bits 139-141 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status radar arpa code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusRadarARPA;
    /** Bits 141-143 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status radar sband code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusRadarSBand;
    /** Bits 143-145 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status radar xband code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusRadarXBand;
    /** Bits 145-147 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status radio hf code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusRadioHF;
    /** Bits 147-149 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status radio inmarsat code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusRadioINMARSAT;
    /** Bits 149-151 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status radio mf code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusRadioMF;
    /** Bits 151-153 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status radio VHF code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusRadioVHF;
    /** Bits 153-155 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status speed log over ground code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusSpeedLogOverGround;
    /** Bits 155-157 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status speed log through water code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusSpeedLogThroughWater;
    /** Bits 157-159 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status transmitting heading device code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusTransmittingHeadingDevice;
    /** Bits 159-161 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status track control system code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusTrackControlSystem;
    /** Bits 161-163 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status vdr code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusVDR;
    /** Bits 163-165 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): SOLAS equipment status future code, decoded as {@link SolasEquipmentStatus}. */
    SolasEquipmentStatus solasEquipmentStatusFuture;
    /** Bits 165-169 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): ice class code, decoded as {@link IceClass}. */
    IceClass iceClass;
    /** Bits 169-187 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): shaft horse power. */
    Integer shaftHorsePower;
    /** Bits 187-199 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): VHF working channel, up to 2 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String vhfWorkingChannel;
    /** Bits 199-241 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): lloyds ship type, up to 7 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String lloydsShipType;
    /** Bits 241-259 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): gross tonnage. */
    Integer grossTonnage;
    /** Bits 259-261 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): laden or ballast code, decoded as {@link BallastStatus}. */
    BallastStatus ladenOrBallast;
    /** Bits 261-263 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): heavy fuel oil code, decoded as {@link BunkerOilStatus}. */
    BunkerOilStatus heavyFuelOil;
    /** Bits 263-265 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): light fuel oil code, decoded as {@link BunkerOilStatus}. */
    BunkerOilStatus lightFuelOil;
    /** Bits 265-267 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): diesel code, decoded as {@link BunkerOilStatus}. */
    BunkerOilStatus diesel;
    /** Bits 267-281 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): total amount of bunker oil. */
    Integer totalAmountOfBunkerOil;
    /** Bits 281-294 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24): number of persons. */
    Integer numberOfPersons;

    /** Codes of the 26 two-bit SOLAS equipment status fields, bits 113-165 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24). */
    public enum SolasEquipmentStatus {
        /** 0 = equipment not available or requested. */
        EQUIPMENT_NOT_AVAILABLE_OR_REQUESTED(0),
        /** 1 = equipment operational. */
        EQUIPMENT_OPERATIONAL(1),
        /** 2 = equipment not operational. */
        EQUIPMENT_NOT_OPERATIONAL(2),
        /** 3 = no data. */
        NO_DATA(3);

        /** The code as transmitted. */
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

    /** Codes of the ice class field, bits 165-169 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24). */
    public enum IceClass {
        /** 0 = not classified. */
        NOT_CLASSIFIED(0),
        /** 1 = iacs pc 1. */
        IACS_PC_1(1),
        /** 2 = iacs pc 2. */
        IACS_PC_2(2),
        /** 3 = iacs pc 3. */
        IACS_PC_3(3),
        /** 4 = iacs pc 4. */
        IACS_PC_4(4),
        /** 5 = iacs pc 5. */
        IACS_PC_5(5),
        /** 6 = iacs pc 6. */
        IACS_PC_6(6),
        /** 7 = iacs pc 7. */
        IACS_PC_7(7),
        /** 8 = fsicr ib rs ice3. */
        FSICR_IB_RS_ICE3(8),
        /** 9 = fsicr ic rs ice2. */
        FSICR_IC_RS_ICE2(9),
        /** 10 = rs ice1. */
        RS_ICE1(10),
        /** 11 = future 1. */
        FUTURE_1(11),
        /** 12 = future 2. */
        FUTURE_2(12),
        /** 13 = future 3. */
        FUTURE_3(13),
        /** 14 = future 4. */
        FUTURE_4(14),
        /** 15 = not available. */
        NOT_AVAILABLE(15);

        /** The code as transmitted. */
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

    /** Codes of the laden or ballast field, bits 259-261 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24). */
    public enum BallastStatus {
        /** 0 = not available. */
        NOT_AVAILABLE(0),
        /** 1 = laden. */
        LADEN(1),
        /** 2 = ballast. */
        BALLAST(2),
        /** 3 = not in use. */
        NOT_IN_USE(3);

        /** The code as transmitted. */
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

    /** Codes of the heavy fuel oil, light fuel oil and diesel bunker status fields, bits 261-267 of the application data (IMO SN.1/Circ.289, DAC=1 FI=24). */
    public enum BunkerOilStatus {
        /** 0 = not available. */
        NOT_AVAILABLE(0),
        /** 1 = no. */
        NO(1),
        /** 2 = yes. */
        YES(2),
        /** 3 = not in use. */
        NOT_IN_USE(3);

        /** The code as transmitted. */
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
