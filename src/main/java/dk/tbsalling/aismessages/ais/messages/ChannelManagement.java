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
import dk.tbsalling.aismessages.ais.messages.types.TxRxMode;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

import static java.lang.String.format;

public class ChannelManagement extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ChannelManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                int channelA, int channelB, TxRxMode transmitReceiveMode, boolean power,
                                Float northEastLongitude, Float northEastLatitude,
                                Float southWestLongitude, Float southWestLatitude,
                                MMSI destinationMmsi1, MMSI destinationMmsi2,
                                boolean addressed, boolean bandA, boolean bandB, int zoneSize) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.channelA = channelA;
        this.channelB = channelB;
        this.transmitReceiveMode = transmitReceiveMode;
        this.power = power;
        this.northEastLongitude = northEastLongitude;
        this.northEastLatitude = northEastLatitude;
        this.southWestLongitude = southWestLongitude;
        this.southWestLatitude = southWestLatitude;
        this.destinationMmsi1 = destinationMmsi1;
        this.destinationMmsi2 = destinationMmsi2;
        this.addressed = addressed;
        this.bandA = bandA;
        this.bandB = bandB;
        this.zoneSize = zoneSize;
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
        return AISMessageType.ChannelManagement;
    }

    @SuppressWarnings("unused")
    public int getChannelA() {
        return channelA;
	}

    @SuppressWarnings("unused")
    public int getChannelB() {
        return channelB;
	}

    @SuppressWarnings("unused")
	public TxRxMode getTransmitReceiveMode() {
        return transmitReceiveMode;
	}

    @SuppressWarnings("unused")
    public boolean getPower() {
        return power;
	}

    @SuppressWarnings("unused")
	public Float getNorthEastLongitude() {
        return northEastLongitude;
	}

    @SuppressWarnings("unused")
	public Float getNorthEastLatitude() {
        return northEastLatitude;
	}

    @SuppressWarnings("unused")
	public Float getSouthWestLongitude() {
        return southWestLongitude;
	}

    @SuppressWarnings("unused")
	public Float getSouthWestLatitude() {
        return southWestLatitude;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi1() {
        return destinationMmsi1;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi2() {
        return destinationMmsi2;
	}

    @SuppressWarnings("unused")
    public boolean getAddressed() {
        return addressed;
	}

    @SuppressWarnings("unused")
    public boolean getBandA() {
        return bandA;
	}

    @SuppressWarnings("unused")
    public boolean getBandB() {
        return bandB;
	}

    @SuppressWarnings("unused")
    public int getZoneSize() {
        return zoneSize;
	}

    @Override
    public String toString() {
        return "ChannelManagement{" +
                "messageType=" + getMessageType() +
                ", channelA=" + getChannelA() +
                ", channelB=" + getChannelB() +
                ", transmitReceiveMode=" + getTransmitReceiveMode() +
                ", power=" + getPower() +
                ", northEastLongitude=" + getNorthEastLongitude() +
                ", northEastLatitude=" + getNorthEastLatitude() +
                ", southWestLongitude=" + getSouthWestLongitude() +
                ", southWestLatitude=" + getSouthWestLatitude() +
                ", destinationMmsi1=" + getDestinationMmsi1() +
                ", destinationMmsi2=" + getDestinationMmsi2() +
                ", addressed=" + getAddressed() +
                ", bandA=" + getBandA() +
                ", bandB=" + getBandB() +
                ", zoneSize=" + getZoneSize() +
                "} " + super.toString();
    }

    private final int channelA;
    private final int channelB;
    private final TxRxMode transmitReceiveMode;
    private final boolean power;
    private final Float northEastLongitude;
    private final Float northEastLatitude;
    private final Float southWestLongitude;
    private final Float southWestLatitude;
    private final MMSI destinationMmsi1;
    private final MMSI destinationMmsi2;
    private final boolean addressed;
    private final boolean bandA;
    private final boolean bandB;
    private final int zoneSize;
}
