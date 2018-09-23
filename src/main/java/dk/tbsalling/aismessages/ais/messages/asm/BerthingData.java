package dk.tbsalling.aismessages.ais.messages.asm;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.util.Arrays.stream;

public class BerthingData extends ApplicationSpecificMessage {

    protected BerthingData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }

    public Integer getMessageLinkageId() {
        return getDecodedValue(() -> messageLinkageId, value -> messageLinkageId = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(0,10)));
    }

    public Integer getBerthLength() {
        return getDecodedValue(() -> berthLength, value -> berthLength = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(10,19)));
    }

    public Float getWaterDepthAtBerth() {
        return getDecodedValue(() -> waterDepthAtBerth, value -> waterDepthAtBerth = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(19,27)) / 10f);
    }

    public MooringPosition getMooringPosition() {
        return getDecodedValue(() -> mooringPosition, value -> mooringPosition = value, () -> Boolean.TRUE, () -> MooringPosition.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(27,30))));
    }

    public Integer getBerthUtcMonth() {
        return getDecodedValue(() -> berthUtcMonth, value -> berthUtcMonth = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(30,34)));
    }

    public Integer getBerthUtcDay() {
        return getDecodedValue(() -> berthUtcDay, value -> berthUtcDay = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(34,39)));
    }

    public Integer getBerthUtcHour() {
        return getDecodedValue(() -> berthUtcHour, value -> berthUtcHour = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(39,44)));
    }

    public Integer getBerthUtcMinute() {
        return getDecodedValue(() -> berthUtcMinute, value -> berthUtcMinute = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(44,50)));
    }

    public ServiceStatus getServiceStatusAgent() {
        return getDecodedValue(() -> serviceStatusAgent, value -> serviceStatusAgent = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(50,52))));
    }

    public ServiceStatus getServiceStatusFuel() {
        return getDecodedValue(() -> serviceStatusFuel, value -> serviceStatusFuel = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(52,54))));
    }

    public ServiceStatus getServiceStatusChandler() {
        return getDecodedValue(() -> serviceStatusChandler, value -> serviceStatusChandler = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(54,56))));
    }

    public ServiceStatus getServiceStatusStevedore() {
        return getDecodedValue(() -> serviceStatusStevedore, value -> serviceStatusStevedore = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(56,58))));
    }

    public ServiceStatus getServiceStatusElectrical() {
        return getDecodedValue(() -> serviceStatusElectrical, value -> serviceStatusElectrical = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(58,60))));
    }

    public ServiceStatus getServiceStatusPotableWater() {
        return getDecodedValue(() -> serviceStatusPotableWater, value -> serviceStatusPotableWater = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(60,62))));
    }

    public ServiceStatus getServiceStatusCustomsHouse() {
        return getDecodedValue(() -> serviceStatusCustomsHouse, value -> serviceStatusCustomsHouse = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(62,64))));
    }

    public ServiceStatus getServiceStatusCartage() {
        return getDecodedValue(() -> serviceStatusCartage, value -> serviceStatusCartage = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(64,66))));
    }

    public ServiceStatus getServiceStatusCrane() {
        return getDecodedValue(() -> serviceStatusCrane, value -> serviceStatusCrane = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(66,68))));
    }

    public ServiceStatus getServiceStatusLift() {
        return getDecodedValue(() -> serviceStatusLift, value -> serviceStatusLift = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(68,70))));
    }

    public ServiceStatus getServiceStatusMedical() {
        return getDecodedValue(() -> serviceStatusMedical, value -> serviceStatusMedical = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(70,72))));
    }

    public ServiceStatus getServiceStatusNavigationRepair() {
        return getDecodedValue(() -> serviceStatusNavigationRepair, value -> serviceStatusNavigationRepair = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(72,74))));
    }

    public ServiceStatus getServiceStatusProvisions() {
        return getDecodedValue(() -> serviceStatusProvisions, value -> serviceStatusProvisions = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(74,76))));
    }

    public ServiceStatus getServiceStatusShipRepair() {
        return getDecodedValue(() -> serviceStatusShipRepair, value -> serviceStatusShipRepair = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(76,78))));
    }

    public ServiceStatus getServiceStatusSurveyor() {
        return getDecodedValue(() -> serviceStatusSurveyor, value -> serviceStatusSurveyor = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(78,80))));
    }

    public ServiceStatus getServiceStatusSteam() {
        return getDecodedValue(() -> serviceStatusSteam, value -> serviceStatusSteam = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(80,82))));
    }

    public ServiceStatus getServiceStatusTugs() {
        return getDecodedValue(() -> serviceStatusTugs, value -> serviceStatusTugs = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(82,84))));
    }

    public ServiceStatus getServiceStatusSolidWasteDisposal() {
        return getDecodedValue(() -> serviceStatusSolidWasteDisposal, value -> serviceStatusSolidWasteDisposal = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(84,86))));
    }

    public ServiceStatus getServiceStatusLiquidWasteDisposal() {
        return getDecodedValue(() -> serviceStatusLiquidWasteDisposal, value -> serviceStatusLiquidWasteDisposal = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(86,88))));
    }

    public ServiceStatus getServiceStatusHazardousWasteDisposal() {
        return getDecodedValue(() -> serviceStatusHazardousWasteDisposal, value -> serviceStatusHazardousWasteDisposal = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(88,90))));
    }

    public ServiceStatus getServiceStatusReservedBallastExchange() {
        return getDecodedValue(() -> serviceStatusReservedBallastExchange, value -> serviceStatusReservedBallastExchange = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(90,92))));
    }

    public ServiceStatus getServiceStatusAdditionalServices() {
        return getDecodedValue(() -> serviceStatusAdditionalServices, value -> serviceStatusAdditionalServices = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(92,94))));
    }

    public ServiceStatus getServiceStatusFutureRegionalUse() {
        return getDecodedValue(() -> serviceStatusFutureRegionalUse, value -> serviceStatusFutureRegionalUse = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(94,96))));
    }

    public ServiceStatus getServiceStatusFutureUse() {
        return getDecodedValue(() -> serviceStatusFutureUse, value -> serviceStatusFutureUse = value, () -> Boolean.TRUE, () -> ServiceStatus.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(96,98))));
    }

    public String getNameOfBerth() {
        return getDecodedValue(() -> nameOfBerth, value -> nameOfBerth = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBinaryData().substring(98,218)));
    }

    public Float getBerthLongitude() {
        return getDecodedValue(() -> berthLongitude, value -> berthLongitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBinaryData().substring(218,243)) / 60000f);
    }

    public Float getBerthLatitude() {
        return getDecodedValue(() -> berthLatitude, value -> berthLatitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBinaryData().substring(243,267)) / 60000f);
    }

    private transient Integer messageLinkageId;
    private transient Integer berthLength;
    private transient Float waterDepthAtBerth;
    private transient MooringPosition mooringPosition;
    private transient Integer berthUtcMonth;
    private transient Integer berthUtcDay;
    private transient Integer berthUtcHour;
    private transient Integer berthUtcMinute;
    private transient ServiceStatus serviceStatusAgent;
    private transient ServiceStatus serviceStatusFuel;
    private transient ServiceStatus serviceStatusChandler;
    private transient ServiceStatus serviceStatusStevedore;
    private transient ServiceStatus serviceStatusElectrical;
    private transient ServiceStatus serviceStatusPotableWater;
    private transient ServiceStatus serviceStatusCustomsHouse;
    private transient ServiceStatus serviceStatusCartage;
    private transient ServiceStatus serviceStatusCrane;
    private transient ServiceStatus serviceStatusLift;
    private transient ServiceStatus serviceStatusMedical;
    private transient ServiceStatus serviceStatusNavigationRepair;
    private transient ServiceStatus serviceStatusProvisions;
    private transient ServiceStatus serviceStatusShipRepair;
    private transient ServiceStatus serviceStatusSurveyor;
    private transient ServiceStatus serviceStatusSteam;
    private transient ServiceStatus serviceStatusTugs;
    private transient ServiceStatus serviceStatusSolidWasteDisposal;
    private transient ServiceStatus serviceStatusLiquidWasteDisposal;
    private transient ServiceStatus serviceStatusHazardousWasteDisposal;
    private transient ServiceStatus serviceStatusReservedBallastExchange;
    private transient ServiceStatus serviceStatusAdditionalServices;
    private transient ServiceStatus serviceStatusFutureRegionalUse;
    private transient ServiceStatus serviceStatusFutureUse;
    private transient String nameOfBerth;
    private transient Float berthLongitude;
    private transient Float berthLatitude;

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
