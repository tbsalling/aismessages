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

import static java.lang.String.format;

/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public abstract class PositionReport extends AISMessage implements ExtendedDynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     *
     * @param metadata                 the metadata
     * @param repeatIndicator          the pre-parsed repeat indicator
     * @param sourceMmsi               the pre-parsed source MMSI
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
    protected PositionReport(Metadata metadata, int repeatIndicator, MMSI sourceMmsi,
                             NavigationStatus navigationStatus, int rateOfTurn, float speedOverGround,
                             boolean positionAccuracy, float latitude, float longitude,
                             float courseOverGround, int trueHeading, int second,
                             ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState,
                             int rawRateOfTurn, int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround) {
        super(metadata, repeatIndicator, sourceMmsi);
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A;
    }

    @SuppressWarnings("unused")
	public NavigationStatus getNavigationStatus() {
        return navigationStatus;
	}

    @SuppressWarnings("unused")
    public int getRateOfTurn() {
        return rateOfTurn;
	}

    @SuppressWarnings("unused")
    public int getRawRateOfTurn() {
        return rawRateOfTurn;
    }

    @SuppressWarnings("unused")
    public float getSpeedOverGround() {
        return speedOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawSpeedOverGround() {
        return rawSpeedOverGround;
    }

    @SuppressWarnings("unused")
    public boolean getPositionAccuracy() {
        return positionAccuracy;
	}

    @SuppressWarnings("unused")
    public float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
    public int getRawLatitude() {
        return rawLatitude;
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
    public float getCourseOverGround() {
        return courseOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawCourseOverGround() {
        return rawCourseOverGround;
    }

    @SuppressWarnings("unused")
    public int getTrueHeading() {
        return trueHeading;
	}

    @SuppressWarnings("unused")
    public int getSecond() {
        return second;
	}

    @SuppressWarnings("unused")
	public ManeuverIndicator getSpecialManeuverIndicator() {
        return specialManeuverIndicator;
	}

    @SuppressWarnings("unused")
    public boolean getRaimFlag() {
        return raimFlag;
	}

    @SuppressWarnings("unused")
    public CommunicationState getCommunicationState() {
        return communicationState;
    }

    @Override
    public String toString() {
        return "PositionReport{" +
                "navigationStatus=" + getNavigationStatus() +
                ", rateOfTurn=" + getRateOfTurn() +
                ", speedOverGround=" + getSpeedOverGround() +
                ", positionAccuracy=" + getPositionAccuracy() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", courseOverGround=" + getCourseOverGround() +
                ", trueHeading=" + getTrueHeading() +
                ", second=" + getSecond() +
                ", specialManeuverIndicator=" + getSpecialManeuverIndicator() +
                ", raimFlag=" + getRaimFlag() +
                "} " + super.toString();
    }

    private final NavigationStatus navigationStatus;
    private final int rateOfTurn;
    private final float speedOverGround;
    private final boolean positionAccuracy;
    private final float latitude;
    private final float longitude;
    private final float courseOverGround;
    private final int trueHeading;
    private final int second;
    private final ManeuverIndicator specialManeuverIndicator;
    private final boolean raimFlag;
    private final CommunicationState communicationState;
    private final int rawRateOfTurn;
    private final int rawSpeedOverGround;
    private final int rawLatitude;
    private final int rawLongitude;
    private final int rawCourseOverGround;
}
