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

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class ExtendedClassBEquipmentPositionReport extends AISMessage implements ExtendedDynamicDataReport {

    protected ExtendedClassBEquipmentPositionReport(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all fields
        this.regionalReserved1 = BIT_DECODER.apply(getBits(38, 46));
        this.speedOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(46, 56)) / 10f;
        this.positionAccuracy = BOOLEAN_DECODER.apply(getBits(56, 57));
        this.longitude = FLOAT_DECODER.apply(getBits(57, 85)) / 600000f;
        this.latitude = FLOAT_DECODER.apply(getBits(85, 112)) / 600000f;
        this.courseOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(112, 124)) / 10f;
        this.trueHeading = UNSIGNED_INTEGER_DECODER.apply(getBits(124, 133));
        this.second = UNSIGNED_INTEGER_DECODER.apply(getBits(133, 139));
        this.regionalReserved2 = BIT_DECODER.apply(getBits(139, 143));
        this.shipName = STRING_DECODER.apply(getBits(143, 263));
        this.shipType = ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(263, 271)));
        this.toBow = UNSIGNED_INTEGER_DECODER.apply(getBits(271, 280));
        this.toStern = UNSIGNED_INTEGER_DECODER.apply(getBits(280, 289));
        this.toPort = UNSIGNED_INTEGER_DECODER.apply(getBits(289, 295));
        this.toStarboard = UNSIGNED_INTEGER_DECODER.apply(getBits(295, 301));
        this.positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(301, 305)));
        this.raimFlag = BOOLEAN_DECODER.apply(getBits(305, 306));
        this.dataTerminalReady = BOOLEAN_DECODER.apply(getBits(306, 307));
        this.assigned = BOOLEAN_DECODER.apply(getBits(307, 308));
        this.regionalReserved3 = BIT_DECODER.apply(getBits(308, 312));
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits != 312)
            errorMessage.append(format("Message of type %s should be at exactly 312 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ExtendedClassBEquipmentPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    @SuppressWarnings("unused")
	public String getRegionalReserved1() {
        return regionalReserved1;
	}

    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        return speedOverGround;
	}

    @SuppressWarnings("unused")
    public Integer getRawSpeedOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(46, 55));
    }

    @SuppressWarnings("unused")
	public Boolean getPositionAccuracy() {
        return positionAccuracy;
	}

    @SuppressWarnings("unused")
    public Boolean getPositionAccurate() {
        return getPositionAccuracy();
    }

    @SuppressWarnings("unused")
	public Float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
    public Integer getRawLatitude() {
        return INTEGER_DECODER.apply(getBits(85, 112));
    }

    @SuppressWarnings("unused")
	public Float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
    public Integer getRawLongitude() {
        return INTEGER_DECODER.apply(getBits(57, 85));
    }

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        return courseOverGround;
	}

    @SuppressWarnings("unused")
    public Integer getRawCourseOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(112, 124));
    }

    @SuppressWarnings("unused")
	public Integer getTrueHeading() {
        return trueHeading;
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        return second;
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved2() {
        return regionalReserved2;
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        return shipName;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        return shipType;
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        return toBow;
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        return toStern;
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        return toStarboard;
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        return toPort;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        return positionFixingDevice;
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        return raimFlag;
	}

    @SuppressWarnings("unused")
	public Boolean getDataTerminalReady() {
        return dataTerminalReady;
	}

    @SuppressWarnings("unused")
	public Boolean getAssigned() {
        return assigned;
	}

    @SuppressWarnings("unused")
    public String getRegionalReserved3() {
        return regionalReserved3;
    }

    @Override
    public String toString() {
        return "ExtendedClassBEquipmentPositionReport{" +
                "messageType=" + getMessageType() +
                ", regionalReserved1='" + getRegionalReserved1() + '\'' +
                ", speedOverGround=" + getSpeedOverGround() +
                ", positionAccuracy=" + getPositionAccuracy() +
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

    private final String regionalReserved1;
    private final Float speedOverGround;
    private final Boolean positionAccuracy;
    private final Float latitude;
    private final Float longitude;
    private final Float courseOverGround;
    private final Integer trueHeading;
    private final Integer second;
    private final String regionalReserved2;
    private final String shipName;
    private final ShipType shipType;
    private final Integer toBow;
    private final Integer toStern;
    private final Integer toStarboard;
    private final Integer toPort;
    private final PositionFixingDevice positionFixingDevice;
    private final Boolean raimFlag;
    private final Boolean dataTerminalReady;
    private final Boolean assigned;
    private final String regionalReserved3;
}
