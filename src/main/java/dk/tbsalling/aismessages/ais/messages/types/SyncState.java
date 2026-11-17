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

/**
 * Synchronization state, the first 2 bits of the SOTDMA and ITDMA communication states (ITU-R M.1371-6 Tables 18 and
 * 20, § A2-3.1.1, synchronization priorities in Table 9).
 */
public enum SyncState {
	/** 0 = UTC direct: the station has direct access to UTC (§ A2-3.1.1.1). */
	UTCDirect(0),
	/** 1 = UTC indirect: synchronized to a station reporting UTC direct (§ A2-3.1.1.2). */
	UTCIndirect(1),
	/** 2 = synchronized to a base station, base direct (§ A2-3.1.1.3). */
	BaseDirect(2),
	/** 3 = synchronized to another station based on the highest number of received stations, or to a mobile station directly synchronized to a base station (§ A2-3.1.1.3, § A2-3.1.1.4). */
	BaseIndirect(3);

	SyncState(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static SyncState fromInteger(int code) {
		for (SyncState b : SyncState.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static SyncState fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
