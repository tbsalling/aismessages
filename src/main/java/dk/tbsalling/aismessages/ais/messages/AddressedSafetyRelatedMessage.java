/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

@SuppressWarnings("serial")
public class AddressedSafetyRelatedMessage extends AISMessage {

    public AddressedSafetyRelatedMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AddressedSafetyRelatedMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public AISMessageType getMessageType() {
        return AISMessageType.AddressedSafetyRelatedMessage;
    }

    @SuppressWarnings("unused")
	public Integer getSequenceNumber() {
        if (sequenceNumber == null) {
            sequenceNumber = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40));
        }
		return sequenceNumber;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi() {
        if (destinationMmsi == null) {
            destinationMmsi = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(38, 40)));
        }
        return destinationMmsi;
	}

    @SuppressWarnings("unused")
	public Boolean getRetransmit() {
        if (retransmit == null) {
            retransmit = BOOLEAN_DECODER.apply(getBits(70, 71));
        }
        return retransmit;
	}

    @SuppressWarnings("unused")
	public Integer getSpare() {
        if (spare == null) {
            spare = UNSIGNED_INTEGER_DECODER.apply(getBits(71, 72));
        }
        return spare;
	}

    @SuppressWarnings("unused")
	public String getText() {
        if (text == null) {
            int extraBitsOfChars = ((getNumberOfBits() - 72) / 6) * 6;
            text = STRING_DECODER.apply(getBits(72, 72 + extraBitsOfChars));
        }
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

    private transient Integer sequenceNumber;
    private transient MMSI destinationMmsi;
    private transient Boolean retransmit;
    private transient Integer spare;
    private transient String text;
}
