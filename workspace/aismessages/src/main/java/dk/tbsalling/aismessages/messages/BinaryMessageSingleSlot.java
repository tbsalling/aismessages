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

package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;

@SuppressWarnings("serial")
public class BinaryMessageSingleSlot extends DecodedAISMessage {

	public BinaryMessageSingleSlot(
			Integer repeatIndicator, MMSI sourceMmsi,
			Boolean destinationIndicator, Boolean binaryDataFlag,
			MMSI destinationMMSI, String binaryData) {
		super(AISMessageType.BinaryMessageSingleSlot, repeatIndicator, sourceMmsi);
		this.destinationIndicator = destinationIndicator;
		this.binaryDataFlag = binaryDataFlag;
		this.destinationMMSI = destinationMMSI;
		this.binaryData = binaryData;
	}
	public final Boolean getDestinationIndicator() {
		return destinationIndicator;
	}
	public final Boolean getBinaryDataFlag() {
		return binaryDataFlag;
	}
	public final MMSI getDestinationMMSI() {
		return destinationMMSI;
	}
	public final String getBinaryData() {
		return binaryData;
	}
	
	public static BinaryMessageSingleSlot fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryMessageSingleSlot))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Boolean destinationIndicator = DecoderImpl.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean binaryDataFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(39, 40));
		MMSI destinationMMSI = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		String binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(40, 168));

		return new BinaryMessageSingleSlot(repeatIndicator, sourceMmsi,
				destinationIndicator, binaryDataFlag, destinationMMSI,
				binaryData);
	}

	private final Boolean destinationIndicator;
	private final Boolean binaryDataFlag;
	private final MMSI destinationMMSI;
	private final String binaryData;
}
