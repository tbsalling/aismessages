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
 * VDES capabilities of Message 24 Part B (ITU-R M.1371-6 Table 77).
 */
public enum VdesCapabilities {
	/** 0 = AIS only. */
	AisOnly(0),
	/** 1 = supports VDES ASM. */
	VdesAsm(1),
	/** 2 = supports VDES ASM/VDE-TER. */
	VdesAsmVdeTer(2),
	/** 3 = supports VDES ASM/VDE-TER/VDE-SAT. */
	VdesAsmVdeTerVdeSat(3);

	VdesCapabilities(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static VdesCapabilities fromInteger(int code) {
		for (VdesCapabilities b : VdesCapabilities.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static VdesCapabilities fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
