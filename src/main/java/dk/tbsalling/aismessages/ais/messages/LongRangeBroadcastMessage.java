package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class LongRangeBroadcastMessage extends AISMessage {

    public LongRangeBroadcastMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected LongRangeBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public AISMessageType getMessageType() {
        return AISMessageType.LongRangeBroadcastMessage;
    }

    @SuppressWarnings("unused")
	public Boolean getAccuracy() {
        if (accuracy == null) {
            accuracy = BOOLEAN_DECODER.apply(getBits(38, 39));
        }
        return accuracy;
	}

    @SuppressWarnings("unused")
    public Boolean getRaim() {
        if (raim == null) {
            raim = BOOLEAN_DECODER.apply(getBits(39, 40));
        }
		return raim;
	}

    @SuppressWarnings("unused")
	public Integer getStatus() {
        if (status == null) {
            status = UNSIGNED_INTEGER_DECODER.apply(getBits(40, 44));
        }
		return status;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        if (longitude == null) {
            longitude = FLOAT_DECODER.apply(getBits(44, 62)) / 600f;
        }
		return longitude;
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        if (latitude == null) {
            latitude = FLOAT_DECODER.apply(getBits(62, 79)) / 600f;
        }
		return latitude;
	}

    @SuppressWarnings("unused")
	public Integer getSpeed() {
        if (speed == null) {
            speed = UNSIGNED_INTEGER_DECODER.apply(getBits(79, 85));
        }
		return speed;
	}

    @SuppressWarnings("unused")
	public Integer getCourse() {
        if (course == null) {
            course = UNSIGNED_INTEGER_DECODER.apply(getBits(85, 94));
        }
		return course;
	}

    @SuppressWarnings("unused")
	public Boolean getGnss() {
        if (gnss == null) {
            gnss = BOOLEAN_DECODER.apply(getBits(94, 95));
        }
		return gnss;
	}

    @SuppressWarnings("unused")
	public Integer getSpare() {
        if (spare == null) {
            spare = UNSIGNED_INTEGER_DECODER.apply(getBits(95, 96));
        }
		return spare;
	}

    @Override
    public String toString() {
        return "LongRangeBroadcastMessage{" +
                "messageType=" + getMessageType() +
                ", accuracy=" + getAccuracy() +
                ", raim=" + getRaim() +
                ", status=" + getStatus() +
                ", longitude=" + getLongitude() +
                ", latitude=" + getLatitude() +
                ", speed=" + getSpeed() +
                ", course=" + getCourse() +
                ", gnss=" + getGnss() +
                ", spare=" + getSpare() +
                "} " + super.toString();
    }

    private transient Boolean accuracy;
	private transient Boolean raim;
	private transient Integer status;
	private transient Float longitude;
	private transient Float latitude;
	private transient Integer speed;
	private transient Integer course;
	private transient Boolean gnss;
	private transient Integer spare;
}
