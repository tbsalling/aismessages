package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.ais.messages.types.ShipType;

@SuppressWarnings("serial")
public class ExtendedClassBEquipmentPositionReport extends DecodedAISMessage {

	public ExtendedClassBEquipmentPositionReport(
			Integer repeatIndicator, MMSI sourceMmsi, String regionalReserved1,
			Float speedOverGround, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer trueHeading,
			Integer second, String regionalReserved2, String shipName,
			ShipType shipType, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort,
			PositionFixingDevice positionFixingDevice, Boolean raimFlag,
			Boolean dataTerminalReady, Boolean assigned) {
		super(AISMessageType.ExtendedClassBEquipmentPositionReport, repeatIndicator, sourceMmsi);
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
	
	public static ExtendedClassBEquipmentPositionReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ExtendedClassBEquipmentPositionReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		String regionalReserved1 = Decoder.convertToBitString(encodedMessage.getBits(38, 46));
		Float speedOverGround = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(46, 55)) / 10);
		Boolean positionAccurate = Decoder.convertToBoolean(encodedMessage.getBits(56, 57));
		Float longitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(57, 85)) / 10000);
		Float latitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(85, 112)) / 10000);
		Float courseOverGround = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(112, 124)) / 10);
		Integer trueHeading = Decoder.convertToUnsignedInteger(encodedMessage.getBits(124, 133));
		Integer second = Decoder.convertToUnsignedInteger(encodedMessage.getBits(133, 139));
		String regionalReserved2 = Decoder.convertToBitString(encodedMessage.getBits(139, 143));
		String shipName = Decoder.convertToString(encodedMessage.getBits(143, 263));
		ShipType shipType = ShipType.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(263, 271)));
		Integer toBow = Decoder.convertToUnsignedInteger(encodedMessage.getBits(271, 280));
		Integer toStern = Decoder.convertToUnsignedInteger(encodedMessage.getBits(280, 289));
		Integer toPort = Decoder.convertToUnsignedInteger(encodedMessage.getBits(289, 295));
		Integer toStarboard = Decoder.convertToUnsignedInteger(encodedMessage.getBits(295, 301));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(301, 305)));
		Boolean raimFlag = Decoder.convertToBoolean(encodedMessage.getBits(305, 306));
		Boolean dataTerminalReady = Decoder.convertToBoolean(encodedMessage.getBits(306, 307));
		Boolean assigned = Decoder.convertToBoolean(encodedMessage.getBits(307, 308));
		
		return new ExtendedClassBEquipmentPositionReport(repeatIndicator,
				sourceMmsi, regionalReserved1, speedOverGround,
				positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, regionalReserved2, shipName, shipType,
				toBow, toStern, toStarboard, toPort, positionFixingDevice,
				raimFlag, dataTerminalReady, assigned);
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
