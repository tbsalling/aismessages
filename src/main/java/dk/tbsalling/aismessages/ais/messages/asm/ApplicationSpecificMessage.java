package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.CachedDecodedValues;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public class ApplicationSpecificMessage implements Serializable, CachedDecodedValues {

    /**
     * Create proper type of ApplicationSpecificMessage
     */
    public static ApplicationSpecificMessage create(AISMessage owner, int designatedAreaCode, int functionalId) {
        requireNonNull(owner);

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
                    asm = new InlandShipStaticAndVoyageRelatedData(owner);
                    break;
            }
        }

        if (asm == null)
            asm = new UnknownApplicationSpecificMessage(owner);

        return asm;
    }

    protected ApplicationSpecificMessage(AISMessage owner) {
        this.owner = owner;
    }

    protected String getBits(int beginIndex, int endIndex) {
        return owner.getBits(beginIndex, endIndex);
    }

    private final AISMessage owner;

}
