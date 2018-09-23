package dk.tbsalling.aismessages.ais.messages.asm;

public final class UnknownApplicationSpecificMessage extends ApplicationSpecificMessage {

    UnknownApplicationSpecificMessage(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }

}
