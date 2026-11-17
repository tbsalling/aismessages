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

import dk.tbsalling.aismessages.ais.BitString;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Message 4: base station report (ITU-R M.1371-6 § A7-3.2, Table 49). Transmitted by AIS base stations, normally in
 * FATDMA slots every 10 s (3 1/3 s when acting as synchronization source), to report UTC date and time together with
 * the station's position. Mobile stations use it to determine whether a base station is within 120 NM before obeying
 * its Messages 20 and 23. 168 bits; same layout as Message 11 ({@link UTCAndDateResponse}).
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaseStationReport extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BaseStationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second,
                                boolean positionAccurate, double latitude, double longitude,
                                PositionFixingDevice positionFixingDevice, boolean raimFlag,
                                SOTDMACommunicationState communicationState, boolean satelliteBroadcastTransmissionControl) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.positionAccurate = positionAccurate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.positionFixingDevice = positionFixingDevice;
        this.raimFlag = raimFlag;
        this.communicationState = communicationState;
        this.satelliteBroadcastTransmissionControl = satelliteBroadcastTransmissionControl;
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.BaseStationReport;
    }

    /** Bits 38-52 (Table 49): UTC year, 1-9999; null when not available. */
    Integer year;
    /** Bits 52-56 (Table 49): UTC month, 1-12; null when not available. */
    Integer month;
    /** Bits 56-61 (Table 49): UTC day, 1-31; null when not available. */
    Integer day;
    /** Bits 61-66 (Table 49): UTC hour, 0-23; null when not available. */
    Integer hour;
    /** Bits 66-72 (Table 49): UTC minute, 0-59; null when not available. */
    Integer minute;
    /** Bits 72-78 (Table 49): UTC second, 0-59; null when not available. */
    Integer second;
    /** Bit 78 (Table 49): position accuracy flag, true = high (≤ 10 m), false = low (&gt; 10 m, default); see Table 48. */
    boolean positionAccurate;
    /** Bits 107-134 (Table 49): latitude in decimal degrees, transmitted in 1/10 000 min; north positive; {@code NaN} when not available (transmitted as 91°). */
    double latitude;
    /** Bits 79-107 (Table 49): longitude in decimal degrees, transmitted in 1/10 000 min; east positive; {@code NaN} when not available (transmitted as 181°). */
    double longitude;
    /** Bits 134-138 (Table 49): type of electronic position fixing device. */
    PositionFixingDevice positionFixingDevice;
    /** Bit 148 (Table 49): RAIM flag of the electronic position fixing device, true = RAIM in use; see Table 48. */
    boolean raimFlag;
    /** Bits 149-168 (Table 49): SOTDMA communication state (Table 18). */
    SOTDMACommunicationState communicationState;
    /**
     * Transmission control for AIS satellite broadcast message, bit 138 (ITU-R M.1371-6 Table 49): false = Class A
     * stations stop transmitting Message 27 within this base station's coverage area (default), true = Class A
     * stations are requested to transmit Message 27 within the coverage area. The coverage area is defined by Message 23.
     */
    boolean satelliteBroadcastTransmissionControl;

}
