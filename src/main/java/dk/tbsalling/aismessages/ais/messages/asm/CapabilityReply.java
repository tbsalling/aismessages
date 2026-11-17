package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * International function message 4: capability reply (DAC=1, FI=4); ITU-R M.1371-6 Annex 4 § A4-5.4, Table 32.
 * Sent with Message 6 in reply to a {@link CapabilityInterrogation}, listing for each of the 64 function identifiers
 * under the given DAC whether the application supports it.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CapabilityReply extends ApplicationSpecificMessage {

    protected CapabilityReply(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields (Table 32: DAC 10 bits, then 64 pairs of 2 bits in FI order; the first bit of each pair is "FI available")
        this.repliedDesignatedAreaCode = binaryData.getUnsignedInt(0, 10);
        long available = 0L;
        for (int fi = 0; fi < 64; fi++) {
            if (binaryData.getBoolean(10 + 2 * fi, 11 + 2 * fi)) {
                available |= 1L << fi;
            }
        }
        this.availableFunctionalIds = available;
    }

    /**
     * @param functionalId a function identifier, 0-63
     * @return true if the replying application reports the given FI as available under {@code getRepliedDesignatedAreaCode()}
     */
    public boolean isFunctionalIdAvailable(int functionalId) {
        if (functionalId < 0 || functionalId > 63) {
            throw new IllegalArgumentException("functionalId must be 0-63; was " + functionalId);
        }
        return (availableFunctionalIds & (1L << functionalId)) != 0;
    }

    /** The DAC (international, regional or test) the capability table applies to. */
    int repliedDesignatedAreaCode;
    /** Bit mask of available function identifiers: bit {@code fi} is set when FI {@code fi} is available. See {@link #isFunctionalIdAvailable(int)}. */
    long availableFunctionalIds;
}
