package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - Tidal Window (DAC=1, FI=14)
 * Provides information about tidal windows for safe vessel passage
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TidalWindow extends ApplicationSpecificMessage {

    protected TidalWindow(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.month = getBinaryData().getUnsignedInt(0, 4);
        this.day = getBinaryData().getUnsignedInt(4, 9);
        this.hour = getBinaryData().getUnsignedInt(9, 14);
        this.minute = getBinaryData().getUnsignedInt(14, 20);
        this.tideTo = getBinaryData().getUnsignedInt(20, 25);
        this.tideFrom = getBinaryData().getUnsignedInt(25, 30);
        this.currentSpeed = getBinaryData().getUnsignedInt(30, 37) / 10f;
        this.currentDirection = getBinaryData().getUnsignedInt(37, 46);
    }

    /** Bits 0-4 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): month. */
    int month;
    /** Bits 4-9 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): day. */
    int day;
    /** Bits 9-14 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): hour. */
    int hour;
    /** Bits 14-20 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): minute. */
    int minute;
    /** Bits 20-25 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): tide to. */
    int tideTo;
    /** Bits 25-30 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): tide from. */
    int tideFrom;
    /** Bits 30-37 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): current speed (transmitted value ÷ 10). */
    float currentSpeed;
    /** Bits 37-46 of the application data (IMO SN.1/Circ.289, DAC=1 FI=14): current direction. */
    int currentDirection;

}
