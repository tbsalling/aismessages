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

package dk.tbsalling.aismessages.ais.messages.types;

public enum AidType {
	Unspecified (0),
	ReferencePoint	(1),
	RACON(2),
	FixedStructure(3),
	FutureUse1(4),
	LightWithoutSectors(5),
	LightWithSectors(6),
	LeadingLightFront(7),
	LeadingLightRear(8),
	BeaconCardinalNorth(9),
	BeaconCardinalEast(10),
	BeaconCardinalSouth(11),
	BeaconCardinalWest(12),
	BeaconPortHand(13),
	BeaconStarboardHand(14),
	BeaconPreferredChannelPortHand(15),
	BeaconPreferredChannelStarboardHand(16),
	BeaconIsolatedDanger(17),
	BeaconSafeWater(18),
	BeaconSpecialMark(19),
	CardinalMarkNorth(20),
	CardinalMarkEast(21),
	CardinalMarkSouth(22),
	CardinalMarkWest(23),
	PortHandMark(24),
	StarboardHandMark(25),
	PreferredChannelPortHand(26),
	PreferredChannelStarboardHand(27),
	IsolatedDanger(28),
	SafeWater(29),
	SpecialMark(30),
	LightVessel(31);

	AidType(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final Integer code;

	public static AidType fromInteger(Integer integer) {
		if (integer != null) {
			for (AidType b : AidType.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
