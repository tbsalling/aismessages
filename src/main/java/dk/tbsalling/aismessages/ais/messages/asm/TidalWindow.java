package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Tidal Window (DAC=1, FI=14)
 * Provides information about tidal windows for safe vessel passage
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TidalWindow extends ApplicationSpecificMessage {

    protected TidalWindow(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.month = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 4));
        this.day = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(4, 9));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(9, 14));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(14, 20));
        this.tideTo = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(20, 25));
        this.tideFrom = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(25, 30));
        this.currentSpeed = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(30, 37)) / 10f;
        this.currentDirection = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(37, 46));
    }

    int month;
    int day;
    int hour;
    int minute;
    int tideTo;
    int tideFrom;
    float currentSpeed;
    int currentDirection;

}
