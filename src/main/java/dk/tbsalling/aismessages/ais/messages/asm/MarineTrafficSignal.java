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

    int linkageId;
    String name;
    float longitude;
    float latitude;
    SignalStatus status;
    int signal;
    int hour;
    int minute;
    int nextSignal;

    public enum SignalStatus {
        NOT_SPECIFIED(0),
        IN_OPERATION(1),
        OUT_OF_ORDER(2),
        NOT_AVAILABLE(3);

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
