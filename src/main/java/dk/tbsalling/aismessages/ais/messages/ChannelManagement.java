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

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.TxRxMode;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChannelManagement extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ChannelManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                int channelA, int channelB, TxRxMode transmitReceiveMode, boolean power,
                                Float northEastLongitude, Float northEastLatitude,
                                Float southWestLongitude, Float southWestLatitude,
                                MMSI destinationMmsi1, MMSI destinationMmsi2,
                                boolean addressed, boolean bandA, boolean bandB, int zoneSize) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.channelA = channelA;
        this.channelB = channelB;
        this.transmitReceiveMode = transmitReceiveMode;
        this.power = power;
        this.northEastLongitude = northEastLongitude;
        this.northEastLatitude = northEastLatitude;
        this.southWestLongitude = southWestLongitude;
        this.southWestLatitude = southWestLatitude;
        this.destinationMmsi1 = destinationMmsi1;
        this.destinationMmsi2 = destinationMmsi2;
        this.addressed = addressed;
        this.bandA = bandA;
        this.bandB = bandB;
        this.zoneSize = zoneSize;
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
        return AISMessageType.ChannelManagement;
    }

    int channelA;
    int channelB;
    TxRxMode transmitReceiveMode;
    boolean power;
    float northEastLongitude;
    float northEastLatitude;
    float southWestLongitude;
    float southWestLatitude;
    MMSI destinationMmsi1;
    MMSI destinationMmsi2;
    boolean addressed;
    boolean bandA;
    boolean bandB;
    int zoneSize;

}
