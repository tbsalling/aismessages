package dk.tbsalling.aismessages.ais.messages.asm;

public interface ApplicationSpecificMessageEnvelope {

    Integer getDesignatedAreaCode();
    Integer getFunctionalId();
    ApplicationSpecificMessage getApplicationSpecificMessage();

}
