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
 * broadcast message with unspecified binary payload. The St. Lawrence Seaway
 * AIS system and the USG PAWSS system use this payload for local extension
 * messages. It is variable in length up to a maximum of 1008 bits (up to 5
 * AIVDM sentence payloads).
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class BinaryBroadcastMessage extends DecodedAISMessage {

	public BinaryBroadcastMessage(Integer repeatIndicator, MMSI sourceMmsi,
			Integer designatedAreaCode, Integer functionalId, String binaryData) {
		super(AISMessageType.BinaryBroadcastMessage, repeatIndicator,
				sourceMmsi);
		this.designatedAreaCode = designatedAreaCode;
		this.functionalId = functionalId;
		this.binaryData = binaryData;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BinaryBroadcastMessage [designatedAreaCode=")
				.append(designatedAreaCode).append(", functionalId=")
				.append(functionalId).append(", binaryData=")
				.append(binaryData).append("]");
		return builder.toString();
	}

	public static BinaryBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer designatedAreaCode = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 52));
		Integer functionalId = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		String binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(52, 56));
		
		return new BinaryBroadcastMessage(repeatIndicator, sourceMmsi, designatedAreaCode, functionalId, binaryData);
	}

	private final Integer designatedAreaCode;
	private final Integer functionalId;
	private final String binaryData;
}
