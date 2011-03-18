package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public class SafetyRelatedAcknowledge extends DecodedAISMessage {

	public SafetyRelatedAcknowledge(
			Integer repeatIndicator, MMSI sourceMmsi, MMSI mmsi1, MMSI mmsi2,
			MMSI mmsi3, MMSI mmsi4) {
		super(AISMessageType.SafetyRelatedAcknowledge, repeatIndicator, sourceMmsi);
		this.mmsi1 = mmsi1;
		this.mmsi2 = mmsi2;
		this.mmsi3 = mmsi3;
		this.mmsi4 = mmsi4;
	}

	public final MMSI getMmsi1() {
		return mmsi1;
	}

	public final MMSI getMmsi2() {
		return mmsi2;
	}

	public final MMSI getMmsi3() {
		return mmsi3;
	}

	public final MMSI getMmsi4() {
		return mmsi4;
	}

	public static BinaryAcknowledge fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.SafetyRelatedAcknowledge))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		MMSI mmsi1 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		MMSI mmsi2 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(72, 102)));
		MMSI mmsi3 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(104, 134)));
		MMSI mmsi4 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(136, 166)));

		return new BinaryAcknowledge(repeatIndicator, sourceMmsi, mmsi1, mmsi2, mmsi3, mmsi4);
	}
	
	private final MMSI mmsi1;
	private final MMSI mmsi2;
	private final MMSI mmsi3;
	private final MMSI mmsi4;
}
