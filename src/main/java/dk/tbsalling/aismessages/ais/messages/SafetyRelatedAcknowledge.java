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
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;
import java.util.stream.IntStream;

import static java.lang.String.format;

/**
 * Message 13: safety related acknowledgement (ITU-R M.1371-6 § A7-3.5 and § A7-3.11, Table 54). Acknowledges up to
 * four addressed safety related messages (Message 12) received from up to four stations, transmitted on the channel the
 * acknowledged message arrived on. 72 to 168 bits in 32-bit steps; same layout as Message 7 ({@link BinaryAcknowledge}).
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SafetyRelatedAcknowledge extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected SafetyRelatedAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                       int spare, MMSI mmsi1, int sequence1,
                                       MMSI mmsi2, int sequence2,
                                       MMSI mmsi3, int sequence3,
                                       MMSI mmsi4, int sequence4,
                                       int numOfAcks) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.spare = spare;
        this.mmsi1 = mmsi1;
        this.sequence1 = sequence1;
        this.mmsi2 = mmsi2;
        this.sequence2 = sequence2;
        this.mmsi3 = mmsi3;
        this.sequence3 = sequence3;
        this.mmsi4 = mmsi4;
        this.sequence4 = sequence4;
        this.numOfAcks = numOfAcks;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (IntStream.of(72, 104, 136, 168).noneMatch(l -> numberOfBits == l))
            errorMessage.append(format("Message of type %s should be exactly 72, 104, 136 or 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.SafetyRelatedAcknowledge;
    }

    /** Bits 38-40 (Table 54): spare, should be zero. */
    int spare;
    /** Bits 40-70 (Table 54): destination ID 1, the MMSI whose message is acknowledged. */
    MMSI mmsi1;
    /** Bits 70-72 (Table 54): sequence number of the acknowledged message from destination 1, 0-3. */
    int sequence1;
    /** Bits 72-102 (Table 54): destination ID 2; null when the message carries fewer than two acknowledgements. */
    MMSI mmsi2;
    /** Bits 102-104 (Table 54): sequence number for destination 2, 0-3; -1 when absent. */
    int sequence2;
    /** Bits 104-134 (Table 54): destination ID 3; null when absent. */
    MMSI mmsi3;
    /** Bits 134-136 (Table 54): sequence number for destination 3, 0-3; -1 when absent. */
    int sequence3;
    /** Bits 136-166 (Table 54): destination ID 4; null when absent. */
    MMSI mmsi4;
    /** Bits 166-168 (Table 54): sequence number for destination 4, 0-3; -1 when absent. */
    int sequence4;
    /** Number of acknowledgements carried, 1-4, derived from the message length (72, 104, 136 or 168 bits). */
    int numOfAcks;
}
