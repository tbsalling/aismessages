package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.messages.CachedDecodedValues;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public abstract class ApplicationSpecificMessage implements Serializable, CachedDecodedValues {

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

        ApplicationSpecificMessage asm = null;

        if (designatedAreaCode == 1) {
            switch (functionalId) {
                case 20:
                    asm = new BerthingData(designatedAreaCode, functionalId, binaryData);
                    break;
                case 24:
                    asm = new ExtendedShipStaticAndVoyageRelatedData(designatedAreaCode, functionalId, binaryData);
                    break;
                case 40:
                    asm = new NumberOfPersonsOnBoard(designatedAreaCode, functionalId, binaryData);
                    break;
            }
        } else if (designatedAreaCode == 200) {
            switch (functionalId) {
                case 10:
                    asm = new InlandShipStaticAndVoyageRelatedData(designatedAreaCode, functionalId, binaryData);
                    break;
            }
        }

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
