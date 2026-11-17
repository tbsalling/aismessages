package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * International function message 0: text telegram using 6-bit ASCII (DAC=1, FI=0); ITU-R M.1371-6 Annex 4 § A4-5.1,
 * Tables 25 to 28, and IMO SN.1/Circ.289.
 * <p>
 * The application data starts with a 1-bit "acknowledge required" flag and an 11-bit text sequence number, followed by
 * the six-bit ASCII text. FI=1 is "discontinued" in ITU-R M.1371-6 but is still decoded with this layout for legacy traffic.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TextDescription extends ApplicationSpecificMessage {

    protected TextDescription(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields (ITU-R M.1371-6 Table 25: acknowledge required flag 1 bit, text sequence number 11 bits, text)
        this.acknowledgeRequired = binaryData.getBoolean(0, 1);
        this.textSequenceNumber = binaryData.getUnsignedInt(1, 12);
        if (binaryData.length() >= 18) {
            this.text = AISText.decode(getBinaryData(), 12, Math.min(getBinaryData().length(), 12 + 936));
        } else {
            this.text = "";
        }
    }

    /** True when a reply (IFM 5) is required; optional for addressed binary messages, always false for broadcasts. */
    boolean acknowledgeRequired;
    /** Sequence number used by the application to sub-divide long texts; 0 = sequence numbers not used. */
    int textSequenceNumber;
    /** The six-bit ASCII text, with trailing padding removed. */
    String text;

}
