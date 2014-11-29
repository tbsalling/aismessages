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

@SuppressWarnings("serial")
public class StandardSARAircraftPositionReport extends DecodedAISMessage {

	public StandardSARAircraftPositionReport(
			Integer repeatIndicator,
			MMSI sourceMmsi,
			Integer altitude,
			Integer speed,
			Boolean positionAccurate,
			Float latitude,
			Float longitude,
			Float courseOverGround,
			Integer second,
			Boolean altitudeSensor,
			Boolean dataTerminalReady,
			Boolean assigned,
			Boolean raimFlag,
			CommunicationStateType communicationStateSelector,
			CommunicationState communicationState,
			NMEATagBlock nmeaTagBlock
			) {
		super(
				AISMessageType.StandardSARAircraftPositionReport, 
				repeatIndicator, 
				sourceMmsi, 
				nmeaTagBlock
				);
		this.altitude = altitude;
		this.speed = speed;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.second = second;
		this.altitudeSensor = altitudeSensor;
		this.dataTerminalReady = dataTerminalReady;
		this.assigned = assigned;
		this.raimFlag = raimFlag;
		this.communicationStateSelector = communicationStateSelector;
		this.communicationState = communicationState;
	}

	public final Integer getAltitude() {
		return altitude;
	}

	public final Integer getSpeed() {
		return speed;
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

	public final Integer getSecond() {
		return second;
	}

	public final Boolean getAltitudeSensor() {
		return altitudeSensor;
	}

	public final Boolean getDataTerminalReady() {
		return dataTerminalReady;
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
			.append("\"altitude\"").append(":").append(altitude).append(",")
			.append("\"sog\"").append(":").append(speed).append(",")
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
			.append("\"second\"").append(":").append(second).append(",")
			.append("\"sensor\"").append(":").append(altitudeSensor.booleanValue() ? "1" : "0").append(",")
			.append("\"dte\"").append(":").append(dataTerminalReady.booleanValue() ? "1" : "0").append(",")
			.append("\"assigned\"").append(":").append(assigned.booleanValue() ? "1" : "0").append(",")
			.append("\"raim\"").append(":").append(raimFlag.booleanValue() ? "1" : "0").append(",")
			.append("\"selector\"").append(":").append(String.format("\"%s\"", communicationStateSelector)).append(",")
			.append("\"communication\"").append(":").append(communicationState);
			if (this.getNMEATagBlock() != null) {
				builder.append(",").append(this.getNMEATagBlock().toString());
			}
			builder.append("}");
		return builder.toString();
	}

	public static StandardSARAircraftPositionReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.StandardSARAircraftPositionReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		 Integer altitude = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 50));
		 Integer speed = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(50, 60));
		 Boolean positionAccurate = DecoderImpl.convertToBoolean(encodedMessage.getBits(60, 61));
		 Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(61, 89)) / 600000f;
		 Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(89, 116)) / 600000f;
		 Float courseOverGround = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(116, 128)) / 10f;
		 Integer second = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(128, 134));
		 Boolean altitudeSensor = DecoderImpl.convertToBoolean(encodedMessage.getBits(134, 135));
		 Boolean dataTerminalReady = DecoderImpl.convertToBoolean(encodedMessage.getBits(142, 143));
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
		
		return new StandardSARAircraftPositionReport(repeatIndicator,
				sourceMmsi, altitude, speed, positionAccurate, latitude,
				longitude, courseOverGround, second, altitudeSensor,
				dataTerminalReady, assigned, raimFlag, communicationStateSelector, communicationState, nmeaTagBlock);
	}

	private final Integer altitude;
	private final Integer speed;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer second;
	private final Boolean altitudeSensor;
	private final Boolean dataTerminalReady;
	private final Boolean assigned;
	private final Boolean raimFlag;
	private final CommunicationStateType communicationStateSelector;
	private final CommunicationState communicationState;
}
