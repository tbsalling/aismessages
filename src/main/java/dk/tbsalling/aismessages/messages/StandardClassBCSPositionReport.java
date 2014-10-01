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
import dk.tbsalling.aismessages.messages.types.CommunicationState;
import dk.tbsalling.aismessages.messages.types.CommunicationStateType;
import dk.tbsalling.aismessages.messages.types.ITDMA;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.SOTDMA;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

/**
 * A less detailed report than types 1-3 for vessels using Class B transmitters.
 * Omits navigational status and rate of turn.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class StandardClassBCSPositionReport extends DecodedAISMessage {

	public StandardClassBCSPositionReport(
			Integer repeatIndicator,
			MMSI sourceMmsi,
			String regionalReserved1,
			Float speedOverGround, 
			Boolean positionAccurate, 
			Float latitude,
			Float longitude,
			Float courseOverGround,
			Integer trueHeading,
			Integer second,
			String regionalReserved2,
			Boolean csUnit,
			Boolean display, 
			Boolean dsc, 
			Boolean band, 
			Boolean message22,
			Boolean assigned, 
			Boolean raimFlag, 
			CommunicationStateType communicationStateSelector, 
			CommunicationState communicationState,
			NMEATagBlock nmeaTagBlock
			) {
		super(
				AISMessageType.StandardClassBCSPositionReport,
				repeatIndicator, 
				sourceMmsi, 
				nmeaTagBlock
				);
		this.regionalReserved1 = regionalReserved1;
		this.speedOverGround = speedOverGround;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.trueHeading = trueHeading;
		this.second = second;
		this.regionalReserved2 = regionalReserved2;
		this.csUnit = csUnit;
		this.display = display;
		this.dsc = dsc;
		this.band = band;
		this.message22 = message22;
		this.assigned = assigned;
		this.raimFlag = raimFlag;
		this.communicationStateSelector = communicationStateSelector;
		this.communicationState = communicationState;
	}

	public final String getRegionalReserved1() {
		return regionalReserved1;
	}

	public final Float getSpeedOverGround() {
		return speedOverGround;
	}

	public final Boolean getPositionAccurate() {
		return positionAccurate;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
	}

	public final Float getCourseOverGround() {
		return courseOverGround;
	}

	public final Integer getTrueHeading() {
		return trueHeading;
	}

	public final Integer getSecond() {
		return second;
	}

	public final String getRegionalReserved2() {
		return regionalReserved2;
	}

	public final Boolean getCsUnit() {
		return csUnit;
	}

	public final Boolean getDisplay() {
		return display;
	}

	public final Boolean getDsc() {
		return dsc;
	}

	public final Boolean getBand() {
		return band;
	}

	public final Boolean getMessage22() {
		return message22;
	}

	public final Boolean getAssigned() {
		return assigned;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
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
			.append("\"sog\"").append(":").append(speedOverGround).append(",")
			.append("\"accuracy\"").append(":").append(positionAccurate.booleanValue() ? "1" : "0").append(",")
			.append("\"loc\"").append(":");
			if (longitude <= 180.0f && longitude >= -180.0f && latitude <= 90.0f && latitude >= -80.0) {
				builder.append("{")
				.append("\"type\"").append(":").append("\"Point\"").append(",")
			    .append("\"coordinates\"").append(":").append("[")
				.append(longitude).append(",")
				.append(latitude).append("]").append("}");
			} else {
				Float nullFloat = null;				
				builder.append(nullFloat);
			}
			builder.append(",")
			.append("\"cog\"").append(":").append(courseOverGround).append(",")
			.append("\"heading\"").append(":").append(trueHeading).append(",")
			.append("\"second\"").append(":").append(second).append(",")
			.append("\"unit\"").append(":").append(csUnit.booleanValue() ? "1" : "0").append(",")
			.append("\"display\"").append(":").append(display.booleanValue() ? "1" : "0").append(",")
			.append("\"dsc\"").append(":").append(dsc.booleanValue() ? "1" : "0").append(",")
			.append("\"band\"").append(":").append(band.booleanValue() ? "1" : "0").append(",")
			.append("\"message22\"").append(":").append(message22.booleanValue() ? "1" : "0").append(",")
			.append("\"mode\"").append(":").append(assigned.booleanValue() ? "1" : "0").append(",")
			.append("\"raim\"").append(":").append(raimFlag.booleanValue() ? "1" : "0").append(",")
			.append("\"selector\"").append(":").append(String.format("\"%s\"", communicationStateSelector)).append(",")
			.append("\"communication\"").append(":").append(communicationState);
			if (this.getNMEATagBlock() != null) {
				builder.append(",").append(this.getNMEATagBlock().toString());
			}
			builder.append("}");
		return builder.toString();
	}
	
	public static StandardClassBCSPositionReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.StandardClassBCSPositionReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		String regionalReserved1 = DecoderImpl.convertToBitString(encodedMessage.getBits(38, 46));
		Float speedOverGround = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(46, 55)) / 10f;
		Boolean positionAccurate = DecoderImpl.convertToBoolean(encodedMessage.getBits(56, 57));
		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(57, 85)) / 600000f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(85, 112)) / 600000f;
		Float courseOverGround = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(112, 124)) / 10f;
		Integer trueHeading = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(124, 133));
		Integer second = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(133, 139));
		String regionalReserved2 = DecoderImpl.convertToBitString(encodedMessage.getBits(139, 141));
		Boolean csUnit = DecoderImpl.convertToBoolean(encodedMessage.getBits(141, 142));
		Boolean display = DecoderImpl.convertToBoolean(encodedMessage.getBits(142, 143));
		Boolean dsc = DecoderImpl.convertToBoolean(encodedMessage.getBits(143, 144));
		Boolean band = DecoderImpl.convertToBoolean(encodedMessage.getBits(144, 145));
		Boolean message22 = DecoderImpl.convertToBoolean(encodedMessage.getBits(145, 146));
		Boolean assigned = DecoderImpl.convertToBoolean(encodedMessage.getBits(146, 147));
		Boolean raimFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(147, 148));
		CommunicationStateType communicationStateSelector = CommunicationStateType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(148, 149)));		
		CommunicationState communicationState = null;
		switch (communicationStateSelector) {
		case SOTDMA:
			communicationState = SOTDMA.fromEncodedString(encodedMessage.getBits(149, 168));
			break;
		case ITDMA:
			communicationState = ITDMA.fromEncodedString(encodedMessage.getBits(149, 168));
			break;
		default:
			throw new UnsupportedCommunicationStateType(communicationStateSelector.getCode());
		}
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();

		return new StandardClassBCSPositionReport(
				repeatIndicator, 
				sourceMmsi,
				regionalReserved1, 
				speedOverGround,
				positionAccurate,
				latitude,
				longitude,
				courseOverGround,
				trueHeading,
				second,
				regionalReserved2,
				csUnit, 
				display, 
				dsc, 
				band, 
				message22,
				assigned,
				raimFlag,
				communicationStateSelector,
				communicationState, 
				nmeaTagBlock
				);
	}
	
	private final String regionalReserved1;
	private final Float speedOverGround;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer trueHeading;
	private final Integer second;
	private final String regionalReserved2;
	private final Boolean csUnit;
	private final Boolean display;
	private final Boolean dsc;
	private final Boolean band;
	private final Boolean message22;
	private final Boolean assigned;
	private final Boolean raimFlag;
	private final CommunicationStateType communicationStateSelector;
	private final CommunicationState communicationState;
}
