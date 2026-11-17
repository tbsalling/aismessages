/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

/**
 * 
 */
package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.BitString;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Messages 1, 2 and 3: Class A position reports (ITU-R M.1371-6 § A7-3.1, Table 46).
 * <p>
 * The three message types share one 168-bit layout and differ only in how the slot was accessed (Table 44): Message 1
 * is the scheduled report, Message 2 the report in assigned mode and Message 3 the special report sent in response to
 * an interrogation or during network entry. Messages 1 and 2 carry a SOTDMA communication state, Message 3 an ITDMA
 * communication state (Table 47). Transmitted by Class A shipborne mobile stations at the reporting intervals of
 * Table 1 (2 s to 3 min depending on speed, course change and navigational status).
 *
 * @author tbsalling
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract sealed class PositionReport extends AISMessage implements ExtendedDynamicDataReport permits PositionReportClassAScheduled, PositionReportClassAAssignedSchedule, PositionReportClassAResponseToInterrogation {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     *
     * @param sourceMmsi               the pre-parsed source MMSI
     * @param repeatIndicator          the pre-parsed repeat indicator
     * @param nmeaTagBlock             the NMEA tag block
     * @param nmeaMessages             the NMEA messages
     * @param bitString                the bit string
     * @param navigationStatus         the navigation status
     * @param rateOfTurn               the rate of turn (calculated from raw value)
     * @param speedOverGround          the speed over ground
     * @param positionAccuracy         the position accuracy flag
     * @param latitude                 the latitude
     * @param longitude                the longitude
     * @param courseOverGround         the course over ground
     * @param trueHeading              the true heading
     * @param second                   the timestamp second
     * @param specialManeuverIndicator the special maneuver indicator
     * @param raimFlag                 the RAIM flag
     * @param communicationState       the communication state
     * @param rawRateOfTurn            the raw rate of turn value
     * @param rawSpeedOverGround       the raw speed over ground value
     * @param rawLatitude              the raw latitude value
     * @param rawLongitude             the raw longitude value
     * @param rawCourseOverGround      the raw course over ground value
     * @param transmitPowerLow         the transmit power flag (false = high power, true = low power)
     */
    protected PositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                             NavigationStatus navigationStatus, int rateOfTurn, float speedOverGround,
                             boolean positionAccuracy, double latitude, double longitude,
                             float courseOverGround, int trueHeading, int second,
                             ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState,
                             int rawRateOfTurn, int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround, boolean transmitPowerLow) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.navigationStatus = navigationStatus;
        this.rateOfTurn = rateOfTurn;
        this.speedOverGround = speedOverGround;
        this.positionAccuracy = positionAccuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseOverGround = courseOverGround;
        this.trueHeading = trueHeading;
        this.second = second;
        this.specialManeuverIndicator = specialManeuverIndicator;
        this.raimFlag = raimFlag;
        this.communicationState = communicationState;
        this.rawRateOfTurn = rawRateOfTurn;
        this.rawSpeedOverGround = rawSpeedOverGround;
        this.rawLatitude = rawLatitude;
        this.rawLongitude = rawLongitude;
        this.rawCourseOverGround = rawCourseOverGround;
        this.transmitPowerLow = transmitPowerLow;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits != 168)
            errorMessage.append(format("Message of type %s should be at exactly 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A;
    }

    /** Bits 38-42 (Table 46): navigational status; null for a code the enum does not define. */
    NavigationStatus navigationStatus;
    /**
     * Rate of turn in degrees per minute, derived from {@code rawRateOfTurn} by inverting ROT_AIS = 4.733 × √ROT_sensor
     * (Table 46); negative = turning left. The sentinels ±127 ("turning at more than 5° per 30 s, no turn indicator") and
     * -128 ("not available") are converted like ordinary values, so check {@code rawRateOfTurn} for those cases.
     */
    int rateOfTurn;
    /** Speed over ground in knots, {@code rawSpeedOverGround} / 10 (Table 46); 102.2 = 102.2 knots or higher, 102.3 = not available. */
    float speedOverGround;
    /** Bit 60 (Table 46): position accuracy flag, true = high (≤ 10 m), false = low (&gt; 10 m, default); see Table 48. */
    boolean positionAccuracy;
    /** Latitude in decimal degrees, {@code rawLatitude} / 600 000 (Table 46); north positive; 91 = not available. */
    double latitude;
    /** Longitude in decimal degrees, {@code rawLongitude} / 600 000 (Table 46); east positive; 181 = not available. */
    double longitude;
    /** Course over ground in degrees, {@code rawCourseOverGround} / 10 (Table 46); 360 = not available. */
    float courseOverGround;
    /** Bits 128-137 (Table 46): true heading in degrees, 0-359; 511 = not available. */
    int trueHeading;
    /**
     * Bits 137-143 (Table 46): UTC second when the report was generated by the EPFS, 0-59; 60 = time stamp not available
     * (default), 61 = positioning system in manual input mode, 62 = dead reckoning, 63 = positioning system inoperative.
     */
    int second;
    /** Bits 143-145 (Table 46): special manoeuvre indicator. */
    ManeuverIndicator specialManeuverIndicator;
    /** Bit 148 (Table 46): RAIM flag of the electronic position fixing device, true = RAIM in use; see Table 48. */
    boolean raimFlag;
    /** Bits 149-168 (Table 46, Table 47): SOTDMA communication state for Messages 1 and 2 (Table 18), ITDMA for Message 3 (Table 20). */
    CommunicationState communicationState;
    /** Bits 42-50 (Table 46): rate of turn as transmitted (ROT_AIS), -128 to +127; +127/-127 = turning right/left at more than 5° per 30 s without turn indicator, -128 = not available. */
    int rawRateOfTurn;
    /** Bits 50-60 (Table 46): speed over ground in 1/10 knot steps, 0-1022; 1022 = 102.2 knots or higher, 1023 = not available. */
    int rawSpeedOverGround;
    /** Bits 89-116 (Table 46): latitude in 1/10 000 min, two's complement; 91° (0x3412140) = not available. */
    int rawLatitude;
    /** Bits 61-89 (Table 46): longitude in 1/10 000 min, two's complement; 181° (0x6791AC0) = not available. */
    int rawLongitude;
    /** Bits 116-128 (Table 46): course over ground in 1/10 degree, 0-3599; 3600 = not available. */
    int rawCourseOverGround;
    /** Bit 147 (Table 46): transmit power flag, false = high power (default), true = low power. Bits 145-147 are spare. */
    boolean transmitPowerLow;
}
