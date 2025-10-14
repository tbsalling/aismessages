package dk.tbsalling.aismessages.ais.messages.asm;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.util.Arrays.stream;

public class BerthingData extends ApplicationSpecificMessage {

    protected BerthingData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.messageLinkageId = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(0, 10));
        this.berthLength = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(10, 19));
        this.waterDepthAtBerth = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(19, 27)) / 10f;
        this.mooringPosition = MooringPosition.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(27, 30)));
        this.berthUtcMonth = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(30, 34));
        this.berthUtcDay = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(34, 39));
        this.berthUtcHour = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(39, 44));
        this.berthUtcMinute = UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(44, 50));
        this.serviceStatusAgent = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(50, 52)));
        this.serviceStatusFuel = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(52, 54)));
        this.serviceStatusChandler = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(54, 56)));
        this.serviceStatusStevedore = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(56, 58)));
        this.serviceStatusElectrical = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(58, 60)));
        this.serviceStatusPotableWater = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(60, 62)));
        this.serviceStatusCustomsHouse = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(62, 64)));
        this.serviceStatusCartage = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(64, 66)));
        this.serviceStatusCrane = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(66, 68)));
        this.serviceStatusLift = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(68, 70)));
        this.serviceStatusMedical = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(70, 72)));
        this.serviceStatusNavigationRepair = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(72, 74)));
        this.serviceStatusProvisions = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(74, 76)));
        this.serviceStatusShipRepair = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(76, 78)));
        this.serviceStatusSurveyor = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(78, 80)));
        this.serviceStatusSteam = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(80, 82)));
        this.serviceStatusTugs = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(82, 84)));
        this.serviceStatusSolidWasteDisposal = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(84, 86)));
        this.serviceStatusLiquidWasteDisposal = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(86, 88)));
        this.serviceStatusHazardousWasteDisposal = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(88, 90)));
        this.serviceStatusReservedBallastExchange = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(90, 92)));
        this.serviceStatusAdditionalServices = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(92, 94)));
        this.serviceStatusFutureRegionalUse = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(94, 96)));
        this.serviceStatusFutureUse = ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(96, 98)));
        this.nameOfBerth = STRING_DECODER.apply(getBinaryData().substring(98, 218));
        this.berthLongitude = FLOAT_DECODER.apply(getBinaryData().substring(218, 243)) / 60000f;
        this.berthLatitude = FLOAT_DECODER.apply(getBinaryData().substring(243, 267)) / 60000f;
    }

    public Integer getMessageLinkageId() {
        return messageLinkageId;
    }

    public Integer getBerthLength() {
        return berthLength;
    }

    public Float getWaterDepthAtBerth() {
        return waterDepthAtBerth;
    }

    public MooringPosition getMooringPosition() {
        return mooringPosition;
    }

    public Integer getBerthUtcMonth() {
        return berthUtcMonth;
    }

    public Integer getBerthUtcDay() {
        return berthUtcDay;
    }

    public Integer getBerthUtcHour() {
        return berthUtcHour;
    }

    public Integer getBerthUtcMinute() {
        return berthUtcMinute;
    }

    public ServiceStatus getServiceStatusAgent() {
        return serviceStatusAgent;
    }

    public ServiceStatus getServiceStatusFuel() {
        return serviceStatusFuel;
    }

    public ServiceStatus getServiceStatusChandler() {
        return serviceStatusChandler;
    }

    public ServiceStatus getServiceStatusStevedore() {
        return serviceStatusStevedore;
    }

    public ServiceStatus getServiceStatusElectrical() {
        return serviceStatusElectrical;
    }

    public ServiceStatus getServiceStatusPotableWater() {
        return serviceStatusPotableWater;
    }

    public ServiceStatus getServiceStatusCustomsHouse() {
        return serviceStatusCustomsHouse;
    }

    public ServiceStatus getServiceStatusCartage() {
        return serviceStatusCartage;
    }

    public ServiceStatus getServiceStatusCrane() {
        return serviceStatusCrane;
    }

    public ServiceStatus getServiceStatusLift() {
        return serviceStatusLift;
    }

    public ServiceStatus getServiceStatusMedical() {
        return serviceStatusMedical;
    }

    public ServiceStatus getServiceStatusNavigationRepair() {
        return serviceStatusNavigationRepair;
    }

    public ServiceStatus getServiceStatusProvisions() {
        return serviceStatusProvisions;
    }

    public ServiceStatus getServiceStatusShipRepair() {
        return serviceStatusShipRepair;
    }

    public ServiceStatus getServiceStatusSurveyor() {
        return serviceStatusSurveyor;
    }

    public ServiceStatus getServiceStatusSteam() {
        return serviceStatusSteam;
    }

    public ServiceStatus getServiceStatusTugs() {
        return serviceStatusTugs;
    }

    public ServiceStatus getServiceStatusSolidWasteDisposal() {
        return serviceStatusSolidWasteDisposal;
    }

    public ServiceStatus getServiceStatusLiquidWasteDisposal() {
        return serviceStatusLiquidWasteDisposal;
    }

    public ServiceStatus getServiceStatusHazardousWasteDisposal() {
        return serviceStatusHazardousWasteDisposal;
    }

    public ServiceStatus getServiceStatusReservedBallastExchange() {
        return serviceStatusReservedBallastExchange;
    }

    public ServiceStatus getServiceStatusAdditionalServices() {
        return serviceStatusAdditionalServices;
    }

    public ServiceStatus getServiceStatusFutureRegionalUse() {
        return serviceStatusFutureRegionalUse;
    }

    public ServiceStatus getServiceStatusFutureUse() {
        return serviceStatusFutureUse;
    }

    public String getNameOfBerth() {
        return nameOfBerth;
    }

    public Float getBerthLongitude() {
        return berthLongitude;
    }

    public Float getBerthLatitude() {
        return berthLatitude;
    }

    private final Integer messageLinkageId;
    private final Integer berthLength;
    private final Float waterDepthAtBerth;
    private final MooringPosition mooringPosition;
    private final Integer berthUtcMonth;
    private final Integer berthUtcDay;
    private final Integer berthUtcHour;
    private final Integer berthUtcMinute;
    private final ServiceStatus serviceStatusAgent;
    private final ServiceStatus serviceStatusFuel;
    private final ServiceStatus serviceStatusChandler;
    private final ServiceStatus serviceStatusStevedore;
    private final ServiceStatus serviceStatusElectrical;
    private final ServiceStatus serviceStatusPotableWater;
    private final ServiceStatus serviceStatusCustomsHouse;
    private final ServiceStatus serviceStatusCartage;
    private final ServiceStatus serviceStatusCrane;
    private final ServiceStatus serviceStatusLift;
    private final ServiceStatus serviceStatusMedical;
    private final ServiceStatus serviceStatusNavigationRepair;
    private final ServiceStatus serviceStatusProvisions;
    private final ServiceStatus serviceStatusShipRepair;
    private final ServiceStatus serviceStatusSurveyor;
    private final ServiceStatus serviceStatusSteam;
    private final ServiceStatus serviceStatusTugs;
    private final ServiceStatus serviceStatusSolidWasteDisposal;
    private final ServiceStatus serviceStatusLiquidWasteDisposal;
    private final ServiceStatus serviceStatusHazardousWasteDisposal;
    private final ServiceStatus serviceStatusReservedBallastExchange;
    private final ServiceStatus serviceStatusAdditionalServices;
    private final ServiceStatus serviceStatusFutureRegionalUse;
    private final ServiceStatus serviceStatusFutureUse;
    private final String nameOfBerth;
    private final Float berthLongitude;
    private final Float berthLatitude;

    public enum MooringPosition {
        UNDEFINED(0),
        PORT_SIDE_TO(1),
        STARBOARD_SIDE_TO(2),
        MEDITERRANEAN_MOORING(3),
        MOORING_BUOY(4),
        ANCHORAGE(5),
        RESERVED_FUTURE_USE_1(6),
        RESERVED_FUTURE_USE_2(7);

        private final int value;

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

        private final int value;

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
