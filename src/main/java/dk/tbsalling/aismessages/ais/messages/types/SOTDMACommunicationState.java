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
import lombok.extern.java.Log;

import static java.util.Objects.requireNonNull;

/**
 * SOTDMA communication state (ITU-R M.1371-6 § A2-3.3.7.2.2, Table 18): slot time-out plus a sub-message whose meaning
 * depends on the time-out value (Table 19). Carried by Messages 1, 2, 4 and 11 and, depending on their selector flag,
 * Messages 9, 18 and 26. Exactly one of the sub-message fields is non-null (both UTC fields for time-out 1).
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Log
public class SOTDMACommunicationState extends CommunicationState {

	private SOTDMACommunicationState(SyncState syncState, Integer slotTimeout, Integer numberOfReceivedStations, Integer slotNumber, Integer utcHour, Integer utcMinute, Integer slotOffset) {
		super(syncState);
		this.slotTimeout = slotTimeout;
		this.numberOfReceivedStations = numberOfReceivedStations;
		this.slotNumber = slotNumber;
		this.utcHour = utcHour;
		this.utcMinute = utcMinute;
		this.slotOffset = slotOffset;
	}

    public static SOTDMACommunicationState fromBitString(BitString bitString) {
		requireNonNull(bitString);

        if (bitString.length() != 19)
			return null;

        SyncState syncState = SyncState.fromInteger(bitString.getUnsignedInt(0, 2));
        final int slotTimeout = bitString.getUnsignedInt(2, 5);
		Integer numberOfReceivedStations=null, slotNumber=null, utcHour=null, utcMinute=null, slotOffset=null;

		switch (slotTimeout) {
			case 3, 5, 7 -> {
				numberOfReceivedStations = bitString.getUnsignedInt(5, 19); // 14 bits: always within the valid range 0-16383
			}
			case 2, 4, 6 -> {
				slotNumber = bitString.getUnsignedInt(5, 19);
				if (slotNumber > 2249)
					log.warning("slotNumber: " + slotNumber + ": Out of range.");
			}
			case 1 -> {
				utcHour = bitString.getUnsignedInt(5, 10);
				if (utcHour > 23)
					log.warning("utcHour: " + utcHour + ": Out of range.");
				utcMinute = bitString.getUnsignedInt(10, 17);
				if (utcMinute > 59)
					log.warning("utcMinute: " + utcMinute + ": Out of range.");
			}
			case 0 -> slotOffset = bitString.getUnsignedInt(5, 19);
		}

		return new SOTDMACommunicationState(syncState, slotTimeout, numberOfReceivedStations, slotNumber, utcHour, utcMinute, slotOffset);
	}

    /** Bits 2-5 (Table 18): slot time-out, frames remaining until a new slot is selected, 0-7; 0 = last transmission in this slot. */
    Integer slotTimeout;
    /** Bits 5-19 when slot time-out is 3, 5 or 7 (Table 19): number of other stations the transmitter currently receives, 0-16383; null otherwise. */
    Integer numberOfReceivedStations;
    /** Bits 5-19 when slot time-out is 2, 4 or 6 (Table 19): slot number used for this transmission, 0-2249; null otherwise. */
    Integer slotNumber;
    /** Bits 5-10 when slot time-out is 1 (Table 19): UTC hour, 0-23; null otherwise. */
    Integer utcHour;
    /** Bits 10-17 when slot time-out is 1 (Table 19): UTC minute, 0-59; null otherwise. */
    Integer utcMinute;
    /** Bits 5-19 when slot time-out is 0 (Table 19): offset to the slot used in the next frame; 0 = slot de-allocated after this transmission; null otherwise. */
    Integer slotOffset;

}
