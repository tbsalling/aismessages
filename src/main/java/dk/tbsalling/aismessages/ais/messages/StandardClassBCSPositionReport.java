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

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

/**
 * A less detailed report than types 1-3 for vessels using Class B transmitters.
 * Omits navigational status and rate of turn.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class StandardClassBCSPositionReport extends AISMessage implements ExtendedDynamicDataReport {

    protected StandardClassBCSPositionReport(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all mandatory fields
        this.regionalReserved1 = BIT_DECODER.apply(getBits(38, 46));
        this.speedOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(46, 56)) / 10f;
        this.positionAccuracy = BOOLEAN_DECODER.apply(getBits(56, 57));
        this.longitude = FLOAT_DECODER.apply(getBits(57, 85)) / 600000f;
        this.latitude = FLOAT_DECODER.apply(getBits(85, 112)) / 600000f;
        this.courseOverGround = UNSIGNED_FLOAT_DECODER.apply(getBits(112, 124)) / 10f;
        this.trueHeading = UNSIGNED_INTEGER_DECODER.apply(getBits(124, 133));
        this.second = UNSIGNED_INTEGER_DECODER.apply(getBits(133, 139));
        this.regionalReserved2 = BIT_DECODER.apply(getBits(139, 141));
        this.csUnit = BOOLEAN_DECODER.apply(getBits(141, 142));
        this.display = BOOLEAN_DECODER.apply(getBits(142, 143));
        this.dsc = BOOLEAN_DECODER.apply(getBits(143, 144));
        this.band = BOOLEAN_DECODER.apply(getBits(144, 145));
        this.message22 = BOOLEAN_DECODER.apply(getBits(145, 146));
        this.assigned = BOOLEAN_DECODER.apply(getBits(146, 147));
        this.raimFlag = BOOLEAN_DECODER.apply(getBits(147, 148));
        this.commStateSelectorFlag = BOOLEAN_DECODER.apply(getBits(148, 149));

        // Communication state depends on selector flag
        if (!this.commStateSelectorFlag) {
            this.communicationState = SOTDMACommunicationState.fromBitString(getBits(149, 168));
        } else {
            this.communicationState = ITDMACommunicationState.fromBitString(getBits(149, 168));
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

    public final AISMessageType getMessageType() {
        return AISMessageType.StandardClassBCSPositionReport;
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
    public float getSpeedOverGround() {
        return speedOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawSpeedOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(46, 56));
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
    public float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
    public int getRawLatitude() {
        return INTEGER_DECODER.apply(getBits(85, 112));
    }

    @SuppressWarnings("unused")
    public float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
    public int getRawLongitude() {
        return INTEGER_DECODER.apply(getBits(57, 85));
    }

    @SuppressWarnings("unused")
    public float getCourseOverGround() {
        return courseOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawCourseOverGround() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(112, 124));
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
	public String getRegionalReserved2() {
        return regionalReserved2;
	}

    @SuppressWarnings("unused")
    public boolean getCsUnit() {
        return csUnit;
	}

    @SuppressWarnings("unused")
    public boolean getDisplay() {
        return display;
	}

    @SuppressWarnings("unused")
    public boolean getDsc() {
        return dsc;
	}

    @SuppressWarnings("unused")
    public boolean getBand() {
        return band;
	}

    @SuppressWarnings("unused")
    public boolean getMessage22() {
        return message22;
	}

    @SuppressWarnings("unused")
    public boolean getAssigned() {
        return assigned;
	}

    @SuppressWarnings("unused")
    public boolean getRaimFlag() {
        return raimFlag;
	}

    public boolean getCommunicationStateSelectorFlag() {
        return commStateSelectorFlag;
    }

    @SuppressWarnings("unused")
    public CommunicationState getCommunicationState() {
        return communicationState;
    }

    @Override
    public String toString() {
        return "StandardClassBCSPositionReport{" +
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
                ", csUnit=" + getCsUnit() +
                ", display=" + getDisplay() +
                ", dsc=" + getDsc() +
                ", band=" + getBand() +
                ", message22=" + getMessage22() +
                ", assigned=" + getAssigned() +
                ", raimFlag=" + getRaimFlag() +
                ", commStateSelectorFlag=" + getCommunicationStateSelectorFlag() +
                ", commState=" + getCommunicationState() +
                "} " + super.toString();
    }

    private final String regionalReserved1;
    private final float speedOverGround;
    private final boolean positionAccuracy;
    private final float latitude;
    private final float longitude;
    private final float courseOverGround;
    private final int trueHeading;
    private final int second;
    private final String regionalReserved2;
    private final boolean csUnit;
    private final boolean display;
    private final boolean dsc;
    private final boolean band;
    private final boolean message22;
    private final boolean assigned;
    private final boolean raimFlag;
    private final boolean commStateSelectorFlag;
    private final CommunicationState communicationState;

}
