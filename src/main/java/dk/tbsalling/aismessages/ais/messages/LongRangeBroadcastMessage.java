package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.BitString;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionLatency;
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

/**
 * Message 27: AIS satellite message, formerly the long-range AIS broadcast message (ITU-R M.1371-6 § A7-3.25,
 * Table 83, Annex 3). Transmitted by Class A and Class B "SO" stations every 3 min on channels 75 and 76 for reception
 * by satellites, with a compressed 96-bit content (coarser position, speed and course) to allow for the longer
 * propagation delays. The repeat indicator is always 3. Some receivers pad it to a full 168-bit slot, which this class
 * accepts. There is no time stamp; the receiving system is expected to supply one.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LongRangeBroadcastMessage extends AISMessage implements DynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected LongRangeBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                        boolean positionAccuracy, boolean raim, NavigationStatus navigationStatus,
                                        double latitude, double longitude, float speedOverGround, float courseOverGround,
                                        PositionLatency positionLatency, int spare) {
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

    /** Bit 38 (Table 83): position accuracy flag as in Message 1, true = high (≤ 10 m). */
    boolean positionAccuracy;
    /** Bit 39 (Table 83): RAIM flag as in Message 1, true = RAIM in use. */
    boolean raim;
    /** Bits 40-44 (Table 83): navigational status as in Message 1. */
    NavigationStatus navigationStatus;
    /** Longitude in decimal degrees, {@code rawLongitude} / 600 (Table 83); east positive; 181 = position older than 6 hours or not available. */
    double longitude;
    /** Latitude in decimal degrees, {@code rawLatitude} / 600 (Table 83); north positive; 91 = position older than 6 hours or not available. */
    double latitude;
    /** Speed over ground in knots, equal to {@code rawSpeedOverGround} since Message 27 uses 1-knot steps (Table 83); 62 = 62 knots or faster, 63 = not available. */
    float speedOverGround;
    /** Course over ground in degrees, equal to {@code rawCourseOverGround} since Message 27 uses 1-degree steps (Table 83); 511 = not available. */
    float courseOverGround;
    /** Bit 94 (Table 83): position latency, whether the reported position is more or less than 5 s old. */
    PositionLatency positionLatency;
    /** Bit 95 (Table 83): spare, set to zero to preserve the byte boundary. */
    int spare;
}
