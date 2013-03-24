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
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer sequenceNumber = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		MMSI destinationMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Boolean retransmit = DecoderImpl.convertToBoolean(encodedMessage.getBits(70, 71));
		Integer designatedAreaCode = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(72, 82));
		Integer functionalId = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(82, 88));
		String binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(88, 1009));

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
