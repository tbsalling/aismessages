package dk.tbsalling.ais.messages.types;

public enum ManeuverIndicator {
	NotAvailable(0),
	NoSpecialManeuver(1),
	SpecialManeuver(2);

	ManeuverIndicator(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	private final Integer code;

	public static ManeuverIndicator fromInteger(Integer integer) {
		if (integer != null) {
			for (ManeuverIndicator b : ManeuverIndicator.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
