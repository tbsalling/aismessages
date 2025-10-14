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
 * intended to be broadcast by a competent authority (an AIS network-control
 * base station) to set to set operational parameters for all mobile stations in
 * an AIS coverage region
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class GroupAssignmentCommand extends AISMessage {

    protected GroupAssignmentCommand(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all mandatory fields
        this.spare1 = STRING_DECODER.apply(getBits(38, 40));
        this.northEastLongitude = FLOAT_DECODER.apply(getBits(40, 58)) / 10f;
        this.northEastLatitude = FLOAT_DECODER.apply(getBits(58, 75)) / 10f;
        this.southWestLongitude = FLOAT_DECODER.apply(getBits(75, 93)) / 10f;
        this.southWestLatitude = FLOAT_DECODER.apply(getBits(93, 110)) / 10f;
        this.stationType = StationType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(110, 114)));
        this.shipType = ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(114, 122)));
        this.spare2 = STRING_DECODER.apply(getBits(122, 160));
        this.transmitReceiveMode = TxRxMode.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(166, 168)));
        this.reportingInterval = ReportingInterval.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(168, 172)));
        this.quietTime = UNSIGNED_INTEGER_DECODER.apply(getBits(172, 176));
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits != 160)
            errorMessage.append(format("Message of type %s should be at exactly 160 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.GroupAssignmentCommand;
    }

    @SuppressWarnings("unused")
    public String getSpare1() {
        return spare1;
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
	public StationType getStationType() {
        return stationType;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        return shipType;
	}

    @SuppressWarnings("unused")
    public String getSpare2() {
        return spare2;
    }

    @SuppressWarnings("unused")
	public TxRxMode getTransmitReceiveMode() {
        return transmitReceiveMode;
	}

    @SuppressWarnings("unused")
	public ReportingInterval getReportingInterval() {
        return reportingInterval;
	}

    @SuppressWarnings("unused")
	public Integer getQuietTime() {
        return quietTime;
	}

    @Override
    public String toString() {
        return "GroupAssignmentCommand{" +
                "messageType=" + getMessageType() +
                ", spare1='" + getSpare1() + '\'' +
                ", northEastLatitude=" + getNorthEastLatitude() +
                ", northEastLongitude=" + getNorthEastLongitude() +
                ", southWestLatitude=" + getSouthWestLatitude() +
                ", southWestLongitude=" + getSouthWestLongitude() +
                ", stationType=" + getStationType() +
                ", shipType=" + getShipType() +
                ", transmitReceiveMode=" + getTransmitReceiveMode() +
                ", reportingInterval=" + getReportingInterval() +
                ", quietTime=" + getQuietTime() +
                ", spare2='" + getSpare2() + '\'' +
                "} " + super.toString();
    }

    private final String spare1;
    private final Float northEastLatitude;
    private final Float northEastLongitude;
    private final Float southWestLatitude;
    private final Float southWestLongitude;
    private final StationType stationType;
    private final ShipType shipType;
    private final TxRxMode transmitReceiveMode;
    private final ReportingInterval reportingInterval;
    private final Integer quietTime;
    private final String spare2;
}
