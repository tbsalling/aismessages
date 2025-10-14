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
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class StandardSARAircraftPositionReport extends AISMessage implements DynamicDataReport {

    protected StandardSARAircraftPositionReport(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all mandatory fields
        this.altitude = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 50));
        this.speed = Float.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(50, 60)));
        this.positionAccuracy = BOOLEAN_DECODER.apply(getBits(60, 61));
        this.longitude = FLOAT_DECODER.apply(getBits(61, 89)) / 600000f;
        this.latitude = FLOAT_DECODER.apply(getBits(89, 116)) / 600000f;
        this.courseOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(116, 128)) / 10f;
        this.second = UNSIGNED_INTEGER_DECODER.apply(getBits(128, 134));
        this.regionalReserved = BIT_DECODER.apply(getBits(134, 142));
        this.dataTerminalReady = BOOLEAN_DECODER.apply(getBits(142, 143));
        this.assigned = BOOLEAN_DECODER.apply(getBits(146, 147));
        this.raimFlag = BOOLEAN_DECODER.apply(getBits(147, 148));
        this.radioStatus = BIT_DECODER.apply(getBits(148, 168));
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

    public AISMessageType getMessageType() {
        return AISMessageType.StandardSARAircraftPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.SAR;
    }

    @SuppressWarnings("unused")
	public Integer getAltitude() {
        return altitude;
	}

    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        return speed;
	}

    @SuppressWarnings("unused")
    public Integer getRawSpeedOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(50, 60));
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
	public Float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
    public Integer getRawLongitude() {
        return INTEGER_DECODER.apply(getBits(61, 89));
    }

    @SuppressWarnings("unused")
    public Float getLatitude() {
        return latitude;
    }

    @SuppressWarnings("unused")
    public Integer getRawLatitude() {
        return INTEGER_DECODER.apply(getBits(89, 116));
    }

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        return courseOverGround;
	}

    @SuppressWarnings("unused")
    public Integer getRawCourseOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(116, 128));
    }

    @SuppressWarnings("unused")
	public Integer getSecond() {
        return second;
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved() {
        return regionalReserved;
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
	public Boolean getRaimFlag() {
        return raimFlag;
	}

    @SuppressWarnings("unused")
	public String getRadioStatus() {
        return radioStatus;
	}

    @Override
    public String toString() {
        return "StandardSARAircraftPositionReport{" +
                "messageType=" + getMessageType() +
                ", altitude=" + getAltitude() +
                ", speed=" + getSpeedOverGround() +
                ", positionAccuracy=" + getPositionAccuracy() +
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

    private final Integer altitude;
    private final Float speed;
    private final Boolean positionAccuracy;
    private final Float latitude;
    private final Float longitude;
    private final Float courseOverGround;
    private final Integer second;
    private final String regionalReserved;
    private final Boolean dataTerminalReady;
    private final Boolean assigned;
    private final Boolean raimFlag;
    private final String radioStatus;

}
