package dk.tbsalling.aismessages.ais.messages.asm;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

public class NumberOfPersonsOnBoard extends ApplicationSpecificMessage {

    protected NumberOfPersonsOnBoard(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }

    public Integer getNumberOfPersons() {
        return getDecodedValue(() -> numberOfPersons, value -> numberOfPersons = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBinaryData().substring(0,13)));
    }

    private transient Integer numberOfPersons;


}
