package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public class BinaryMessageSingleSlot extends DecodedAISMessage {

	public BinaryMessageSingleSlot(
			Integer repeatIndicator, MMSI sourceMmsi,
			Boolean destinationIndicator, Boolean binaryDataFlag,
			MMSI destinationMMSI, String binaryData) {
		super(AISMessageType.BinaryMessageSingleSlot, repeatIndicator, sourceMmsi);
		this.destinationIndicator = destinationIndicator;
		this.binaryDataFlag = binaryDataFlag;
		this.destinationMMSI = destinationMMSI;
		this.binaryData = binaryData;
	}
	public final Boolean getDestinationIndicator() {
		return destinationIndicator;
	}
	public final Boolean getBinaryDataFlag() {
		return binaryDataFlag;
	}
	public final MMSI getDestinationMMSI() {
		return destinationMMSI;
	}
	public final String getBinaryData() {
		return binaryData;
	}
	
	public static BinaryMessageSingleSlot fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryMessageSingleSlot))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Boolean destinationIndicator = Decoder.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean binaryDataFlag = Decoder.convertToBoolean(encodedMessage.getBits(39, 40));
		MMSI destinationMMSI = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		String binaryData = Decoder.convertToBitString(encodedMessage.getBits(40, 168));

		return new BinaryMessageSingleSlot(repeatIndicator, sourceMmsi,
				destinationIndicator, binaryDataFlag, destinationMMSI,
				binaryData);
	}

	private final Boolean destinationIndicator;
	private final Boolean binaryDataFlag;
	private final MMSI destinationMMSI;
	private final String binaryData;
}
