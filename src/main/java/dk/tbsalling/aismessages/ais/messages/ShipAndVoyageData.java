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

import dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static dk.tbsalling.aismessages.ais.Decoders.*;

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
        if (numberOfBits != 424 && numberOfBits != 422 && numberOfBits != 426) {
            throw new InvalidMessage("Message of type " + messageType + " expected to be 422, 424 or 426 bits long; not " + numberOfBits);
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

	/** @return The UTC ETA Month (1-12) 0 = not available. */
    @SuppressWarnings("unused")
    public Integer getEtaMonth() {
        return getDecodedValue(() -> etaMonth, value -> etaMonth = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(274, 278)));
    }

    /** @return The UTC ETA Day (1-31) 0 = not available. */
    @SuppressWarnings("unused")
    public Integer getEtaDay() {
        return getDecodedValue(() -> etaDay, value -> etaDay = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(278, 283)));
    }

    /** @return The UTC ETA Hour (0-23) 24 = not available. */
    @SuppressWarnings("unused")
    public Integer getEtaHour() {
        return getDecodedValue(() -> etaHour, value -> etaHour = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(283, 288)));
    }

    /** @return The UTC ETA Minute (0-59) 60 = not available. */
    @SuppressWarnings("unused")
    public Integer getEtaMinute() {
        return getDecodedValue(() -> etaMinute, value -> etaMinute = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(288, 294)));
    }

    @Deprecated
    @SuppressWarnings("unused")
	public String getEta() {
        return String.format("%02d-%02d %02d:%02d", this.getEtaDay(), this.getEtaMonth(), this.getEtaHour(), this.getEtaMinute());
	}

    /**
     * @return The vessel AIS ETA, with the year filled in based on the metadata received time.
     * If the AIS Month and day are the received date or later, the first calendar date after the received is used for the year.
     * Unavailable hour (24) or minute (60) will be treated as 0
     * Examples:
     * Received on 2018-06-01 and ETA AIS is 06-03, this will return 2018-06-03
     * Received on 2018-12-31 and ETA AIS is 01-02, this will return 2019-01-02
     * Received on 2018-06-01 and ETA AIS is 05-31, this will return 2019-05-31
     * Received on 2018-06-01 and ETA AIS is 06-03 24:00, this will return 2018-06-01 00:00
     * Received on 2018-06-01 and ETA AIS is 06-03 12:60, this will return 2018-06-03 12:00
     */
    @SuppressWarnings("unused")
	public Optional<ZonedDateTime> getEtaAfterReceived() {
        Metadata meta = this.getMetadata();
        ZonedDateTime received = (meta == null) ? null : meta.getReceived().atZone(ZoneOffset.UTC);
        if(received == null) {
            return Optional.empty();
        }
        int month = this.getEtaMonth();
        int day = this.getEtaDay();
        int hour = this.getEtaHour() == 24 ? 0 : this.getEtaHour();
        int minute = this.getEtaMinute() == 60 ? 0 : this.getEtaMinute();
        if(month <= 0 || month > 12 || day <= 0 || day > 31 || hour < 0 || hour >= 24 || minute < 0 || minute >= 60) {
            return Optional.empty();
        } else {
            // determine year according to rules from the javadoc
            int year;
            if(month < received.getMonthValue() || (month == received.getMonthValue() && day < received.getDayOfMonth())) {
                year = received.getYear() + 1;
            } else {
                year = received.getYear();
            }
            return Optional.of(ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneOffset.UTC));
        }
    }

    @SuppressWarnings("unused")
	public Float getDraught() {
        return getDecodedValue(() -> draught, value -> draught = value, () -> Boolean.TRUE, () -> UNSIGNED_FLOAT_DECODER.apply(getBits(294, 302)) / 10f);
	}

    @SuppressWarnings("unused")
    public Integer getRawDraught() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(294, 302));
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
                ", eta='" + String.format("%02d-%02d %02d:%02d", this.getEtaDay(), this.getEtaMonth(), this.getEtaHour(), this.getEtaMinute()) + '\'' +
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
    private transient Integer etaMonth;
    private transient Integer etaDay;
    private transient Integer etaHour;
    private transient Integer etaMinute;
    private transient Float draught;
    private transient String destination;
    private transient Boolean dataTerminalReady;
}
