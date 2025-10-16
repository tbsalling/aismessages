package dk.tbsalling.aismessages.ais.messages.asm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - UTC/Date Inquiry (DAC=1, FI=10)
 * Empty payload - used to request UTC/Date response
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UtcDateInquiry extends ApplicationSpecificMessage {

    protected UtcDateInquiry(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
        // No fields to decode - empty payload
    }

}
