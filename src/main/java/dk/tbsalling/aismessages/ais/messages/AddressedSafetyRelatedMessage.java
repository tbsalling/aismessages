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
 * Message 12: addressed safety related message (ITU-R M.1371-6 § A7-3.10, Table 59). Point-to-point safety related
 * text in six-bit ASCII from a mobile or base station, acknowledged by Message 13. Variable length, 72 to 1008 bits
 * (1 to 5 slots; Table 60 gives the number of characters per slot count).
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AddressedSafetyRelatedMessage extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AddressedSafetyRelatedMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                            int sequenceNumber, MMSI destinationMmsi,
                                            boolean retransmit, int spare, String text) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.sequenceNumber = sequenceNumber;
        this.destinationMmsi = destinationMmsi;
        this.retransmit = retransmit;
        this.spare = spare;
        this.text = text;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits > 1008)
            errorMessage.append(format("Message of type %s should be at most 1008 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.AddressedSafetyRelatedMessage;
    }

    /** Bits 38-40 (Table 59): sequence number, 0-3, used to pair the message with its acknowledgement (§ A2-5.3.1). */
    int sequenceNumber;
    /** Bits 40-70 (Table 59): MMSI of the destination station. */
    MMSI destinationMmsi;
    /** Bit 70 (Table 59): retransmit flag, true = retransmitted, false = first transmission (default). */
    boolean retransmit;
    /** Bit 71 (Table 59): spare, should be zero. */
    int spare;
    /** Bits 72 to end (Table 59): safety related text in six-bit ASCII (Table 45), at most 156 characters; trailing padding removed. */
    String text;
}
