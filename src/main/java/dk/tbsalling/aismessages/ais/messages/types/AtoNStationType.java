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
 * Type of AIS aid-to-navigation station reported in Message 28 (ITU-R M.1371-6 Table 84). See IALA Recommendation
 * R0126, IALA R1016 and IMO MSC Circular 1473 for the definitions.
 */
public enum AtoNStationType {
	PhysicalFloating(0),
	PhysicalFixed(1),
	SyntheticPredicted(2),
	SyntheticMonitored(3),
	Virtual(4),
	Mobile(5),
	ReservedForFutureUse6(6),
	ReservedForFutureUse7(7);

	AtoNStationType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static AtoNStationType fromInteger(int code) {
		for (AtoNStationType b : AtoNStationType.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static AtoNStationType fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
