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
        if (interrogatedMmsi1 == null) {
            interrogatedMmsi1 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(40, 70)));
        }
        return interrogatedMmsi1;
	}

    @SuppressWarnings("unused")
	public final Integer getType1_1() {
        if (type1_1 == null) {
            type1_1 = UNSIGNED_INTEGER_DECODER.apply(getBits(70, 76));
        }
        return type1_1;
	}

    @SuppressWarnings("unused")
	public final Integer getOffset1_1() {
        if (offset1_1 == null) {
            offset1_1 = UNSIGNED_INTEGER_DECODER.apply(getBits(76, 88));
        }
        return offset1_1;
	}

    @SuppressWarnings("unused")
	public final Integer getType1_2() {
        if (getNumberOfBits() > 88 && type1_2 == null) {
            type1_2 = UNSIGNED_INTEGER_DECODER.apply(getBits(90, 96));
        }
        return type1_2;
	}

    @SuppressWarnings("unused")
	public final Integer getOffset1_2() {
        if (getNumberOfBits() > 88 && offset1_2 == null) {
            offset1_2 = UNSIGNED_INTEGER_DECODER.apply(getBits(96, 108));
        }
        return offset1_2;
	}

    @SuppressWarnings("unused")
	public final MMSI getInterrogatedMmsi2() {
        if (getNumberOfBits() > 160 && interrogatedMmsi2 == null) {
            interrogatedMmsi2 = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(110, 140)));
        }
        return interrogatedMmsi2;
	}

    @SuppressWarnings("unused")
	public final Integer getType2_1() {
        if (getNumberOfBits() > 160 && type2_1 == null) {
            type2_1 = UNSIGNED_INTEGER_DECODER.apply(getBits(140, 146));
        }
        return type2_1;
	}

    @SuppressWarnings("unused")
	public final Integer getOffset2_1() {
        if (getNumberOfBits() > 160 && offset2_1 == null) {
            offset2_1 = UNSIGNED_INTEGER_DECODER.apply(getBits(146, 158));
        }
        return offset2_1;
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
