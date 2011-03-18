package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public class UTCAndDateInquiry extends DecodedAISMessage {

	public UTCAndDateInquiry(
			Integer repeatIndicator, MMSI sourceMmsi, MMSI destinationMmsi) {
		super(AISMessageType.UTCAndDateInquiry, repeatIndicator, sourceMmsi);
		this.destinationMmsi = destinationMmsi;
	}

	public final MMSI getDestinationMmsi() {
		return destinationMmsi;
	}

	public static UTCAndDateInquiry fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.UTCAndDateInquiry))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		MMSI destinationMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));

		return new UTCAndDateInquiry(repeatIndicator, sourceMmsi, destinationMmsi);
	}
	
	private MMSI destinationMmsi;
}
