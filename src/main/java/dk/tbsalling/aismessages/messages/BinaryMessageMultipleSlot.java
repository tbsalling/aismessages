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
import dk.tbsalling.aismessages.exceptions.UnsupportedCommunicationStateType;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.ApplicationIdentifier;
import dk.tbsalling.aismessages.messages.types.CommunicationState;
import dk.tbsalling.aismessages.messages.types.CommunicationStateType;
import dk.tbsalling.aismessages.messages.types.ITDMA;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.SOTDMA;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

@SuppressWarnings("serial")
public class BinaryMessageMultipleSlot extends DecodedAISMessage {

	public BinaryMessageMultipleSlot(
			Integer repeatIndicator, MMSI sourceMmsi, Boolean addressed,
			Boolean structured, MMSI destinationMmsi, ApplicationIdentifier applicationIdentifier,
			String data, CommunicationStateType communicationStateSelector, CommunicationState communicationState, NMEATagBlock nmeaTagBlock) {
		super(AISMessageType.BinaryMessageMultipleSlot, repeatIndicator, sourceMmsi, nmeaTagBlock);
		this.addressed = addressed;
		this.structured = structured;
		this.destinationMmsi = destinationMmsi;
		this.applicationIdentifier = applicationIdentifier;
		this.data = data;
		this.communicationStateSelector = communicationStateSelector;
		this.communicationState = communicationState;
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
	public final ApplicationIdentifier getApplicationIdentifier() {
		return applicationIdentifier;
	}
	public final String getData() {
		return data;
	}
	public final CommunicationStateType getCommunicationStateSelector() {
		return communicationStateSelector;
	}
	public final CommunicationState getCommunicationState() {
		return communicationState;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
		.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
		.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
		.append("\"indicator\"").append(":").append(addressed.booleanValue() ? "1" : "0").append(",")
		.append("\"binary\"").append(":").append(structured.booleanValue() ? "1" : "0").append(",")
		.append("\"destination\"").append(":").append(destinationMmsi == null ? null : String.format("\"%s\"", destinationMmsi.getMMSI())).append(",")
		.append("\"application\"").append(":").append(applicationIdentifier).append(",")
		.append("\"data\"").append(":").append(String.format("\"%s\"", data)).append(",")
		.append("\"selector\"").append(":").append(String.format("\"%s\"", communicationStateSelector)).append(",")
		.append("\"communication\"").append(":").append(communicationState);
		if (this.getNMEATagBlock() != null) {
			builder.append(",").append(this.getNMEATagBlock().toString());
		}
		builder.append("}");
		return builder.toString();		
	}
	
	public static BinaryMessageMultipleSlot fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryMessageMultipleSlot))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		int endBinary = encodedMessage.getNumberOfBits() - 20;
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Boolean addressed = DecoderImpl.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean structured = DecoderImpl.convertToBoolean(encodedMessage.getBits(39, 40));
		
		MMSI destinationMmsi = null;
		String binaryData = null;
		ApplicationIdentifier applicationIdentifier = null;
		
		if (addressed) {
			destinationMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
			if (structured) {
				applicationIdentifier = ApplicationIdentifier.fromEncodedString(encodedMessage.getBits(72, 88));//DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(40, 56));
				binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(88, endBinary));
			}
		}
		else {
			if (structured) {
				applicationIdentifier = ApplicationIdentifier.fromEncodedString(encodedMessage.getBits(40, 56));//DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(40, 56));
				binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(56, endBinary));
			}
			else
				binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(40, endBinary));
		}
		
		CommunicationStateType communicationStateSelector = CommunicationStateType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(endBinary, endBinary + 1)));		
		CommunicationState communicationState = null;
		switch (communicationStateSelector) {
		case SOTDMA:
			communicationState = SOTDMA.fromEncodedString(encodedMessage.getBits(endBinary + 1, encodedMessage.getNumberOfBits()));
			break;
		case ITDMA:
			communicationState = ITDMA.fromEncodedString(encodedMessage.getBits(endBinary + 1, encodedMessage.getNumberOfBits()));
			break;
		default:
			throw new UnsupportedCommunicationStateType(communicationStateSelector.getCode());
		}
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new BinaryMessageMultipleSlot(repeatIndicator, sourceMmsi, addressed, structured, destinationMmsi, applicationIdentifier, binaryData, communicationStateSelector, communicationState, nmeaTagBlock);
	}
	
	private final Boolean addressed;
	private final Boolean structured;
	private final MMSI destinationMmsi;
	private final ApplicationIdentifier applicationIdentifier;
	private final String data;
	private final CommunicationStateType communicationStateSelector;
	private final CommunicationState communicationState;
}
