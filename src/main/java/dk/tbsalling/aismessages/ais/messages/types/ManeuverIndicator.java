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
 * Special manoeuvre indicator of Messages 1, 2 and 3 (ITU-R M.1371-6 Table 46).
 */
public enum ManeuverIndicator {
	NotAvailable(0),
	NoSpecialManeuver(1),
	/** Engaged in special manoeuvre, i.e. regional passing arrangement on inland waterway. */
	SpecialManeuver(2),
	ReservedForRegionalUse(3);

	ManeuverIndicator(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static ManeuverIndicator fromInteger(int code) {
		for (ManeuverIndicator b : ManeuverIndicator.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static ManeuverIndicator fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
