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

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * A less detailed report than types 1-3 for vessels using Class B transmitters.
 * Omits navigational status and rate of turn.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class StandardClassBCSPositionReport extends AISMessage {

    public StandardClassBCSPositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected StandardClassBCSPositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.StandardClassBCSPositionReport;
    }

    @SuppressWarnings("unused")
	public String getRegionalReserved1() {
        if (regionalReserved1 == null) {
            regionalReserved1 = BIT_DECODER.apply(getBits(38, 46));
        }
		return regionalReserved1;
	}

    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        if (speedOverGround == null) {
            speedOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(46, 55)) / 10f;
        }
		return speedOverGround;
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        if (positionAccurate == null) {
            positionAccurate = BOOLEAN_DECODER.apply(getBits(56, 57));
        }
		return positionAccurate;
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        if (latitude == null) {
            latitude = FLOAT_DECODER.apply(getBits(85, 112)) / 600000f;
        }
		return latitude;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        if (longitude == null) {
            longitude = FLOAT_DECODER.apply(getBits(57, 85)) / 600000f;
        }
		return longitude;
	}

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        if (courseOverGround == null) {
            courseOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(112, 124)) / 10f;
        }
		return courseOverGround;
	}

    @SuppressWarnings("unused")
	public Integer getTrueHeading() {
        if (trueHeading == null) {
            trueHeading = UNSIGNED_INTEGER_DECODER.apply(getBits(124, 133));
        }
		return trueHeading;
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        if (second == null) {
            second = UNSIGNED_INTEGER_DECODER.apply(getBits(133, 139));
        }
		return second;
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved2() {
        if (regionalReserved2 == null) {
            regionalReserved2 = BIT_DECODER.apply(getBits(139, 141));
        }
		return regionalReserved2;
	}

    @SuppressWarnings("unused")
	public Boolean getCsUnit() {
        if (csUnit == null) {
            csUnit = BOOLEAN_DECODER.apply(getBits(141, 142));
        }
		return csUnit;
	}

    @SuppressWarnings("unused")
	public Boolean getDisplay() {
        if (display == null) {
            display = BOOLEAN_DECODER.apply(getBits(142, 143));
        }
		return display;
	}

    @SuppressWarnings("unused")
	public Boolean getDsc() {
        if (dsc == null) {
            dsc = BOOLEAN_DECODER.apply(getBits(143, 144));
        }
		return dsc;
	}

    @SuppressWarnings("unused")
	public Boolean getBand() {
        if (band == null) {
            band = BOOLEAN_DECODER.apply(getBits(144, 145));
        }
		return band;
	}

    @SuppressWarnings("unused")
	public Boolean getMessage22() {
        if (message22 == null) {
            message22 = BOOLEAN_DECODER.apply(getBits(145, 146));
        }
		return message22;
	}

    @SuppressWarnings("unused")
	public Boolean getAssigned() {
        if (assigned == null) {
            assigned = BOOLEAN_DECODER.apply(getBits(146, 147));
        }
		return assigned;
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        if (raimFlag == null) {
            raimFlag = BOOLEAN_DECODER.apply(getBits(147, 148));
        }
		return raimFlag;
	}

    @SuppressWarnings("unused")
	public String getRadioStatus() {
        if (radioStatus == null) {
            radioStatus = BIT_DECODER.apply(getBits(148, 168));
        }
		return radioStatus;
	}

    @Override
    public String toString() {
        return "StandardClassBCSPositionReport{" +
                "messageType=" + getMessageType() +
                ", regionalReserved1='" + getRegionalReserved1() + '\'' +
                ", speedOverGround=" + getSpeedOverGround() +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", courseOverGround=" + getCourseOverGround() +
                ", trueHeading=" + getTrueHeading() +
                ", second=" + getSecond() +
                ", regionalReserved2='" + getRegionalReserved2() + '\'' +
                ", csUnit=" + getCsUnit() +
                ", display=" + getDisplay() +
                ", dsc=" + getDsc() +
                ", band=" + getBand() +
                ", message22=" + getMessage22() +
                ", assigned=" + getAssigned() +
                ", raimFlag=" + getRaimFlag() +
                ", radioStatus='" + getRadioStatus() + '\'' +
                "} " + super.toString();
    }

    private transient String regionalReserved1;
	private transient Float speedOverGround;
	private transient Boolean positionAccurate;
	private transient Float latitude;
	private transient Float longitude;
	private transient Float courseOverGround;
	private transient Integer trueHeading;
	private transient Integer second;
	private transient String regionalReserved2;
	private transient Boolean csUnit;
	private transient Boolean display;
	private transient Boolean dsc;
	private transient Boolean band;
	private transient Boolean message22;
	private transient Boolean assigned;
	private transient Boolean raimFlag;
	private transient String radioStatus;
}
