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
import dk.tbsalling.aismessages.messages.types.CommunicationState;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.messages.types.SOTDMA;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

/**
 * This message is to be used by fixed-location base stations to periodically report a position and time reference.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class BaseStationReport extends DecodedAISMessage {
	
	public BaseStationReport(
			Integer repeatIndicator,
			MMSI sourceMmsi,
			Integer year,
			Integer month,
			Integer day,
			Integer hour,
			Integer minute,
			Integer second,
			Boolean positionAccurate,
			Float latitude,
			Float longitude,
			PositionFixingDevice positionFixingDevice,
			Boolean transmission,
			Boolean raimFlag,
			CommunicationState communicationState,
			NMEATagBlock nmeaTagBlock
			) {
		super(AISMessageType.BaseStationReport, repeatIndicator, sourceMmsi, nmeaTagBlock);
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.positionFixingDevice = positionFixingDevice;
		this.transmission = transmission;
		this.raimFlag = raimFlag;
		this.communicationState = communicationState;
	}
	public final Integer getYear() {
		return year;
	}
	public final Integer getMonth() {
		return month;
	}
	public final Integer getDay() {
		return day;
	}
	public final Integer getHour() {
		return hour;
	}
	public final Integer getMinute() {
		return minute;
	}
	public final Integer getSecond() {
		return second;
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
	public final PositionFixingDevice getPositionFixingDevice() {
		return positionFixingDevice;
	}
	public final Boolean getTransmissionControl() {
		return transmission;
	}
	public final Boolean getRaimFlag() {
		return raimFlag;
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
		.append("\"year\"").append(":").append(year).append(",")
		.append("\"month\"").append(":").append(month).append(",")
		.append("\"day\"").append(":").append(day).append(",")
		.append("\"hour\"").append(":").append(hour).append(",")
		.append("\"minute\"").append(":").append(minute).append(",")
		.append("\"second\"").append(":").append(second).append(",")
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
		.append("\"device\"").append(":").append(String.format("\"%s\"", positionFixingDevice)).append(",")
		.append("\"transmission\"").append(":").append(transmission.booleanValue() ? "1" : "0").append(",")
		.append("\"raim\"").append(":").append(raimFlag.booleanValue() ? "1" : "0").append(",")
		.append("\"communication\"").append(":").append(communicationState);
		if (this.getNMEATagBlock() != null) {
			builder.append(",").append(this.getNMEATagBlock().toString());
		}
		builder.append("}");
		return builder.toString();		
	}

	public static BaseStationReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BaseStationReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Integer year = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 52));
		Integer month = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		Integer day = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(56, 61));
		Integer hour  = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(61, 66));
		Integer minute = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(66, 72));
		Integer second = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(72, 78));
		Boolean positionAccurate = DecoderImpl.convertToBoolean(encodedMessage.getBits(78, 79));
		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(79, 107)) / 600000f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(107, 134)) / 600000f;
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(134, 138)));
		Boolean transmission = DecoderImpl.convertToBoolean(encodedMessage.getBits(138, 139));
		Boolean raimFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(148, 149));
		CommunicationState communicationState = SOTDMA.fromEncodedString(encodedMessage.getBits(149, 168));
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new BaseStationReport(
				repeatIndicator,
				sourceMmsi,
				year,
				month,
				day,
				hour,
				minute,
				second,
				positionAccurate,
				latitude,
				longitude,
				positionFixingDevice,
				transmission,
				raimFlag,
				communicationState,
				nmeaTagBlock
				);
	}

	private final Integer year;
	private final Integer month;
	private final Integer day;
	private final Integer hour;
	private final Integer minute;
	private final Integer second;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final PositionFixingDevice positionFixingDevice;
	private final Boolean transmission;
	private final Boolean raimFlag;
	private final CommunicationState communicationState;
}
