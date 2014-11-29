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
 * used to broadcast differential corrections for GPS. The data in the payload
 * is intended to be passed directly to GPS receivers capable of accepting such
 * corrections.
 * 
 * @author tbsalling
 * @copyright S-Consult ApS, Denmark. All rights reserved. See license.txt file.	
 * 
 */
@SuppressWarnings("serial")
public class GNSSBinaryBroadcastMessage extends DecodedAISMessage {

	public GNSSBinaryBroadcastMessage(
			Integer repeatIndicator, 
			MMSI sourceMmsi,
			Float latitude, 
			Float longitude, 
			String binaryData,
			Integer broadcastId,
			Integer stationId,
			Float z,
			Integer sequence,
			Integer n,
			Integer health,
			String dgnss,			
			NMEATagBlock nmeaTagBlock
			) {
		super(AISMessageType.GNSSBinaryBroadcastMessage,
				repeatIndicator, 
				sourceMmsi,
				nmeaTagBlock);
		this.latitude = latitude;
		this.longitude = longitude;
		this.binaryData = binaryData;
		this.broadcastId = broadcastId;
		this.stationId = stationId;
		this.z = z;
		this.sequence = sequence;
		this.n = n;
		this.health = health;
		this.dgnss = dgnss;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
	}

	public final String getBinaryData() {
		return binaryData;
	}
	
	public final Integer getBroadcastId() {
		return broadcastId;
	}

	public final Integer getStationId() {
		return stationId;
	}

	public final Float getZ() {
		return z;
	}

	public final Integer getSequence() {
		return sequence;
	}

	public final Integer getN() {
		return n;
	}

	public final Integer getHealth() {
		return health;
	}

	public final String getDgnss() {
		return dgnss;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
		.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
		.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
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
		.append("\"data\"").append(":").append(String.format("\"%s\"", binaryData)).append(",")
		.append("\"broadcastId\"").append(":").append(broadcastId).append(",")
		.append("\"stationId\"").append(":").append(stationId).append(",")
		.append("\"z\"").append(":").append(z).append(",")
		.append("\"sequence\"").append(":").append(sequence).append(",")
		.append("\"n\"").append(":").append(n).append(",")
		.append("\"health\"").append(":").append(health).append(",")
		.append("\"dgnss\"").append(":").append(String.format("\"%s\"", binaryData));
		if (this.getNMEATagBlock() != null) {
			builder.append(",").append(this.getNMEATagBlock().toString());
		}
		builder.append("}");
	return builder.toString();		
	}
	
	public static GNSSBinaryBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.GNSSBinaryBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(40, 58)) / 600.0f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(58, 75)) / 600.0f;
		String binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(80, encodedMessage.getNumberOfBits()));
		Integer broadcastId = null;
		Integer stationId = null;
		Float z = null;
		Integer sequence = null;
		Integer n = null;
		Integer health = null;
		String dgnss = null;
		if (binaryData != null && binaryData.length() > 0) {
			broadcastId = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(80, 86));
			stationId = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(86, 96));
			z = DecoderImpl.convertToFloat(encodedMessage.getBits(96, 109)) / 60.0f;
			sequence = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(109, 112));
			n = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(112, 117));
			health = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(117, 120));
			dgnss = DecoderImpl.convertToBitString(encodedMessage.getBits(120, encodedMessage.getNumberOfBits()));
		}
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new GNSSBinaryBroadcastMessage(
				repeatIndicator, 
				sourceMmsi, 
				latitude, 
				longitude, 
				binaryData,				
				broadcastId,
				stationId,
				z,
				sequence,
				n,
				health,
				dgnss,
				nmeaTagBlock
				);
	}
	
	private final Float latitude;
	private final Float longitude;
	private final String binaryData;
	private final Integer broadcastId;
	private final Integer stationId;
	private final Float z;
	private final Integer sequence;
	private final Integer n;
	private final Integer health;
	private final String dgnss;
}
