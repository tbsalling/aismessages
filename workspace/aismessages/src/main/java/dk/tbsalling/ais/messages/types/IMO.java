package dk.tbsalling.ais.messages.types;

public class IMO {

	public IMO(Long mmsi) {
		this.imo = mmsi;
	}
	
	public static IMO valueOf(Long mmsi) {
		return new IMO(mmsi);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imo == null) ? 0 : imo.hashCode());
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
		IMO other = (IMO) obj;
		if (imo == null) {
			if (other.imo != null)
				return false;
		} else if (!imo.equals(other.imo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IMO [imo=" + imo + "]";
	}

	private final Long imo;
}
