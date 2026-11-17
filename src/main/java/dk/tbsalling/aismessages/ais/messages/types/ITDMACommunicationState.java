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


import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static java.util.Objects.requireNonNull;

/**
 * ITDMA communication state (ITU-R M.1371-6 § A2-3.3.7.3.2, Table 20): pre-announces the next transmission slot of
 * an incremental TDMA sequence. Carried by Message 3 and, depending on their selector flag, Messages 9, 18 and 26.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ITDMACommunicationState extends CommunicationState {

	private ITDMACommunicationState(SyncState syncState, Integer slotIncrement, Integer numberOfSlots, Boolean keepFlag) {
		super(syncState);
		this.slotIncrement = slotIncrement;
		this.numberOfSlots = numberOfSlots;
		this.keepFlag = keepFlag;
	}

    public static ITDMACommunicationState fromBitString(BitString bitString) {
		requireNonNull(bitString);

        if (bitString.length() != 19)
			return null;

		return new ITDMACommunicationState(
                SyncState.fromInteger(bitString.getUnsignedInt(0, 2)),
                bitString.getUnsignedInt(2, 15),
                bitString.getUnsignedInt(15, 18),
                bitString.getBoolean(18, 19)
		);
	}

    /** Bits 2-15 (Table 20): offset in slots from this transmission to the next slot to be used, 0-8191; 0 = no more ITDMA transmissions. Add 8192 when {@code numberOfSlots} is 5-7. */
    Integer slotIncrement;
    /** Bits 15-18 (Table 20): number of consecutive slots to allocate, coded 0-4 = 1-5 slots, 5-7 = 1-3 slots at slot increment + 8192. */
    Integer numberOfSlots;
    /** Bit 18 (Table 20): keep flag, true = the slot remains allocated for one additional frame. */
    Boolean keepFlag;
}
