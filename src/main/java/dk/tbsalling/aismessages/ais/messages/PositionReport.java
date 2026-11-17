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
     * @param rateOfTurn               the rate of turn in degrees per minute; NaN unless measured
     * @param rateOfTurnIndicator      what kind of rate of turn value was transmitted
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
     * @param transmitPowerLow         the transmit power flag (false = high power, true = low power)
     */
    protected PositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                             NavigationStatus navigationStatus, float rateOfTurn, RateOfTurnIndicator rateOfTurnIndicator, float speedOverGround, SpeedOverGroundIndicator speedOverGroundIndicator,
                             boolean positionAccuracy, double latitude, double longitude,
                             float courseOverGround, int trueHeading, int second,
                             ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState, boolean transmitPowerLow) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.navigationStatus = navigationStatus;
        this.rateOfTurn = rateOfTurn;
        this.speedOverGround = speedOverGround;
        this.speedOverGroundIndicator = speedOverGroundIndicator;
        this.positionAccuracy = positionAccuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseOverGround = courseOverGround;
        this.trueHeading = trueHeading;
        this.second = second;
        this.specialManeuverIndicator = specialManeuverIndicator;
        this.raimFlag = raimFlag;
        this.communicationState = communicationState;
        this.rateOfTurnIndicator = rateOfTurnIndicator;
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
     * Rate of turn in degrees per minute, decoded by inverting ROT_AIS = 4.733 × √ROT_sensor (Table 46); negative =
     * turning left. {@code NaN} unless {@code rateOfTurnIndicator} is {@link RateOfTurnIndicator#Measured}.
     */
    float rateOfTurn;
    /** Meaning of the transmitted rate of turn: a measured value, turning without a turn indicator, or not available (Table 46). */
    RateOfTurnIndicator rateOfTurnIndicator;
    /** Speed over ground in knots, transmitted in 1/10 knot (Table 46); 102.2 when 102.2 knots or higher and {@code NaN} when not available, as told by {@code speedOverGroundIndicator}. */
    float speedOverGround;
    /** Meaning of {@code speedOverGround}: measured, at least the field's maximum, or not available (see {@link SpeedOverGroundIndicator}). */
    SpeedOverGroundIndicator speedOverGroundIndicator;
    /** Bit 60 (Table 46): position accuracy flag, true = high (≤ 10 m), false = low (&gt; 10 m, default); see Table 48. */
    boolean positionAccuracy;
    /** Latitude in decimal degrees, transmitted in 1/10 000 min (Table 46); north positive; {@code NaN} when not available (transmitted as 91°). */
    double latitude;
    /** Longitude in decimal degrees, transmitted in 1/10 000 min (Table 46); east positive; {@code NaN} when not available (transmitted as 181°). */
    double longitude;
    /** Course over ground in degrees, transmitted in 1/10° (Table 46); {@code NaN} when not available (transmitted as 360°). */
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
    /** Bit 147 (Table 46): transmit power flag, false = high power (default), true = low power. Bits 145-147 are spare. */
    boolean transmitPowerLow;
}
