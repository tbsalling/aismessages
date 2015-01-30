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
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class StandardSARAircraftPositionReport extends AISMessage implements DynamicDataReport {

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

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.SAR;
    }

    @SuppressWarnings("unused")
	public Integer getAltitude() {
        return getDecodedValue(() -> altitude, value -> altitude = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(38, 50)));
	}

    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        return getDecodedValue(() -> speed, value -> speed = value, () -> Boolean.TRUE, () -> Float.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(50, 60))));
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        return getDecodedValue(() -> positionAccurate, value -> positionAccurate = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(60, 61)));
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        return getDecodedValue(() -> longitude, value -> longitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(61, 89)) / 600000f);
	}

    @SuppressWarnings("unused")
    public Float getLatitude() {
        return getDecodedValue(() -> latitude, value -> latitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(89, 116)) / 600000f);
    }

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        return getDecodedValue(() -> courseOverGround, value -> courseOverGround = value, () -> Boolean.TRUE, () -> UNSIGNED_FLOAT_DECODER.apply(getBits(116, 128)) / 10f);
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        return getDecodedValue(() -> second, value -> second = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(128, 134)));
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved() {
        return getDecodedValue(() -> regionalReserved, value -> regionalReserved = value, () -> Boolean.TRUE, () -> BIT_DECODER.apply(getBits(134, 142)));
	}

    @SuppressWarnings("unused")
	public Boolean getDataTerminalReady() {
        return getDecodedValue(() -> dataTerminalReady, value -> dataTerminalReady = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(142, 143)));
	}

    @SuppressWarnings("unused")
	public Boolean getAssigned() {
        return getDecodedValue(() -> assigned, value -> assigned = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(146, 147)));
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        return getDecodedValue(() -> raimFlag, value -> raimFlag = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(147, 148)));
	}

    @SuppressWarnings("unused")
	public String getRadioStatus() {
        return getDecodedValue(() -> radioStatus, value -> radioStatus = value, () -> Boolean.TRUE, () -> BIT_DECODER.apply(getBits(148, 168)));
	}

    @Override
    public String toString() {
        return "StandardSARAircraftPositionReport{" +
                "messageType=" + getMessageType() +
                ", altitude=" + getAltitude() +
                ", speed=" + getSpeedOverGround() +
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
	private transient Float speed;
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
