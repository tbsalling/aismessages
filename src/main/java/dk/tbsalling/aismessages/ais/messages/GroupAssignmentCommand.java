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
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * intended to be broadcast by a competent authority (an AIS network-control
 * base station) to set to set operational parameters for all mobile stations in
 * an AIS coverage region
 * 
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class GroupAssignmentCommand extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected GroupAssignmentCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                     String spare1, float northEastLongitude, float northEastLatitude,
                                     float southWestLongitude, float southWestLatitude,
                                     StationType stationType, ShipType shipType, String spare2,
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

    String spare1;
    float northEastLatitude;
    float northEastLongitude;
    float southWestLatitude;
    float southWestLongitude;
    StationType stationType;
    ShipType shipType;
    TxRxMode transmitReceiveMode;
    ReportingInterval reportingInterval;
    int quietTime;
    String spare2;
}
