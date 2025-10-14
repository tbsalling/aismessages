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

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class ChannelManagement extends AISMessage {

    protected ChannelManagement(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);
        this.channelA = UNSIGNED_INTEGER_DECODER.apply(getBits(40, 52));
        this.channelB = UNSIGNED_INTEGER_DECODER.apply(getBits(52, 64));
        this.transmitReceiveMode = TxRxMode.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(64, 68)));
        this.power = BOOLEAN_DECODER.apply(getBits(68, 69));
        this.addressed = BOOLEAN_DECODER.apply(getBits(139, 140));
        if (!addressed) {
            this.northEastLongitude = FLOAT_DECODER.apply(getBits(69, 87)) / 10f;
            this.northEastLatitude = FLOAT_DECODER.apply(getBits(87, 104)) / 10f;
            this.southWestLongitude = FLOAT_DECODER.apply(getBits(104, 122)) / 10f;
            this.southWestLatitude = FLOAT_DECODER.apply(getBits(122, 139)) / 10f;
            this.destinationMmsi1 = null;
            this.destinationMmsi2 = null;
        } else {
            this.northEastLongitude = null;
            this.northEastLatitude = null;
            this.southWestLongitude = null;
            this.southWestLatitude = null;
            this.destinationMmsi1 = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(69, 99)));
            this.destinationMmsi2 = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(104, 134)));
        }
        this.bandA = BOOLEAN_DECODER.apply(getBits(140, 141));
        this.bandB = BOOLEAN_DECODER.apply(getBits(141, 142));
        this.zoneSize = UNSIGNED_INTEGER_DECODER.apply(getBits(142, 145));
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
	public Integer getChannelA() {
        return channelA;
	}

    @SuppressWarnings("unused")
	public Integer getChannelB() {
        return channelB;
	}

    @SuppressWarnings("unused")
	public TxRxMode getTransmitReceiveMode() {
        return transmitReceiveMode;
	}

    @SuppressWarnings("unused")
	public Boolean getPower() {
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
	public Boolean getAddressed() {
        return addressed;
	}

    @SuppressWarnings("unused")
	public Boolean getBandA() {
        return bandA;
	}

    @SuppressWarnings("unused")
	public Boolean getBandB() {
        return bandB;
	}

    @SuppressWarnings("unused")
	public Integer getZoneSize() {
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

    private final Integer channelA;
    private final Integer channelB;
    private final TxRxMode transmitReceiveMode;
    private final Boolean power;
    private final Float northEastLongitude;
    private final Float northEastLatitude;
    private final Float southWestLongitude;
    private final Float southWestLatitude;
    private final MMSI destinationMmsi1;
    private final MMSI destinationMmsi2;
    private final Boolean addressed;
    private final Boolean bandA;
    private final Boolean bandB;
    private final Integer zoneSize;
}
