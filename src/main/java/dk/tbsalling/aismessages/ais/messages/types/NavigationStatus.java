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
 * Navigational status of Messages 1, 2, 3 (bits 38-42) and 27 (bits 40-44); ITU-R M.1371-6 Table 46. Set by the user
 * of a Class A station; at anchor or moored with SOG ≤ 3 knots selects the 3-minute reporting interval (Table 1).
 */
public enum NavigationStatus {
	/** 0 = under way using engine. */
	UnderwayUsingEngine (0),
	/** 1 = at anchor. */
	AtAnchor(1),
	/** 2 = not under command. */
	NotUnderCommand(2),
	/** 3 = restricted manoeuvrability. */
	RestrictedManoeuverability(3),
	/** 4 = constrained by her draught. */
	ConstrainedByHerDraught(4),
	/** 5 = moored. */
	Moored(5),
	/** 6 = aground. */
	Aground(6),
	/** 7 = engaged in fishing. */
	EngagedInFising(7),
	/** 8 = under way sailing. */
	UnderwaySailing(8),
	/** 9 = reserved for future use. */
	ReservedForFutureUse9(9),
	/** 10 = reserved for future use. */
	ReservedForFutureUse10(10),
	/** 11 = power-driven vessel towing astern (regional use). */
	PowerDrivenVesselTowingAstern(11),
	/** 12 = power-driven vessel pushing ahead or towing alongside (regional use). */
	PowerDrivenVesselPushingAheadOrTowingAlongside(12),
	/** 13 = reserved for future use. */
	ReservedForFutureUse13(13),
	/** 14 = active AIS-SART, active MOB-AIS or active EPIRB-AIS. */
	SartMobOrEpirb(14),
	/** 15 = undefined (default); also used by AIS-SART, MOB-AIS and EPIRB-AIS under test. */
	Undefined(15);

	NavigationStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static NavigationStatus fromInteger(int code) {
		for (NavigationStatus b : NavigationStatus.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static NavigationStatus fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
