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

import java.util.stream.IntStream;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class SafetyRelatedAcknowledge extends AISMessage {

    protected SafetyRelatedAcknowledge(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);
        this.spare = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40));
        this.mmsi1 = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70)));
        this.sequence1 = UNSIGNED_INTEGER_DECODER.apply(getBits(70, 72));
        if (getNumberOfBits() > 72) {
            this.mmsi2 = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(72, 102)));
            this.sequence2 = UNSIGNED_INTEGER_DECODER.apply(getBits(102, 104));
        } else {
            this.mmsi2 = null;
            this.sequence2 = null;
        }
        if (getNumberOfBits() > 104) {
            this.mmsi3 = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(104, 134)));
            this.sequence3 = UNSIGNED_INTEGER_DECODER.apply(getBits(134, 136));
        } else {
            this.mmsi3 = null;
            this.sequence3 = null;
        }
        if (getNumberOfBits() > 136) {
            this.mmsi4 = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(136, 166)));
            this.sequence4 = UNSIGNED_INTEGER_DECODER.apply(getBits(166, 168));
        } else {
            this.mmsi4 = null;
            this.sequence4 = null;
        }
        final int numberOfBits = getNumberOfBits();
        int acks = 1;
        if (numberOfBits > 72) {
            acks++;
        }
        if (numberOfBits > 104) {
            acks++;
        }
        if (numberOfBits > 136) {
            acks++;
        }
        this.numOfAcks = acks;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (IntStream.of(72, 104, 136, 168).noneMatch(l -> numberOfBits == l))
            errorMessage.append(format("Message of type %s should be exactly 72, 104, 136 or 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.SafetyRelatedAcknowledge;
    }


    @SuppressWarnings("unused")
    public int getSpare() {
        return spare;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi1() {
        return mmsi1;
	}

    @SuppressWarnings("unused")
    public int getSequence1() {
        return sequence1;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi2() {
        return mmsi2;
	}

    @SuppressWarnings("unused")
	public Integer getSequence2() {
        return sequence2;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi3() {
        return mmsi3;
	}

    @SuppressWarnings("unused")
	public Integer getSequence3() {
        return sequence3;
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi4() {
        return mmsi4;
	}

    @SuppressWarnings("unused")
	public Integer getSequence4() {
        return sequence4;
	}

    @SuppressWarnings("unused")
    public int getNumOfAcks() {
		return numOfAcks;
	}

    @Override
    public String toString() {
        return "SafetyRelatedAcknowledge{" +
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

    private final int spare;
    private final MMSI mmsi1;
    private final int sequence1;
    private final MMSI mmsi2;
    private final Integer sequence2;
    private final MMSI mmsi3;
    private final Integer sequence3;
    private final MMSI mmsi4;
    private final Integer sequence4;
    private final int numOfAcks;
}
