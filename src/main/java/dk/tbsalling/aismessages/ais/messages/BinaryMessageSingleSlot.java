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
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

@SuppressWarnings("serial")
public class BinaryMessageSingleSlot extends AISMessage {

    public BinaryMessageSingleSlot(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BinaryMessageSingleSlot(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageSingleSlot;
    }

    @SuppressWarnings("unused")
	public Boolean getDestinationIndicator() {
        if (destinationIndicator == null) {
            destinationIndicator = BOOLEAN_DECODER.apply(getBits(38, 39));
        }
        return destinationIndicator;
	}

    @SuppressWarnings("unused")
	public Boolean getBinaryDataFlag() {
        if (binaryDataFlag == null) {
            binaryDataFlag = BOOLEAN_DECODER.apply(getBits(39, 40));
        }
        return binaryDataFlag;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMMSI() {
        if (destinationMMSI == null) {
            destinationMMSI = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(40, 70)));
        }
        return destinationMMSI;
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        if (binaryData == null) {
            binaryData = BIT_DECODER.apply(getBits(40, 168));
        }
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

    private transient Boolean destinationIndicator;
    private transient Boolean binaryDataFlag;
    private transient MMSI destinationMMSI;
    private transient String binaryData;
}
