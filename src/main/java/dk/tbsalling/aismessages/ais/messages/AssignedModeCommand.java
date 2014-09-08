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
 * used by a base station with control authority to configure the scheduling of
 * AIS informational messages from subordinate stations, either as a frequency
 * per 10-minute interval or by specifying the TDMA slot(s) offset on which
 * those messages should be transmitted.
 * 
 * @author tbsalling
 * 
 */
public class AssignedModeCommand extends AISMessage {

    public AssignedModeCommand(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AssignedModeCommand(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AssignedModeCommand;
    }

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsiA() {
		if (destinationMmsiA == null) {
            destinationMmsiA = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(40, 70)));
        }
        return destinationMmsiA;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetA() {
        if (offsetA == null) {
            offsetA = UNSIGNED_INTEGER_DECODER.apply(getBits(70, 82));
        }
        return offsetA;
	}

    @SuppressWarnings("unused")
	public Integer getIncrementA() {
        if (incrementA == null) {
            incrementA = UNSIGNED_INTEGER_DECODER.apply(getBits(82, 92));
        }
        return incrementA;
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsiB() {
        if (getNumberOfBits() >= 144 && destinationMmsiB == null) {
            destinationMmsiB = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(92, 122)));
        }
        return destinationMmsiB;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetB() {
        if (getNumberOfBits() >= 144 && offsetB == null) {
            offsetB = UNSIGNED_INTEGER_DECODER.apply(getBits(122, 134));
        }
        return offsetB;
	}

    @SuppressWarnings("unused")
	public Integer getIncrementB() {
        if (getNumberOfBits() >= 144 && incrementB == null) {
            incrementB = UNSIGNED_INTEGER_DECODER.apply(getBits(134, 144));
        }
        return incrementB;
	}

    @Override
    public String toString() {
        return "AssignedModeCommand{" +
                "messageType=" + getMessageType() +
                ", destinationMmsiA=" + getDestinationMmsiA() +
                ", offsetA=" + getOffsetA() +
                ", incrementA=" + getIncrementA() +
                ", destinationMmsiB=" + getDestinationMmsiB() +
                ", offsetB=" + getOffsetB() +
                ", incrementB=" + getIncrementB() +
                "} " + super.toString();
    }

    private transient MMSI destinationMmsiA;
    private transient Integer offsetA;
    private transient Integer incrementA;
    private transient MMSI destinationMmsiB;
    private transient Integer offsetB;
    private transient Integer incrementB;
}
