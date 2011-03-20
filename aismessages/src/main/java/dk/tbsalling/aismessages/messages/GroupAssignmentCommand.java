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
import dk.tbsalling.aismessages.messages.types.ReportingInterval;
import dk.tbsalling.aismessages.messages.types.ShipType;
import dk.tbsalling.aismessages.messages.types.StationType;
import dk.tbsalling.aismessages.messages.types.TxRxMode;

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
			Integer repeatIndicator, MMSI sourceMmsi, Float northEastLatitude,
			Float northEastLongitude, Float southWestLatitude,
			Float southWestLongitude, StationType stationType,
			ShipType shipType, TxRxMode transmitReceiveMode,
			ReportingInterval reportingInterval, Integer quietTime) {
		super(AISMessageType.GroupAssignmentCommand, repeatIndicator, sourceMmsi);
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
	
	public static GroupAssignmentCommand fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.GroupAssignmentCommand))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		
		Float northEastLatitude = (float) (DecoderImpl.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		Float northEastLongitude = (float) (DecoderImpl.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		Float southWestLatitude = (float) (DecoderImpl.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		Float southWestLongitude = (float) (DecoderImpl.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		StationType stationType = StationType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		ShipType shipType = ShipType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		TxRxMode transmitReceiveMode = TxRxMode.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(128, 137)));
		ReportingInterval reportingInterval = ReportingInterval.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Integer quietTime = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(128, 137));
		
		return new GroupAssignmentCommand(repeatIndicator, sourceMmsi, northEastLatitude, northEastLongitude, southWestLatitude, southWestLongitude, stationType, shipType, transmitReceiveMode, reportingInterval, quietTime);
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
