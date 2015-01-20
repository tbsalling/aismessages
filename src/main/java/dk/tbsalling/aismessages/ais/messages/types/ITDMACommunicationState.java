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

package dk.tbsalling.aismessages.ais.messages.types;

import java.io.Serializable;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.INTEGER_DECODER;
import static java.util.Objects.requireNonNull;

public class ITDMACommunicationState extends CommunicationState implements Serializable {

	private ITDMACommunicationState(SyncState syncState, Integer slotIncrement, Integer numberOfSlots, Boolean keepFlag) {
		super(syncState);
		this.slotIncrement = slotIncrement;
		this.numberOfSlots = numberOfSlots;
		this.keepFlag = keepFlag;
	}

	public static ITDMACommunicationState fromBitString(String bitString) {
		requireNonNull(bitString);
		bitString = bitString.trim();

		if (bitString.length() != 19 || !bitString.matches("(0|1)*"))
			return null;

		return new ITDMACommunicationState(
			SyncState.fromInteger(INTEGER_DECODER.apply(bitString.substring(0, 2))),
			INTEGER_DECODER.apply(bitString.substring(2, 15)),
			INTEGER_DECODER.apply(bitString.substring(15, 18)),
			BOOLEAN_DECODER.apply(bitString.substring(18, 19))
		);
	}

	@SuppressWarnings("unused")
	public Integer getSlotIncrement() {
		return slotIncrement;
	}

	@SuppressWarnings("unused")
	public Integer getNumberOfSlots() {
		return numberOfSlots;
	}

	@SuppressWarnings("unused")
	public Boolean getKeepFlag() {
		return keepFlag;
	}

	private final Integer slotIncrement;
	private final Integer numberOfSlots;
	private final Boolean keepFlag;
}
