package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public class BinaryMessageMultipleSlot extends DecodedAISMessage {

	public BinaryMessageMultipleSlot(
			Integer repeatIndicator, MMSI sourceMmsi, Boolean addressed,
			Boolean structured, MMSI destinationMmsi, Integer applicationId,
			String data, String radioStatus) {
		super(AISMessageType.BinaryMessageMultipleSlot, repeatIndicator, sourceMmsi);
		this.addressed = addressed;
		this.structured = structured;
		this.destinationMmsi = destinationMmsi;
		this.applicationId = applicationId;
		this.data = data;
		this.radioStatus = radioStatus;
	}
	
	public final Boolean getAddressed() {
		return addressed;
	}
	public final Boolean getStructured() {
		return structured;
	}
	public final MMSI getDestinationMmsi() {
		return destinationMmsi;
	}
	public final Integer getApplicationId() {
		return applicationId;
	}
	public final String getData() {
		return data;
	}
	public final String getRadioStatus() {
		return radioStatus;
	}
	
	public static BinaryMessageMultipleSlot fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryMessageMultipleSlot))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Boolean addressed = Decoder.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean structured = Decoder.convertToBoolean(encodedMessage.getBits(39, 40));
		MMSI destinationMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer applicationId = Decoder.convertToUnsignedInteger(encodedMessage.getBits(70, 86));
		String data = Decoder.convertToBitString(encodedMessage.getBits(86, 86+1004+1));
		String radioStatus = null; // Decoder.convertToBinaryString(encodedMessage.getBits(6, 8));

		return new BinaryMessageMultipleSlot(repeatIndicator, sourceMmsi,
				addressed, structured, destinationMmsi, applicationId, data, radioStatus);
	}
	
	private final Boolean addressed;
	private final Boolean structured;
	private final MMSI destinationMmsi;
	private final Integer applicationId;
	private final String data;
	private final String radioStatus;
}
