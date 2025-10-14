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
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class UTCAndDateResponse extends AISMessage {

    protected UTCAndDateResponse(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
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
        return AISMessageType.UTCAndDateResponse;
    }

    @SuppressWarnings("unused")
	public Integer getYear() {
        return year;
	}

    @SuppressWarnings("unused")
	public Integer getMonth() {
        return month;
	}

    @SuppressWarnings("unused")
	public Integer getDay() {
        return day;
	}

    @SuppressWarnings("unused")
	public Integer getHour() {
        return hour;
	}

    @SuppressWarnings("unused")
	public Integer getMinute() {
        return minute;
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        return second;
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        return positionAccurate;
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        return positionFixingDevice;
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        return raimFlag;
	}

    @Override
    public String toString() {
        return "UTCAndDateResponse{" +
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

    private final Integer year;
    private final Integer month;
    private final Integer day;
    private final Integer hour;
    private final Integer minute;
    private final Integer second;
    private final Boolean positionAccurate;
    private final Float latitude;
    private final Float longitude;
    private final PositionFixingDevice positionFixingDevice;
    private final Boolean raimFlag;
}
