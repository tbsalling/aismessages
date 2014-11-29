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

/**
 * 
 */
package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;


/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class LongRangeAISBroadcastMessage extends DecodedAISMessage {
	public LongRangeAISBroadcastMessage(
			AISMessageType messageType,
			Integer repeatIndicator,
			MMSI sourceMmsi,
			Boolean positionAccurate,
			Boolean raimFlag,
			Float longitude,
			Float latitude,
			NavigationStatus navigationStatus, 
			Float speedOverGround,
			Float courseOverGround,
			Boolean positionLatency,			
			NMEATagBlock nmeaTagBlock
			) {
		super(messageType, repeatIndicator, sourceMmsi, nmeaTagBlock);
		this.positionAccurate = positionAccurate;
		this.raimFlag = raimFlag;
		this.navigationStatus = navigationStatus;
		this.longitude = longitude;
		this.latitude = latitude;
		this.speedOverGround = speedOverGround;
		this.courseOverGround = courseOverGround;
		this.positionLatency = positionLatency;
	}		

	public final Boolean getPositionAccurate() {
		return positionAccurate;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
	}
	
	public final NavigationStatus getNavigationStatus() {
		return navigationStatus;
	}
	
	public final Float getLongitude() {
		return longitude;
	}
	
	public final Float getLatitude() {
		return latitude;
	}
	
	public final Float getSpeedOverGround() {
		return speedOverGround;
	}
	
	public final Float getCourseOverGround() {
		return courseOverGround;
	}	

	public final Boolean getPositionLatency() {
		return positionLatency;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
		.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
		.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
		.append("\"accuracy\"").append(":").append(positionAccurate.booleanValue() ? "1" : "0").append(",")
		.append("\"raim\"").append(":").append(raimFlag.booleanValue() ? "1" : "0").append(",")
		.append("\"navigation\"").append(":").append(String.format("\"%s\"", navigationStatus.name())).append(",")
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
		.append("\"sog\"").append(":").append(speedOverGround).append(",")
		.append("\"cog\"").append(":").append(courseOverGround).append(",")
		.append("\"latency\"").append(":").append(positionLatency.booleanValue() ? "1" : "0");
		if (this.getNMEATagBlock() != null) {
			builder.append(",").append(this.getNMEATagBlock().toString());
		}
		builder.append("}");
		return builder.toString();
	}

	public static LongRangeAISBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.LongRangeAISBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Boolean positionAccurate = DecoderImpl.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean raimFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(39, 40));
		NavigationStatus navigationStatus = NavigationStatus.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(40, 44)));
		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(44, 62)) / 600.0f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(62, 79)) / 600.0f;
		Float speedOverGround = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(79, 85));
		Float courseOverGround = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(85, 94));
		Boolean positionLatency = DecoderImpl.convertToBoolean(encodedMessage.getBits(94, 95));
		
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
			
		return new LongRangeAISBroadcastMessage(
				AISMessageType.LongRangeAISBroadcastMessage,
				repeatIndicator,
				sourceMmsi,
				positionAccurate,
				raimFlag,
				longitude, 
				latitude, 
				navigationStatus,
				speedOverGround,
				courseOverGround,
				positionLatency,
				nmeaTagBlock
				);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((courseOverGround == null) ? 0 : courseOverGround.hashCode());
		result = prime * result	+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result	+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result	+ ((navigationStatus == null) ? 0 : navigationStatus.hashCode());
		result = prime * result	+ ((positionAccurate == null) ? 0 : positionAccurate.hashCode());
		result = prime * result	+ ((positionLatency == null) ? 0 : positionLatency.hashCode());
		result = prime * result	+ ((raimFlag == null) ? 0 : raimFlag.hashCode());
		result = prime * result	+ ((speedOverGround == null) ? 0 : speedOverGround.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LongRangeAISBroadcastMessage))
			return false;
		LongRangeAISBroadcastMessage other = (LongRangeAISBroadcastMessage) obj;
		if (courseOverGround == null) {
			if (other.courseOverGround != null)
				return false;
		} else if (!courseOverGround.equals(other.courseOverGround))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (navigationStatus != other.navigationStatus)
			return false;
		if (positionAccurate == null) {
			if (other.positionAccurate != null)
				return false;
		} else if (!positionAccurate.equals(other.positionAccurate))
			return false;		
		if (positionLatency == null) {
			if (other.positionLatency != null)
				return false;
		} else if (!positionLatency.equals(other.positionLatency))
			return false;		
		if (raimFlag == null) {
			if (other.raimFlag != null)
				return false;
		} else if (!raimFlag.equals(other.raimFlag))
			return false;
		if (speedOverGround == null) {
			if (other.speedOverGround != null)
				return false;
		} else if (!speedOverGround.equals(other.speedOverGround))
			return false;

		return true;
	}

	private final Boolean positionAccurate;
	private final Boolean raimFlag;
	private final NavigationStatus navigationStatus;
	private final Float longitude;
	private final Float latitude;
	private final Float speedOverGround;
	private final Float courseOverGround;
	private final Boolean positionLatency;
}
