package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
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

    protected UtcDateResponse(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.year = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 14));
        this.month = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(14, 18));
        this.day = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(18, 23));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(23, 28));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(28, 34));
        this.second = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(34, 40));
    }

    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;

}
