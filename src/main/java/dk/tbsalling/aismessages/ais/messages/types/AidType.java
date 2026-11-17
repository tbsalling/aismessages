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
 * Type of aid to navigation. Codes 0-31 are defined in ITU-R M.1371-6 Table 72 and are used by Message 21 (5-bit
 * field) and Message 28 (7-bit field). Codes 32-50 are the mobile aid-to-navigation types of Table 85 and only occur
 * in Message 28; 51-63 are reserved for future use and 64-127 for regional use, both of which map to {@code null}.
 * <p>
 * Where two constants share a code, {@link #fromInteger(int)} returns the one declared first.
 */
public enum AidType {
	/** 0 = default, type of AtoN not specified. */
	Unspecified (0),
	/** 1 = reference point. */
	ReferencePoint	(1),
	/** RACON or mobile aid to navigation. */
	RACON(2),
	/** Fixed structures, such as oil platforms and wind farms. */
	FixedStructure(3),
	/** 4 = emergency wreck marking buoy. */
	EmergencyWreckMarkingBuoy(4),
	/** @deprecated since 5.1.0; code 4 is defined as {@link #EmergencyWreckMarkingBuoy} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	FutureUse1(4),
	/** 5 = fixed AtoN: light, without sectors. */
	LightWithoutSectors(5),
	/** 6 = fixed AtoN: light, with sectors. */
	LightWithSectors(6),
	/** 7 = fixed AtoN: leading light front. */
	LeadingLightFront(7),
	/** 8 = fixed AtoN: leading light rear. */
	LeadingLightRear(8),
	/** 9 = fixed AtoN: beacon, cardinal N. */
	BeaconCardinalNorth(9),
	/** 10 = fixed AtoN: beacon, cardinal E. */
	BeaconCardinalEast(10),
	/** 11 = fixed AtoN: beacon, cardinal S. */
	BeaconCardinalSouth(11),
	/** 12 = fixed AtoN: beacon, cardinal W. */
	BeaconCardinalWest(12),
	/** 13 = fixed AtoN: beacon, port hand. */
	BeaconPortHand(13),
	/** 14 = fixed AtoN: beacon, starboard hand. */
	BeaconStarboardHand(14),
	/** 15 = fixed AtoN: beacon, preferred channel port hand. */
	BeaconPreferredChannelPortHand(15),
	/** 16 = fixed AtoN: beacon, preferred channel starboard hand. */
	BeaconPreferredChannelStarboardHand(16),
	/** 17 = fixed AtoN: beacon, isolated danger. */
	BeaconIsolatedDanger(17),
	/** 18 = fixed AtoN: beacon, safe water. */
	BeaconSafeWater(18),
	/** 19 = fixed AtoN: beacon, special mark. */
	BeaconSpecialMark(19),
	/** 20 = floating AtoN: cardinal mark N. */
	CardinalMarkNorth(20),
	/** 21 = floating AtoN: cardinal mark E. */
	CardinalMarkEast(21),
	/** 22 = floating AtoN: cardinal mark S. */
	CardinalMarkSouth(22),
	/** 23 = floating AtoN: cardinal mark W. */
	CardinalMarkWest(23),
	/** 24 = floating AtoN: port hand mark. */
	PortHandMark(24),
	/** 25 = floating AtoN: starboard hand mark. */
	StarboardHandMark(25),
	/** 26 = floating AtoN: preferred channel port hand. */
	PreferredChannelPortHand(26),
	/** 27 = floating AtoN: preferred channel starboard hand. */
	PreferredChannelStarboardHand(27),
	/** 28 = floating AtoN: isolated danger. */
	IsolatedDanger(28),
	/** 29 = floating AtoN: safe water. */
	SafeWater(29),
	/** 30 = floating AtoN: special mark. */
	SpecialMark(30),
	/** Light vessel, LANBY or rig. */
	LightVessel(31),
	/** Ocean data acquisition system (ODAS); mobile AtoN, Message 28 only. */
	OceanDataAcquisitionSystem(32),
	/** Mobile AtoN, Message 28 only. */
	WaterSamplingOrMonitoringEquipment(33),
	/** Mobile AtoN, Message 28 only. */
	ResearchEquipment(34),
	/** Mobile AtoN, Message 28 only. */
	TowedCablePipeOrSemiSubmergedObjectMarker(35),
	/** Mobile AtoN, Message 28 only. */
	TowedVesselOrObject(36),
	/** Flotsam marker, large (greater than 5 metres); mobile AtoN, Message 28 only. */
	FlotsamMarkerLarge(37),
	/** Flotsam marker, small (less than 5 metres); mobile AtoN, Message 28 only. */
	FlotsamMarkerSmall(38),
	/** Mobile AtoN, Message 28 only. */
	NavigationHazard(39),
	/** Mobile AtoN, Message 28 only. */
	SyntheticTargetMarker(40),
	/** Mobile AtoN, Message 28 only. */
	ProtectedSpeciesMarker(41),
	/** Mobile AtoN, Message 28 only. */
	MilitaryOperationTargetMarker(42),
	/** Mobile AtoN, Message 28 only. */
	DangerousObject(43),
	/** Mobile AtoN, Message 28 only. */
	PollutionSpillMarker(44),
	/** Mobile AtoN, Message 28 only. */
	SearchAndRescueDatumMark(45),
	/** Mobile AtoN, Message 28 only. */
	DatumMark(46),
	/** Operating underwater (at times); mobile AtoN, Message 28 only. */
	OperatingUnderwater(47),
	/** Mobile AtoN, Message 28 only. */
	UnderwaterOperationsMarker(48),
	/** Mobile AtoN, Message 28 only. */
	MilitaryOperationOrRestrictedArea(49),
	/** Mobile AtoN, Message 28 only. */
	DynamicArea(50);

	AidType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static AidType fromInteger(int code) {
		for (AidType b : AidType.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static AidType fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
