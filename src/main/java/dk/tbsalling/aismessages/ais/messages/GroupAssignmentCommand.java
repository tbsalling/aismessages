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
import dk.tbsalling.aismessages.ais.messages.types.ReportingInterval;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.ais.messages.types.StationType;
import dk.tbsalling.aismessages.ais.messages.types.TxRxMode;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * intended to be broadcast by a competent authority (an AIS network-control
 * base station) to set to set operational parameters for all mobile stations in
 * an AIS coverage region
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class GroupAssignmentCommand extends AISMessage {

    public GroupAssignmentCommand(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected GroupAssignmentCommand(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.GroupAssignmentCommand;
    }

    @SuppressWarnings("unused")
    public String getSpare1() {
        if (spare1 == null) {
            spare1 = STRING_DECODER.apply(getBits(38, 40));
        }
        return spare1;
    }

    @SuppressWarnings("unused")
    public Float getNorthEastLongitude() {
        if (northEastLongitude == null) {
            northEastLongitude = FLOAT_DECODER.apply(getBits(40, 58)) / 10f;
        }
        return northEastLongitude;
    }

    @SuppressWarnings("unused")
	public Float getNorthEastLatitude() {
        if (northEastLatitude == null) {
            northEastLatitude = FLOAT_DECODER.apply(getBits(58, 75)) / 10f;
        }
        return northEastLatitude;
	}

    @SuppressWarnings("unused")
	public Float getSouthWestLongitude() {
        if (southWestLongitude == null) {
            southWestLongitude = FLOAT_DECODER.apply(getBits(75, 93)) / 10f;
        }
        return southWestLongitude;
	}

    @SuppressWarnings("unused")
    public Float getSouthWestLatitude() {
        if (southWestLatitude == null) {
            southWestLatitude = FLOAT_DECODER.apply(getBits(93, 110)) / 10f;
        }
        return southWestLatitude;
    }

    @SuppressWarnings("unused")
	public StationType getStationType() {
        if (stationType == null) {
            stationType = StationType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(110, 114)));
        }
		return stationType;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        if (shipType == null) {
            shipType = ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(114, 122)));
        }
        return shipType;
	}

    @SuppressWarnings("unused")
    public String getSpare2() {
        if (spare2 == null) {
            spare2 = STRING_DECODER.apply(getBits(122, 166));
        }
        return spare2;
    }

    @SuppressWarnings("unused")
	public TxRxMode getTransmitReceiveMode() {
        if (transmitReceiveMode == null) {
            transmitReceiveMode = TxRxMode.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(166, 168)));
        }
        return transmitReceiveMode;
	}

    @SuppressWarnings("unused")
	public ReportingInterval getReportingInterval() {
        if (reportingInterval == null) {
            reportingInterval = ReportingInterval.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(168, 172)));
        }
        return reportingInterval;
	}

    @SuppressWarnings("unused")
	public Integer getQuietTime() {
        if (quietTime == null) {
            quietTime = UNSIGNED_INTEGER_DECODER.apply(getBits(172, 176));
        }
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

    private transient String spare1;
    private transient Float northEastLatitude;
    private transient Float northEastLongitude;
    private transient Float southWestLatitude;
    private transient Float southWestLongitude;
    private transient StationType stationType;
    private transient ShipType shipType;
    private transient TxRxMode transmitReceiveMode;
    private transient ReportingInterval reportingInterval;
    private transient Integer quietTime;
    private transient String spare2;
}
