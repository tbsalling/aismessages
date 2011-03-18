package dk.tbsalling.ais.messages.types;

public enum TxRxMode {
	TxABRxAB (0),
	TxARxAB(1),
	TxBRxAB(2),
	FutureUse(3);

	TxRxMode(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	private final Integer code;

	public static TxRxMode fromInteger(Integer integer) {
		if (integer != null) {
			for (TxRxMode b : TxRxMode.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
