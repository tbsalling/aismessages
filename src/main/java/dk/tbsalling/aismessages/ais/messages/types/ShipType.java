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
	NotAvailable(0),
	ScienceOrResearchVessel(1),
	TrainingVessel(2),
	/** Ship owned or operated by a government. */
	GovernmentShip(3),
	IceBreaker(4),
	/** Buoy (aids to navigation) tender. */
	BuoyTender(5),
	CableLayer(6),
	PipeLayer(7),
	ReservedForFutureUse8(8),
	SpecialPurposeShipNoAdditionalInfo(9),
	ReservedForFutureUse10(10),
	/** Floating production, storage and offloading vessel. */
	FPSO(11),
	FishFactoryShip(12),
	FishFarmSupportVessel(13),
	OffshoreSupportVessel(14),
	ReservedForFutureUse15(15),
	ReservedForFutureUse16(16),
	ConstructionVessel(17),
	CrewBoat(18),
	SupportVesselNoAdditionalInfo(19),
	WingInGround(20),
	WingInGroundHazardousA(21),
	WingInGroundHazardousB(22),
	WingInGroundHazardousC(23),
	WingInGroundHazardousD(24),
	ReservedForFutureUse25(25),
	ReservedForFutureUse26(26),
	ReservedForFutureUse27(27),
	ReservedForFutureUse28(28),
	WingInGroundNoAdditionalInfo(29),
	Fishing(30),
	Towing(31),
	/** Towing and length of the tow exceeds 200 m or breadth exceeds 25 m. */
	LargeTowing(32),
	/** Dredger. */
	DredgingOrUnderwaterOps(33),
	DivingOps(34),
	/** Warship or naval auxiliary. */
	MilitaryOps(35),
	Sailing(36),
	PleasureCraft(37),
	Trawler(38),
	PatrolVessel(39),
	HighSpeedCraft(40),
	HighSpeedCraftHazardousA(41),
	HighSpeedCraftHazardousB(42),
	HighSpeedCraftHazardousC(43),
	HighSpeedCraftHazardousD(44),
	HighSpeedCraftPassenger(45),
	/** HSC Ro-Ro ship (vehicle / rail). */
	HighSpeedCraftRoRo(46),
	ReservedForFutureUse47(47),
	ReservedForFutureUse48(48),
	HighSpeedCraftNoAdditionalInfo(49),
	PilotVessel(50),
	SearchAndRescueVessel(51),
	Tug(52),
	PortTender(53),
	/** Anti-pollution or firefighting responder. */
	AntiPollutionEquipment(54),
	LawEnforcement(55),
	SpareLocalVessel1(56),
	SpareLocalVessel2(57),
	MedicalTransport(58),
	/** Ships of States not parties to an armed conflict. */
	ShipAccordingToRRResolutionNo18(59),
	Passenger(60),
	PassengerHazardousA(61),
	PassengerHazardousB(62),
	PassengerHazardousC(63),
	PassengerHazardousD(64),
	PassengerCruiseShip(65),
	/** @deprecated since 5.1.0; code 65 is defined as {@link #PassengerCruiseShip} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	PassengerFuture1(65),
	PassengerFerry(66),
	/** @deprecated since 5.1.0; code 66 is defined as {@link #PassengerFerry} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	PassengerFuture2(66),
	/** Passenger (excursion) ship, i.e. harbour cruise boat, whale watcher, etc. */
	PassengerExcursionShip(67),
	/** @deprecated since 5.1.0; code 67 is defined as {@link #PassengerExcursionShip} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	PassengerFuture3(67),
	PassengerFuture4(68),
	PassengerNoAdditionalInfo(69),
	Cargo(70),
	CargoHazardousA(71),
	CargoHazardousB(72),
	CargoHazardousC(73),
	CargoHazardousD(74),
	CargoBulkCarrier(75),
	/** @deprecated since 5.1.0; code 75 is defined as {@link #CargoBulkCarrier} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	CargoFuture1(75),
	CargoContainerShip(76),
	/** @deprecated since 5.1.0; code 76 is defined as {@link #CargoContainerShip} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	CargoFuture2(76),
	CargoRollOnRollOffCarrier(77),
	/** @deprecated since 5.1.0; code 77 is defined as {@link #CargoRollOnRollOffCarrier} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	CargoFuture3(77),
	CargoLandingCraft(78),
	/** @deprecated since 5.1.0; code 78 is defined as {@link #CargoLandingCraft} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	CargoFuture4(78),
	CargoNoAdditionalInfo(79),
	Tanker(80),
	TankerHazardousA(81),
	TankerHazardousB(82),
	TankerHazardousC(83),
	TankerHazardousD(84),
	/** Tanker, non-hazardous or non-pollutant carrier. */
	TankerNonHazardous(85),
	/** @deprecated since 5.1.0; code 85 is defined as {@link #TankerNonHazardous} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	TankerFuture1(85),
	/** Integrated / articulated tug and tank barge (dimensions should reflect tug and barge). */
	IntegratedTugAndTankBarge(86),
	/** @deprecated since 5.1.0; code 86 is defined as {@link #IntegratedTugAndTankBarge} in ITU-R M.1371-6. */
	@Deprecated(since = "5.1.0")
	TankerFuture2(86),
	TankerFuture3(87),
	TankerFuture4(88),
	TankerNoAdditionalInfo(89),
	Other(90),
	OtherHazardousA(91),
	OtherHazardousB(92),
	OtherHazardousC(93),
	OtherHazardousD(94),
	OtherFuture1(95),
	OtherFuture2(96),
	OtherFuture3(97),
	OtherFuture4(98),
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
