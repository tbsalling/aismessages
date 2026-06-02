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

    int messageLinkageId;
    int berthLength;
    float waterDepthAtBerth;
    MooringPosition mooringPosition;
    int berthUtcMonth;
    int berthUtcDay;
    int berthUtcHour;
    int berthUtcMinute;
    ServiceStatus serviceStatusAgent;
    ServiceStatus serviceStatusFuel;
    ServiceStatus serviceStatusChandler;
    ServiceStatus serviceStatusStevedore;
    ServiceStatus serviceStatusElectrical;
    ServiceStatus serviceStatusPotableWater;
    ServiceStatus serviceStatusCustomsHouse;
    ServiceStatus serviceStatusCartage;
    ServiceStatus serviceStatusCrane;
    ServiceStatus serviceStatusLift;
    ServiceStatus serviceStatusMedical;
    ServiceStatus serviceStatusNavigationRepair;
    ServiceStatus serviceStatusProvisions;
    ServiceStatus serviceStatusShipRepair;
    ServiceStatus serviceStatusSurveyor;
    ServiceStatus serviceStatusSteam;
    ServiceStatus serviceStatusTugs;
    ServiceStatus serviceStatusSolidWasteDisposal;
    ServiceStatus serviceStatusLiquidWasteDisposal;
    ServiceStatus serviceStatusHazardousWasteDisposal;
    ServiceStatus serviceStatusReservedBallastExchange;
    ServiceStatus serviceStatusAdditionalServices;
    ServiceStatus serviceStatusFutureRegionalUse;
    ServiceStatus serviceStatusFutureUse;
    String nameOfBerth;
    float berthLongitude;
    float berthLatitude;

    public enum MooringPosition {
        UNDEFINED(0),
        PORT_SIDE_TO(1),
        STARBOARD_SIDE_TO(2),
        MEDITERRANEAN_MOORING(3),
        MOORING_BUOY(4),
        ANCHORAGE(5),
        RESERVED_FUTURE_USE_1(6),
        RESERVED_FUTURE_USE_2(7);

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

    public enum ServiceStatus {
        SERVICE_NOT_AVAILABLE_OR_REQUESTED(0),
        SERVICE_AVAILABLE(1),
        NO_DATA_OR_UNKNOWN(2),
        NOT_USED(3);

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
