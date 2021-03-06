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
public class BinaryMessageSingleSlot extends AISMessage {

    public BinaryMessageSingleSlot(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BinaryMessageSingleSlot(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuffer errorMessage = new StringBuffer();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits > 168)
            errorMessage.append(format("Message of type %s should be at least 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageSingleSlot;
    }

    @SuppressWarnings("unused")
	public Boolean getDestinationIndicator() {
        return getDecodedValue(() -> destinationIndicator, value -> destinationIndicator = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(38, 39)));
	}

    @SuppressWarnings("unused")
	public Boolean getBinaryDataFlag() {
        return getDecodedValue(() -> binaryDataFlag, value -> binaryDataFlag = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(39, 40)));
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMMSI() {
        return getDecodedValue(() -> destinationMMSI, value -> destinationMMSI = value, () -> Boolean.TRUE, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70))));
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        return getDecodedValue(() -> binaryData, value -> binaryData = value, () -> Boolean.TRUE, () -> BIT_DECODER.apply(getBits(40, 168)));
	}

    @Override
    public String toString() {
        return "BinaryMessageSingleSlot{" +
                "messageType=" + getMessageType() +
                ", destinationIndicator=" + getDestinationIndicator() +
                ", destinationMMSI=" + getDestinationMMSI() +
                ", binaryDataFlag=" + getBinaryDataFlag() +
                ", binaryData='" + getBinaryData() + '\'' +
                "} " + super.toString();
    }

    private transient Boolean destinationIndicator;
    private transient Boolean binaryDataFlag;
    private transient MMSI destinationMMSI;
    private transient String binaryData;
}
