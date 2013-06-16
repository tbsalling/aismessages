package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;

@SuppressWarnings("serial")
public class LongRangeBroadcastMessage extends DecodedAISMessage {

	protected LongRangeBroadcastMessage(
			Integer repeatIndicator, MMSI sourceMmsi, boolean accuracy, boolean raim, int status,
			float longitude, float latitude, int speed, int course, boolean gnss, int spare) {
		super(AISMessageType.LongRangeBroadcastMessage, repeatIndicator, sourceMmsi);
		
		this.accuracy = accuracy;
		this.raim = raim;
		this.status = status;
		this.longitude = longitude;
		this.latitude = latitude;
		this.speed = speed;
		this.course = course;
		this.gnss = gnss;
		this.spare = spare;
	}
	
	public final boolean getAccuracy() {
		return accuracy;
	}
	
	public final boolean getRaim() {
		return raim;
	}
	
	public final int getStatus() {
		return status;
	}
	
	public final float getLongtitude() {
		return longitude;
	}
	
	public final float getLatitude() {
		return latitude;
	}
	
	public final int getSpeed() {
		return speed;
	}
	
	public final int getCourse() {
		return course;
	}
	
	public final boolean getGnss() {
		return gnss;
	}
	
	public final int getSpare() {
		return spare;
	}
	
	public static LongRangeBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.LongRangeBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		
		boolean accuracy = DecoderImpl.convertToBoolean(encodedMessage.getBits(38, 39));
		boolean raim = DecoderImpl.convertToBoolean(encodedMessage.getBits(39, 40));
		int status = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(40, 44));
		float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(44, 62)) / 600F;
		float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(62, 79)) / 600F;
		int speed = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(79, 85));
		int course = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(85, 94));
		boolean gnss = DecoderImpl.convertToBoolean(encodedMessage.getBits(94, 95));
		int spare = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(95, 96));
		
		return new LongRangeBroadcastMessage(repeatIndicator, sourceMmsi, accuracy, raim, status, longitude, latitude, speed, course, gnss, spare);
	}

	
	private final boolean accuracy;
	private final boolean raim;
	private final int status;
	private final float longitude;
	private final float latitude;
	private final int speed;
	private final int course;
	private final boolean gnss;
	private final int spare;
}
