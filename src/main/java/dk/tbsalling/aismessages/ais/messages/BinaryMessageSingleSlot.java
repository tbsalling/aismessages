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
 * Message 25: single slot binary message (ITU-R M.1371-6 Table 79).
 * <p>
 * The layout depends on two flags: the destination indicator (addressed messages carry a 30-bit destination ID
 * followed by 2 spare bits) and the binary data flag (structured messages carry a 16-bit application identifier,
 * DAC + FI, in front of the application data). The remaining bits are the application data, at most 128 bits.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BinaryMessageSingleSlot extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BinaryMessageSingleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                      boolean destinationIndicator, boolean binaryDataFlag,
                                      MMSI destinationMMSI, int designatedAreaCode, int functionalId,
                                      BitString data, ApplicationSpecificMessage applicationSpecificMessage) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.destinationIndicator = destinationIndicator;
        this.binaryDataFlag = binaryDataFlag;
        this.destinationMMSI = destinationMMSI;
        this.designatedAreaCode = designatedAreaCode;
        this.functionalId = functionalId;
        this.data = data;
        this.applicationSpecificMessage = applicationSpecificMessage;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits < 40)
            errorMessage.append(format("Message of type %s should be at least 40 bits long; not %d.", getMessageType(), numberOfBits));
        else if (numberOfBits > 168)
            errorMessage.append(format("Message of type %s should be at most 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageSingleSlot;
    }

    /**
     * @return all bits following the two flags (bit 40 to the end of the message) as a string of '0' and '1',
     * regardless of whether they hold a destination ID, an application identifier or application data.
     * @deprecated since 5.1.0; use {@code getDestinationMMSI()}, {@code getDesignatedAreaCode()},
     * {@code getFunctionalId()} and {@code getData()}, which follow the layout of ITU-R M.1371-6 Table 79.
     */
    @Deprecated(since = "5.1.0")
    public String getBinaryData() {
        BitString bitString = getMetadata().bitString();
        return bitString.length() > 40 ? bitString.getBits(40, bitString.length()) : "";
    }

    /** True when the message is addressed to {@link #getDestinationMMSI()}; false for a broadcast. */
    boolean destinationIndicator;
    /** True when the data is structured, i.e. preceded by a 16-bit application identifier (DAC + FI). */
    boolean binaryDataFlag;
    /** Destination of an addressed message; null for a broadcast. */
    MMSI destinationMMSI;
    /** Designated area code of a structured message; -1 when the binary data flag is not set. */
    int designatedAreaCode;
    /** Function identifier of a structured message; -1 when the binary data flag is not set. */
    int functionalId;
    /** The application data (at most 128 bits), excluding destination ID and application identifier. */
    BitString data;
    /** Decoded application specific message of a structured message; null when the binary data flag is not set. */
    ApplicationSpecificMessage applicationSpecificMessage;
}
