package dk.tbsalling.ais.messages.types;

public enum ShipType {
	NotAvailable(0),
	WingInGround(20),
	WingInGroundHazardousA(21),
	WingInGroundHazardousB(22),
	WingInGroundHazardousC(23),
	WingInGroundHazardousD(24),
	Fishing(30),
	Towing(31),
	LargeTowing(32),
	DredgingOrUnderwaterOps(33),
	DivingOps(34),
	MilitaryOps(35),
	Sailing(36),
	PleasureCraft(37),
	HighSpeedCraft(40),
	HighSpeedCraftHarzardousA(41),
	HighSpeedCraftHarzardousB(42),
	HighSpeedCraftHarzardousC(43),
	HighSpeedCraftHarzardousD(44),
	PilotVessel(50),
	SearchAndRescueVessel(51),
	Tug(52),
	PortTender(53),
	AntiPollutionEquipment(54),
	LawEnforcement(55),
	SpareLocalVessel1(56),
	SpareLocalVessel2(57),
	MedicalTransport(58),
	ShipAccordingToRRResolutionNo18(59),
	Passenger(60),
	PassengerHazardousA(61),
	PassengerHazardousB(62),
	PassengerHazardousC(63),
	PassengerHazardousD(64),
	PassengerFuture1(65),
	PassengerFuture2(66),
	PassengerFuture3(67),
	PassengerFuture4(68),
	PassengerNoAdditionalInfo(69),
	Cargo(70),
	CargoHazardousA(71),
	CargoHazardousB(72),
	CargoHazardousC(73),
	CargoHazardousD(74),
	CargoFuture1(75),
	CargoFuture2(76),
	CargoFuture3(77),
	CargoFuture4(78),
	CargoNoAdditionalInfo(79),
	Tanker(80),
	TankerHazardousA(81),
	TankerHazardousB(82),
	TankerHazardousC(83),
	TankerHazardousD(84),
	TankerFuture1(85),
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

	ShipType(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	private final Integer code;

	public static ShipType fromInteger(Integer integer) {
		if (integer != null) {
			for (ShipType b : ShipType.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
