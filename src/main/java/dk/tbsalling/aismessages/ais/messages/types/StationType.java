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
 * Station type selector of Message 23, bits 110-114 (ITU-R M.1371-6 Table 74): which mobile stations a group
 * assignment command applies to.
 */
public enum StationType {
	/** 0 = all types of mobiles (default). */
	AllTypesOfMobileStations(0),
	/** 1 = Class A AIS stations only. */
	ClassAMobileStation(1),
	/** 2 = all types of Class B mobile stations. */
	AllTypesOfClassBMobileStations(2),
	/** 3 = SAR airborne mobile station. */
	SARAirborneMobileStation(3),
	/** 4 = Class B "SO" mobile stations only. */
	ClassBSOMobileStation(4),
	/** 5 = Class B "CS" AIS stations only. */
	ClassBCSShipborneMobileStation(5),
	/** 6 = inland waterways. */
	InlandWaterways(6),
	/** 7 = regional use. */
	RegionalUse1(7),
	/** 8 = regional use. */
	RegionalUse2(8),
	/** 9 = regional use. */
	RegionalUse3(9),
	/** 10 = base station coverage area, used with Message 4 to control Message 27 transmissions; only the area fields apply. */
	BaseStationCoverageArea(10),
	/** 11 = reserved for future use. */
	FutureUse1(11),
	/** 12 = reserved for future use. */
	FutureUse2(12),
	/** 13 = reserved for future use. */
	FutureUse3(13),
	/** 14 = reserved for future use. */
	FutureUse4(14),
	/** 15 = reserved for future use. */
	FutureUse5(15);
	
	StationType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static StationType fromInteger(int code) {
		for (StationType b : StationType.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static StationType fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
