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
 * @author tbsalling
 *
 */
@Getter
@ToString
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
     */
    protected PositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                             NavigationStatus navigationStatus, int rateOfTurn, float speedOverGround,
                             boolean positionAccuracy, float latitude, float longitude,
                             float courseOverGround, int trueHeading, int second,
                             ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState,
                             int rawRateOfTurn, int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround) {
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

    NavigationStatus navigationStatus;
    int rateOfTurn;
    float speedOverGround;
    boolean positionAccuracy;
    float latitude;
    float longitude;
    float courseOverGround;
    int trueHeading;
    int second;
    ManeuverIndicator specialManeuverIndicator;
    boolean raimFlag;
    CommunicationState communicationState;
    int rawRateOfTurn;
    int rawSpeedOverGround;
    int rawLatitude;
    int rawLongitude;
    int rawCourseOverGround;
}
