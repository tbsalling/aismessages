package dk.tbsalling.ais.messages.types;

public enum PositionFixingDevice {
	Undefined (0),
	Gps(1),
	Glonass(2),
	CombinedGpsGlonass(3),
	LoranC(4),
	Chayka(5),
	IntegratedNavigationSystem(6),
	Surveyed(7),
	Galileo(8);

	PositionFixingDevice(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	private final Integer code;

	public static PositionFixingDevice fromInteger(Integer integer) {
		if (integer != null) {
			for (PositionFixingDevice b : PositionFixingDevice.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
