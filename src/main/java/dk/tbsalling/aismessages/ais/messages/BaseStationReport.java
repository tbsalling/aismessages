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
import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

/**
 * This message is to be used by fixed-location base stations to periodically report a position and time reference.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class BaseStationReport extends AISMessage {

    protected BaseStationReport(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all fields
        this.year = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 52));
        this.month = UNSIGNED_INTEGER_DECODER.apply(getBits(52, 56));
        this.day = UNSIGNED_INTEGER_DECODER.apply(getBits(56, 61));
        this.hour = UNSIGNED_INTEGER_DECODER.apply(getBits(61, 66));
        this.minute = UNSIGNED_INTEGER_DECODER.apply(getBits(66, 72));
        this.second = UNSIGNED_INTEGER_DECODER.apply(getBits(72, 78));
        this.positionAccurate = BOOLEAN_DECODER.apply(getBits(78, 79));
        this.longitude = FLOAT_DECODER.apply(getBits(79, 107)) / 600000f;
        this.latitude = FLOAT_DECODER.apply(getBits(107, 134)) / 600000f;
        this.positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(134, 138)));
        this.raimFlag = BOOLEAN_DECODER.apply(getBits(148, 149));
        this.communicationState = SOTDMACommunicationState.fromBitString(getBits(149, 168));
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
        return AISMessageType.BaseStationReport;
    }

    @SuppressWarnings("unused")
    public int getYear() {
        return year;
	}

    @SuppressWarnings("unused")
    public int getMonth() {
        return month;
	}

    @SuppressWarnings("unused")
    public int getDay() {
        return day;
	}

    @SuppressWarnings("unused")
    public int getHour() {
        return hour;
	}

    @SuppressWarnings("unused")
    public int getMinute() {
        return minute;
	}

    @SuppressWarnings("unused")
    public int getSecond() {
        return second;
	}

    @SuppressWarnings("unused")
    public boolean getPositionAccurate() {
        return positionAccurate;
	}

    @SuppressWarnings("unused")
    public float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
    public float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        return positionFixingDevice;
	}

    @SuppressWarnings("unused")
    public boolean getRaimFlag() {
        return raimFlag;
	}

    @SuppressWarnings("unused")
    public SOTDMACommunicationState getCommunicationState() {
        return communicationState;
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
                ", communicationState=" + getCommunicationState() +
                "} " + super.toString();
    }

    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;
    private final boolean positionAccurate;
    private final float latitude;
    private final float longitude;
    private final PositionFixingDevice positionFixingDevice;
    private final boolean raimFlag;
    private final SOTDMACommunicationState communicationState;
}
