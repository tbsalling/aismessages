package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

/**
 * an addressed point-to-point message with unspecified binary payload. The St.
 * Lawrence Seaway AIS system and the USG PAWSS system use this payload for
 * local extension messages. It is variable in length up to a maximum of 1008
 * bits (up to 5 AIVDM sentence payloads).
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class AddressedBinaryMessage extends DecodedAISMessage {

	public AddressedBinaryMessage(
			Integer repeatIndicator, MMSI sourceMmsi, Integer sequenceNumber,
			MMSI destinationMmsi, Boolean retransmit,
			Integer designatedAreaCode, Integer functionalId, String binaryData) {
		super(AISMessageType.AddressedBinaryMessage, repeatIndicator, sourceMmsi);
		this.sequenceNumber = sequenceNumber;
		this.destinationMmsi = destinationMmsi;
		this.retransmit = retransmit;
		this.designatedAreaCode = designatedAreaCode;
		this.functionalId = functionalId;
		this.binaryData = binaryData;
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

	public final Integer getDesignatedAreaCode() {
		return designatedAreaCode;
	}

	public final Integer getFunctionalId() {
		return functionalId;
	}

	public final String getBinaryData() {
		return binaryData;
	}

	public static AddressedBinaryMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.AddressedBinaryMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer sequenceNumber = Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		MMSI destinationMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Boolean retransmit = Decoder.convertToBoolean(encodedMessage.getBits(70, 71));
		Integer designatedAreaCode = Decoder.convertToUnsignedInteger(encodedMessage.getBits(72, 82));
		Integer functionalId = Decoder.convertToUnsignedInteger(encodedMessage.getBits(82, 88));
		String binaryData = Decoder.convertToBitString(encodedMessage.getBits(88, 1009));

		return new AddressedBinaryMessage(repeatIndicator, sourceMmsi,
				sequenceNumber, destinationMmsi, retransmit,
				designatedAreaCode, functionalId, binaryData);
	}
	
	private final Integer sequenceNumber;
	private final MMSI destinationMmsi;
	private final Boolean retransmit;
	private final Integer designatedAreaCode;
	private final Integer functionalId;
	private final String binaryData;
}
