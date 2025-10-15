package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;
import java.util.stream.IntStream;

import static java.lang.String.format;

public class LongRangeBroadcastMessage extends AISMessage implements DynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected LongRangeBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                        boolean positionAccuracy, boolean raim, NavigationStatus navigationStatus,
                                        float latitude, float longitude, int speed, int course,
                                        int positionLatency, int spare,
                                        int rawLongitude, int rawLatitude, int rawSpeedOverGround, int rawCourseOverGround) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.positionAccuracy = positionAccuracy;
        this.raim = raim;
        this.navigationStatus = navigationStatus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.course = course;
        this.positionLatency = positionLatency;
        this.spare = spare;
        this.rawLongitude = rawLongitude;
        this.rawLatitude = rawLatitude;
        this.rawSpeedOverGround = rawSpeedOverGround;
        this.rawCourseOverGround = rawCourseOverGround;
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
        return rawLongitude;
    }

    @SuppressWarnings("unused")
    public float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
    public int getRawLatitude() {
        return rawLatitude;
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
        return rawSpeedOverGround;
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
        return rawCourseOverGround;
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
    private final int rawLongitude;
    private final int rawLatitude;
    private final int rawSpeedOverGround;
    private final int rawCourseOverGround;
}
