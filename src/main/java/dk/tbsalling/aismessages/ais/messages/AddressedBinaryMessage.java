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
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

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

    protected AddressedBinaryMessage(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all fields
        this.sequenceNumber = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40));
        this.destinationMmsi = MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70)));
        this.retransmit = BOOLEAN_DECODER.apply(getBits(70, 71));
        this.spare = UNSIGNED_INTEGER_DECODER.apply(getBits(71, 72));
        this.designatedAreaCode = UNSIGNED_INTEGER_DECODER.apply(getBits(72, 82));
        this.functionalId = UNSIGNED_INTEGER_DECODER.apply(getBits(82, 88));
        this.binaryData = BIT_DECODER.apply(getBits(88, getNumberOfBits()));
        this.applicationSpecificMessage = ApplicationSpecificMessage.create(designatedAreaCode, functionalId, binaryData);
    }

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AddressedBinaryMessage(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock,
                                     int repeatIndicator, MMSI sourceMmsi,
                                     int sequenceNumber, MMSI destinationMmsi, boolean retransmit, int spare,
                                     int designatedAreaCode, int functionalId, String binaryData,
                                     ApplicationSpecificMessage applicationSpecificMessage) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock, repeatIndicator, sourceMmsi);
        this.sequenceNumber = sequenceNumber;
        this.destinationMmsi = destinationMmsi;
        this.retransmit = retransmit;
        this.spare = spare;
        this.designatedAreaCode = designatedAreaCode;
        this.functionalId = functionalId;
        this.binaryData = binaryData;
        this.applicationSpecificMessage = applicationSpecificMessage;
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
    public int getSequenceNumber() {
        return sequenceNumber;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi() {
        return destinationMmsi;
	}

    @SuppressWarnings("unused")
    public boolean getRetransmit() {
        return retransmit;
	}

    @SuppressWarnings("unused")
    public int getSpare() {
        return spare;
	}

    @SuppressWarnings("unused")
    public int getDesignatedAreaCode() {
        return designatedAreaCode;
	}

    @SuppressWarnings("unused")
    public int getFunctionalId() {
        return functionalId;
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        return binaryData;
	}

    @SuppressWarnings("unused")
    public ApplicationSpecificMessage getApplicationSpecificMessage() {
        return applicationSpecificMessage;
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

    private final int sequenceNumber;
    private final MMSI destinationMmsi;
    private final boolean retransmit;
    private final int spare;
    private final int designatedAreaCode;
    private final int functionalId;
    private final String binaryData;
    private final ApplicationSpecificMessage applicationSpecificMessage;
}
