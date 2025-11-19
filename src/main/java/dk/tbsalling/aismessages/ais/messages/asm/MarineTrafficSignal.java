package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
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

    protected MarineTrafficSignal(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 10));
        this.name = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(10, 130));
        this.longitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(130, 158)) / 600000f;
        this.latitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(158, 185)) / 600000f;
        this.status = SignalStatus.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(185, 187)));
        this.signal = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(187, 192));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(192, 197));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(197, 203));
        this.nextSignal = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(203, 208));
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
