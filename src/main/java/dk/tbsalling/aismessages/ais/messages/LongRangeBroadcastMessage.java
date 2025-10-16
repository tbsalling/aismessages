package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;
import java.util.stream.IntStream;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LongRangeBroadcastMessage extends AISMessage implements DynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected LongRangeBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                        boolean positionAccuracy, boolean raim, NavigationStatus navigationStatus,
                                        float latitude, float longitude, float speedOverGround, float courseOverGround,
                                        int positionLatency, int spare,
                                        int rawLongitude, int rawLatitude, int rawSpeedOverGround, int rawCourseOverGround) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.positionAccuracy = positionAccuracy;
        this.raim = raim;
        this.navigationStatus = navigationStatus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedOverGround = speedOverGround;
        this.courseOverGround = courseOverGround;
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

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

    boolean positionAccuracy;
    boolean raim;
    NavigationStatus navigationStatus;
    float longitude;
    float latitude;
    float speedOverGround;
    float courseOverGround;
    int positionLatency;
    int spare;
    int rawLongitude;
    int rawLatitude;
    int rawSpeedOverGround;
    int rawCourseOverGround;
}
