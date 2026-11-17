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
 * Message 15: interrogation (ITU-R M.1371-6 § A7-3.13, Table 64). Requests one or two specific message types from one
 * station, or one message type each from two stations; the responses are transmitted on the channel the interrogation
 * was received on, in the slot given by the offset when a base station assigns one. Table 63 lists which message types
 * may be requested from which station class. 88, 110 or 160 bits depending on how many requests are present
 * (also accepted at 112 bits, the byte-aligned form of the 110-bit variant).
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Interrogation extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected Interrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                            MMSI interrogatedMmsi1, int type1_1, int offset1_1, int type1_2, int offset1_2,
                            MMSI interrogatedMmsi2, int type2_1, int offset2_1) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.interrogatedMmsi1 = interrogatedMmsi1;
        this.type1_1 = type1_1;
        this.offset1_1 = offset1_1;
        this.type1_2 = type1_2;
        this.offset1_2 = offset1_2;
        this.interrogatedMmsi2 = interrogatedMmsi2;
        this.type2_1 = type2_1;
        this.offset2_1 = offset2_1;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (IntStream.of(88, 110, 112, 160).noneMatch(l -> numberOfBits == l))
            errorMessage.append(format("Message of type %s should be exactly 88, 110, 112 or 160 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.Interrogation;
    }

    /** Bits 40-70 (Table 64): destination ID 1, the MMSI of the first interrogated station. */
    MMSI interrogatedMmsi1;
    /** Bits 70-76 (Table 64): message ID 1.1, the first message type requested from the first station. */
    int type1_1;
    /** Bits 76-88 (Table 64): slot offset 1.1 for the response, relative to this transmission; 0 = responder allocates the slot itself. */
    int offset1_1;
    /** Bits 90-96 (Table 64): message ID 1.2, the second message type requested from the first station; -1 when absent, 0 when set to zero because a second station is interrogated instead. */
    int type1_2;
    /** Bits 96-108 (Table 64): slot offset 1.2 for the second response from the first station; -1 when absent. */
    int offset1_2;
    /** Bits 110-140 (Table 64): destination ID 2, the MMSI of the second interrogated station; null when absent. */
    MMSI interrogatedMmsi2;
    /** Bits 140-146 (Table 64): message ID 2.1, the message type requested from the second station; -1 when absent. */
    int type2_1;
    /** Bits 146-158 (Table 64): slot offset 2.1 for the response from the second station; -1 when absent. */
    int offset2_1;
}
