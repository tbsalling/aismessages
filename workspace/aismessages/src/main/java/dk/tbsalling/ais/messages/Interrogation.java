package dk.tbsalling.ais.messages;

import java.util.logging.Logger;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

/**
 * Used by a base station to query one or two other AIS transceivers for status messages of specified types.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class Interrogation extends DecodedAISMessage {

    private static final Logger log = Logger.getLogger(Interrogation.class.getName());

    public Interrogation(Integer repeatIndicator,
			MMSI sourceMmsi, MMSI mmsi1, Integer type1_1, Integer offset1_1,
			Integer type1_2, Integer offset1_2, MMSI mmsi2, Integer type2_1,
			Integer offset2_1) {
		super(AISMessageType.Interrogation, repeatIndicator, sourceMmsi);
		this.interrogatedMmsi1 = mmsi1;
		this.type1_1 = type1_1;
		this.offset1_1 = offset1_1;
		this.type1_2 = type1_2;
		this.offset1_2 = offset1_2;
		this.interrogatedMmsi2 = mmsi2;
		this.type2_1 = type2_1;
		this.offset2_1 = offset2_1;
	}

	public final MMSI getInterrogatedMmsi1() {
		return interrogatedMmsi1;
	}

	public final Integer getType1_1() {
		return type1_1;
	}

	public final Integer getOffset1_1() {
		return offset1_1;
	}

	public final Integer getType1_2() {
		return type1_2;
	}

	public final Integer getOffset1_2() {
		return offset1_2;
	}

	public final MMSI getInterrogatedMmsi2() {
		return interrogatedMmsi2;
	}

	public final Integer getType2_1() {
		return type2_1;
	}

	public final Integer getOffset2_1() {
		return offset2_1;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Interrogation [interrogatedMmsi1=")
				.append(interrogatedMmsi1).append(", type1_1=").append(type1_1)
				.append(", offset1_1=").append(offset1_1).append(", type1_2=")
				.append(type1_2).append(", offset1_2=").append(offset1_2)
				.append(", interrogatedMmsi2=").append(interrogatedMmsi2)
				.append(", type2_1=").append(type2_1).append(", offset2_1=")
				.append(offset2_1).append("]");
		return builder.toString();
	}

	public static Interrogation fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.Interrogation))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
		
		int messageLength = encodedMessage.getNumberOfBits();
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		MMSI mmsi1 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer type1_1 = Decoder.convertToUnsignedInteger(encodedMessage.getBits(70, 76));
		Integer offset1_1 = Decoder.convertToUnsignedInteger(encodedMessage.getBits(76, 88));
		Integer type1_2 = messageLength > 88 ? Decoder.convertToUnsignedInteger(encodedMessage.getBits(90, 96)) : null;
		Integer offset1_2 = messageLength > 88 ? Decoder.convertToUnsignedInteger(encodedMessage.getBits(96, 108)) : null;
		
		MMSI mmsi2 = messageLength > 160 ? MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(110, 140))) : null;
		Integer type2_1 = messageLength > 160 ? Decoder.convertToUnsignedInteger(encodedMessage.getBits(140, 146)) : null;
		Integer offset2_1 = messageLength > 160 ? Decoder.convertToUnsignedInteger(encodedMessage.getBits(146, 158)) : null;
		
		return new Interrogation(repeatIndicator, sourceMmsi, mmsi1, type1_1, offset1_1, type1_2, offset1_2, mmsi2, type2_1, offset2_1);
	}

	private final MMSI interrogatedMmsi1;
	private final Integer type1_1;
	private final Integer offset1_1;
	private final Integer type1_2;
	private final Integer offset1_2;
	private final MMSI interrogatedMmsi2;
	private final Integer type2_1;
	private final Integer offset2_1;
}
