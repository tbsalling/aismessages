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

import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.lang.ref.WeakReference;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

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

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final int numberOfBits = getNumberOfBits();
        final StringBuilder message = new StringBuilder();

        if (numberOfBits <= 72) {
            message.append(format("Message of type %s should be at least 72 bits long; not %d.", getMessageType(), numberOfBits));

            if (numberOfBits >= 40)
                message.append(format(" Unparseable binary payload: \"%s\".", getBits(40, numberOfBits)));
        }

        if (numberOfBits > 1008)
            message.append(format("Message of type %s should be at most 1008 bits long; not %d.", getMessageType(), numberOfBits));

        if (message.length() > 0) {
            if (numberOfBits >= 38)
                message.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(message.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AddressedBinaryMessage;
    }

    @SuppressWarnings("unused")
    public Integer getSequenceNumber() {
        return getDecodedValue(() -> sequenceNumber, ref -> sequenceNumber = ref, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40)));
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi() {
        return getDecodedValue(() -> destinationMmsi, ref -> destinationMmsi = ref, () -> Boolean.TRUE, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70))));
	}

    @SuppressWarnings("unused")
	public Boolean getRetransmit() {
        return getDecodedValue(() -> retransmit, ref -> retransmit = ref, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(70, 71)));
	}

    @SuppressWarnings("unused")
	public int getSpare() {
        return getDecodedValue(() -> spare, ref -> spare = ref, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(71, 72)));
	}

    @SuppressWarnings("unused")
	public Integer getDesignatedAreaCode() {
        return getDecodedValue(() -> designatedAreaCode, ref -> designatedAreaCode = ref, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(72, 82)));
	}

    @SuppressWarnings("unused")
	public Integer getFunctionalId() {
        return getDecodedValue(() -> functionalId, ref -> functionalId = ref, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(82, 88)));
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        return getDecodedValueByWeakReference(() -> binaryData, ref -> binaryData = ref, () -> Boolean.TRUE, () -> BIT_DECODER.apply(getBits(88, getNumberOfBits())));
	}

    @SuppressWarnings("unused")
    public ApplicationSpecificMessage getApplicationSpecificMessage() {
        ApplicationSpecificMessage asm = this.applicationSpecificMessage == null ? null : this.applicationSpecificMessage.get();
        if (asm == null) {
            asm = ApplicationSpecificMessage.create(getDesignatedAreaCode(), getFunctionalId(), getBinaryData());
            applicationSpecificMessage = new WeakReference<>(asm);
        }

        if (asm.getDesignatedAreaCode() >= 0 && asm.getDesignatedAreaCode() != this.getDesignatedAreaCode().intValue())
            throw new IllegalStateException("Implementation error: DAC of AISMessage does not match ASM: " + asm.getDesignatedAreaCode() + " " + this.getDesignatedAreaCode());

        if (asm.getFunctionalId() >= 0 && asm.getFunctionalId() != this.getFunctionalId().intValue())
            throw new IllegalStateException("Implementation error: FI of AISMessage does not match ASM: " + asm.getFunctionalId() + " " + this.getFunctionalId());

        return asm;
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
    private transient WeakReference<String> binaryData;
    private transient WeakReference<ApplicationSpecificMessage> applicationSpecificMessage;
}
