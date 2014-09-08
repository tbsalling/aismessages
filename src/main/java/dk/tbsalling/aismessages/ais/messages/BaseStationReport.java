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
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * This message is to be used by fixed-location base stations to periodically report a position and time reference.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class BaseStationReport extends AISMessage {

    public BaseStationReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BaseStationReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BaseStationReport;
    }

    @SuppressWarnings("unused")
	public Integer getYear() {
        if (year == null) {
            year = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 52));
        }
        return year;
	}

    @SuppressWarnings("unused")
	public Integer getMonth() {
        if (month == null) {
            month = UNSIGNED_INTEGER_DECODER.apply(getBits(52, 56));
        }
        return month;
	}

    @SuppressWarnings("unused")
	public Integer getDay() {
        if (day == null) {
            day = UNSIGNED_INTEGER_DECODER.apply(getBits(56, 61));
        }
        return day;
	}

    @SuppressWarnings("unused")
	public Integer getHour() {
        if (hour == null) {
            hour = UNSIGNED_INTEGER_DECODER.apply(getBits(61, 66));
        }
        return hour;
	}

    @SuppressWarnings("unused")
	public Integer getMinute() {
        if (minute == null) {
            minute = UNSIGNED_INTEGER_DECODER.apply(getBits(66, 72));
        }
		return minute;
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        if (second == null) {
            second = UNSIGNED_INTEGER_DECODER.apply(getBits(72, 78));
        }
        return second;
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        if (positionAccurate == null) {
            positionAccurate = BOOLEAN_DECODER.apply(getBits(78, 79));
        }
        return positionAccurate;
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        if (latitude == null) {
            latitude = FLOAT_DECODER.apply(getBits(107, 134)) / 600000f;
        }
        return latitude;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        if (longitude == null) {
            longitude = FLOAT_DECODER.apply(getBits(79, 107)) / 600000f;
        }
        return longitude;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        if (positionFixingDevice == null) {
            positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(134, 138)));
        }
        return positionFixingDevice;
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
        return "BaseStationReport{" +
                "messageType=" + getMessageType() +
                ", year=" + getYear() +
                ", month=" + getMonth() +
                ", day=" + getDay() +
                ", hour=" + getHour() +
                ", minute=" + getMinute() +
                ", second=" + getSecond() +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", raimFlag=" + getRaimFlag() +
                "} " + super.toString();
    }

    private transient Integer year;
    private transient Integer month;
    private transient Integer day;
    private transient Integer hour;
    private transient Integer minute;
    private transient Integer second;
    private transient Boolean positionAccurate;
    private transient Float latitude;
    private transient Float longitude;
    private transient PositionFixingDevice positionFixingDevice;
    private transient Boolean raimFlag;
}
