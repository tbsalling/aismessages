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

import java.lang.ref.WeakReference;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public abstract class PositionReport extends AISMessage implements ExtendedDynamicDataReport {

    public PositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected PositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
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
        return getDecodedValue(() -> navigationStatus, value -> navigationStatus = value, () -> Boolean.TRUE, () -> NavigationStatus.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(38, 42))));
	}

    @SuppressWarnings("unused")
	public Integer getRateOfTurn() {
        return getDecodedValue(() -> rateOfTurn, value -> rateOfTurn = value, () -> Boolean.TRUE, () -> {int rot = INTEGER_DECODER.apply(getBits(42, 50));return (int) (Math.signum(rot) * Math.pow(rot / 4.733, 2));});
	}

    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        return getDecodedValue(() -> speedOverGround, value -> speedOverGround = value, () -> Boolean.TRUE, () -> UNSIGNED_FLOAT_DECODER.apply(getBits(50, 60)) / 10f);
	}

    @SuppressWarnings("unused")
	public Integer getRawSpeedOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(50, 60));
    }

    @SuppressWarnings("unused")
	public Boolean getPositionAccuracy() {
        return getDecodedValue(() -> positionAccuracy, value -> positionAccuracy = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(60, 61)));
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        return getDecodedValue(() -> latitude, value -> latitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(89, 116)) / 600000f);
	}

    @SuppressWarnings("unused")
    public Integer getRawLatitude() {
        return INTEGER_DECODER.apply(getBits(89, 116));
    }

    @SuppressWarnings("unused")
	public Float getLongitude() {
        return getDecodedValue(() -> longitude, value -> longitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(61, 89)) / 600000f);
	}

    @SuppressWarnings("unused")
    public Integer getRawLongitude() {
        return INTEGER_DECODER.apply(getBits(61, 89));
    }

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        return getDecodedValue(() -> courseOverGround, value -> courseOverGround = value, () -> Boolean.TRUE, () -> UNSIGNED_FLOAT_DECODER.apply(getBits(116, 128)) / 10f);
	}

    @SuppressWarnings("unused")
    public Integer getRawCourseOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(116, 128));
    }

    @SuppressWarnings("unused")
	public Integer getTrueHeading() {
        return getDecodedValue(() -> trueHeading, value -> trueHeading = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(128, 137)) );
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        return getDecodedValue(() -> second, value -> second = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(137, 143)));
	}

    @SuppressWarnings("unused")
	public ManeuverIndicator getSpecialManeuverIndicator() {
        return getDecodedValue(() -> specialManeuverIndicator, value -> specialManeuverIndicator = value, () -> Boolean.TRUE, () -> ManeuverIndicator.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(143, 145))));
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        return getDecodedValue(() -> raimFlag, value -> raimFlag = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(148, 149)));
	}

    @SuppressWarnings("unused")
    public CommunicationState getCommunicationState() {
        if (this instanceof PositionReportClassAScheduled || this instanceof PositionReportClassAAssignedSchedule)
            return getDecodedValueByWeakReference(() -> communicationState, value -> communicationState = value, () -> Boolean.TRUE, () -> SOTDMACommunicationState.fromBitString(getBits(149, 168)));
        else if (this instanceof PositionReportClassAResponseToInterrogation)
            return getDecodedValueByWeakReference(() -> communicationState, value -> communicationState = value, () -> Boolean.TRUE, () -> ITDMACommunicationState.fromBitString(getBits(149, 168)));
        else
            return null;
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

    private transient NavigationStatus navigationStatus;
	private transient Integer rateOfTurn;
	private transient Float speedOverGround;
	private transient Boolean positionAccuracy;
	private transient Float latitude;
	private transient Float longitude;
	private transient Float courseOverGround;
	private transient Integer trueHeading;
	private transient Integer second;
	private transient ManeuverIndicator specialManeuverIndicator;
	private transient Boolean raimFlag;
	private transient WeakReference<CommunicationState> communicationState;
}
