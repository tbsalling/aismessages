package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.ReportingInterval;
import dk.tbsalling.ais.messages.types.ShipType;
import dk.tbsalling.ais.messages.types.StationType;
import dk.tbsalling.ais.messages.types.TxRxMode;

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
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		
		Float northEastLatitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		Float northEastLongitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		Float southWestLatitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		Float southWestLongitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10);
		StationType stationType = StationType.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		ShipType shipType = ShipType.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		TxRxMode transmitReceiveMode = TxRxMode.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(128, 137)));
		ReportingInterval reportingInterval = ReportingInterval.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Integer quietTime = Decoder.convertToUnsignedInteger(encodedMessage.getBits(128, 137));
		
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
