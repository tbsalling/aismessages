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
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class ExtendedClassBEquipmentPositionReport extends AISMessage {

    public ExtendedClassBEquipmentPositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ExtendedClassBEquipmentPositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ExtendedClassBEquipmentPositionReport;
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
            regionalReserved2 = BIT_DECODER.apply(getBits(139, 143));
        }
        return regionalReserved2;
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        if (shipName == null) {
            shipName = STRING_DECODER.apply(getBits(143, 263));
        }
        return shipName;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        if (shipType == null) {
            shipType = ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(263, 271)));
        }
        return shipType;
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        if (toBow == null) {
            toBow = UNSIGNED_INTEGER_DECODER.apply(getBits(271, 280));
        }
        return toBow;
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        if (toStern == null) {
            toStern = UNSIGNED_INTEGER_DECODER.apply(getBits(280, 289));
        }
        return toStern;
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        if (toStarboard == null) {
            toStarboard = UNSIGNED_INTEGER_DECODER.apply(getBits(295, 301));
        }
        return toStarboard;
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        if (toPort == null) {
            toPort = UNSIGNED_INTEGER_DECODER.apply(getBits(289, 295));
        }
        return toPort;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        if (positionFixingDevice == null) {
            positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(301, 305)));
        }
        return positionFixingDevice;
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        if (raimFlag == null) {
            raimFlag = BOOLEAN_DECODER.apply(getBits(305, 306));
        }
        return raimFlag;
	}

    @SuppressWarnings("unused")
	public Boolean getDataTerminalReady() {
        if (dataTerminalReady == null) {
            dataTerminalReady = BOOLEAN_DECODER.apply(getBits(306, 307));
        }
        return dataTerminalReady;
	}

    @SuppressWarnings("unused")
	public Boolean getAssigned() {
        if (assigned == null) {
            assigned = BOOLEAN_DECODER.apply(getBits(307, 308));
        }
        return assigned;
	}

    @Override
    public String toString() {
        return "ExtendedClassBEquipmentPositionReport{" +
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
                ", shipName='" + getShipName() + '\'' +
                ", shipType=" + getShipType() +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toStarboard=" + getToStarboard() +
                ", toPort=" + getToPort() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", raimFlag=" + getRaimFlag() +
                ", dataTerminalReady=" + getDataTerminalReady() +
                ", assigned=" + getAssigned() +
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
    private transient String shipName;
    private transient ShipType shipType;
    private transient Integer toBow;
    private transient Integer toStern;
    private transient Integer toStarboard;
    private transient Integer toPort;
    private transient PositionFixingDevice positionFixingDevice;
    private transient Boolean raimFlag;
    private transient Boolean dataTerminalReady;
    private transient Boolean assigned;
}
