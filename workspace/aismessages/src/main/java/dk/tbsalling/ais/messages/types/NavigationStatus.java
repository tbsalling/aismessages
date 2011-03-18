package dk.tbsalling.ais.messages.types;

public enum NavigationStatus {
	UnderwayUsingEngine (0),
	AtAnchor(1),
	NotUnderCommand(2),
	RestrictedManoeuverability(3),
	ConstrainedByHerDraught(4),
	Moored(5),
	Aground(6),
	EngagedInFising(7),
	UnderwaySailing(8),
	ReservedForFutureUse9(9),
	ReservedForFutureUse10(10),
	ReservedForFutureUse11(11),
	ReservedForFutureUse12(12),
	ReservedForFutureUse13(13),
	ReservedForFutureUse14(14),
	NotDefined(15);

	NavigationStatus(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	private final Integer code;

	public static NavigationStatus fromInteger(Integer integer) {
		if (integer != null) {
			for (NavigationStatus b : NavigationStatus.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
