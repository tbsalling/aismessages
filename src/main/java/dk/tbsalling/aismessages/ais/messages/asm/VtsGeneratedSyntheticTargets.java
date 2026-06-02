package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - VTS Generated/Synthetic Targets (DAC=1, FI=17)
 * Used by VTS to broadcast information about synthetic targets
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VtsGeneratedSyntheticTargets extends ApplicationSpecificMessage {

    protected VtsGeneratedSyntheticTargets(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = getBinaryData().getUnsignedInt(0, 10);
        this.name = AISText.decode(getBinaryData(), 10, 130);
        this.accuracy = getBinaryData().getBoolean(130, 131);
        this.longitude = getBinaryData().getSignedFloat(131, 159) / 600000f;
        this.latitude = getBinaryData().getSignedFloat(159, 186) / 600000f;
        this.speedOverGround = getBinaryData().getUnsignedInt(186, 196) / 10f;
        this.courseOverGround = getBinaryData().getUnsignedInt(196, 205) / 10f;
        this.second = getBinaryData().getUnsignedInt(205, 211);
        this.cog = getBinaryData().getUnsignedInt(211, 220) / 10f;
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
