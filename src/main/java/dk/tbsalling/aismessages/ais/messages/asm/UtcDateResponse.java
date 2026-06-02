package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - UTC/Date Response (DAC=1, FI=11)
 * Response to UTC/Date inquiry with current UTC date and time
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UtcDateResponse extends ApplicationSpecificMessage {

    protected UtcDateResponse(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.year = getBinaryData().getUnsignedInt(0, 14);
        this.month = getBinaryData().getUnsignedInt(14, 18);
        this.day = getBinaryData().getUnsignedInt(18, 23);
        this.hour = getBinaryData().getUnsignedInt(23, 28);
        this.minute = getBinaryData().getUnsignedInt(28, 34);
        this.second = getBinaryData().getUnsignedInt(34, 40);
    }

    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;

}
