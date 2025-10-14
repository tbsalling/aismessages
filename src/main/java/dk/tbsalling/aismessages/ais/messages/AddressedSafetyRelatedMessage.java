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

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class AddressedSafetyRelatedMessage extends AISMessage {

    protected AddressedSafetyRelatedMessage(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all fields
        this.sequenceNumber = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40));
        this.destinationMmsi = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70)));
        this.retransmit = BOOLEAN_DECODER.apply(getBits(70, 71));
        this.spare = UNSIGNED_INTEGER_DECODER.apply(getBits(71, 72));
        int extraBitsOfChars = ((getNumberOfBits() - 72) / 6) * 6;
        this.text = STRING_DECODER.apply(getBits(72, 72 + extraBitsOfChars));
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.AddressedSafetyRelatedMessage;
    }

    @SuppressWarnings("unused")
	public Integer getSequenceNumber() {
        return sequenceNumber;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi() {
        return destinationMmsi;
	}

    @SuppressWarnings("unused")
	public Boolean getRetransmit() {
        return retransmit;
	}

    @SuppressWarnings("unused")
	public Integer getSpare() {
        return spare;
	}

    @SuppressWarnings("unused")
	public String getText() {
        return text;
	}

    @Override
    public String toString() {
        return "AddressedSafetyRelatedMessage{" +
                "messageType=" + getMessageType() +
                ", sequenceNumber=" + getSequenceNumber() +
                ", destinationMmsi=" + getDestinationMmsi() +
                ", retransmit=" + getRetransmit() +
                ", spare=" + getSpare() +
                ", text='" + getText() + '\'' +
                "} " + super.toString();
    }

    private final Integer sequenceNumber;
    private final MMSI destinationMmsi;
    private final Boolean retransmit;
    private final Integer spare;
    private final String text;
}
