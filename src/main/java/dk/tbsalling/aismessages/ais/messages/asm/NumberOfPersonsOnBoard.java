package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;

public class NumberOfPersonsOnBoard extends ApplicationSpecificMessage {

    protected NumberOfPersonsOnBoard(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.numberOfPersons = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 13));
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    private final int numberOfPersons;


}
