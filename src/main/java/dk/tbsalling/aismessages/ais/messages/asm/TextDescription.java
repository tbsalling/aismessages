package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - Text description (DAC=1, FI=0)
 * Used for broadcasting text messages
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TextDescription extends ApplicationSpecificMessage {

    protected TextDescription(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        if (binaryData.length() >= 6) {
            this.text = AISText.decode(getBinaryData(), 0, Math.min(getBinaryData().length(), 936));
        } else {
            this.text = "";
        }
    }

    String text;

}
