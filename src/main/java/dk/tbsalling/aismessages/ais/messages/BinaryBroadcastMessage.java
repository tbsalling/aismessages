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
 * Broadcast message with unspecified binary payload. The St. Lawrence Seaway
 * AIS system and the USG PAWSS system use this payload for local extension
 * messages. It is variable in length up to a maximum of 1008 bits (up to 5
 * AIVDM sentence payloads).
 * 
 * @author tbsalling
 * 
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BinaryBroadcastMessage extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                     Integer spare, Integer designatedAreaCode, Integer functionalId,
                                     String binaryData, ApplicationSpecificMessage applicationSpecificMessage) {
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
                final String bs = getMetadata().bitString();
                errorMessage.append(format(" Unparseable binary payload: \"%s\".", bs.substring(40, Math.min(numberOfBits, bs.length()))));
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

    @SuppressWarnings("unused")
    public ApplicationSpecificMessage getApplicationSpecificMessage() {
        if (applicationSpecificMessage.getDesignatedAreaCode() != designatedAreaCode)
            throw new IllegalStateException("Implementation error: DAC of AISMessage does not match ASM: " + applicationSpecificMessage.getDesignatedAreaCode() + " " + this.getDesignatedAreaCode());

        if (applicationSpecificMessage.getFunctionalId() != functionalId)
            throw new IllegalStateException("Implementation error: FI of AISMessage does not match ASM: " + applicationSpecificMessage.getFunctionalId() + " " + this.getFunctionalId());

        return applicationSpecificMessage;
    }

    int spare;
    int designatedAreaCode;
    int functionalId;
    String binaryData;
    ApplicationSpecificMessage applicationSpecificMessage;

}
