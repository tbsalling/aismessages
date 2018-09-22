package dk.tbsalling.aismessages.ais.messages.asm;

public final class UnknownApplicationSpecificMessage extends ApplicationSpecificMessage {

    @Override
    public Integer getDesignatedAreaCode() {
        return null;
    }

    @Override
    public Integer getFunctionalId() {
        return null;
    }

    UnknownApplicationSpecificMessage(String binaryData) {
        super(binaryData);
    }

}
