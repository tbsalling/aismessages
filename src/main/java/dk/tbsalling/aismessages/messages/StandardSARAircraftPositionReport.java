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
public class StandardSARAircraftPositionReport extends DecodedAISMessage {

	public StandardSARAircraftPositionReport(
			Integer repeatIndicator, MMSI sourceMmsi, Integer altitude,
			Integer speed, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer second,
			String regionalReserved, Boolean dataTerminalReady,
			Boolean assigned, Boolean raimFlag, String radioStatus) {
		super(AISMessageType.StandardSARAircraftPositionReport, repeatIndicator, sourceMmsi);
		this.altitude = altitude;
		this.speed = speed;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.second = second;
		this.regionalReserved = regionalReserved;
		this.dataTerminalReady = dataTerminalReady;
		this.assigned = assigned;
		this.raimFlag = raimFlag;
		this.radioStatus = radioStatus;
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

	public final String getRegionalReserved() {
		return regionalReserved;
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

	public final String getRadioStatus() {
		return radioStatus;
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
		 Float longitude = (float) (DecoderImpl.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10000);
		 Float latitude = (float) (DecoderImpl.convertToSignedInteger(encodedMessage.getBits(89, 116)) / 10000);
		 Float courseOverGround = (float) (DecoderImpl.convertToSignedInteger(encodedMessage.getBits(116, 128)) / 10);
		 Integer second = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(128, 134));
		 String regionalReserved = DecoderImpl.convertToBitString(encodedMessage.getBits(134, 142));
		 Boolean dataTerminalReady = DecoderImpl.convertToBoolean(encodedMessage.getBits(142, 143));
		 Boolean assigned = DecoderImpl.convertToBoolean(encodedMessage.getBits(146, 147));
		 Boolean raimFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(147, 148));
		 String radioStatus = DecoderImpl.convertToBitString(encodedMessage.getBits(148, 168));
		
		return new StandardSARAircraftPositionReport(repeatIndicator,
				sourceMmsi, altitude, speed, positionAccurate, latitude,
				longitude, courseOverGround, second, regionalReserved,
				dataTerminalReady, assigned, raimFlag, radioStatus);
	}

	private final Integer altitude;
	private final Integer speed;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer second;
	private final String regionalReserved;
	private final Boolean dataTerminalReady;
	private final Boolean assigned;
	private final Boolean raimFlag;
	private final String radioStatus;
}
