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

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

/**
 * an addressed point-to-point message with unspecified binary payload. The St.
 * Lawrence Seaway AIS system and the USG PAWSS system use this payload for
 * local extension messages. It is variable in length up to a maximum of 1008
 * bits (up to 5 AIVDM sentence payloads).
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class AddressedBinaryMessage extends AISMessage {

    public AddressedBinaryMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AddressedBinaryMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AddressedBinaryMessage;
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
            destinationMmsi = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(40, 70)));
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
	public int getSpare() {
        if (spare == null) {
            spare = UNSIGNED_INTEGER_DECODER.apply(getBits(71, 72));
        }
        return spare;
	}

    @SuppressWarnings("unused")
	public Integer getDesignatedAreaCode() {
        if (designatedAreaCode == null) {
            designatedAreaCode = UNSIGNED_INTEGER_DECODER.apply(getBits(72, 82));
        }
        return designatedAreaCode;
	}

    @SuppressWarnings("unused")
	public Integer getFunctionalId() {
        if (functionalId == null) {
            functionalId = UNSIGNED_INTEGER_DECODER.apply(getBits(82, 88));
        }
        return functionalId;
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        if (binaryData == null) {
            binaryData = BIT_DECODER.apply(getBits(88, getNumberOfBits()));
        }
        return binaryData;
	}

    @Override
    public String toString() {
        return "AddressedBinaryMessage{" +
                "messageType=" + getMessageType() +
                ", sequenceNumber=" + getSequenceNumber() +
                ", destinationMmsi=" + getDestinationMmsi() +
                ", retransmit=" + getRetransmit() +
                ", spare=" + getSpare() +
                ", designatedAreaCode=" + getDesignatedAreaCode() +
                ", functionalId=" + getFunctionalId() +
                ", binaryData='" + getBinaryData() + '\'' +
                "} " + super.toString();
    }

    private transient Integer sequenceNumber;
    private transient MMSI destinationMmsi;
    private transient Boolean retransmit;
    private transient Integer spare;
    private transient Integer designatedAreaCode;
    private transient Integer functionalId;
    private transient String binaryData;
}
