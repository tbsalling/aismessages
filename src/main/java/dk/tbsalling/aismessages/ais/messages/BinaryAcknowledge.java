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

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

/**
 * a receipt acknowledgement to the senders of a previous messages of type 6.
 * Total length varies between 72 and 168 bits by 32-bit increments, depending
 * on the number of destination MMSIs included.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class BinaryAcknowledge extends AISMessage {

    public BinaryAcknowledge(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BinaryAcknowledge(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryAcknowledge;
    }

    @SuppressWarnings("unused")
	public Integer getSpare() {
        if (spare == null) {
            spare = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40));
        }
        return spare;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi1() {
        if (mmsi1 == null) {
            mmsi1 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(40, 70)));
        }
        return mmsi1;
	}

    @SuppressWarnings("unused")
	public Integer getSequence1() {
        if (sequence1 == null) {
            sequence1 = UNSIGNED_INTEGER_DECODER.apply(getBits(70, 72));
        }
        return sequence1;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi2() {
        if (getNumberOfBits() > 72 && mmsi2 == null) {
            mmsi2 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(72, 102)));
        }
        return mmsi2;
	}

    @SuppressWarnings("unused")
	public Integer getSequence2() {
        if (getNumberOfBits() > 72 && sequence2 == null) {
            sequence2 = UNSIGNED_INTEGER_DECODER.apply(getBits(102, 104));
        }
        return sequence2;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi3() {
        if (getNumberOfBits() > 104 && mmsi3 == null) {
            mmsi3 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(104, 134)));
        }
        return mmsi3;
	}

    @SuppressWarnings("unused")
	public Integer getSequence3() {
        if (getNumberOfBits() > 104 && sequence3 == null) {
            sequence3 = UNSIGNED_INTEGER_DECODER.apply(getBits(134, 136));
        }
        return sequence3;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi4() {
        if (getNumberOfBits() > 136 && mmsi4 == null) {
            mmsi4 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(136, 166)));
        }
        return mmsi4;
	}

    @SuppressWarnings("unused")
	public Integer getSequence4() {
        if (getNumberOfBits() > 136 && sequence4 == null) {
            sequence4 = UNSIGNED_INTEGER_DECODER.apply(getBits(166, 168));
        }
        return sequence4;
	}

    @SuppressWarnings("unused")
	public Integer getNumOfAcks() {
        if (numOfAcks == null) {
            final int numberOfBits = getNumberOfBits();
            if (numberOfBits > 72) {
                numOfAcks++;
            }
            if (numberOfBits > 104) {
                numOfAcks++;
            }
            if (numberOfBits > 136) {
                numOfAcks++;
            }
        }
        return numOfAcks;
	}

    @Override
    public String toString() {
        return "BinaryAcknowledge{" +
                "messageType=" + getMessageType() +
                ", spare=" + getSpare() +
                ", mmsi1=" + getMmsi1() +
                ", sequence1=" + getSequence1() +
                ", mmsi2=" + getMmsi2() +
                ", sequence2=" + getSequence2() +
                ", mmsi3=" + getMmsi3() +
                ", sequence3=" + getSequence3() +
                ", mmsi4=" + getMmsi4() +
                ", sequence4=" + getSequence4() +
                ", numOfAcks=" + getNumOfAcks() +
                "} " + super.toString();
    }

    private transient Integer spare;
	private transient MMSI mmsi1;
	private transient Integer sequence1;
	private transient MMSI mmsi2;
	private transient Integer sequence2;
	private transient MMSI mmsi3;
	private transient Integer sequence3;
	private transient MMSI mmsi4;
	private transient Integer sequence4;
	private transient Integer numOfAcks;
}
