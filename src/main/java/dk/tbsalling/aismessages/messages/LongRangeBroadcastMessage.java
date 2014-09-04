package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.decoder.Decoder;
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

    @SuppressWarnings("unused")
	public final boolean getAccuracy() {
		return accuracy;
	}

    @SuppressWarnings("unused")
    public final boolean getRaim() {
		return raim;
	}

    @SuppressWarnings("unused")
	public final int getStatus() {
		return status;
	}

    @SuppressWarnings("unused")
	public final float getLongtitude() {
		return longitude;
	}

    @SuppressWarnings("unused")
	public final float getLatitude() {
		return latitude;
	}

    @SuppressWarnings("unused")
	public final int getSpeed() {
		return speed;
	}

    @SuppressWarnings("unused")
	public final int getCourse() {
		return course;
	}

    @SuppressWarnings("unused")
	public final boolean getGnss() {
		return gnss;
	}

    @SuppressWarnings("unused")
	public final int getSpare() {
		return spare;
	}
	
	public static LongRangeBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.LongRangeBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		
		boolean accuracy = Decoder.convertToBoolean(encodedMessage.getBits(38, 39));
		boolean raim = Decoder.convertToBoolean(encodedMessage.getBits(39, 40));
		int status = Decoder.convertToUnsignedInteger(encodedMessage.getBits(40, 44));
		float longitude = Decoder.convertToFloat(encodedMessage.getBits(44, 62)) / 600F;
		float latitude = Decoder.convertToFloat(encodedMessage.getBits(62, 79)) / 600F;
		int speed = Decoder.convertToUnsignedInteger(encodedMessage.getBits(79, 85));
		int course = Decoder.convertToUnsignedInteger(encodedMessage.getBits(85, 94));
		boolean gnss = Decoder.convertToBoolean(encodedMessage.getBits(94, 95));
		int spare = Decoder.convertToUnsignedInteger(encodedMessage.getBits(95, 96));
		
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
