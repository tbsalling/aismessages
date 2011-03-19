/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.internal.DecoderImpl;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public class BinaryMessageMultipleSlot extends DecodedAISMessage {

	public BinaryMessageMultipleSlot(
			Integer repeatIndicator, MMSI sourceMmsi, Boolean addressed,
			Boolean structured, MMSI destinationMmsi, Integer applicationId,
			String data, String radioStatus) {
		super(AISMessageType.BinaryMessageMultipleSlot, repeatIndicator, sourceMmsi);
		this.addressed = addressed;
		this.structured = structured;
		this.destinationMmsi = destinationMmsi;
		this.applicationId = applicationId;
		this.data = data;
		this.radioStatus = radioStatus;
	}
	
	public final Boolean getAddressed() {
		return addressed;
	}
	public final Boolean getStructured() {
		return structured;
	}
	public final MMSI getDestinationMmsi() {
		return destinationMmsi;
	}
	public final Integer getApplicationId() {
		return applicationId;
	}
	public final String getData() {
		return data;
	}
	public final String getRadioStatus() {
		return radioStatus;
	}
	
	public static BinaryMessageMultipleSlot fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryMessageMultipleSlot))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Boolean addressed = DecoderImpl.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean structured = DecoderImpl.convertToBoolean(encodedMessage.getBits(39, 40));
		MMSI destinationMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer applicationId = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(70, 86));
		String data = DecoderImpl.convertToBitString(encodedMessage.getBits(86, 86+1004+1));
		String radioStatus = null; // Decoder.convertToBinaryString(encodedMessage.getBits(6, 8));

		return new BinaryMessageMultipleSlot(repeatIndicator, sourceMmsi,
				addressed, structured, destinationMmsi, applicationId, data, radioStatus);
	}
	
	private final Boolean addressed;
	private final Boolean structured;
	private final MMSI destinationMmsi;
	private final Integer applicationId;
	private final String data;
	private final String radioStatus;
}
