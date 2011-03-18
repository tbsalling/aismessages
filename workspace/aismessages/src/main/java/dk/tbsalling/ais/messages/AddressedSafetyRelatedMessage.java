package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public class AddressedSafetyRelatedMessage extends DecodedAISMessage {

	public AddressedSafetyRelatedMessage(
			Integer repeatIndicator, MMSI sourceMmsi, Integer sequenceNumber,
			MMSI destinationMmsi, Boolean retransmit, String text) {
		super(AISMessageType.AddressedSafetyRelatedMessage, repeatIndicator, sourceMmsi);
		this.sequenceNumber = sequenceNumber;
		this.destinationMmsi = destinationMmsi;
		this.retransmit = retransmit;
		this.text = text;
	}

	public final Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public final MMSI getDestinationMmsi() {
		return destinationMmsi;
	}

	public final Boolean getRetransmit() {
		return retransmit;
	}

	public final String getText() {
		return text;
	}
	
	public static AddressedSafetyRelatedMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.AddressedSafetyRelatedMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Integer sequenceNumber = Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		MMSI destinationMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Boolean retransmit = Decoder.convertToBoolean(encodedMessage.getBits(70, 71));
		String text = Decoder.convertToString(encodedMessage.getBits(70, 1009));

		return new AddressedSafetyRelatedMessage(repeatIndicator, sourceMmsi, sequenceNumber, destinationMmsi, retransmit, text);
	}

	private final Integer sequenceNumber;
	private final MMSI destinationMmsi;
	private final Boolean retransmit;
	private final String text;
}
