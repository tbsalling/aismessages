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

import static java.lang.String.format;


/**
 * Message 20: data link management message (ITU-R M.1371-6 § A7-3.18, Table 70). Transmitted by base stations to
 * pre-announce their FATDMA slot reservations (up to four reservation blocks of offset, number of slots, time-out and
 * increment) on the channel the message is sent on; mobile stations within 120 NM must not use the reserved slots until
 * the time-out expires. 72 to 160 bits, one block per 30 bits. Carries no navigational information.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataLinkManagement extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected DataLinkManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                 int offsetNumber1, int reservedSlots1, int timeout1, int increment1,
                                 Integer offsetNumber2, Integer reservedSlots2, Integer timeout2, Integer increment2,
                                 Integer offsetNumber3, Integer reservedSlots3, Integer timeout3, Integer increment3,
                                 Integer offsetNumber4, Integer reservedSlots4, Integer timeout4, Integer increment4) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.offsetNumber1 = offsetNumber1;
        this.reservedSlots1 = reservedSlots1;
        this.timeout1 = timeout1;
        this.increment1 = increment1;
        this.offsetNumber2 = offsetNumber2;
        this.reservedSlots2 = reservedSlots2;
        this.timeout2 = timeout2;
        this.increment2 = increment2;
        this.offsetNumber3 = offsetNumber3;
        this.reservedSlots3 = reservedSlots3;
        this.timeout3 = timeout3;
        this.increment3 = increment3;
        this.offsetNumber4 = offsetNumber4;
        this.reservedSlots4 = reservedSlots4;
        this.timeout4 = timeout4;
        this.increment4 = increment4;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits < 72 || numberOfBits > 160)
            errorMessage.append(format("Message of type %s should be 72-160 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.DataLinkManagement;
    }

    /** Bits 40-52 (Table 70): offset number 1, from the slot this message was received in to the first reserved slot of block 1. */
    int offsetNumber1;
    /** Bits 52-56 (Table 70): number of consecutive slots reserved in block 1, 1-15 (a block should not exceed 5 slots). */
    int reservedSlots1;
    /** Bits 56-59 (Table 70): time-out of block 1 in minutes, after which the reservation lapses unless refreshed. */
    int timeout1;
    /** Bits 59-70 (Table 70): increment 1 between the starting slots of repeated block 1 reservations; 0 = one block per frame. */
    int increment1;
    /** Bits 70-82 (Table 70): offset number 2; null when the message carries fewer than two blocks. */
    Integer offsetNumber2;
    /** Bits 82-86 (Table 70): number of slots 2, 1-15; null when absent. */
    Integer reservedSlots2;
    /** Bits 86-89 (Table 70): time-out 2 in minutes; null when absent. */
    Integer timeout2;
    /** Bits 89-100 (Table 70): increment 2; null when absent. */
    Integer increment2;
    /** Bits 100-112 (Table 70): offset number 3; null when absent. */
    Integer offsetNumber3;
    /** Bits 112-116 (Table 70): number of slots 3, 1-15; null when absent. */
    Integer reservedSlots3;
    /** Bits 116-119 (Table 70): time-out 3 in minutes; null when absent. */
    Integer timeout3;
    /** Bits 119-130 (Table 70): increment 3; null when absent. */
    Integer increment3;
    /** Bits 130-142 (Table 70): offset number 4; null when absent. */
    Integer offsetNumber4;
    /** Bits 142-146 (Table 70): number of slots 4, 1-15; null when absent. */
    Integer reservedSlots4;
    /** Bits 146-149 (Table 70): time-out 4 in minutes; null when absent. */
    Integer timeout4;
    /** Bits 149-160 (Table 70): increment 4; null when absent. */
    Integer increment4;
}
