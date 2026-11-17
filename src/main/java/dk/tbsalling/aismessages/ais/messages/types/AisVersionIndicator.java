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
 * AIS version indicator of Message 5: the edition of Recommendation ITU-R M.1371 the station complies with (ITU-R M.1371-6 Table 50).
 */
public enum AisVersionIndicator {
	/** 0 = station compliant with Recommendation ITU-R M.1371-1. */
	ITU_R_M1371_1(0),
	/** 1 = station compliant with Recommendation ITU-R M.1371-3. */
	ITU_R_M1371_3(1),
	/** 2 = station compliant with Recommendation ITU-R M.1371-5. */
	ITU_R_M1371_5(2),
	/** 3 = station compliant with Recommendation ITU-R M.1371-6 (or later). */
	ITU_R_M1371_6(3);

	AisVersionIndicator(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static AisVersionIndicator fromInteger(int code) {
		for (AisVersionIndicator b : AisVersionIndicator.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static AisVersionIndicator fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
