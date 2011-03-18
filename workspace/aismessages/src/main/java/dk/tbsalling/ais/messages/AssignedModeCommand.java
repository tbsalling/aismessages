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

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

/**
 * used by a base station with control authority to configure the scheduling of
 * AIS informational messages from subordinate stations, either as a frequency
 * per 10-minute interval or by specifying the TDMA slot(s) offset on which
 * those messages should be transmitted.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class AssignedModeCommand extends DecodedAISMessage {

	public AssignedModeCommand(
			Integer repeatIndicator, MMSI sourceMmsi, MMSI destinationMmsiA,
			Integer offsetA, Integer incrementA, MMSI destinationMmsiB,
			Integer offsetB, Integer incrementB) {
		super(AISMessageType.AssignedModeCommand, repeatIndicator, sourceMmsi);
		this.destinationMmsiA = destinationMmsiA;
		this.offsetA = offsetA;
		this.incrementA = incrementA;
		this.destinationMmsiB = destinationMmsiB;
		this.offsetB = offsetB;
		this.incrementB = incrementB;
	}

	public final MMSI getDestinationMmsiA() {
		return destinationMmsiA;
	}

	public final Integer getOffsetA() {
		return offsetA;
	}

	public final Integer getIncrementA() {
		return incrementA;
	}

	public final MMSI getDestinationMmsiB() {
		return destinationMmsiB;
	}

	public final Integer getOffsetB() {
		return offsetB;
	}

	public final Integer getIncrementB() {
		return incrementB;
	}
	
	public static AssignedModeCommand fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.AssignedModeCommand))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
		
		int messageLength = encodedMessage.getNumberOfBits();
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		MMSI destinationMmsiA = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer offsetA = Decoder.convertToUnsignedInteger(encodedMessage.getBits(70, 82));
		Integer incrementA = Decoder.convertToUnsignedInteger(encodedMessage.getBits(82, 92));
		
		MMSI destinationMmsiB = messageLength >= 144 ? MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(92, 122))) : null;
		Integer offsetB = messageLength >= 144 ? Decoder.convertToUnsignedInteger(encodedMessage.getBits(122, 134)) : null;
		Integer incrementB = messageLength >= 144 ? Decoder.convertToUnsignedInteger(encodedMessage.getBits(134, 144)) : null;
		
		return new AssignedModeCommand(repeatIndicator, sourceMmsi, destinationMmsiA, offsetA, incrementA, destinationMmsiB, offsetB, incrementB);
	}
	
	private final MMSI destinationMmsiA;
	private final Integer offsetA;
	private final Integer incrementA;
	private final MMSI destinationMmsiB;
	private final Integer offsetB;
	private final Integer incrementB;
}
