package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * International function message 3: capability interrogation (DAC=1, FI=3); ITU-R M.1371-6 Annex 4 § A4-5.3,
 * Table 31. Sent with Message 6 to ask another application which function identifiers it supports under the requested
 * DAC; the reply is a {@link CapabilityReply}.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CapabilityInterrogation extends ApplicationSpecificMessage {

    protected CapabilityInterrogation(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields (Table 31: requested DAC 10 bits, 70 spare bits)
        this.requestedDesignatedAreaCode = binaryData.getUnsignedInt(0, 10);
    }

    /** The DAC (international, regional or test) whose supported function identifiers are requested. */
    int requestedDesignatedAreaCode;
}
