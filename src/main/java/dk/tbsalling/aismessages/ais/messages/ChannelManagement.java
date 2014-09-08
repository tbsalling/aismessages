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
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.TxRxMode;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

@SuppressWarnings("serial")
public class ChannelManagement extends AISMessage {

    public ChannelManagement(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ChannelManagement(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ChannelManagement;
    }

    @SuppressWarnings("unused")
	public Integer getChannelA() {
        if (channelA == null) {
            channelA = UNSIGNED_INTEGER_DECODER.apply(getBits(40, 52));
        }
        return channelA;
	}

    @SuppressWarnings("unused")
	public Integer getChannelB() {
        if (channelB == null) {
            channelB = UNSIGNED_INTEGER_DECODER.apply(getBits(52, 64));
        }
        return channelB;
	}

    @SuppressWarnings("unused")
	public TxRxMode getTransmitReceiveMode() {
        if (transmitReceiveMode == null) {
            transmitReceiveMode = TxRxMode.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(64, 68)));
        }
        return transmitReceiveMode;
	}

    @SuppressWarnings("unused")
	public Boolean getPower() {
        if (power == null) {
            power = BOOLEAN_DECODER.apply(getBits(68, 69));
        }
        return power;
	}

    @SuppressWarnings("unused")
	public Float getNorthEastLongitude() {
        if (!getAddressed() && northEastLongitude == null) {
            northEastLongitude = FLOAT_DECODER.apply(getBits(69, 87)) / 10f;
        }
        return northEastLongitude;
	}

    @SuppressWarnings("unused")
	public Float getNorthEastLatitude() {
        if (!getAddressed() && northEastLatitude == null) {
            northEastLatitude = FLOAT_DECODER.apply(getBits(87, 104)) / 10f;
        }
        return northEastLatitude;
	}

    @SuppressWarnings("unused")
	public Float getSouthWestLongitude() {
        if (!getAddressed() && southWestLongitude == null) {
            southWestLongitude = FLOAT_DECODER.apply(getBits(104, 122)) / 10f;
        }
        return southWestLongitude;
	}

    @SuppressWarnings("unused")
	public Float getSouthWestLatitude() {
        if (!getAddressed() && southWestLatitude == null) {
            southWestLatitude = FLOAT_DECODER.apply(getBits(122, 138)) / 10f;
        }
        return southWestLatitude;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi1() {
        if (getAddressed() && destinationMmsi1 == null) {
            destinationMmsi1 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(69, 99)));
        }
        return destinationMmsi1;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi2() {
        if (getAddressed() && destinationMmsi2 == null) {
            destinationMmsi2 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(104, 134)));
        }
        return destinationMmsi2;
	}

    @SuppressWarnings("unused")
	public Boolean getAddressed() {
        if (addressed == null) {
            addressed = BOOLEAN_DECODER.apply(getBits(139, 140));
        }
        return addressed;
	}

    @SuppressWarnings("unused")
	public Boolean getBandA() {
        if (bandA == null) {
            bandA = BOOLEAN_DECODER.apply(getBits(140, 141));
        }
        return bandA;
	}

    @SuppressWarnings("unused")
	public Boolean getBandB() {
        if (bandB == null) {
            bandB = BOOLEAN_DECODER.apply(getBits(141, 142));
        }
        return bandB;
	}

    @SuppressWarnings("unused")
	public Integer getZoneSize() {
        if (zoneSize == null) {
            zoneSize = UNSIGNED_INTEGER_DECODER.apply(getBits(142, 145));
        }
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

    private transient Integer channelA;
    private transient Integer channelB;
    private transient TxRxMode transmitReceiveMode;
    private transient Boolean power;
    private transient Float northEastLongitude;
    private transient Float northEastLatitude;
    private transient Float southWestLongitude;
    private transient Float southWestLatitude;
    private transient MMSI destinationMmsi1;
    private transient MMSI destinationMmsi2;
    private transient Boolean addressed;
    private transient Boolean bandA;
    private transient Boolean bandB;
    private transient Integer zoneSize;
}
