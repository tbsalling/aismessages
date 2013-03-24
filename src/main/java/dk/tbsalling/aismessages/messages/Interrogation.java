/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;

/**
 * Used by a base station to query one or two other AIS transceivers for status messages of specified types.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class Interrogation extends DecodedAISMessage {

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
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		MMSI mmsi1 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer type1_1 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(70, 76));
		Integer offset1_1 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(76, 88));
		Integer type1_2 = messageLength > 88 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(90, 96)) : null;
		Integer offset1_2 = messageLength > 88 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(96, 108)) : null;
		
		MMSI mmsi2 = messageLength > 160 ? MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(110, 140))) : null;
		Integer type2_1 = messageLength > 160 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(140, 146)) : null;
		Integer offset2_1 = messageLength > 160 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(146, 158)) : null;
		
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
