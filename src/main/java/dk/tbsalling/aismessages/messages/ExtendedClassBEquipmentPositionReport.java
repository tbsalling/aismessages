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

import org.apache.commons.lang3.StringEscapeUtils;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

@SuppressWarnings("serial")
public class ExtendedClassBEquipmentPositionReport extends DecodedAISMessage {

	public ExtendedClassBEquipmentPositionReport(
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
			String shipName,
			ShipType shipType,
			Integer toBow, 
			Integer toStern,
			Integer toStarboard,
			Integer toPort,
			PositionFixingDevice positionFixingDevice, 
			Boolean raimFlag,
			Boolean dataTerminalReady, 
			Boolean assigned, 
			NMEATagBlock nmeaTagBlock
			) {
		super(
				AISMessageType.ExtendedClassBEquipmentPositionReport, 
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
		this.shipName = shipName;
		this.shipType = shipType;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.positionFixingDevice = positionFixingDevice;
		this.raimFlag = raimFlag;
		this.dataTerminalReady = dataTerminalReady;
		this.assigned = assigned;
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

	public final String getShipName() {
		return shipName;
	}

	public final ShipType getShipType() {
		return shipType;
	}

	public final Integer getToBow() {
		return toBow;
	}

	public final Integer getToStern() {
		return toStern;
	}

	public final Integer getToStarboard() {
		return toStarboard;
	}

	public final Integer getToPort() {
		return toPort;
	}

	public final PositionFixingDevice getPositionFixingDevice() {
		return positionFixingDevice;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
	}

	public final Boolean getDataTerminalReady() {
		return dataTerminalReady;
	}

	public final Boolean getAssigned() {
		return assigned;
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
			.append("\"name\"").append(":").append(String.format("\"%s\"", StringEscapeUtils.escapeJson(shipName))).append(",")
			.append("\"cargo\"").append(":").append(String.format("\"%s\"", shipType)).append(",")
			.append("\"bow\"").append(":").append(toBow).append(",")
			.append("\"stern\"").append(":").append(toStern).append(",")
			.append("\"port\"").append(":").append(toPort).append(",")
			.append("\"starboard\"").append(":").append(toStarboard).append(",")
			.append("\"device\"").append(":").append(String.format("\"%s\"", positionFixingDevice)).append(",")
			.append("\"raim\"").append(":").append(raimFlag.booleanValue() ? "1" : "0").append(",")
			.append("\"dte\"").append(":").append(dataTerminalReady.booleanValue() ? "1" : "0").append(",")
			.append("\"mode\"").append(":").append(assigned.booleanValue() ? "1" : "0");
			if (this.getNMEATagBlock() != null) {
				builder.append(",").append(this.getNMEATagBlock().toString());
			}
			builder.append("}");
		return builder.toString();
	}

	public static ExtendedClassBEquipmentPositionReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ExtendedClassBEquipmentPositionReport))
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
		String regionalReserved2 = DecoderImpl.convertToBitString(encodedMessage.getBits(139, 143));
		String shipName = DecoderImpl.convertToString(encodedMessage.getBits(143, 263));
		ShipType shipType = ShipType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(263, 271)));
		Integer toBow = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(271, 280));
		Integer toStern = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(280, 289));
		Integer toPort = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(289, 295));
		Integer toStarboard = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(295, 301));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(301, 305)));
		Boolean raimFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(305, 306));
		Boolean dataTerminalReady = DecoderImpl.convertToBoolean(encodedMessage.getBits(306, 307));
		Boolean assigned = DecoderImpl.convertToBoolean(encodedMessage.getBits(307, 308));
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new ExtendedClassBEquipmentPositionReport(
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
				shipName, 
				shipType,
				toBow,
				toStern, 
				toStarboard,
				toPort, 
				positionFixingDevice,
				raimFlag,
				dataTerminalReady,
				assigned, 
				nmeaTagBlock);
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
	private final String shipName;
	private final ShipType shipType;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final PositionFixingDevice positionFixingDevice;
	private final Boolean raimFlag;
	private final Boolean dataTerminalReady;
	private final Boolean assigned;
}
