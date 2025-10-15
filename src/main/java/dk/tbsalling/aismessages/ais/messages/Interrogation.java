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

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.stream.IntStream;

import static java.lang.String.format;

/**
 * Used by a base station to query one or two other AIS transceivers for status messages of specified types.
 * @author tbsalling
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class Interrogation extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected Interrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
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

    MMSI interrogatedMmsi1;
    int type1_1;
    int offset1_1;
    int type1_2;
    int offset1_2;
    MMSI interrogatedMmsi2;
    int type2_1;
    int offset2_1;
}
