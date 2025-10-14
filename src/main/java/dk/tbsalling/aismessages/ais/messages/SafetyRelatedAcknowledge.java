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

import java.util.stream.IntStream;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class SafetyRelatedAcknowledge extends AISMessage {

    public SafetyRelatedAcknowledge(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected SafetyRelatedAcknowledge(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
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
	public Integer getSpare() {
        return getDecodedValue(() -> spare, value -> spare = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40)));
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi1() {
        return getDecodedValue(() -> mmsi1, value -> mmsi1 = value, () -> Boolean.TRUE, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70))));
	}

    @SuppressWarnings("unused")
	public Integer getSequence1() {
        return getDecodedValue(() -> sequence1, value -> sequence1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(70, 72)));
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi2() {
        return getDecodedValue(() -> mmsi2, value -> mmsi2 = value, () -> getNumberOfBits() > 72, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(72, 102))));
	}

    @SuppressWarnings("unused")
	public Integer getSequence2() {
        return getDecodedValue(() -> sequence2, value -> sequence2 = value, () -> getNumberOfBits() > 72, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(102, 104)));
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi3() {
        return getDecodedValue(() -> mmsi3, value -> mmsi3 = value, () -> getNumberOfBits() > 104, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(104, 134))));
	}

    @SuppressWarnings("unused")
	public Integer getSequence3() {
        return getDecodedValue(() -> sequence3, value -> sequence3 = value, () -> getNumberOfBits() > 104, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(134, 136)));
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi4() {
        return getDecodedValue(() -> mmsi4, value -> mmsi4 = value, () -> getNumberOfBits() > 136, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(136, 166))));
	}

    @SuppressWarnings("unused")
	public Integer getSequence4() {
        return getDecodedValue(() -> sequence4, value -> sequence4 = value, () -> getNumberOfBits() > 136, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(166, 168)));
	}

    @SuppressWarnings("unused")
	public Integer getNumOfAcks() {
        if (numOfAcks == null) {
        	final int numberOfBits = getNumberOfBits();
            numOfAcks = 1;
            if(numberOfBits > 72) {
                numOfAcks ++;
            }
            if(numberOfBits > 104) {
                numOfAcks ++;
            }
            if(numberOfBits > 136) {
                numOfAcks ++;
            }
        }
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
