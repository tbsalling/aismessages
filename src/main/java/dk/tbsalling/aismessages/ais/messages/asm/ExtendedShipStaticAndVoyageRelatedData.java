package dk.tbsalling.aismessages.ais.messages.asm;

public class ExtendedShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {
    public ExtendedShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);
    }
}
