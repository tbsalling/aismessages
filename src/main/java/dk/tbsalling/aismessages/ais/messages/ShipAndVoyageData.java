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
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

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

    protected ShipAndVoyageData(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all fields
        this.imo = IMO.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70)));
        this.callsign = STRING_DECODER.apply(getBits(70, 112));
        this.shipName = STRING_DECODER.apply(getBits(112, 232));
        this.shipType = ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(232, 240)));
        this.toBow = UNSIGNED_INTEGER_DECODER.apply(getBits(240, 249));
        this.toStern = UNSIGNED_INTEGER_DECODER.apply(getBits(249, 258));
        this.toPort = UNSIGNED_INTEGER_DECODER.apply(getBits(258, 264));
        this.toStarboard = UNSIGNED_INTEGER_DECODER.apply(getBits(264, 270));
        this.positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(270, 274)));
        this.etaMonth = UNSIGNED_INTEGER_DECODER.apply(getBits(274, 278));
        this.etaDay = UNSIGNED_INTEGER_DECODER.apply(getBits(278, 283));
        this.etaHour = UNSIGNED_INTEGER_DECODER.apply(getBits(283, 288));
        this.etaMinute = UNSIGNED_INTEGER_DECODER.apply(getBits(288, 294));
        this.draught = UNSIGNED_FLOAT_DECODER.apply(getBits(294, 302)) / 10f;
        this.destination = STRING_DECODER.apply(getBits(302, 422));
        this.dataTerminalReady = BOOLEAN_DECODER.apply(getBits(422, 423));
    }

    @Override
    protected void checkAISMessage() {
        final AISMessageType messageType = getMessageType();
        if (messageType != AISMessageType.ShipAndVoyageRelatedData) {
            throw new UnsupportedMessageType(messageType.getCode());
        }
        final int numberOfBits = getNumberOfBits();
        if (numberOfBits != 424 && numberOfBits != 422 && numberOfBits != 426) {
            throw new InvalidMessage("Message of type %s expected to be 422, 424 or 426 bits long; not %d".formatted(messageType, numberOfBits));
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
        return imo;
	}

    @SuppressWarnings("unused")
	public String getCallsign() {
        return callsign;
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        return shipName;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        return shipType;
	}

    @SuppressWarnings("unused")
    public int getToBow() {
        return toBow;
	}

    @SuppressWarnings("unused")
    public int getToStern() {
        return toStern;
	}

    @SuppressWarnings("unused")
    public int getToStarboard() {
        return toStarboard;
	}

    @SuppressWarnings("unused")
    public int getToPort() {
        return toPort;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        return positionFixingDevice;
	}

	/** @return The UTC ETA Month (1-12) 0 = not available. */
    @SuppressWarnings("unused")
    public int getEtaMonth() {
        return etaMonth;
    }

    /** @return The UTC ETA Day (1-31) 0 = not available. */
    @SuppressWarnings("unused")
    public int getEtaDay() {
        return etaDay;
    }

    /** @return The UTC ETA Hour (0-23) 24 = not available. */
    @SuppressWarnings("unused")
    public int getEtaHour() {
        return etaHour;
    }

    /** @return The UTC ETA Minute (0-59) 60 = not available. */
    @SuppressWarnings("unused")
    public int getEtaMinute() {
        return etaMinute;
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
    public float getDraught() {
        return draught;
	}

    @SuppressWarnings("unused")
    public int getRawDraught() {
        return UNSIGNED_INTEGER_DECODER.apply(getBits(294, 302));
    }

    @SuppressWarnings("unused")
	public String getDestination() {
        return destination;
	}

    @SuppressWarnings("unused")
    public boolean getDataTerminalReady() {
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
                ", eta='" + String.format("%02d-%02d %02d:%02d", this.getEtaDay(), this.getEtaMonth(), this.getEtaHour(), this.getEtaMinute()) + '\'' +
                ", draught=" + getDraught() +
                ", destination='" + getDestination() + '\'' +
                ", dataTerminalReady=" + getDataTerminalReady() +
                "} " + super.toString();
    }

    private final IMO imo;
    private final String callsign;
    private final String shipName;
    private final ShipType shipType;
    private final int toBow;
    private final int toStern;
    private final int toStarboard;
    private final int toPort;
    private final PositionFixingDevice positionFixingDevice;
    private final int etaMonth;
    private final int etaDay;
    private final int etaHour;
    private final int etaMinute;
    private final float draught;
    private final String destination;
    private final boolean dataTerminalReady;
}
