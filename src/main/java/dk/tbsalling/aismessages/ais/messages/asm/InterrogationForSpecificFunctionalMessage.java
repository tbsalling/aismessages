package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * International function message 2: interrogation for a specific functional message (DAC=1, FI=2); ITU-R M.1371-6
 * Annex 4 § A4-5.2, Table 30. Sent with Message 6 to ask another application for the functional message identified by
 * the requested DAC and FI; the reply is an addressed binary message.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InterrogationForSpecificFunctionalMessage extends ApplicationSpecificMessage {

    protected InterrogationForSpecificFunctionalMessage(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields (Table 30: requested DAC 10 bits, requested FI 6 bits, 64 spare bits)
        this.requestedDesignatedAreaCode = binaryData.getUnsignedInt(0, 10);
        this.requestedFunctionalId = binaryData.getUnsignedInt(10, 16);
    }

    /** The DAC (international, regional or test) of the requested functional message. */
    int requestedDesignatedAreaCode;
    /** The FI of the requested functional message. */
    int requestedFunctionalId;
}
