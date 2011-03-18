package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

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
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		 Integer altitude = Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 50));
		 Integer speed = Decoder.convertToUnsignedInteger(encodedMessage.getBits(50, 60));
		 Boolean positionAccurate = Decoder.convertToBoolean(encodedMessage.getBits(60, 61));
		 Float longitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10000);
		 Float latitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(89, 116)) / 10000);
		 Float courseOverGround = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(116, 128)) / 10);
		 Integer second = Decoder.convertToUnsignedInteger(encodedMessage.getBits(128, 134));
		 String regionalReserved = Decoder.convertToBitString(encodedMessage.getBits(134, 142));
		 Boolean dataTerminalReady = Decoder.convertToBoolean(encodedMessage.getBits(142, 143));
		 Boolean assigned = Decoder.convertToBoolean(encodedMessage.getBits(146, 147));
		 Boolean raimFlag = Decoder.convertToBoolean(encodedMessage.getBits(147, 148));
		 String radioStatus = Decoder.convertToBitString(encodedMessage.getBits(148, 168));
		
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
