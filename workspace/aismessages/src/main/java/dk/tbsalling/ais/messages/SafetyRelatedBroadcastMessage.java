package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public class SafetyRelatedBroadcastMessage extends DecodedAISMessage {
	
	public SafetyRelatedBroadcastMessage(Integer repeatIndicator, MMSI sourceMmsi, String text) {
		super(AISMessageType.SafetyRelatedBroadcastMessage, repeatIndicator, sourceMmsi);
		this.text = text;
	}

	public final String getText() {
		return text;
	}

	public static SafetyRelatedBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.SafetyRelatedBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		String text = Decoder.convertToString(encodedMessage.getBits(40, 1049));

		return new SafetyRelatedBroadcastMessage(repeatIndicator, sourceMmsi, text);
	}

	private final String text;
}
