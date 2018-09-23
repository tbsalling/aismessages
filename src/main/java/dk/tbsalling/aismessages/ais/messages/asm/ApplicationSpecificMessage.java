package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.messages.CachedDecodedValues;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public abstract class ApplicationSpecificMessage implements Serializable, CachedDecodedValues {

    /**
     * Create proper type of ApplicationSpecificMessage
     */
    public static ApplicationSpecificMessage create(int designatedAreaCode, int functionalId, String binaryData) {
        requireNonNull(binaryData);

        ApplicationSpecificMessage asm = null;

        if (designatedAreaCode == 1) {
            switch (functionalId) {
                case 24:
                    //      asm = ExtendedShipStaticAndVoyageRelatedData.create
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
