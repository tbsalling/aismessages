package dk.tbsalling.aismessages.messages.types;

import java.io.Serializable;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.messages.PositionReport;

@SuppressWarnings("serial")
public class ITDMA extends CommunicationState implements Serializable {

	public ITDMA(
			Integer value,
			CommunicationStateType communicationStateType,
			Integer synchState,
			Integer slotIncrement,
			Integer slotCount,
			Boolean keepFlag
			) {
		super(
				value, 
				communicationStateType,
				synchState
				);
		this.slotIncrement = slotIncrement;
		this.slotCount = slotCount;
		this.keepFlag = keepFlag;
	}
	
	public CommunicationStateType getCommunicationStateType() {
		return this.communicationStateType;
	}
	
	public Integer getSynchState() {
		return this.synchState;
	}
	
	public Integer getSlotIncrement() {
		return this.slotIncrement;
	}
	
	public Integer getSlotCount() {
		return this.slotCount;
	}
	
	public Boolean getKeepFlag() {
		return this.keepFlag;
	}
	
	public static ITDMA fromEncodedString(String encodedString) {
		CommunicationStateType communicationStateType = CommunicationStateType.ITDMA;
		Integer value = DecoderImpl.convertToUnsignedInteger(encodedString.substring(0, 19));
		Integer synchState = DecoderImpl.convertToUnsignedInteger(encodedString.substring(0, 2));
		Integer slotIncrement = DecoderImpl.convertToUnsignedInteger(encodedString.substring(2, 15));
		Integer slotCount = DecoderImpl.convertToUnsignedInteger(encodedString.substring(15, 18));
		Boolean keepFlag = DecoderImpl.convertToBoolean(encodedString.substring(18, 19));
		
		return new ITDMA(
				value,
				communicationStateType,
				synchState,
				slotIncrement,
				slotCount,
				keepFlag
				);
	}	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"value\"").append(":").append(getValue()).append(",")
		.append("\"state\"").append(":").append(String.format("\"%s\"", communicationStateType)).append(",")
		.append("\"synchState\"").append(":").append(synchState).append(",")
		.append("\"slotIncrement\"").append(":").append(slotIncrement).append(",")
		.append("\"slotCount\"").append(":").append(slotCount).append(",")
		.append("\"keep\"").append(":").append(keepFlag.booleanValue() ? "1" : "0")
		.append("}");
		return builder.toString();	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((communicationStateType == null) ? 0 : communicationStateType.hashCode());
		result = prime * result	+ ((synchState == null) ? 0 : synchState.hashCode());
		result = prime * result	+ ((slotIncrement == null) ? 0 : slotIncrement.hashCode());
		result = prime * result	+ ((slotCount == null) ? 0 : slotCount.hashCode());
		result = prime * result	+ ((keepFlag == null) ? 0 : keepFlag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PositionReport))
			return false;
		ITDMA other = (ITDMA) obj;
		if (communicationStateType !=  other.communicationStateType)
			return false;
		if (synchState == null) {
			if (other.synchState != null)
				return false;
		} else if (!synchState.equals(other.synchState))
			return false;
		if (slotIncrement == null) {
			if (other.slotIncrement != null)
				return false;
		} else if (!slotIncrement.equals(other.slotIncrement))
			return false;		
		if (slotCount == null) {
			if (other.slotCount != null)
				return false;
		} else if (!slotCount.equals(other.slotCount))
			return false;		
		if (keepFlag == null) {
			if (other.keepFlag != null)
				return false;
		} else if (!keepFlag.equals(other.keepFlag))
			return false;
		return true;
	}

	protected final Integer slotIncrement;
	protected final Integer slotCount;
	protected final Boolean keepFlag;
}
