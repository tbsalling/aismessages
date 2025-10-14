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

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public abstract class PositionReport extends AISMessage implements ExtendedDynamicDataReport {

    protected PositionReport(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all fields
        this.navigationStatus = NavigationStatus.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(38, 42)));
        int rot = INTEGER_DECODER.apply(getBits(42, 50));
        this.rateOfTurn = (int) (Math.signum(rot) * Math.pow(rot / 4.733, 2));
        this.speedOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(50, 60)) / 10f;
        this.positionAccuracy = BOOLEAN_DECODER.apply(getBits(60, 61));
        this.longitude = FLOAT_DECODER.apply(getBits(61, 89)) / 600000f;
        this.latitude = FLOAT_DECODER.apply(getBits(89, 116)) / 600000f;
        this.courseOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(116, 128)) / 10f;
        this.trueHeading = UNSIGNED_INTEGER_DECODER.apply(getBits(128, 137));
        this.second = UNSIGNED_INTEGER_DECODER.apply(getBits(137, 143));
        this.specialManeuverIndicator = ManeuverIndicator.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(143, 145)));
        this.raimFlag = BOOLEAN_DECODER.apply(getBits(148, 149));

        // Communication state depends on the concrete class type
        if (this instanceof PositionReportClassAScheduled || this instanceof PositionReportClassAAssignedSchedule) {
            this.communicationState = SOTDMACommunicationState.fromBitString(getBits(149, 168));
        } else if (this instanceof PositionReportClassAResponseToInterrogation) {
            this.communicationState = ITDMACommunicationState.fromBitString(getBits(149, 168));
        } else {
            this.communicationState = null;
        }
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
    public float getSpeedOverGround() {
        return speedOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawSpeedOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(50, 60));
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
        return INTEGER_DECODER.apply(getBits(89, 116));
    }

    @SuppressWarnings("unused")
    public float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
    public int getRawLongitude() {
        return INTEGER_DECODER.apply(getBits(61, 89));
    }

    @SuppressWarnings("unused")
    public float getCourseOverGround() {
        return courseOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawCourseOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(116, 128));
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
}
