package dk.tbsalling.aismessages.ais.messages.asm;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public class ApplicationSpecificMessage implements Serializable {

    /**
     * Create proper type of ApplicationSpecificMessage
     * @param designatedAreaCode
     * @param functionalId
     * @param bitString
     */
    public static ApplicationSpecificMessage create(Integer designatedAreaCode, Integer functionalId, String bitString) {
        requireNonNull(designatedAreaCode);
        requireNonNull(functionalId);
        requireNonNull(bitString);

        if (!bitString.matches("[0-1]+"))
            throw new IllegalArgumentException("Contains non-binary digits: " + bitString);

        ApplicationSpecificMessage asm = null;

        if (designatedAreaCode == 1) {
            if (functionalId == 24) {
                //      asm = ExtendedShipStaticAndVoyageRelatedData.create
            }
        }

        if (asm == null)
            asm = new UnknownApplicationSpecificMessage();

        return asm;
    }

    private String bitString;

}
