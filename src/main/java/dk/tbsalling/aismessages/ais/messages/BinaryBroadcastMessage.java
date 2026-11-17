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

import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
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
 * Message 8: binary broadcast message (ITU-R M.1371-6 § A7-3.6, Table 55). Broadcast of application specific data
 * (Annex 4) from a mobile or base station; never acknowledged. Variable length, 56 to 1008 bits (1 to 5 slots; mobile
 * stations should not exceed 3 slots without FATDMA reservations). The 16-bit application identifier (DAC + FI,
 * Table 23) selects the {@link ApplicationSpecificMessage} the data is decoded into.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BinaryBroadcastMessage extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                     Integer spare, Integer designatedAreaCode, Integer functionalId,
                                     BitString binaryData, ApplicationSpecificMessage applicationSpecificMessage) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.spare = spare;
        this.designatedAreaCode = designatedAreaCode;
        this.functionalId = functionalId;
        this.binaryData = binaryData;
        this.applicationSpecificMessage = applicationSpecificMessage;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits < 56) {
            errorMessage.append(format("Message of type %s should be at least 56 bits long; not %d.", getMessageType(), numberOfBits));

            if (numberOfBits >= 40) {
                final BitString bs = getMetadata().bitString();
                errorMessage.append(format(" Unparseable binary payload: \"%s\".", bs.getBits(40, Math.min(numberOfBits, bs.length()))));
            }
        } else if (numberOfBits > 1008)
            errorMessage.append(format("Message of type %s should be at most 1008 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.BinaryBroadcastMessage;
    }

    public ApplicationSpecificMessage getApplicationSpecificMessage() {
        if (applicationSpecificMessage.getDesignatedAreaCode() != designatedAreaCode)
            throw new IllegalStateException("Implementation error: DAC of AISMessage does not match ASM: " + applicationSpecificMessage.getDesignatedAreaCode() + " " + designatedAreaCode);

        if (applicationSpecificMessage.getFunctionalId() != functionalId)
            throw new IllegalStateException("Implementation error: FI of AISMessage does not match ASM: " + applicationSpecificMessage.getFunctionalId() + " " + functionalId);

        return applicationSpecificMessage;
    }

    /** Bits 38-40 (Table 55): spare, should be zero. */
    int spare;
    /** Bits 40-50 (Table 55, Table 23): designated area code of the application identifier, 0 = test, 1-9 = international, 10-999 = regional. */
    int designatedAreaCode;
    /** Bits 50-56 (Table 55, Table 23): function identifier of the application identifier, 0-63. */
    int functionalId;
    /** Bits 56 to end (Table 55): application data, at most 952 bits. */
    BitString binaryData;
    /** The application data decoded according to the application identifier; {@code UnknownApplicationSpecificMessage} for unsupported DAC/FI pairs. */
    ApplicationSpecificMessage applicationSpecificMessage;

}
