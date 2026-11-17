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

    /** Bits 0-10 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): linkage ID. */
    int linkageId;
    /** Bits 10-130 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): name, up to 20 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String name;
    /** Bit 130 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): accuracy flag. */
    boolean accuracy;
    /** Bits 131-159 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): longitude in decimal degrees (1/10 000 min ÷ 600 000), signed. */
    float longitude;
    /** Bits 159-186 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): latitude in decimal degrees (1/10 000 min ÷ 600 000), signed. */
    float latitude;
    /** Bits 186-196 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): speed over ground (transmitted value ÷ 10). */
    float speedOverGround;
    /** Bits 196-205 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): course over ground (transmitted value ÷ 10). */
    float courseOverGround;
    /** Bits 205-211 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): second. */
    int second;
    /** Bits 211-220 of the application data (IMO SN.1/Circ.289, DAC=1 FI=17): COG (transmitted value ÷ 10). */
    float cog;

}
