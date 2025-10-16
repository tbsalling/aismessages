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
public abstract sealed class ApplicationSpecificMessage permits BerthingData, InlandShipStaticAndVoyageRelatedData, ExtendedShipStaticAndVoyageRelatedData, NumberOfPersonsOnBoard, UnknownApplicationSpecificMessage {

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
                case 20 -> new BerthingData(designatedAreaCode, functionalId, binaryData);
                case 24 -> new ExtendedShipStaticAndVoyageRelatedData(designatedAreaCode, functionalId, binaryData);
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
