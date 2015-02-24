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

/**
 * Used by a base station to query one or two other AIS transceivers for status messages of specified types.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class Interrogation extends AISMessage {

    public Interrogation(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected Interrogation(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.Interrogation;
    }

    @SuppressWarnings("unused")
	public final MMSI getInterrogatedMmsi1() {
        return getDecodedValue(() -> interrogatedMmsi1, value -> interrogatedMmsi1 = value, () -> Boolean.TRUE, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70))));
	}

    @SuppressWarnings("unused")
	public final Integer getType1_1() {
        return getDecodedValue(() -> type1_1, value -> type1_1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(70, 76)));
	}

    @SuppressWarnings("unused")
	public final Integer getOffset1_1() {
        return getDecodedValue(() -> offset1_1, value -> offset1_1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(76, 88)));
	}

    @SuppressWarnings("unused")
	public final Integer getType1_2() {
        return getDecodedValue(() -> type1_2, value -> type1_2 = value, () -> getNumberOfBits() > 88, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(90, 96)));
	}

    @SuppressWarnings("unused")
	public final Integer getOffset1_2() {
        return getDecodedValue(() -> offset1_2, value -> offset1_2 = value, () -> getNumberOfBits() > 88, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(96, 108)));
	}

    @SuppressWarnings("unused")
	public final MMSI getInterrogatedMmsi2() {
        return getDecodedValue(() -> interrogatedMmsi2, value -> interrogatedMmsi2 = value, () -> getNumberOfBits() >= 110, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(110, 140))));
	}

    @SuppressWarnings("unused")
	public final Integer getType2_1() {
        return getDecodedValue(() -> type2_1, value -> type2_1 = value, () -> getNumberOfBits() >= 110, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(140, 146)));
	}

    @SuppressWarnings("unused")
	public final Integer getOffset2_1() {
        return getDecodedValue(() -> offset2_1, value -> offset2_1 = value, () -> getNumberOfBits() >= 110, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(146, 158)));
	}

    @Override
    public String toString() {
        return "Interrogation{" +
                "messageType=" + getMessageType() +
                ", interrogatedMmsi1=" + getInterrogatedMmsi1() +
                ", type1_1=" + getType1_1() +
                ", offset1_1=" + getOffset1_1() +
                ", type1_2=" + getType1_2() +
                ", offset1_2=" + getOffset1_2() +
                ", interrogatedMmsi2=" + getInterrogatedMmsi2() +
                ", type2_1=" + getType2_1() +
                ", offset2_1=" + getOffset2_1() +
                "} " + super.toString();
    }

    private transient MMSI interrogatedMmsi1;
	private transient Integer type1_1;
	private transient Integer offset1_1;
	private transient Integer type1_2;
	private transient Integer offset1_2;
	private transient MMSI interrogatedMmsi2;
	private transient Integer type2_1;
	private transient Integer offset2_1;
}
