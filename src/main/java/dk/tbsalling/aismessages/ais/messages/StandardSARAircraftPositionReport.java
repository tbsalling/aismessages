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

@SuppressWarnings("serial")
public class StandardSARAircraftPositionReport extends AISMessage {

    public StandardSARAircraftPositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected StandardSARAircraftPositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public AISMessageType getMessageType() {
        return AISMessageType.StandardSARAircraftPositionReport;
    }

    @SuppressWarnings("unused")
	public Integer getAltitude() {
        if (altitude == null) {
            altitude = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 50));
        }
        return altitude;
	}

    @SuppressWarnings("unused")
	public Integer getSpeed() {
        if (speed == null) {
            speed = UNSIGNED_INTEGER_DECODER.apply(getBits(50, 60));
        }
		return speed;
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        if (positionAccurate == null) {
            positionAccurate = BOOLEAN_DECODER.apply(getBits(60, 61));
        }
		return positionAccurate;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        if (longitude == null) {
            longitude = FLOAT_DECODER.apply(getBits(61, 89)) / 600000f;
        }
		return longitude;
	}

    @SuppressWarnings("unused")
    public Float getLatitude() {
        if (latitude == null) {
            latitude = FLOAT_DECODER.apply(getBits(89, 116)) / 600000f;
        }
        return latitude;
    }

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        if (courseOverGround == null) {
            courseOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(116, 128)) / 10f;
        }
		return courseOverGround;
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        if (second == null) {
            second = UNSIGNED_INTEGER_DECODER.apply(getBits(128, 134));
        }
		return second;
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved() {
        if (regionalReserved == null) {
            regionalReserved = BIT_DECODER.apply(getBits(134, 142));
        }
		return regionalReserved;
	}

    @SuppressWarnings("unused")
	public Boolean getDataTerminalReady() {
        if (dataTerminalReady == null) {
            dataTerminalReady = BOOLEAN_DECODER.apply(getBits(142, 143));
        }
		return dataTerminalReady;
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
        return "StandardSARAircraftPositionReport{" +
                "messageType=" + getMessageType() +
                ", altitude=" + getAltitude() +
                ", speed=" + getSpeed() +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", courseOverGround=" + getCourseOverGround() +
                ", second=" + getSecond() +
                ", regionalReserved='" + getRegionalReserved() + '\'' +
                ", dataTerminalReady=" + getDataTerminalReady() +
                ", assigned=" + getAssigned() +
                ", raimFlag=" + getRaimFlag() +
                ", radioStatus='" + getRadioStatus() + '\'' +
                "} " + super.toString();
    }

    private transient Integer altitude;
	private transient Integer speed;
	private transient Boolean positionAccurate;
	private transient Float latitude;
	private transient Float longitude;
	private transient Float courseOverGround;
	private transient Integer second;
	private transient String regionalReserved;
	private transient Boolean dataTerminalReady;
	private transient Boolean assigned;
	private transient Boolean raimFlag;
	private transient String radioStatus;
}
