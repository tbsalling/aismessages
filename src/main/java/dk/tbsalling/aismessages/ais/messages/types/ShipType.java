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
 * Type of ship and cargo type (ITU-R M.1371-6 Table 51). Codes 100-199 are reserved for regional use and 200-255 for
 * future use; both map to {@code null}.
 * <p>
 * The {@code Hazardous A/B/C/D} constants correspond to IMO hazard or pollutant categories X, Y, Z and OS, which were
 * formerly named A, B, C and D.
 * <p>
 * Where two constants share a code, {@link #fromInteger(int)} returns the one declared first.
 */
public enum ShipType {
	/** 0 = not available or no ship (default). */
	NotAvailable(0),
	/** 1 = science / research vessel. */
	ScienceOrResearchVessel(1),
	/** 2 = training vessel. */
	TrainingVessel(2),
	/** Ship owned or operated by a government. */
	GovernmentShip(3),
	/** 4 = ice breaker. */
	IceBreaker(4),
	/** Buoy (aids to navigation) tender. */
	BuoyTender(5),
	/** 6 = cable layer. */
	CableLayer(6),
	/** 7 = pipe layer. */
	PipeLayer(7),
	/** 8 = reserved for future use. */
	ReservedForFutureUse8(8),
	/** 9 = special purpose ship, no additional information. */
	SpecialPurposeShipNoAdditionalInfo(9),
	/** 10 = reserved for future use. */
	ReservedForFutureUse10(10),
	/** Floating production, storage and offloading vessel. */
	FPSO(11),
	/** 12 = fish factory ship. */
	FishFactoryShip(12),
	/** 13 = fish farm support vessel. */
	FishFarmSupportVessel(13),
	/** 14 = offshore support vessel. */
	OffshoreSupportVessel(14),
	/** 15 = reserved for future use. */
	ReservedForFutureUse15(15),
	/** 16 = reserved for future use. */
	ReservedForFutureUse16(16),
	/** 17 = construction vessel. */
	ConstructionVessel(17),
	/** 18 = crew boat. */
	CrewBoat(18),
	/** 19 = support vessel, no additional information. */
	SupportVesselNoAdditionalInfo(19),
	/** 20 = wing in ground (WIG), all ships of this type. */
	WingInGround(20),
	/** 21 = WIG, carrying DG, HS, MP or MHB, IMO hazard or pollutant category X (formerly A). */
	WingInGroundHazardousA(21),
	/** 22 = WIG, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Y (formerly B). */
	WingInGroundHazardousB(22),
	/** 23 = WIG, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Z (formerly C). */
	WingInGroundHazardousC(23),
	/** 24 = WIG, carrying DG, HS, MP or MHB, IMO hazard or pollutant category OS (formerly D). */
	WingInGroundHazardousD(24),
	/** 25 = reserved for future use. */
	ReservedForFutureUse25(25),
	/** 26 = reserved for future use. */
	ReservedForFutureUse26(26),
	/** 27 = reserved for future use. */
	ReservedForFutureUse27(27),
	/** 28 = reserved for future use. */
	ReservedForFutureUse28(28),
	/** 29 = WIG vessel, no additional information. */
	WingInGroundNoAdditionalInfo(29),
	/** 30 = fishing vessel. */
	Fishing(30),
	/** 31 = towing. */
	Towing(31),
	/** Towing and length of the tow exceeds 200 m or breadth exceeds 25 m. */
	LargeTowing(32),
	/** Dredger. */
	DredgingOrUnderwaterOps(33),
	/** 34 = diving vessel. */
	DivingOps(34),
	/** Warship or naval auxiliary. */
	MilitaryOps(35),
	/** 36 = sailing vessel. */
	Sailing(36),
	/** 37 = pleasure motor craft. */
	PleasureCraft(37),
	/** 38 = trawler. */
	Trawler(38),
	/** 39 = patrol vessel. */
	PatrolVessel(39),
	/** 40 = high-speed craft (HSC), all ships of this type. */
	HighSpeedCraft(40),
	/** 41 = HSC, carrying DG, HS, MP or MHB, IMO hazard or pollutant category X (formerly A). */
	HighSpeedCraftHazardousA(41),
	/** 42 = HSC, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Y (formerly B). */
	HighSpeedCraftHazardousB(42),
	/** 43 = HSC, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Z (formerly C). */
	HighSpeedCraftHazardousC(43),
	/** 44 = HSC, carrying DG, HS, MP or MHB, IMO hazard or pollutant category OS (formerly D). */
	HighSpeedCraftHazardousD(44),
	/** 45 = HSC, carrying passengers. */
	HighSpeedCraftPassenger(45),
	/** HSC Ro-Ro ship (vehicle / rail). */
	HighSpeedCraftRoRo(46),
	/** 47 = reserved for future use. */
	ReservedForFutureUse47(47),
	/** 48 = reserved for future use. */
	ReservedForFutureUse48(48),
	/** 49 = HSC, no additional information. */
	HighSpeedCraftNoAdditionalInfo(49),
	/** 50 = pilot vessel. */
	PilotVessel(50),
	/** 51 = search and rescue vessel. */
	SearchAndRescueVessel(51),
	/** 52 = tug. */
	Tug(52),
	/** 53 = port or fish tender. */
	PortTender(53),
	/** Anti-pollution or firefighting responder. */
	AntiPollutionEquipment(54),
	/** 55 = law enforcement vessel. */
	LawEnforcement(55),
	/** 56 = spare 1, for assignments to local vessels. */
	SpareLocalVessel1(56),
	/** 57 = spare 2, for assignments to local vessels. */
	SpareLocalVessel2(57),
	/** 58 = medical transport (as defined in the 1949 Geneva Conventions and Additional Protocols). */
	MedicalTransport(58),
	/** Ships of States not parties to an armed conflict. */
	ShipAccordingToRRResolutionNo18(59),
	/** 60 = passenger ship, all ships of this type. */
	Passenger(60),
	/** 61 = passenger ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category X (formerly A). */
	PassengerHazardousA(61),
	/** 62 = passenger ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Y (formerly B). */
	PassengerHazardousB(62),
	/** 63 = passenger ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Z (formerly C). */
	PassengerHazardousC(63),
	/** 64 = passenger ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category OS (formerly D). */
	PassengerHazardousD(64),
	/** 65 = passenger (cruise) ship. */
	PassengerCruiseShip(65),
	/** @deprecated since 6.0.0; code 65 is defined as {@link #PassengerCruiseShip} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	PassengerFuture1(65),
	/** 66 = passenger (ferry) ship. */
	PassengerFerry(66),
	/** @deprecated since 6.0.0; code 66 is defined as {@link #PassengerFerry} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	PassengerFuture2(66),
	/** Passenger (excursion) ship, i.e. harbour cruise boat, whale watcher, etc. */
	PassengerExcursionShip(67),
	/** @deprecated since 6.0.0; code 67 is defined as {@link #PassengerExcursionShip} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	PassengerFuture3(67),
	/** 68 = reserved for future use. */
	PassengerFuture4(68),
	/** 69 = passenger ship, no additional information. */
	PassengerNoAdditionalInfo(69),
	/** 70 = cargo ship, all ships of this type. */
	Cargo(70),
	/** 71 = cargo ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category X (formerly A). */
	CargoHazardousA(71),
	/** 72 = cargo ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Y (formerly B). */
	CargoHazardousB(72),
	/** 73 = cargo ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Z (formerly C). */
	CargoHazardousC(73),
	/** 74 = cargo ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category OS (formerly D). */
	CargoHazardousD(74),
	/** 75 = cargo ship, bulk carrier. */
	CargoBulkCarrier(75),
	/** @deprecated since 6.0.0; code 75 is defined as {@link #CargoBulkCarrier} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	CargoFuture1(75),
	/** 76 = cargo ship, container ship. */
	CargoContainerShip(76),
	/** @deprecated since 6.0.0; code 76 is defined as {@link #CargoContainerShip} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	CargoFuture2(76),
	/** 77 = cargo ship, roll-on roll-off carrier. */
	CargoRollOnRollOffCarrier(77),
	/** @deprecated since 6.0.0; code 77 is defined as {@link #CargoRollOnRollOffCarrier} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	CargoFuture3(77),
	/** 78 = cargo ship, landing craft. */
	CargoLandingCraft(78),
	/** @deprecated since 6.0.0; code 78 is defined as {@link #CargoLandingCraft} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	CargoFuture4(78),
	/** 79 = cargo ship, no additional information. */
	CargoNoAdditionalInfo(79),
	/** 80 = tanker, all ships of this type. */
	Tanker(80),
	/** 81 = tanker, carrying DG, HS, MP or MHB, IMO hazard or pollutant category X (formerly A). */
	TankerHazardousA(81),
	/** 82 = tanker, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Y (formerly B). */
	TankerHazardousB(82),
	/** 83 = tanker, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Z (formerly C). */
	TankerHazardousC(83),
	/** 84 = tanker, carrying DG, HS, MP or MHB, IMO hazard or pollutant category OS (formerly D). */
	TankerHazardousD(84),
	/** Tanker, non-hazardous or non-pollutant carrier. */
	TankerNonHazardous(85),
	/** @deprecated since 6.0.0; code 85 is defined as {@link #TankerNonHazardous} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	TankerFuture1(85),
	/** Integrated / articulated tug and tank barge (dimensions should reflect tug and barge). */
	IntegratedTugAndTankBarge(86),
	/** @deprecated since 6.0.0; code 86 is defined as {@link #IntegratedTugAndTankBarge} in ITU-R M.1371-6. */
	@Deprecated(since = "6.0.0")
	TankerFuture2(86),
	/** 87 = reserved for future use. */
	TankerFuture3(87),
	/** 88 = reserved for future use. */
	TankerFuture4(88),
	/** 89 = tanker, no additional information. */
	TankerNoAdditionalInfo(89),
	/** 90 = other type of ship, all ships of this type. */
	Other(90),
	/** 91 = other type of ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category X (formerly A). */
	OtherHazardousA(91),
	/** 92 = other type of ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Y (formerly B). */
	OtherHazardousB(92),
	/** 93 = other type of ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category Z (formerly C). */
	OtherHazardousC(93),
	/** 94 = other type of ship, carrying DG, HS, MP or MHB, IMO hazard or pollutant category OS (formerly D). */
	OtherHazardousD(94),
	/** 95 = reserved for future use. */
	OtherFuture1(95),
	/** 96 = reserved for future use. */
	OtherFuture2(96),
	/** 97 = reserved for future use. */
	OtherFuture3(97),
	/** 98 = reserved for future use. */
	OtherFuture4(98),
	/** 99 = other type of ship, no additional information. */
	OtherNoAdditionalInfo(99);

	ShipType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static ShipType fromInteger(int code) {
		for (ShipType b : ShipType.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static ShipType fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
