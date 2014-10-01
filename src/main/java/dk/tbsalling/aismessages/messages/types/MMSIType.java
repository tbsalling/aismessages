/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.messages.types;

public enum MMSIType {
	Invalid(-1),
	ShipGroup(0),
	SARAircraft(1),
	Europe(2),
	NorthCentralAmericaCaribbean(3),
	Asia(4),
	Oceana(5),
	Africa(6),
	SouthAmerica(7),
	Handheld(8),
	FreeForm(9),
	SARTransponders(970),
	Overboard(972),
	EPIRB(974),
	Auxiliary(98),
	NavigationalAid(99);


	MMSIType(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}
	
	private static Integer detectCode(String mmsi) {
		if (mmsi.matches(shipGroupRegExp))
			return ShipGroup.code;
		else
		if (mmsi.matches(sarAircraftRegExp))
			return SARAircraft.code;
		else
		if (mmsi.matches(europeRegExp))
			return Europe.code;
		else
		if (mmsi.matches(northCentralAmericaCaribbeanRegExp))
			return NorthCentralAmericaCaribbean.code;
		else
		if (mmsi.matches(asiaRegExp))
			return Asia.code;
		else
		if (mmsi.matches(oceanaRegExp))
			return Oceana.code;
		else
		if (mmsi.matches(africaRegExp))
			return Africa.code;
		else
		if (mmsi.matches(southAmericaRegExp))
			return SouthAmerica.code;
		else
		if (mmsi.matches(handheldRegExp))
			return Handheld.code;
		else
		if (mmsi.matches(sarTranspondersRegExp))
			return SARTransponders.code;
		else
		if (mmsi.matches(overboardRegExp))
			return Overboard.code;
		else
		if (mmsi.matches(epirbRegExp))
			return EPIRB.code;
		else
		if (mmsi.matches(auxiliaryRegExp))
			return Auxiliary.code;
		else
		if (mmsi.matches(navigationalAidRegExp))
			return NavigationalAid.code;
		else
		if (mmsi.matches(freeFormRegExp))
				return FreeForm.code;
		else
			return null;
	}

	private final Integer code;
	private static final String shipGroupRegExp 					= "^0\\d{8}$";
	private static final String sarAircraftRegExp 					= "^1\\d{8}$";
	private static final String europeRegExp 						= "^2\\d{8}$";
	private static final String northCentralAmericaCaribbeanRegExp 	= "^3\\d{8}$";
	private static final String asiaRegExp 							= "^4\\d{8}$";
	private static final String oceanaRegExp 						= "^5\\d{8}$";
	private static final String africaRegExp 						= "^6\\d{8}$";
	private static final String southAmericaRegExp 					= "^7\\d{8}$";
	private static final String handheldRegExp 						= "^8\\d{8}$";
	private static final String freeFormRegExp						= "^9\\d{8}$";
	private static final String sarTranspondersRegExp 				= "^970\\d{6}$";
	private static final String overboardRegExp 					= "^972\\d{6}$";
	private static final String epirbRegExp 						= "^974\\d{6}$";
	private static final String auxiliaryRegExp 					= "^98\\d{7}$";
	private static final String navigationalAidRegExp 				= "^99\\d{7}$";

	public static MMSIType fromMMSI(MMSI mmsi) {
		if (mmsi != null) {
			Integer integer = detectCode(mmsi.getMMSI());
			if (integer == null)
				return Invalid;
			
			for (MMSIType b : MMSIType.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
