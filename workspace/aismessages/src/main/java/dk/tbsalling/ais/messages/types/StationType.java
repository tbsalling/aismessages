package dk.tbsalling.ais.messages.types;

public enum StationType {
	AllTypesOfMobiles (0),
	ReservedForFutureUse(1),
	AllTypesOfClassBMobileStations(2),
	SARAirborneMobileStation(3),
	AidToNavigationStation(4),
	ClassBShipborneMobileStation(5),
	RegionalUse1(6),
	RegionalUse2(7),
	RegionalUse3(8),
	RegionalUse4(9),
	FutureUse1(10),
	FutureUse2(11),
	FutureUse3(12),
	FutureUse4(13),
	FutureUse5(14),
	FutureUse6(15);
	
	StationType(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	private final Integer code;

	public static StationType fromInteger(Integer integer) {
		if (integer != null) {
			for (StationType b : StationType.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
