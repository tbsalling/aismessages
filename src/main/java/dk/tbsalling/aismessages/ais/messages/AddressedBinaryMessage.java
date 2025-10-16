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
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * an addressed point-to-point message with unspecified binary payload. The St.
 * Lawrence Seaway AIS system and the USG PAWSS system use this payload for
 * local extension messages. It is variable in length up to a maximum of 1008
 * bits (up to 5 AIVDM sentence payloads).
 * 
 * @author tbsalling
 * 
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AddressedBinaryMessage extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AddressedBinaryMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                     int sequenceNumber, MMSI destinationMmsi, boolean retransmit, int spare,
                                     int designatedAreaCode, int functionalId, String binaryData,
                                     ApplicationSpecificMessage applicationSpecificMessage) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.sequenceNumber = sequenceNumber;
        this.destinationMmsi = destinationMmsi;
        this.retransmit = retransmit;
        this.spare = spare;
        this.designatedAreaCode = designatedAreaCode;
        this.functionalId = functionalId;
        this.binaryData = binaryData;
        this.applicationSpecificMessage = applicationSpecificMessage;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final int numberOfBits = getNumberOfBits();
        final StringBuilder message = new StringBuilder();

        if (numberOfBits <= 72) {
            message.append(format("Message of type %s should be at least 72 bits long; not %d.", getMessageType(), numberOfBits));

            if (numberOfBits >= 40) {
                final String bs = getMetadata().bitString();
                message.append(format(" Unparseable binary payload: \"%s\".", bs.substring(40, Math.min(numberOfBits, bs.length()))));
            }
        }

        if (numberOfBits > 1008)
            message.append(format("Message of type %s should be at most 1008 bits long; not %d.", getMessageType(), numberOfBits));

        if (message.length() > 0) {
            if (numberOfBits >= 38)
                message.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(message.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.AddressedBinaryMessage;
    }

    int sequenceNumber;
    MMSI destinationMmsi;
    boolean retransmit;
    int spare;
    int designatedAreaCode;
    int functionalId;
    String binaryData;
    ApplicationSpecificMessage applicationSpecificMessage;
}
