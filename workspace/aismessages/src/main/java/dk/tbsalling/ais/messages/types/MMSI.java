package dk.tbsalling.ais.messages.types;

public class MMSI {

	public MMSI(Long mmsi) {
		this.mmsi = mmsi;
	}
	
	public static MMSI valueOf(Long mmsi) {
		return new MMSI(mmsi);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mmsi == null) ? 0 : mmsi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MMSI other = (MMSI) obj;
		if (mmsi == null) {
			if (other.mmsi != null)
				return false;
		} else if (!mmsi.equals(other.mmsi))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MMSI [mmsi=" + mmsi + "]";
	}

	private final Long mmsi;

}
