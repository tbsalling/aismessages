package dk.tbsalling.aismessages.ais.messages.asm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnknownApplicationSpecificMessage extends ApplicationSpecificMessage {

    UnknownApplicationSpecificMessage(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }

}
