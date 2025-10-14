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

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class AddressedSafetyRelatedMessage extends AISMessage {

    public AddressedSafetyRelatedMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AddressedSafetyRelatedMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
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
        return getDecodedValue(() -> sequenceNumber, value -> sequenceNumber=value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40)));
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi() {
        return getDecodedValue(() -> destinationMmsi, value -> destinationMmsi = value, () -> Boolean.TRUE, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70))));
	}

    @SuppressWarnings("unused")
	public Boolean getRetransmit() {
        return getDecodedValue(() -> retransmit, value -> retransmit = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(70, 71)));
	}

    @SuppressWarnings("unused")
	public Integer getSpare() {
        return getDecodedValue(() -> spare, value -> spare = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(71, 72)));
	}

    @SuppressWarnings("unused")
	public String getText() {
        return getDecodedValue(() -> text, value -> text = value, () -> Boolean.TRUE, () -> {
            int extraBitsOfChars = ((getNumberOfBits() - 72)/6)*6;
            return STRING_DECODER.apply(getBits(72, 72 + extraBitsOfChars));
        });
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

    private transient Integer sequenceNumber;
    private transient MMSI destinationMmsi;
    private transient Boolean retransmit;
    private transient Integer spare;
    private transient String text;
}
