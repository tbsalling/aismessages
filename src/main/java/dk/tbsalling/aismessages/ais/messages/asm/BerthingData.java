package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Berthing Data (DAC=1, FI=20)
 * Berth details for a vessel's arrival: berth length and depth, mooring position, berthing time, availability of port
 * services and the berth's name and position. Carried in Message 6 or 8.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BerthingData extends ApplicationSpecificMessage {

    BerthingData(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.messageLinkageId = getBinaryData().getUnsignedInt(0, 10);
        this.berthLength = getBinaryData().getUnsignedInt(10, 19);
        this.waterDepthAtBerth = getBinaryData().getUnsignedInt(19, 27) / 10f;
        this.mooringPosition = MooringPosition.valueOf(getBinaryData().getUnsignedInt(27, 30));
        this.berthUtcMonth = getBinaryData().getUnsignedInt(30, 34);
        this.berthUtcDay = getBinaryData().getUnsignedInt(34, 39);
        this.berthUtcHour = getBinaryData().getUnsignedInt(39, 44);
        this.berthUtcMinute = getBinaryData().getUnsignedInt(44, 50);
        this.serviceStatusAgent = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(50, 52));
        this.serviceStatusFuel = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(52, 54));
        this.serviceStatusChandler = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(54, 56));
        this.serviceStatusStevedore = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(56, 58));
        this.serviceStatusElectrical = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(58, 60));
        this.serviceStatusPotableWater = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(60, 62));
        this.serviceStatusCustomsHouse = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(62, 64));
        this.serviceStatusCartage = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(64, 66));
        this.serviceStatusCrane = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(66, 68));
        this.serviceStatusLift = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(68, 70));
        this.serviceStatusMedical = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(70, 72));
        this.serviceStatusNavigationRepair = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(72, 74));
        this.serviceStatusProvisions = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(74, 76));
        this.serviceStatusShipRepair = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(76, 78));
        this.serviceStatusSurveyor = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(78, 80));
        this.serviceStatusSteam = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(80, 82));
        this.serviceStatusTugs = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(82, 84));
        this.serviceStatusSolidWasteDisposal = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(84, 86));
        this.serviceStatusLiquidWasteDisposal = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(86, 88));
        this.serviceStatusHazardousWasteDisposal = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(88, 90));
        this.serviceStatusReservedBallastExchange = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(90, 92));
        this.serviceStatusAdditionalServices = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(92, 94));
        this.serviceStatusFutureRegionalUse = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(94, 96));
        this.serviceStatusFutureUse = ServiceStatus.valueOf(getBinaryData().getUnsignedInt(96, 98));
        this.nameOfBerth = AISText.decode(getBinaryData(), 98, 218);
        this.berthLongitude = getBinaryData().getSignedFloat(218, 243) / 60000f;
        this.berthLatitude = getBinaryData().getSignedFloat(243, 267) / 60000f;
    }

    /** Bits 0-10 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): message linkage ID. */
    int messageLinkageId;
    /** Bits 10-19 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): berth length. */
    int berthLength;
    /** Bits 19-27 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): water depth at berth (transmitted value ÷ 10). */
    float waterDepthAtBerth;
    /** Bits 27-30 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): mooring position code, decoded as {@link MooringPosition}. */
    MooringPosition mooringPosition;
    /** Bits 30-34 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): berth UTC month. */
    int berthUtcMonth;
    /** Bits 34-39 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): berth UTC day. */
    int berthUtcDay;
    /** Bits 39-44 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): berth UTC hour. */
    int berthUtcHour;
    /** Bits 44-50 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): berth UTC minute. */
    int berthUtcMinute;
    /** Bits 50-52 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status agent code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusAgent;
    /** Bits 52-54 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status fuel code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusFuel;
    /** Bits 54-56 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status chandler code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusChandler;
    /** Bits 56-58 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status stevedore code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusStevedore;
    /** Bits 58-60 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status electrical code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusElectrical;
    /** Bits 60-62 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status potable water code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusPotableWater;
    /** Bits 62-64 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status customs house code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusCustomsHouse;
    /** Bits 64-66 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status cartage code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusCartage;
    /** Bits 66-68 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status crane code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusCrane;
    /** Bits 68-70 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status lift code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusLift;
    /** Bits 70-72 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status medical code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusMedical;
    /** Bits 72-74 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status navigation repair code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusNavigationRepair;
    /** Bits 74-76 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status provisions code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusProvisions;
    /** Bits 76-78 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status ship repair code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusShipRepair;
    /** Bits 78-80 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status surveyor code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusSurveyor;
    /** Bits 80-82 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status steam code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusSteam;
    /** Bits 82-84 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status tugs code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusTugs;
    /** Bits 84-86 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status solid waste disposal code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusSolidWasteDisposal;
    /** Bits 86-88 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status liquid waste disposal code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusLiquidWasteDisposal;
    /** Bits 88-90 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status hazardous waste disposal code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusHazardousWasteDisposal;
    /** Bits 90-92 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status reserved ballast exchange code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusReservedBallastExchange;
    /** Bits 92-94 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status additional services code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusAdditionalServices;
    /** Bits 94-96 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status future regional use code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusFutureRegionalUse;
    /** Bits 96-98 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): service status future use code, decoded as {@link ServiceStatus}. */
    ServiceStatus serviceStatusFutureUse;
    /** Bits 98-218 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): name of berth, up to 20 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String nameOfBerth;
    /** Bits 218-243 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): berth longitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    float berthLongitude;
    /** Bits 243-267 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20): berth latitude in decimal degrees (1/1 000 min ÷ 60 000), signed. */
    float berthLatitude;

    /** Codes of the mooring position field, bits 27-30 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20). */
    public enum MooringPosition {
        /** 0 = undefined. */
        UNDEFINED(0),
        /** 1 = port side to. */
        PORT_SIDE_TO(1),
        /** 2 = starboard side to. */
        STARBOARD_SIDE_TO(2),
        /** 3 = mediterranean mooring. */
        MEDITERRANEAN_MOORING(3),
        /** 4 = mooring buoy. */
        MOORING_BUOY(4),
        /** 5 = anchorage. */
        ANCHORAGE(5),
        /** 6 = reserved future use 1. */
        RESERVED_FUTURE_USE_1(6),
        /** 7 = reserved future use 2. */
        RESERVED_FUTURE_USE_2(7);

        /** The code as transmitted. */
        int value;

        MooringPosition(int value) {
            this.value = value;
        }

        public static MooringPosition valueOf(int value) {
            return stream(values())
                .filter(e -> e.value == value)
                .findFirst()
                .get();
        }
    }

    /** Codes of the 24 two-bit service status fields (agent, fuel, chandler and so on), bits 50-98 of the application data (IMO SN.1/Circ.289, DAC=1 FI=20). */
    public enum ServiceStatus {
        /** 0 = service not available or requested. */
        SERVICE_NOT_AVAILABLE_OR_REQUESTED(0),
        /** 1 = service available. */
        SERVICE_AVAILABLE(1),
        /** 2 = no data or unknown. */
        NO_DATA_OR_UNKNOWN(2),
        /** 3 = not used. */
        NOT_USED(3);

        /** The code as transmitted. */
        int value;

        ServiceStatus(int value) {
            this.value = value;
        }

        public static ServiceStatus valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

}
