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
import dk.tbsalling.aismessages.ais.messages.types.CommunicationState;
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
 * Message 26: multiple slot binary message with communication state (ITU-R M.1371-6 Table 81).
 * <p>
 * The layout depends on two flags: the destination indicator (addressed messages carry a 30-bit destination ID
 * followed by 2 spare bits) and the binary data flag (structured messages carry a 16-bit application identifier,
 * DAC + FI, in front of the application data). The message ends with 4 spare bits, a 1-bit communication state
 * selector and a 19-bit SOTDMA or ITDMA communication state. Everything in between is the application data.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BinaryMessageMultipleSlot extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BinaryMessageMultipleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                        boolean addressed, boolean structured, MMSI destinationMmsi,
                                        int applicationId, int designatedAreaCode, int functionalId, BitString data,
                                        ApplicationSpecificMessage applicationSpecificMessage,
                                        boolean commStateSelectorFlag, CommunicationState communicationState) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.addressed = addressed;
        this.structured = structured;
        this.destinationMmsi = destinationMmsi;
        this.applicationId = applicationId;
        this.designatedAreaCode = designatedAreaCode;
        this.functionalId = functionalId;
        this.data = data;
        this.applicationSpecificMessage = applicationSpecificMessage;
        this.commStateSelectorFlag = commStateSelectorFlag;
        this.communicationState = communicationState;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits < 64 || numberOfBits > 1064)
            errorMessage.append(format("Message of type %s should be 64-1064 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageMultipleSlot;
    }

    /** True when the message is addressed to {@link #getDestinationMmsi()}; false for a broadcast. */
    boolean addressed;
    /** True when the data is structured, i.e. preceded by a 16-bit application identifier (DAC + FI). */
    boolean structured;
    /** Destination of an addressed message; null for a broadcast. */
    MMSI destinationMmsi;
    /** The 16-bit application identifier (DAC in the upper 10 bits, FI in the lower 6) of a structured message; -1 when unstructured. */
    int applicationId;
    /** Designated area code of a structured message; -1 when unstructured. */
    int designatedAreaCode;
    /** Function identifier of a structured message; -1 when unstructured. */
    int functionalId;
    /** The application data, excluding destination ID, application identifier and the trailing communication state. */
    BitString data;
    /** Decoded application specific message of a structured message; null when unstructured. */
    ApplicationSpecificMessage applicationSpecificMessage;
    /** False when {@link #getCommunicationState()} is SOTDMA, true when it is ITDMA. */
    boolean commStateSelectorFlag;
    /** The communication state carried in the last 19 bits of the message. */
    CommunicationState communicationState;
}
