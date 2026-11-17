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
	Unspecified (0),
	ReferencePoint	(1),
	/** RACON or mobile aid to navigation. */
	RACON(2),
	/** Fixed structures, such as oil platforms and wind farms. */
	FixedStructure(3),
	EmergencyWreckMarkingBuoy(4),
	/** @deprecated since 5.1.0; code 4 is defined as {@link #EmergencyWreckMarkingBuoy} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
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
