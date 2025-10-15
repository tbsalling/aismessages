package dk.tbsalling.aismessages.ais.messages.asm;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public abstract class ApplicationSpecificMessage implements Serializable {

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

    @SuppressWarnings("unused")
    public final int getDesignatedAreaCode() {
        return designatedAreaCode;
    };

    @SuppressWarnings("unused")
    public final int getFunctionalId() {
        return functionalId;
    }

    @SuppressWarnings("unused")
    public final String getBinaryData() {
        return binaryData;
    }

    protected ApplicationSpecificMessage(int designatedAreaCode, int functionalId, String binaryData) {
        this.designatedAreaCode = designatedAreaCode;
        this.functionalId = functionalId;
        this.binaryData = binaryData;
    }

    private final int designatedAreaCode;
    private final int functionalId;
    private final String binaryData;

}
