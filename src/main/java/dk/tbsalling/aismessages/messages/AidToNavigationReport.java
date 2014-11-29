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
import dk.tbsalling.aismessages.messages.types.AidType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class AidToNavigationReport extends DecodedAISMessage {

	public AidToNavigationReport(
			Integer repeatIndicator,
			MMSI sourceMmsi,
			AidType aidType,
			String name,
			Boolean positionAccurate,
			Float latitude,
			Float longitude, 
			Integer toBow,
			Integer toStern,
			Integer toStarboard,
			Integer toPort,
			PositionFixingDevice positionFixingDevice,
			Integer second,
			Boolean offPosition,
			Integer regionalUse,
			Boolean raimFlag,
			Boolean virtualAid,
			Boolean assignedMode,
			String nameExtension,
			NMEATagBlock nmeaTagBlock
			) {
		super(
				AISMessageType.AidToNavigationReport, 
				repeatIndicator,
				sourceMmsi, 
				nmeaTagBlock
				);
		this.aidType = aidType;
		this.name = name;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.positionFixingDevice = positionFixingDevice;
		this.second = second;
		this.offPosition = offPosition;
		this.regionalUse = regionalUse;
		this.raimFlag = raimFlag;
		this.virtualAid = virtualAid;
		this.assignedMode = assignedMode;
		this.nameExtension = nameExtension;
	}

	public final AidType getAidType() {
		return aidType;
	}

	public final String getName() {
		return name;
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

	public final Integer getSecond() {
		return second;
	}

	public final Boolean getOffPosition() {
		return offPosition;
	}

	public final Integer getRegionalUse() {
		return regionalUse;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
	}

	public final Boolean getVirtualAid() {
		return virtualAid;
	}

	public final Boolean getAssignedMode() {
		return assignedMode;
	}

	public final String getNameExtension() {
		return nameExtension;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
			.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
			.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
			.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
			.append("\"aid\"").append(":").append(String.format("\"%s\"", aidType.name())).append(",")
			.append("\"name\"").append(":").append(String.format("\"%s\"", StringEscapeUtils.escapeJson(name))).append(",")
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
			.append("\"bow\"").append(":").append(toBow).append(",")
			.append("\"stern\"").append(":").append(toStern).append(",")
			.append("\"port\"").append(":").append(toPort).append(",")
			.append("\"starboard\"").append(":").append(toStarboard).append(",")
			.append("\"device\"").append(":").append(String.format("\"%s\"", positionFixingDevice.name())).append(",")
			.append("\"second\"").append(":").append(second).append(",")
			.append("\"off\"").append(":").append(offPosition.booleanValue() ? "1" : "0").append(",")
			.append("\"regional\"").append(":").append(regionalUse).append(",")
			.append("\"raim\"").append(":").append(raimFlag.booleanValue() ? "1" : "0").append(",")
			.append("\"virtual\"").append(":").append(virtualAid.booleanValue() ? "1" : "0").append(",")
			.append("\"mode\"").append(":").append(assignedMode.booleanValue() ? "1" : "0").append(",")
			.append("\"extension\"").append(":").append(String.format("\"%s\"", StringEscapeUtils.escapeJson(nameExtension)));		
			if (this.getNMEATagBlock() != null) {
				builder.append(",").append(this.getNMEATagBlock().toString());
			}
			builder.append("}");
		return builder.toString();
	}

	public static AidToNavigationReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.AidToNavigationReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
		
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		AidType aidType = AidType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 43)));
		String name = DecoderImpl.convertToString(encodedMessage.getBits(43, 163));
		Boolean positionAccurate = DecoderImpl.convertToBoolean(encodedMessage.getBits(163, 164));
		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(164, 192)) / 600000f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(192, 219)) / 600000f;
		Integer toBow = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(219, 228));
		Integer toStern = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(228, 237));
		Integer toPort = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(237, 243));
		Integer toStarboard = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(243, 249));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(249, 253)));
		Integer second = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(253, 259));
		Boolean offPosition = DecoderImpl.convertToBoolean(encodedMessage.getBits(259, 260));
		Integer regionalUse = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(260,268));
		Boolean raimFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(268, 269));
		Boolean virtualAid = DecoderImpl.convertToBoolean(encodedMessage.getBits(269, 270));
		Boolean assignedMode = DecoderImpl.convertToBoolean(encodedMessage.getBits(270, 271));
		String nameExtension = DecoderImpl.convertToString(encodedMessage.getBits(272, encodedMessage.getNumberOfBits())); //361
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new AidToNavigationReport(
				repeatIndicator,
				sourceMmsi, 
				aidType,
				name, 
				positionAccurate,
				latitude,
				longitude,
				toBow,
				toStern,
				toStarboard,
				toPort,
				positionFixingDevice, 
				second, 
				offPosition,
				regionalUse,
				raimFlag, 
				virtualAid, 
				assignedMode, 
				nameExtension,
				nmeaTagBlock
				);
	}
	
	private final AidType aidType;
	private final String name;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final PositionFixingDevice positionFixingDevice;
	private final Integer second;
	private final Boolean offPosition;
	private final Integer regionalUse;
	private final Boolean raimFlag;
	private final Boolean virtualAid;
	private final Boolean assignedMode;
	private final String nameExtension;
}
