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

import java.time.Instant;
import java.util.stream.IntStream;

import static java.lang.String.format;

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

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BinaryAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                int spare, MMSI mmsi1, int sequence1,
                                MMSI mmsi2, Integer sequence2,
                                MMSI mmsi3, Integer sequence3,
                                MMSI mmsi4, Integer sequence4,
                                int numOfAcks) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.spare = spare;
        this.mmsi1 = mmsi1;
        this.sequence1 = sequence1;
        this.mmsi2 = mmsi2;
        this.sequence2 = sequence2;
        this.mmsi3 = mmsi3;
        this.sequence3 = sequence3;
        this.mmsi4 = mmsi4;
        this.sequence4 = sequence4;
        this.numOfAcks = numOfAcks;
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
        return AISMessageType.BinaryAcknowledge;
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
