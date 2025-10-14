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

import static java.lang.String.format;

@SuppressWarnings("serial")
public class BinaryMessageSingleSlot extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BinaryMessageSingleSlot(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock,
                                      int repeatIndicator, MMSI sourceMmsi,
                                      boolean destinationIndicator, boolean binaryDataFlag,
                                      MMSI destinationMMSI, String binaryData) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock, repeatIndicator, sourceMmsi);
        this.destinationIndicator = destinationIndicator;
        this.binaryDataFlag = binaryDataFlag;
        this.destinationMMSI = destinationMMSI;
        this.binaryData = binaryData;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits > 168)
            errorMessage.append(format("Message of type %s should be at least 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageSingleSlot;
    }

    @SuppressWarnings("unused")
    public boolean getDestinationIndicator() {
        return destinationIndicator;
	}

    @SuppressWarnings("unused")
    public boolean getBinaryDataFlag() {
        return binaryDataFlag;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMMSI() {
        return destinationMMSI;
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        return binaryData;
	}

    @Override
    public String toString() {
        return "BinaryMessageSingleSlot{" +
                "messageType=" + getMessageType() +
                ", destinationIndicator=" + getDestinationIndicator() +
                ", destinationMMSI=" + getDestinationMMSI() +
                ", binaryDataFlag=" + getBinaryDataFlag() +
                ", binaryData='" + getBinaryData() + '\'' +
                "} " + super.toString();
    }

    private final boolean destinationIndicator;
    private final boolean binaryDataFlag;
    private final MMSI destinationMMSI;
    private final String binaryData;
}
