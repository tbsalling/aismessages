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
        this.speed = UNSIGNED_INTEGER_DECODER.apply(getBits(50, 60));
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
    public int getAltitude() {
        return altitude;
	}

    @SuppressWarnings("unused")
    public float getSpeedOverGround() {
        return speed;
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
    public boolean getPositionAccurate() {
        return getPositionAccuracy();
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
    public float getLatitude() {
        return latitude;
    }

    @SuppressWarnings("unused")
    public int getRawLatitude() {
        return INTEGER_DECODER.apply(getBits(89, 116));
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
    public int getSecond() {
        return second;
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved() {
        return regionalReserved;
	}

    @SuppressWarnings("unused")
    public boolean getDataTerminalReady() {
        return dataTerminalReady;
	}

    @SuppressWarnings("unused")
    public boolean getAssigned() {
        return assigned;
	}

    @SuppressWarnings("unused")
    public boolean getRaimFlag() {
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

    private final int altitude;
    private final float speed;
    private final boolean positionAccuracy;
    private final float latitude;
    private final float longitude;
    private final float courseOverGround;
    private final int second;
    private final String regionalReserved;
    private final boolean dataTerminalReady;
    private final boolean assigned;
    private final boolean raimFlag;
    private final String radioStatus;

}
