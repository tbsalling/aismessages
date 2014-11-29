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
import dk.tbsalling.aismessages.messages.types.ReportingInterval;
import dk.tbsalling.aismessages.messages.types.ShipType;
import dk.tbsalling.aismessages.messages.types.StationType;
import dk.tbsalling.aismessages.messages.types.TxRxMode;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

/**
 * intended to be broadcast by a competent authority (an AIS network-control
 * base station) to set to set operational parameters for all mobile stations in
 * an AIS coverage region
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class GroupAssignmentCommand extends DecodedAISMessage {
	
	public GroupAssignmentCommand(
			Integer repeatIndicator, 
			MMSI sourceMmsi, 
			Float northEastLatitude,
			Float northEastLongitude, 
			Float southWestLatitude,
			Float southWestLongitude,
			StationType stationType,
			ShipType shipType,
			TxRxMode transmitReceiveMode,
			ReportingInterval reportingInterval,
			Integer quietTime,
			NMEATagBlock nmeaTagBlock
			) {
		super(
				AISMessageType.GroupAssignmentCommand,
				repeatIndicator,
				sourceMmsi,
				nmeaTagBlock
				);
		this.northEastLatitude = northEastLatitude;
		this.northEastLongitude = northEastLongitude;
		this.southWestLatitude = southWestLatitude;
		this.southWestLongitude = southWestLongitude;
		this.stationType = stationType;
		this.shipType = shipType;
		this.transmitReceiveMode = transmitReceiveMode;
		this.reportingInterval = reportingInterval;
		this.quietTime = quietTime;
	}
	public final Float getNorthEastLatitude() {
		return northEastLatitude;
	}
	public final Float getNorthEastLongitude() {
		return northEastLongitude;
	}
	public final Float getSouthWestLatitude() {
		return southWestLatitude;
	}
	public final Float getSouthWestLongitude() {
		return southWestLongitude;
	}
	public final StationType getStationType() {
		return stationType;
	}
	public final ShipType getShipType() {
		return shipType;
	}
	public final TxRxMode getTransmitReceiveMode() {
		return transmitReceiveMode;
	}
	public final ReportingInterval getReportingInterval() {
		return reportingInterval;
	}
	public final Integer getQuietTime() {
		return quietTime;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
		.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
		.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
		.append("\"loc\"").append(":").append("{")
		.append("\"type\"").append(":").append("\"Polygon\"").append(",")
	    .append("\"coordinates\"").append(":").append("[").append("[")
   		 .append("[")
	    .append(southWestLongitude).append(",")
	    .append(northEastLatitude)
	    .append("]").append(",")
	    .append("[")
	    .append(northEastLongitude).append(",")
	    .append(northEastLatitude)
	    .append("]").append(",")
	    .append("[")
	    .append(northEastLongitude).append(",")
	    .append(southWestLatitude)
	    .append("]").append(",")
	    .append("[")
	    .append(southWestLongitude).append(",")
	    .append(southWestLatitude)
	    .append("]").append(",")
	    .append("[")
	    .append(southWestLongitude).append(",")
	    .append(northEastLatitude)
	    .append("]")
	    .append("]").append("]").append("}").append(",")
		.append("\"station\"").append(":").append(String.format("\"%s\"", stationType)).append(",")
		.append("\"cargo\"").append(":").append(String.format("\"%s\"", shipType)).append(",")
		.append("\"tr\"").append(":").append(String.format("\"%s\"", transmitReceiveMode)).append(",")
		.append("\"reporting\"").append(":").append(String.format("\"%s\"", reportingInterval)).append(",")
		.append("\"quiet\"").append(":").append(quietTime);
		if (this.getNMEATagBlock() != null) {
			builder.append(",").append(this.getNMEATagBlock().toString());
		}
		builder.append("}");
	return builder.toString();		
	}
	
	public static GroupAssignmentCommand fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.GroupAssignmentCommand))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		
		Float northEastLatitude = DecoderImpl.convertToFloat(encodedMessage.getBits(58, 75)) / 600.0f;
		Float northEastLongitude = DecoderImpl.convertToFloat(encodedMessage.getBits(40, 58)) / 600.0f;
		Float southWestLatitude = DecoderImpl.convertToFloat(encodedMessage.getBits(93, 110)) / 600.0f;
		Float southWestLongitude = DecoderImpl.convertToFloat(encodedMessage.getBits(75, 93)) / 600.0f;
		StationType stationType = StationType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(110, 114)));
		ShipType shipType = ShipType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(114, 122)));
		TxRxMode transmitReceiveMode = TxRxMode.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(144, 146)));
		ReportingInterval reportingInterval = ReportingInterval.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(146, 150)));
		Integer quietTime = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(150, 154));
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new GroupAssignmentCommand(
				repeatIndicator, 
				sourceMmsi, 
				northEastLatitude,
				northEastLongitude,
				southWestLatitude,
				southWestLongitude,
				stationType, 
				shipType,
				transmitReceiveMode,
				reportingInterval,
				quietTime,
				nmeaTagBlock
				);
	}
	
	private final Float northEastLatitude;
	private final Float northEastLongitude;
	private final Float southWestLatitude;
	private final Float southWestLongitude;
	private final StationType stationType;
	private final ShipType shipType;
	private final TxRxMode transmitReceiveMode;
	private final ReportingInterval reportingInterval;
	private final Integer quietTime;
}
