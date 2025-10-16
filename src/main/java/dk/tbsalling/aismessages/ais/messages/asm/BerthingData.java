package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BerthingData extends ApplicationSpecificMessage {

    BerthingData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.messageLinkageId = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 10));
        this.berthLength = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(10, 19));
        this.waterDepthAtBerth = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(19, 27)) / 10f;
        this.mooringPosition = MooringPosition.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(27, 30)));
        this.berthUtcMonth = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(30, 34));
        this.berthUtcDay = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(34, 39));
        this.berthUtcHour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(39, 44));
        this.berthUtcMinute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(44, 50));
        this.serviceStatusAgent = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(50, 52)));
        this.serviceStatusFuel = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(52, 54)));
        this.serviceStatusChandler = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(54, 56)));
        this.serviceStatusStevedore = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(56, 58)));
        this.serviceStatusElectrical = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(58, 60)));
        this.serviceStatusPotableWater = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(60, 62)));
        this.serviceStatusCustomsHouse = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(62, 64)));
        this.serviceStatusCartage = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(64, 66)));
        this.serviceStatusCrane = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(66, 68)));
        this.serviceStatusLift = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(68, 70)));
        this.serviceStatusMedical = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(70, 72)));
        this.serviceStatusNavigationRepair = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(72, 74)));
        this.serviceStatusProvisions = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(74, 76)));
        this.serviceStatusShipRepair = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(76, 78)));
        this.serviceStatusSurveyor = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(78, 80)));
        this.serviceStatusSteam = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(80, 82)));
        this.serviceStatusTugs = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(82, 84)));
        this.serviceStatusSolidWasteDisposal = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(84, 86)));
        this.serviceStatusLiquidWasteDisposal = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(86, 88)));
        this.serviceStatusHazardousWasteDisposal = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(88, 90)));
        this.serviceStatusReservedBallastExchange = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(90, 92)));
        this.serviceStatusAdditionalServices = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(92, 94)));
        this.serviceStatusFutureRegionalUse = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(94, 96)));
        this.serviceStatusFutureUse = ServiceStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(96, 98)));
        this.nameOfBerth = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(98, 218));
        this.berthLongitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(218, 243)) / 60000f;
        this.berthLatitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(243, 267)) / 60000f;
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
