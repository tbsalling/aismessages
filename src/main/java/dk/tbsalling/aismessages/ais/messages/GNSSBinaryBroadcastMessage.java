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
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * used to broadcast differential corrections for GPS. The data in the payload
 * is intended to be passed directly to GPS receivers capable of accepting such
 * corrections.
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class GNSSBinaryBroadcastMessage extends AISMessage {

    public GNSSBinaryBroadcastMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected GNSSBinaryBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.GNSSBinaryBroadcastMessage;
    }

    @SuppressWarnings("unused")
	public Integer getSpare1() {
        if (spare1 == null) {
            spare1 = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40));
        }
        return spare1;
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        if (latitude == null) {
            latitude = FLOAT_DECODER.apply(getBits(58, 75)) / 10f;
        }
        return latitude;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        if (longitude == null) {
            longitude = FLOAT_DECODER.apply(getBits(40, 58)) / 10f;
        }
        return longitude;
	}

    @SuppressWarnings("unused")
	public Integer getSpare2() {
        if (spare2 == null) {
            spare2 = UNSIGNED_INTEGER_DECODER.apply(getBits(75, 80));
        }
        return spare2;
	}

    @SuppressWarnings("unused")
	public Integer getMType() {
        if (getNumberOfBits() > 80 && mType == null) {
            mType = UNSIGNED_INTEGER_DECODER.apply(getBits(80, 86));
        }
        return mType;
	}

    @SuppressWarnings("unused")
	public Integer getStationId() {
        if (getNumberOfBits() > 80 && stationId == null) {
            stationId = UNSIGNED_INTEGER_DECODER.apply(getBits(86, 96));
        }
        return stationId;
	}

    @SuppressWarnings("unused")
	public Integer getZCount() {
        if (getNumberOfBits() > 80 && zCount == null) {
            zCount = UNSIGNED_INTEGER_DECODER.apply(getBits(96, 109));
        }
        return zCount;
	}

    @SuppressWarnings("unused")
	public Integer getSequenceNumber() {
        if (getNumberOfBits() > 80 && sequenceNumber == null) {
            sequenceNumber = UNSIGNED_INTEGER_DECODER.apply(getBits(109, 112));
        }
        return sequenceNumber;
	}

    @SuppressWarnings("unused")
	public Integer getNumOfWords() {
        if (getNumberOfBits() > 80 && numOfWords == null) {
            numOfWords = UNSIGNED_INTEGER_DECODER.apply(getBits(112, 117));
        }
        return numOfWords;
	}

    @SuppressWarnings("unused")
	public Integer getHealth() {
        if (getNumberOfBits() > 80 && health == null) {
            health = UNSIGNED_INTEGER_DECODER.apply(getBits(117, 120));
        }
        return health;
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        if (getNumberOfBits() > 80 && binaryData == null) {
            binaryData = BIT_DECODER.apply(getBits(80, getNumberOfBits()));
        }
        return binaryData;
	}

    @Override
    public String toString() {
        return "GNSSBinaryBroadcastMessage{" +
                "messageType=" + getMessageType() +
                ", spare1=" + getSpare1() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", spare2=" + getSpare2() +
                ", mType=" + getMType() +
                ", stationId=" + getStationId() +
                ", zCount=" + getZCount() +
                ", sequenceNumber=" + getSequenceNumber() +
                ", numOfWords=" + getNumOfWords() +
                ", health=" + getHealth() +
                ", binaryData='" + getBinaryData() + '\'' +
                "} " + super.toString();
    }

    private transient Integer spare1;
    private transient Float latitude;
    private transient Float longitude;
    private transient Integer spare2;
    private transient Integer mType;
    private transient Integer stationId;
    private transient Integer zCount;
    private transient Integer sequenceNumber;
    private transient Integer numOfWords;
    private transient Integer health;
    private transient String binaryData;
}
