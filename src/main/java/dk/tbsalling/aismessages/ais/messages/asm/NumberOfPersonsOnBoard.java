package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NumberOfPersonsOnBoard extends ApplicationSpecificMessage {

    protected NumberOfPersonsOnBoard(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.numberOfPersons = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 13));
    }

    int numberOfPersons;

}
