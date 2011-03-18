package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.PositionFixingDevice;

/**
 * This message is to be used by fixed-location base stations to periodically report a position and time reference.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class BaseStationReport extends DecodedAISMessage {
	
	public BaseStationReport(
			Integer repeatIndicator, MMSI sourceMmsi, Integer year,
			Integer month, Integer day, Integer hour, Integer minute,
			Integer second, Boolean positionAccurate, Float latitude,
			Float longitude, PositionFixingDevice positionFixingDevice,
			Boolean raimFlag) {
		super(AISMessageType.BaseStationReport, repeatIndicator, sourceMmsi);
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
		this.raimFlag = raimFlag;
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
	public final Boolean getRaimFlag() {
		return raimFlag;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseStationReport [year=").append(year)
				.append(", month=").append(month).append(", day=").append(day)
				.append(", hour=").append(hour).append(", minute=")
				.append(minute).append(", second=").append(second)
				.append(", positionAccurate=").append(positionAccurate)
				.append(", latitude=").append(latitude).append(", longitude=")
				.append(longitude).append(", positionFixingDevice=")
				.append(positionFixingDevice).append(", raimFlag=")
				.append(raimFlag).append("]");
		return builder.toString();
	}

	public static BaseStationReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BaseStationReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Integer year = Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 52));
		Integer month = Decoder.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		Integer day = Decoder.convertToUnsignedInteger(encodedMessage.getBits(56, 61));
		Integer hour  = Decoder.convertToUnsignedInteger(encodedMessage.getBits(61, 66));
		Integer minute = Decoder.convertToUnsignedInteger(encodedMessage.getBits(66, 72));
		Integer second = Decoder.convertToUnsignedInteger(encodedMessage.getBits(72, 78));
		Boolean positionAccurate = Decoder.convertToBoolean(encodedMessage.getBits(78, 79));
		Float longitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(79, 107)) / 10000);
		Float latitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(107, 134)) / 10000);
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(134, 138)));
		Boolean raimFlag = Decoder.convertToBoolean(encodedMessage.getBits(148, 149));

		return new BaseStationReport(repeatIndicator, sourceMmsi, year, month,
				day, hour, minute, second, positionAccurate, latitude,
				longitude, positionFixingDevice, raimFlag);
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
	private final Boolean raimFlag;
	//radio
}
