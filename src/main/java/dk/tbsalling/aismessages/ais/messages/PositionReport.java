/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

/**
 * 
 */
package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public abstract class PositionReport extends AISMessage {

    public PositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected PositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    @SuppressWarnings("unused")
	public NavigationStatus getNavigationStatus() {
        if (navigationStatus == null) {
            navigationStatus = NavigationStatus.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(38, 42)));
        }
		return navigationStatus;
	}

    @SuppressWarnings("unused")
	public Integer getRateOfTurn() {
        if (rateOfTurn == null) {
            rateOfTurn = INTEGER_DECODER.apply(getBits(42, 50));
        }
		return rateOfTurn;
	}

    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        if (speedOverGround == null) {
            speedOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(50, 60)) / 10f;
        }
		return speedOverGround;
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        if (positionAccurate == null) {
            positionAccurate = BOOLEAN_DECODER.apply(getBits(60, 61));
        }
		return positionAccurate;
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        if (latitude == null) {
            latitude = FLOAT_DECODER.apply(getBits(89, 116)) / 600000f;
        }
		return latitude;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        if (longitude == null) {
            longitude = FLOAT_DECODER.apply(getBits(61, 89)) / 600000f;
        }
		return longitude;
	}

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        if (courseOverGround == null) {
            courseOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(116, 128)) / 10f;
        }
		return courseOverGround;
	}

    @SuppressWarnings("unused")
	public Integer getTrueHeading() {
        if (trueHeading == null) {
            trueHeading = UNSIGNED_INTEGER_DECODER.apply(getBits(128, 137));
        }
		return trueHeading;
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        if (second == null) {
            second = UNSIGNED_INTEGER_DECODER.apply(getBits(137, 143));
        }
		return second;
	}

    @SuppressWarnings("unused")
	public ManeuverIndicator getManeuverIndicator() {
        if (maneuverIndicator == null) {
            maneuverIndicator = ManeuverIndicator.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(143, 145)));
        }
		return maneuverIndicator;
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        if (raimFlag == null) {
            raimFlag = BOOLEAN_DECODER.apply(getBits(148, 149));
        }
		return raimFlag;
	}

    @Override
    public String toString() {
        return "PositionReport{" +
                "navigationStatus=" + getNavigationStatus() +
                ", rateOfTurn=" + getRateOfTurn() +
                ", speedOverGround=" + getSpeedOverGround() +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", courseOverGround=" + getCourseOverGround() +
                ", trueHeading=" + getTrueHeading() +
                ", second=" + getSecond() +
                ", maneuverIndicator=" + getManeuverIndicator() +
                ", raimFlag=" + getRaimFlag() +
                "} " + super.toString();
    }

    private transient NavigationStatus navigationStatus;
	private transient Integer rateOfTurn;
	private transient Float speedOverGround;
	private transient Boolean positionAccurate;
	private transient Float latitude;
	private transient Float longitude;
	private transient Float courseOverGround;
	private transient Integer trueHeading;
	private transient Integer second;
	private transient ManeuverIndicator maneuverIndicator;
	private transient Boolean raimFlag;
}
