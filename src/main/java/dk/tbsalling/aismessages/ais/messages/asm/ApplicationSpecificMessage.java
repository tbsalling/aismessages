package dk.tbsalling.aismessages.ais.messages.asm;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public abstract sealed class ApplicationSpecificMessage permits 
    AreaNotice,
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
    public static ApplicationSpecificMessage create(int designatedAreaCode, int functionalId, String binaryData) {
        requireNonNull(binaryData);

        ApplicationSpecificMessage asm = switch (designatedAreaCode) {
            case 1 -> switch (functionalId) {
                case 0 -> new TextDescription(designatedAreaCode, functionalId, binaryData);
                case 1 -> new TextDescription(designatedAreaCode, functionalId, binaryData);
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

    private final int designatedAreaCode;
    private final int functionalId;
    private final String binaryData;

}
