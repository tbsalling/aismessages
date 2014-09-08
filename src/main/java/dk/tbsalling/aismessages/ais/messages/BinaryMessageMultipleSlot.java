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

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

@SuppressWarnings("serial")
public class BinaryMessageMultipleSlot extends AISMessage {

    public BinaryMessageMultipleSlot(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BinaryMessageMultipleSlot(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageMultipleSlot;
    }

    @SuppressWarnings("unused")
    public Boolean getAddressed() {
        if (addressed == null) {
            addressed = BOOLEAN_DECODER.apply(getBits(38, 39));
        }
        return addressed;
    }

    @SuppressWarnings("unused")
    public Boolean getStructured() {
        if (structured == null) {
            structured = BOOLEAN_DECODER.apply(getBits(39, 40));
        }
        return structured;
    }

    @SuppressWarnings("unused")
    public MMSI getDestinationMmsi() {
        if (destinationMmsi == null) {
            destinationMmsi = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(40, 70)));
        }
        return destinationMmsi;
    }

    @SuppressWarnings("unused")
    public Integer getApplicationId() {
        if (applicationId == null) {
            applicationId = UNSIGNED_INTEGER_DECODER.apply(getBits(70, 86));
        }
        return applicationId;
    }

    @SuppressWarnings("unused")
    public String getData() {
        if (data == null) {
            data = BIT_DECODER.apply(getBits(86, 86 + 1004 + 1));
        }
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

    private transient Boolean addressed;
    private transient Boolean structured;
    private transient MMSI destinationMmsi;
    private transient Integer applicationId;
    private transient String data;
    // private transient String radioStatus;
}
