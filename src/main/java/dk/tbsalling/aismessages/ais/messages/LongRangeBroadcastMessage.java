package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class LongRangeBroadcastMessage extends AISMessage implements DynamicDataReport {

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

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A; // TODO this could also be type B (though expected to be very rare).
    }

    /**
     * @return true if position accuracy lte 10m; false otherwise.
     */
    @SuppressWarnings("unused")
	public Boolean getPositionAccuracy() {
        return getDecodedValue(() -> positionAccuracy, value -> positionAccuracy = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(38, 39)));
	}

    @SuppressWarnings("unused")
    public Boolean getRaim() {
        return getDecodedValue(() -> raim, value -> raim = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(39, 40)));
	}

    @SuppressWarnings("unused")
	public NavigationStatus getNavigationalStatus() {
        return getDecodedValue(() -> navigationStatus, value -> navigationStatus = value, () -> Boolean.TRUE, () -> NavigationStatus.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 44))));
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        return getDecodedValue(() -> longitude, value -> longitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(44, 62)) / 600f);
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        return getDecodedValue(() -> latitude, value -> latitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(62, 79)) / 600f);
	}

    /**
     * @return Knots (0-62); 63 = not available = default
     */
    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        return Float.valueOf(getDecodedValue(() -> speed, value -> speed = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(79, 85))));
	}

    /**
     * @return Degrees (0-359); 511 = not available = default
     */
    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        return Float.valueOf(getDecodedValue(() -> course, value -> course = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(85, 94))));
	}

    /**
     * @return 0 if reported position latency is less than 5 seconds; 1 if reported position latency is greater than 5 seconds = default
     */
    @SuppressWarnings("unused")
	public Integer getPositionLatency() {
        return getDecodedValue(() -> positionLatency, value -> positionLatency = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(94, 95)));
	}

   @SuppressWarnings("unused")
	public Integer getSpare() {
        return getDecodedValue(() -> spare, value -> spare = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(95, 96)));
	}

    @Override
    public String toString() {
        return "LongRangeBroadcastMessage{" +
                "messageType=" + getMessageType() +
                ", positionAccuracy=" + getPositionAccuracy() +
                ", raim=" + getRaim() +
                ", status=" + getNavigationalStatus() +
                ", longitude=" + getLongitude() +
                ", latitude=" + getLatitude() +
                ", speed=" + getSpeedOverGround() +
                ", course=" + getCourseOverGround() +
                ", positionLatency=" + getPositionLatency() +
                ", spare=" + getSpare() +
                "} " + super.toString();
    }

    private transient Boolean positionAccuracy;
	private transient Boolean raim;
	private transient NavigationStatus navigationStatus;
	private transient Float longitude;
	private transient Float latitude;
	private transient Integer speed;
	private transient Integer course;
	private transient Integer positionLatency;
	private transient Integer spare;
}
