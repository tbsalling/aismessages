package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - Number of Persons on Board (DAC=1, FI=40)
 * Reports the current number of persons on board.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NumberOfPersonsOnBoard extends ApplicationSpecificMessage {

    protected NumberOfPersonsOnBoard(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.numberOfPersons = getBinaryData().getUnsignedInt(0, 13);
    }

    /** Bits 0-13 of the application data (IMO SN.1/Circ.289, DAC=1 FI=40): number of persons. */
    int numberOfPersons;

}
