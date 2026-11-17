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

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Message 23: group assignment command (ITU-R M.1371-6 § A7-3.21, Table 74). Transmitted by a base station acting as
 * controlling entity to all mobile stations within a geographical area that match the station type and ship type
 * selectors, commanding their Tx/Rx mode, reporting interval (Table 75) and a quiet time. Station type 10 marks the
 * base station coverage area used to control Message 27 transmissions. 160 bits.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupAssignmentCommand extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected GroupAssignmentCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                     int spare1, double northEastLongitude, double northEastLatitude,
                                     double southWestLongitude, double southWestLatitude,
                                     StationType stationType, ShipType shipType, int spare2,
                                     TxRxMode transmitReceiveMode, ReportingInterval reportingInterval, int quietTime) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.spare1 = spare1;
        this.northEastLongitude = northEastLongitude;
        this.northEastLatitude = northEastLatitude;
        this.southWestLongitude = southWestLongitude;
        this.southWestLatitude = southWestLatitude;
        this.stationType = stationType;
        this.shipType = shipType;
        this.spare2 = spare2;
        this.transmitReceiveMode = transmitReceiveMode;
        this.reportingInterval = reportingInterval;
        this.quietTime = quietTime;
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.GroupAssignmentCommand;
    }

    /** Bits 38-40 (Table 74): spare, should be zero. */
    int spare1;
    /** Bits 58-75 (Table 74): latitude of the north-east corner of the area in decimal degrees transmitted in 1/10 min; north positive. */
    double northEastLatitude;
    /** Bits 40-58 (Table 74): longitude of the north-east corner of the area in decimal degrees transmitted in 1/10 min; east positive. */
    double northEastLongitude;
    /** Bits 93-110 (Table 74): latitude of the south-west corner of the area in decimal degrees transmitted in 1/10 min. */
    double southWestLatitude;
    /** Bits 75-93 (Table 74): longitude of the south-west corner of the area in decimal degrees transmitted in 1/10 min. */
    double southWestLongitude;
    /** Bits 110-114 (Table 74): station type selector; 0 = all types of mobiles (default), 10 = base station coverage area. */
    StationType stationType;
    /** Bits 114-122 (Table 74, Table 51): type of ship and cargo type selector; 0 = all types (default). */
    ShipType shipType;
    /** Bits 144-146 (Table 74): commanded Tx/Rx mode; 0 = both channels (default). */
    TxRxMode transmitReceiveMode;
    /** Bits 146-150 (Table 74, Table 75): commanded reporting interval; 0 = as given by the autonomous mode. */
    ReportingInterval reportingInterval;
    /** Bits 150-154 (Table 74): quiet time in minutes, 1-15; 0 = no quiet time commanded (default). */
    int quietTime;
    /** Bits 122-144 (Table 74): spare, should be zero. */
    int spare2;
}
