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
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

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
			Integer repeatIndicator,
			MMSI sourceMmsi,
			MMSI destinationMmsiA,
			Integer offsetA,
			Integer incrementA,
			MMSI destinationMmsiB,
			Integer offsetB,
			Integer incrementB,
			NMEATagBlock nmeaTagBloc
			) {
		super(AISMessageType.AssignedModeCommand,
				repeatIndicator,
				sourceMmsi,
				nmeaTagBloc
				);
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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
			.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
			.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
			.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
			.append("\"destinationA\"").append(":").append(destinationMmsiA).append(",")
			.append("\"offsetA\"").append(":").append(offsetA).append(",")
			.append("\"incrementA\"").append(":").append(incrementA).append(",")
			.append("\"destinationB\"").append(":").append(destinationMmsiB).append(",")
			.append("\"offsetB\"").append(":").append(offsetB).append(",")
			.append("\"incrementB\"").append(":").append(incrementB);
			if (this.getNMEATagBlock() != null) {
				builder.append(",").append(this.getNMEATagBlock().toString());
			}
			builder.append("}");
		return builder.toString();
	}
	
	public static AssignedModeCommand fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.AssignedModeCommand))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
		
		int messageLength = encodedMessage.getNumberOfBits();
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		MMSI destinationMmsiA = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer offsetA = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(70, 82));
		Integer incrementA = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(82, 92));
		
		MMSI destinationMmsiB = messageLength >= 144 ? MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(92, 122))) : null;
		Integer offsetB = messageLength >= 144 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(122, 134)) : null;
		Integer incrementB = messageLength >= 144 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(134, 144)) : null;
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new AssignedModeCommand(
				repeatIndicator, 
				sourceMmsi,
				destinationMmsiA,
				offsetA,
				incrementA,
				destinationMmsiB,
				offsetB, 
				incrementB,
				nmeaTagBlock
				);
	}
	
	private final MMSI destinationMmsiA;
	private final Integer offsetA;
	private final Integer incrementA;
	private final MMSI destinationMmsiB;
	private final Integer offsetB;
	private final Integer incrementB;
}
