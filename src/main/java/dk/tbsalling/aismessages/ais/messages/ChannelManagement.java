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
import dk.tbsalling.aismessages.ais.messages.types.TxRxMode;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Message 22: channel management (ITU-R M.1371-6 § A7-3.20, Table 73). Transmitted by a base station, accompanied by
 * Message 4, to command the VHF data link parameters (channels, Tx/Rx mode, power) either for a geographical area
 * (broadcast form, with the north-east and south-west corners of a Mercator rectangle, § A2-4.1.3) or for one or two
 * individual stations (addressed form, in which the area fields carry the two station IDs and the coordinate getters
 * return null). 168 bits. In M.1371-6 the channel numbers are always 2087 and 2088 and bandwidth and transitional zone
 * fields are always zero.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChannelManagement extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ChannelManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
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

    /** Bits 40-52 (Table 73): channel A number per RR Appendix 18; 2087 = AIS 1 (161.975 MHz). */
    int channelA;
    /** Bits 52-64 (Table 73): channel B number per RR Appendix 18; 2088 = AIS 2 (162.025 MHz). */
    int channelB;
    /** Bits 64-68 (Table 73): Tx/Rx mode; 0 = transmit and receive on both channels (default). */
    TxRxMode transmitReceiveMode;
    /** Bit 68 (Table 73): power, false = high (default), true = low. */
    boolean power;
    /** Bits 69-87 (Table 73): longitude of the north-east corner of the area in decimal degrees (1/10 min / 10); east positive; 181 = not available. Null in the addressed form. */
    Float northEastLongitude;
    /** Bits 87-104 (Table 73): latitude of the north-east corner of the area in decimal degrees (1/10 min / 10); north positive; 91 = not available. Null in the addressed form. */
    Float northEastLatitude;
    /** Bits 104-122 (Table 73): longitude of the south-west corner of the area in decimal degrees; null in the addressed form. */
    Float southWestLongitude;
    /** Bits 122-139 (Table 73): latitude of the south-west corner of the area in decimal degrees; null in the addressed form. */
    Float southWestLatitude;
    /** Bits 69-99 (Table 73): addressed station ID 1 (18 MSBs in the longitude 1 field, 12 LSBs in the latitude 1 field); null in the broadcast form. */
    MMSI destinationMmsi1;
    /** Bits 104-134 (Table 73): addressed station ID 2 (18 MSBs in the longitude 2 field, 12 LSBs in the latitude 2 field); null in the broadcast form. */
    MMSI destinationMmsi2;
    /** Bit 139 (Table 73): addressed or broadcast message indicator, true = addressed to individual stations, false = broadcast for a geographical area (default). */
    boolean addressed;
    /** Bit 140 (Table 73): channel A bandwidth, always false (25 kHz) in M.1371-6. */
    boolean bandA;
    /** Bit 141 (Table 73): channel B bandwidth, always false (25 kHz) in M.1371-6. */
    boolean bandB;
    /** Bits 142-145 (Table 73): transitional zone size, always 0 in M.1371-6. */
    int zoneSize;

}
