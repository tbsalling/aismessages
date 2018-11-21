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

import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.lang.ref.WeakReference;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.lang.String.format;

/**
 * broadcast message with unspecified binary payload. The St. Lawrence Seaway
 * AIS system and the USG PAWSS system use this payload for local extension
 * messages. It is variable in length up to a maximum of 1008 bits (up to 5
 * AIVDM sentence payloads).
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class BinaryBroadcastMessage extends AISMessage {

    public BinaryBroadcastMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BinaryBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuffer errorMessage = new StringBuffer();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits <= 56) {
            errorMessage.append(format("Message of type %s should be at least 56 bits long; not %d.", getMessageType(), numberOfBits));

            if (numberOfBits >= 40)
                errorMessage.append(format(" Unparseable binary payload: \"%s\".", getBits(40, numberOfBits)));
        } else if (numberOfBits > 1008)
            errorMessage.append(format("Message of type %s should be at least 56 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryBroadcastMessage;
    }

    @SuppressWarnings("unused")
	public Integer getSpare() {
        return getDecodedValue(() -> spare, ref -> spare = ref, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40)));
	}

    @SuppressWarnings("unused")
	public Integer getDesignatedAreaCode() {
        return getDecodedValue(() -> designatedAreaCode, value -> designatedAreaCode = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(40, 50)));
	}

    @SuppressWarnings("unused")
	public Integer getFunctionalId() {
        return getDecodedValue(() -> functionalId, value -> functionalId = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(50, 56)));
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        return getDecodedValueByWeakReference(() -> binaryData, value -> binaryData = value, () -> Boolean.TRUE, () -> BIT_DECODER.apply(getBits(56, getNumberOfBits())));
	}

    @SuppressWarnings("unused")
    public ApplicationSpecificMessage getApplicationSpecificMessage() {
        ApplicationSpecificMessage asm = this.applicationSpecificMessage == null ? null : this.applicationSpecificMessage.get();
        if (asm == null) {
            asm = ApplicationSpecificMessage.create(getDesignatedAreaCode(), getFunctionalId(), getBinaryData());
            applicationSpecificMessage = new WeakReference<>(asm);
        }

        if (asm.getDesignatedAreaCode() != this.getDesignatedAreaCode().intValue())
            throw new IllegalStateException("Implementation error: DAC of AISMessage does not match ASM: " + asm.getDesignatedAreaCode() + " " + this.getDesignatedAreaCode());

        if (asm.getFunctionalId() != this.getFunctionalId().intValue())
            throw new IllegalStateException("Implementation error: FI of AISMessage does not match ASM: " + asm.getFunctionalId() + " " + this.getFunctionalId());

        return asm;
    }

    @Override
    public String toString() {
        return "BinaryBroadcastMessage{" +
                "messageType=" + getMessageType() +
                ", spare=" + getSpare() +
                ", designatedAreaCode=" + getDesignatedAreaCode() +
                ", functionalId=" + getFunctionalId() +
                ", binaryData='" + getBinaryData() + '\'' +
                "} " + super.toString();
    }

    private transient Integer spare;
    private transient Integer designatedAreaCode;
	private transient Integer functionalId;
    private transient WeakReference<String> binaryData;
	private transient WeakReference<ApplicationSpecificMessage> applicationSpecificMessage;
}
