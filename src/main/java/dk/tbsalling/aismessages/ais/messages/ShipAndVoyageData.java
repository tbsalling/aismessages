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
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.TIME_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * Message has a total of 424 bits, occupying two AIVDM sentences. In practice,
 * the information in these fields (especially ETA and destination) is not
 * reliable, as it has to be hand-updated by humans rather than gathered
 * automatically from sensors.
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class ShipAndVoyageData extends AISMessage implements StaticDataReport {

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
        if (numberOfBits != 424 && numberOfBits != 422) {
            throw new InvalidMessage("Message of type " + messageType + " expected to be 424 bits long; not " + numberOfBits);
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ShipAndVoyageRelatedData;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A;
    }

    @SuppressWarnings("unused")
	public IMO getImo() {
        return getDecodedValue(() -> imo, value -> imo = value, () -> Boolean.TRUE, () -> IMO.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70))));
	}

    @SuppressWarnings("unused")
	public String getCallsign() {
        return getDecodedValue(() -> callsign, value -> callsign = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBits(70, 112)));
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        return getDecodedValue(() -> shipName, value -> shipName = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBits(112, 232)));
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        return getDecodedValue(() -> shipType, value -> shipType = value, () -> Boolean.TRUE, () -> ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(232, 240))));
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        return getDecodedValue(() -> toBow, value -> toBow = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(240, 249)));
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        return getDecodedValue(() -> toStern, value -> toStern = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(249, 258)));
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        return getDecodedValue(() -> toStarboard, value -> toStarboard = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(264, 270)));
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        return getDecodedValue(() -> toPort, value -> toPort = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(258, 264)));
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        return getDecodedValue(() -> positionFixingDevice, value -> positionFixingDevice = value, () -> Boolean.TRUE, () -> PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(270, 274))));
	}

    @SuppressWarnings("unused")
	public String getEta() {
        return getDecodedValue(() -> eta, value -> eta = value, () -> Boolean.TRUE, () -> TIME_DECODER.apply(getBits(274, 294)));
	}

    @SuppressWarnings("unused")
	public Float getDraught() {
        return getDecodedValue(() -> draught, value -> draught = value, () -> Boolean.TRUE, () -> UNSIGNED_FLOAT_DECODER.apply(getBits(294, 302)) / 10f);
	}

    @SuppressWarnings("unused")
	public String getDestination() {
        return getDecodedValue(() -> destination, value -> destination = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBits(302, 422)));
	}

    @SuppressWarnings("unused")
	public Boolean getDataTerminalReady() {
        return getDecodedValue(() -> dataTerminalReady, value -> dataTerminalReady = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(422, 423)));
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
