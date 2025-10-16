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

package dk.tbsalling.aismessages.ais.messages.types;


import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@EqualsAndHashCode(callSuper = true)
public class ITDMACommunicationState extends CommunicationState {

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
                SyncState.fromInteger(BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(0, 2))),
                BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(2, 15)),
                BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(15, 18)),
                BitDecoder.INSTANCE.decodeBoolean(bitString.substring(18, 19))
		);
	}

    Integer slotIncrement;
    Integer numberOfSlots;
    Boolean keepFlag;
}
