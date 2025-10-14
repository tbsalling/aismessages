package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.util.stream.IntStream;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class LongRangeBroadcastMessage extends AISMessage implements DynamicDataReport {

    protected LongRangeBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all mandatory fields
        this.positionAccuracy = BOOLEAN_DECODER.apply(getBits(38, 39));
        this.raim = BOOLEAN_DECODER.apply(getBits(39, 40));
        this.navigationStatus = NavigationStatus.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 44)));
        this.longitude = FLOAT_DECODER.apply(getBits(44, 62)) / 600f;
        this.latitude = FLOAT_DECODER.apply(getBits(62, 79)) / 600f;
        this.speed = UNSIGNED_INTEGER_DECODER.apply(getBits(79, 85));
        this.course = UNSIGNED_INTEGER_DECODER.apply(getBits(85, 94));
        this.positionLatency = UNSIGNED_INTEGER_DECODER.apply(getBits(94, 95));
        this.spare = UNSIGNED_INTEGER_DECODER.apply(getBits(95, 96));
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (IntStream.of(96, 168).noneMatch(l -> numberOfBits == l))
            errorMessage.append(format("Message of type %s should be exactly 96 or 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
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
    public boolean getPositionAccuracy() {
        return positionAccuracy;
	}

    @SuppressWarnings("unused")
    public boolean getRaim() {
        return raim;
	}

    @SuppressWarnings("unused")
	public NavigationStatus getNavigationalStatus() {
        return navigationStatus;
	}

    @SuppressWarnings("unused")
    public float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
    public int getRawLongitude() {
        return INTEGER_DECODER.apply(getBits(44, 62));
    }

    @SuppressWarnings("unused")
    public float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
    public int getRawLatitude() {
        return INTEGER_DECODER.apply(getBits(62, 79));
    }

    /**
     * @return Knots (0-62); 63 = not available = default
     */
    @SuppressWarnings("unused")
    public float getSpeedOverGround() {
        return speed;
	}

    @SuppressWarnings("unused")
    public int getRawSpeedOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(79, 85));
    }

    /**
     * @return Degrees (0-359); 511 = not available = default
     */
    @SuppressWarnings("unused")
    public float getCourseOverGround() {
        return course;
	}

    @SuppressWarnings("unused")
    public int getRawCourseOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(85, 94));
    }

    /**
     * @return 0 if reported position latency is less than 5 seconds; 1 if reported position latency is greater than 5 seconds = default
     */
    @SuppressWarnings("unused")
    public int getPositionLatency() {
        return positionLatency;
	}

   @SuppressWarnings("unused")
   public int getSpare() {
       return spare;
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

    private final boolean positionAccuracy;
    private final boolean raim;
    private final NavigationStatus navigationStatus;
    private final float longitude;
    private final float latitude;
    private final int speed;
    private final int course;
    private final int positionLatency;
    private final int spare;
}
