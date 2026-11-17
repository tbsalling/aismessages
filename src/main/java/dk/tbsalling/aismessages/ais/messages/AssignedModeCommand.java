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
 * Message 16: assignment mode command (ITU-R M.1371-6 § A7-3.14, Table 65). Transmitted by a base station acting as
 * controlling entity to put one or two mobile stations into assigned mode (§ A2-3.3.6), either by assigning a reporting
 * rate (increment 0, offset = number of reports per 10 min, multiples of 20 up to 600) or by assigning transmission
 * slots (offset to the first slot and an increment code 1-6 = 1125, 375, 225, 125, 75 or 45 slots; 7 = disregard).
 * 96 bits for one station, 144 bits for two. The assignment times out 4 to 8 min after the first transmission.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssignedModeCommand extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AssignedModeCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                  MMSI destinationMmsiA, Integer offsetA, Integer incrementA,
                                  MMSI destinationMmsiB, Integer offsetB, Integer incrementB) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.destinationMmsiA = destinationMmsiA;
        this.offsetA = offsetA;
        this.incrementA = incrementA;
        this.destinationMmsiB = destinationMmsiB;
        this.offsetB = offsetB;
        this.incrementB = incrementB;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (IntStream.of(96, 144).noneMatch(l -> numberOfBits == l))
            errorMessage.append(format("Message of type %s should be exactly 96 or 144 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.AssignedModeCommand;
    }

    /** Bits 40-70 (Table 65): destination ID A, the MMSI of the first assigned station. */
    MMSI destinationMmsiA;
    /** Bits 70-82 (Table 65): offset A, from the current slot to the first assigned slot; when increment A is 0, the number of reports per 10 min instead. */
    Integer offsetA;
    /** Bits 82-92 (Table 65): increment A to the next assigned slot, coded 0 = reporting rate assignment, 1 = 1125, 2 = 375, 3 = 225, 4 = 125, 5 = 75, 6 = 45 slots, 7 = undefined (disregard). */
    Integer incrementA;
    /** Bits 92-122 (Table 65): destination ID B, the MMSI of the second assigned station; null in the 96-bit single-station form. */
    MMSI destinationMmsiB;
    /** Bits 122-134 (Table 65): offset B, as offset A; null when absent. */
    Integer offsetB;
    /** Bits 134-144 (Table 65): increment B, coded as increment A; null when absent. */
    Integer incrementB;

}
