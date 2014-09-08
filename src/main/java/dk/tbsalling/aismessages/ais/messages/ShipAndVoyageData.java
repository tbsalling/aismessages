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

import dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.IMO;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.TIME_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

/**
 * Message has a total of 424 bits, occupying two AIVDM sentences. In practice,
 * the information in these fields (especially ETA and destination) is not
 * reliable, as it has to be hand-updated by humans rather than gathered
 * automatically from sensors.
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class ShipAndVoyageData extends AISMessage {

    public ShipAndVoyageData(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ShipAndVoyageData(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    @Override
    protected void checkAISMessage() {
        final AISMessageType messageType = getMessageType();
        if (messageType != AISMessageType.ShipAndVoyageRelatedData) {
            throw new UnsupportedMessageType(messageType.getCode());
        }
        final int numberOfBits = getNumberOfBits();
        if (numberOfBits != 424) {
            throw new InvalidMessage("Message of type " + messageType + " expected to be 424 bits long; not " + numberOfBits);
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ShipAndVoyageRelatedData;
    }

    @SuppressWarnings("unused")
	public IMO getImo() {
        if (imo == null) {
            imo = IMO.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(40, 70)));
        }
        return imo;
	}

    @SuppressWarnings("unused")
	public String getCallsign() {
        if (callsign == null) {
            callsign = STRING_DECODER.apply(getBits(70, 112));
        }
        return callsign;
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        if (shipName == null) {
            shipName = STRING_DECODER.apply(getBits(112, 232));
        }
        return shipName;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        if (shipType == null) {
            shipType = ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(232, 240)));
        }
        return shipType;
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        if (toBow == null) {
            toBow = UNSIGNED_INTEGER_DECODER.apply(getBits(240, 249));
        }
        return toBow;
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        if (toStern == null) {
            toStern = UNSIGNED_INTEGER_DECODER.apply(getBits(249, 258));
        }
        return toStern;
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        if (toStarboard == null) {
            toStarboard = UNSIGNED_INTEGER_DECODER.apply(getBits(264, 270));
        }
        return toStarboard;
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        if (toPort == null) {
            toPort = UNSIGNED_INTEGER_DECODER.apply(getBits(258, 264));
        }
        return toPort;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        if (positionFixingDevice == null) {
            positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(270, 274)));
        }
        return positionFixingDevice;
	}

    @SuppressWarnings("unused")
	public String getEta() {
        if (eta == null) {
            eta = TIME_DECODER.apply(getBits(274, 294));
        }
        return eta;
	}

    @SuppressWarnings("unused")
	public Float getDraught() {
        if (draught == null) {
            draught = UNSIGNED_FLOAT_DECODER.apply(getBits(294, 302)) / 10f;
        }
        return draught;
	}

    @SuppressWarnings("unused")
	public String getDestination() {
        if (destination == null) {
            destination = STRING_DECODER.apply(getBits(302, 422));
        }
        return destination;
	}

    @SuppressWarnings("unused")
	public Boolean getDataTerminalReady() {
        if (dataTerminalReady == null) {
            dataTerminalReady = BOOLEAN_DECODER.apply(getBits(422, 423));
        }
        return dataTerminalReady;
	}

    @Override
    public String toString() {
        return "ShipAndVoyageData{" +
                "messageType=" + getMessageType() +
                ", imo=" + getImo() +
                ", callsign='" + getCallsign() + '\'' +
                ", shipName='" + getShipName() + '\'' +
                ", shipType=" + getShipType() +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toStarboard=" + getToStarboard() +
                ", toPort=" + getToPort() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", eta='" + getEta() + '\'' +
                ", draught=" + getDraught() +
                ", destination='" + getDestination() + '\'' +
                ", dataTerminalReady=" + getDataTerminalReady() +
                "} " + super.toString();
    }

    private transient IMO imo;
    private transient String callsign;
    private transient String shipName;
    private transient ShipType shipType;
    private transient Integer toBow;
    private transient Integer toStern;
    private transient Integer toStarboard;
    private transient Integer toPort;
    private transient PositionFixingDevice positionFixingDevice;
    private transient String eta;
    private transient Float draught;
    private transient String destination;
    private transient Boolean dataTerminalReady;
}
