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
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static java.lang.String.format;

/**
 * used to broadcast differential corrections for GPS. The data in the payload
 * is intended to be passed directly to GPS receivers capable of accepting such
 * corrections.
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class GNSSBinaryBroadcastMessage extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected GNSSBinaryBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock,
                                         int repeatIndicator, MMSI sourceMmsi,
                                         int spare1, float latitude, float longitude, int spare2,
                                         Integer mType, Integer stationId, Integer zCount,
                                         Integer sequenceNumber, Integer numOfWords, Integer health,
                                         String binaryData) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock, repeatIndicator, sourceMmsi);
        this.spare1 = spare1;
        this.latitude = latitude;
        this.longitude = longitude;
        this.spare2 = spare2;
        this.mType = mType;
        this.stationId = stationId;
        this.zCount = zCount;
        this.sequenceNumber = sequenceNumber;
        this.numOfWords = numOfWords;
        this.health = health;
        this.binaryData = binaryData;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits < 80 || numberOfBits > 816)
            errorMessage.append(format("Message of type %s should be 80-816 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.GNSSBinaryBroadcastMessage;
    }

    @SuppressWarnings("unused")
    public int getSpare1() {
        return spare1;
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
    public int getSpare2() {
        return spare2;
	}

    @SuppressWarnings("unused")
	public Integer getMType() {
        return mType;
	}

    @SuppressWarnings("unused")
	public Integer getStationId() {
        return stationId;
	}

    @SuppressWarnings("unused")
	public Integer getZCount() {
        return zCount;
	}

    @SuppressWarnings("unused")
	public Integer getSequenceNumber() {
        return sequenceNumber;
	}

    @SuppressWarnings("unused")
	public Integer getNumOfWords() {
        return numOfWords;
	}

    @SuppressWarnings("unused")
	public Integer getHealth() {
        return health;
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
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

    private final int spare1;
    private final float latitude;
    private final float longitude;
    private final int spare2;
    private final Integer mType;
    private final Integer stationId;
    private final Integer zCount;
    private final Integer sequenceNumber;
    private final Integer numOfWords;
    private final Integer health;
    private final String binaryData;
}
