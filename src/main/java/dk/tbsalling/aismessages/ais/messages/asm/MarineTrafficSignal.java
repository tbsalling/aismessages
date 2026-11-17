package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Marine Traffic Signal (DAC=1, FI=18 or FI=19)
 * Used for marine traffic signal information
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MarineTrafficSignal extends ApplicationSpecificMessage {

    protected MarineTrafficSignal(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = getBinaryData().getUnsignedInt(0, 10);
        this.name = AISText.decode(getBinaryData(), 10, 130);
        this.longitude = getBinaryData().getSignedFloat(130, 158) / 600000f;
        this.latitude = getBinaryData().getSignedFloat(158, 185) / 600000f;
        this.status = SignalStatus.valueOf(getBinaryData().getUnsignedInt(185, 187));
        this.signal = getBinaryData().getUnsignedInt(187, 192);
        this.hour = getBinaryData().getUnsignedInt(192, 197);
        this.minute = getBinaryData().getUnsignedInt(197, 203);
        this.nextSignal = getBinaryData().getUnsignedInt(203, 208);
    }

    /** Bits 0-10 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): linkage ID. */
    int linkageId;
    /** Bits 10-130 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): name, up to 20 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String name;
    /** Bits 130-158 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): longitude in decimal degrees (1/10 000 min ÷ 600 000), signed. */
    float longitude;
    /** Bits 158-185 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): latitude in decimal degrees (1/10 000 min ÷ 600 000), signed. */
    float latitude;
    /** Bits 185-187 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): status code, decoded as {@link SignalStatus}. */
    SignalStatus status;
    /** Bits 187-192 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): signal. */
    int signal;
    /** Bits 192-197 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): hour. */
    int hour;
    /** Bits 197-203 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): minute. */
    int minute;
    /** Bits 203-208 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19): next signal. */
    int nextSignal;

    /** Codes of the status field, bits 185-187 of the application data (IMO SN.1/Circ.289, DAC=1 FI=18 or FI=19). */
    public enum SignalStatus {
        /** 0 = not specified. */
        NOT_SPECIFIED(0),
        /** 1 = in operation. */
        IN_OPERATION(1),
        /** 2 = out of order. */
        OUT_OF_ORDER(2),
        /** 3 = not available. */
        NOT_AVAILABLE(3);

        /** The code as transmitted. */
        int value;

        SignalStatus(int value) {
            this.value = value;
        }

        public static SignalStatus valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .get();
        }
    }

}
