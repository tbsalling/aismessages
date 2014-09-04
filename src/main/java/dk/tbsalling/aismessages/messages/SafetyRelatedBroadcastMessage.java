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

import dk.tbsalling.aismessages.decoder.Decoder;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;

@SuppressWarnings("serial")
public class SafetyRelatedBroadcastMessage extends DecodedAISMessage {
	
	public SafetyRelatedBroadcastMessage(Integer repeatIndicator, MMSI sourceMmsi, int spare, String text) {
		super(AISMessageType.SafetyRelatedBroadcastMessage, repeatIndicator, sourceMmsi);
		this.spare = spare;
		this.text = text;
	}

    @SuppressWarnings("unused")
	public final int getSpare() {
		return spare;
	}

    @SuppressWarnings("unused")
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
		
		int spare = Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		int extraBitsOfChars = ((encodedMessage.getNumberOfBits() - 40) / 6) * 6;
		String text = Decoder.convertToString(encodedMessage.getBits(40, 40 + extraBitsOfChars));

		return new SafetyRelatedBroadcastMessage(repeatIndicator, sourceMmsi, spare, text);
	}

	private final int spare;
	private final String text;
}
