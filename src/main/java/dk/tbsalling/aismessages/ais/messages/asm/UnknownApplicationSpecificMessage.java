package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Fallback for a DAC/FI combination this library does not decode (ITU-R M.1371-6 Annex 4). It exposes only the
 * application identifier and the raw application data via the inherited getters.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnknownApplicationSpecificMessage extends ApplicationSpecificMessage {

    UnknownApplicationSpecificMessage(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }

}
