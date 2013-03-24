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
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Integer sequenceNumber = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		MMSI destinationMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Boolean retransmit = DecoderImpl.convertToBoolean(encodedMessage.getBits(70, 71));
		String text = DecoderImpl.convertToString(encodedMessage.getBits(70, 1009));

		return new AddressedSafetyRelatedMessage(repeatIndicator, sourceMmsi, sequenceNumber, destinationMmsi, retransmit, text);
	}

	private final Integer sequenceNumber;
	private final MMSI destinationMmsi;
	private final Boolean retransmit;
	private final String text;
}
