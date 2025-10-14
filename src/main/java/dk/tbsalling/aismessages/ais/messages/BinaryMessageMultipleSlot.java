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
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

@SuppressWarnings("serial")
public class BinaryMessageMultipleSlot extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected BinaryMessageMultipleSlot(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock,
                                        int repeatIndicator, MMSI sourceMmsi,
                                        boolean addressed, boolean structured, MMSI destinationMmsi,
                                        int applicationId, String data) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock, repeatIndicator, sourceMmsi);
        this.addressed = addressed;
        this.structured = structured;
        this.destinationMmsi = destinationMmsi;
        this.applicationId = applicationId;
        this.data = data;
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageMultipleSlot;
    }

    @SuppressWarnings("unused")
    public boolean getAddressed() {
        return addressed;
    }

    @SuppressWarnings("unused")
    public boolean getStructured() {
        return structured;
    }

    @SuppressWarnings("unused")
    public MMSI getDestinationMmsi() {
        return destinationMmsi;
    }

    @SuppressWarnings("unused")
    public int getApplicationId() {
        return applicationId;
    }

    @SuppressWarnings("unused")
    public String getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public String getRadioStatus() {
        return null; // BIT_DECODER.apply(getBits(6, 8));
    }

    @Override
    public String toString() {
        return "BinaryMessageMultipleSlot{" +
                "messageType=" + getMessageType() +
                ", addressed=" + getAddressed() +
                ", structured=" + getStructured() +
                ", destinationMmsi=" + getDestinationMmsi() +
                ", applicationId=" + getApplicationId() +
                ", data='" + getData() + '\'' +
                ", radioStatus='" + getRadioStatus() + '\'' +
                "} " + super.toString();
    }

    private final boolean addressed;
    private final boolean structured;
    private final MMSI destinationMmsi;
    private final int applicationId;
    private final String data;
    // private final String radioStatus;
}
