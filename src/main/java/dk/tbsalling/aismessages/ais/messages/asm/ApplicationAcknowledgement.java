package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * International function message 5: application acknowledgement to an addressed binary message (DAC=1, FI=5);
 * ITU-R M.1371-6 Annex 4 § A4-5.5, Table 33. Sent with Message 6 by an application to confirm reception of an
 * addressed binary message whose "acknowledge required" flag was set. Binary broadcasts are never acknowledged.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ApplicationAcknowledgement extends ApplicationSpecificMessage {

    protected ApplicationAcknowledgement(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields (Table 33: DAC 10, FI 6, text sequence number 11, AI available 1, AI response 3, 49 spare bits)
        this.receivedDesignatedAreaCode = binaryData.getUnsignedInt(0, 10);
        this.receivedFunctionalId = binaryData.getUnsignedInt(10, 16);
        this.textSequenceNumber = binaryData.getUnsignedInt(16, 27);
        this.applicationIdentifierAvailable = binaryData.getBoolean(27, 28);
        this.applicationIdentifierResponse = binaryData.getUnsignedInt(28, 31);
    }

    /** DAC of the functional message being acknowledged (recommended to be spare by the recommendation). */
    int receivedDesignatedAreaCode;
    /** FI of the functional message being acknowledged. */
    int receivedFunctionalId;
    /** Text sequence number of the acknowledged functional message; 0 = no sequence number. */
    int textSequenceNumber;
    /** False = received but the application identifier is not available; true = available. */
    boolean applicationIdentifierAvailable;
    /** 0 = unable to respond, 1 = reception acknowledged, 2 = response to follow, 3 = able to respond but currently inhibited, 4-7 = spare. */
    int applicationIdentifierResponse;
}
