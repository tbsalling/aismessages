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
        return getDecodedValue(() -> accuracy, value -> accuracy = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(38, 39)));
	}

    @SuppressWarnings("unused")
    public Boolean getRaim() {
        return getDecodedValue(() -> raim, value -> raim = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(39, 40)));
	}

    @SuppressWarnings("unused")
	public Integer getStatus() {
        return getDecodedValue(() -> status, value -> status = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(40, 44)));
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        return getDecodedValue(() -> longitude, value -> longitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(44, 62)) / 600f);
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        return getDecodedValue(() -> latitude, value -> latitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(62, 79)) / 600f);
	}

    @SuppressWarnings("unused")
	public Integer getSpeed() {
        return getDecodedValue(() -> speed, value -> speed = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(79, 85)));
	}

    @SuppressWarnings("unused")
	public Integer getCourse() {
        return getDecodedValue(() -> course, value -> course = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(85, 94)));
	}

    @SuppressWarnings("unused")
	public Boolean getGnss() {
        return getDecodedValue(() -> gnss, value -> gnss = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(94, 95)));
	}

    @SuppressWarnings("unused")
	public Integer getSpare() {
        return getDecodedValue(() -> spare, value -> spare = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(95, 96)));
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
