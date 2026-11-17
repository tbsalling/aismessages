package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.requireNonNull;

/**
 * Application specific message: the decoded data content of a binary AIS message (Messages 6, 8, 25 and 26), selected
 * by its 16-bit application identifier, a 10-bit designated area code plus a 6-bit function identifier (ITU-R
 * M.1371-6 Annex 4 § A4-1 and § A4-2.1, Table 23). International messages under DAC=1 are defined by Annex 4 Table 24
 * and IMO SN.1/Circ.289; regional ones by the authority for the DAC. Pairs this library does not decode resolve to
 * {@link UnknownApplicationSpecificMessage}.
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public abstract sealed class ApplicationSpecificMessage permits
    ApplicationAcknowledgement,
    AreaNotice,
    CapabilityInterrogation,
    CapabilityReply,
    InterrogationForSpecificFunctionalMessage,
        BerthingData,
    DangerousCargoIndication,
    Environmental,
        ExtendedShipStaticAndVoyageRelatedData,
        InlandShipStaticAndVoyageRelatedData,
    MarineTrafficSignal,
    MeteorologicalAndHydrographicalData,
        NumberOfPersonsOnBoard,
    RouteInformation,
    TextDescription,
    TidalWindow,
    UnknownApplicationSpecificMessage,
    UtcDateInquiry,
    UtcDateResponse,
    VtsGeneratedSyntheticTargets,
    WeatherObservation {

    /**
     * Create proper type of ApplicationSpecificMessage
     *
     * @param designatedAreaCode Designated area code.
     * @param functionalId Functional ID
     * @param binaryData Binary data
     * @return Application Specific Message
     */
    public static ApplicationSpecificMessage create(int designatedAreaCode, int functionalId, BitString binaryData) {
        requireNonNull(binaryData);

        ApplicationSpecificMessage asm = switch (designatedAreaCode) {
            case 1 -> switch (functionalId) {
                case 0 -> new TextDescription(designatedAreaCode, functionalId, binaryData);
                case 1 -> new TextDescription(designatedAreaCode, functionalId, binaryData); // discontinued in ITU-R M.1371-6 Table 24; kept for legacy traffic
                case 2 -> new InterrogationForSpecificFunctionalMessage(designatedAreaCode, functionalId, binaryData);
                case 3 -> new CapabilityInterrogation(designatedAreaCode, functionalId, binaryData);
                case 4 -> new CapabilityReply(designatedAreaCode, functionalId, binaryData);
                case 5 -> new ApplicationAcknowledgement(designatedAreaCode, functionalId, binaryData);
                case 10 -> new UtcDateInquiry(designatedAreaCode, functionalId, binaryData);
                case 11 -> new UtcDateResponse(designatedAreaCode, functionalId, binaryData);
                case 14 -> new TidalWindow(designatedAreaCode, functionalId, binaryData);
                case 17 -> new VtsGeneratedSyntheticTargets(designatedAreaCode, functionalId, binaryData);
                case 18 -> new MarineTrafficSignal(designatedAreaCode, functionalId, binaryData);
                case 19 -> new MarineTrafficSignal(designatedAreaCode, functionalId, binaryData);
                case 20 -> new BerthingData(designatedAreaCode, functionalId, binaryData);
                case 21 -> new WeatherObservation(designatedAreaCode, functionalId, binaryData);
                case 22 -> new AreaNotice(designatedAreaCode, functionalId, binaryData);
                case 23 -> new AreaNotice(designatedAreaCode, functionalId, binaryData);
                case 24 -> new ExtendedShipStaticAndVoyageRelatedData(designatedAreaCode, functionalId, binaryData);
                case 25 -> new DangerousCargoIndication(designatedAreaCode, functionalId, binaryData);
                case 26 -> new Environmental(designatedAreaCode, functionalId, binaryData);
                case 27 -> new RouteInformation(designatedAreaCode, functionalId, binaryData);
                case 28 -> new RouteInformation(designatedAreaCode, functionalId, binaryData);
                case 31 -> new MeteorologicalAndHydrographicalData(designatedAreaCode, functionalId, binaryData);
                case 40 -> new NumberOfPersonsOnBoard(designatedAreaCode, functionalId, binaryData);
                default -> null;
            };
            case 200 -> switch (functionalId) {
                case 10 -> new InlandShipStaticAndVoyageRelatedData(designatedAreaCode, functionalId, binaryData);
                default -> null;
            };
            default -> null;
        };

        if (asm == null)
            asm = new UnknownApplicationSpecificMessage(designatedAreaCode, functionalId, binaryData);

        return asm;
    }

    /** Bits 15-6 of the application identifier (ITU-R M.1371-6 Table 23): designated area code, 0 = test, 1-9 = international, 10-999 = regional, 1000-1023 = reserved. */
    private final int designatedAreaCode;
    /** Bits 5-0 of the application identifier (Table 23): function identifier, 0-63, defined by the authority for the DAC. */
    private final int functionalId;
    /** The application data following the 16-bit application identifier; field positions in subclasses are relative to its first bit. */
    private final BitString binaryData;

}
