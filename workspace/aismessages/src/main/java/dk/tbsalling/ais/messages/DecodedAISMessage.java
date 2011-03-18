package dk.tbsalling.ais.messages;

import java.io.Serializable;

import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

@SuppressWarnings("serial")
public abstract class DecodedAISMessage implements Serializable {
	
	protected DecodedAISMessage(AISMessageType messageType, Integer repeatIndicator, MMSI sourceMmsi) {
		this.messageType = messageType;
		this.repeatIndicator = repeatIndicator;
		this.sourceMmsi = sourceMmsi;
	}

	public AISMessageType getMessageType() {
		return messageType;
	}

	public Integer getRepeatIndicator() {
		return repeatIndicator;
	}

	public final MMSI getSourceMmsi() {
		return sourceMmsi;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DecodedAISMessage [messageType=").append(messageType)
				.append(", repeatIndicator=").append(repeatIndicator)
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result
				+ ((repeatIndicator == null) ? 0 : repeatIndicator.hashCode());
		result = prime * result
				+ ((sourceMmsi == null) ? 0 : sourceMmsi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DecodedAISMessage))
			return false;
		DecodedAISMessage other = (DecodedAISMessage) obj;
		if (messageType != other.messageType)
			return false;
		if (repeatIndicator == null) {
			if (other.repeatIndicator != null)
				return false;
		} else if (!repeatIndicator.equals(other.repeatIndicator))
			return false;
		if (sourceMmsi == null) {
			if (other.sourceMmsi != null)
				return false;
		} else if (!sourceMmsi.equals(other.sourceMmsi))
			return false;
		return true;
	}

	private final AISMessageType messageType;
	private final Integer repeatIndicator;
	private final MMSI sourceMmsi;
}
