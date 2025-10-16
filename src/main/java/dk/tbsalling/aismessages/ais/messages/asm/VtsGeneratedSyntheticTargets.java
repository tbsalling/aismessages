package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - VTS Generated/Synthetic Targets (DAC=1, FI=17)
 * Used by VTS to broadcast information about synthetic targets
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VtsGeneratedSyntheticTargets extends ApplicationSpecificMessage {

    protected VtsGeneratedSyntheticTargets(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 10));
        this.name = BitDecoder.INSTANCE.decodeString(getBinaryData().substring(10, 130));
        this.accuracy = BitDecoder.INSTANCE.decodeBoolean(getBinaryData().substring(130, 131));
        this.longitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(131, 159)) / 600000f;
        this.latitude = BitDecoder.INSTANCE.decodeFloat(getBinaryData().substring(159, 186)) / 600000f;
        this.speedOverGround = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(186, 196)) / 10f;
        this.courseOverGround = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(196, 205)) / 10f;
        this.second = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(205, 211));
        this.cog = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(211, 220)) / 10f;
    }

    int linkageId;
    String name;
    boolean accuracy;
    float longitude;
    float latitude;
    float speedOverGround;
    float courseOverGround;
    int second;
    float cog;

}
