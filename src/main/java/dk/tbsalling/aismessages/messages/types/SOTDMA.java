package dk.tbsalling.aismessages.messages.types;

import java.io.Serializable;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.messages.PositionReport;

@SuppressWarnings("serial")
public class SOTDMA extends CommunicationState implements Serializable {

	public SOTDMA(
			Integer value,
			CommunicationStateType communicationStateType,
			Integer synchState,
			Integer slotTimeout,
			SOTDMASubmessageType submessage,
			Integer receivedStations,
			Integer slotCount,
			Integer hour,
			Integer minute,
			Integer slotOffset
			) {
		super(
				value,
				communicationStateType, 
				synchState
				);
		this.slotTimeout = slotTimeout;
		this.submessage = submessage;
		this.receivedStations = receivedStations;
		this.slotCount = slotCount;
		this.hour = hour;
		this.minute = minute;
		this.slotOffset = slotOffset;
	}
	
	public CommunicationStateType getCommunicationStateType() {
		return this.communicationStateType;
	}
	
	public Integer getSynchState() {
		return this.synchState;
	}
	
	public Integer getSlotTimeout() {
		return this.slotTimeout;
	}
	
	public SOTDMASubmessageType submessage() {
		return this.submessage;
	}
	
	public Integer getReceivedStations() {
		return this.receivedStations;
	}
	
	public Integer getSlotCount() {
		return this.slotCount;
	}	
	
	public Integer getHour() {
		return this.hour;
	}
	
	public Integer getMinute() {
		return this.minute;
	}
	
	public Integer getSlotOffset() {
		return this.slotOffset;
	}
	
	public static SOTDMA fromEncodedString(String encodedString) {
		CommunicationStateType communicationStateType = CommunicationStateType.SOTDMA;
		Integer value = DecoderImpl.convertToUnsignedInteger(encodedString.substring(0, 19));
		Integer synchState = DecoderImpl.convertToUnsignedInteger(encodedString.substring(0, 2));
		Integer slotTimeout = DecoderImpl.convertToUnsignedInteger(encodedString.substring(2, 5));
		SOTDMASubmessageType submessage = SOTDMASubmessageType.Unknown;
		Integer receivedStations = null;
		Integer slotCount = null;
		Integer hour = null;
		Integer minute = null;
		Integer slotOffset = null;
		switch (slotTimeout) {
		case 3:
		case 5:
		case 7:
			submessage = SOTDMASubmessageType.ReceivedStations;
			receivedStations = DecoderImpl.convertToUnsignedInteger(encodedString.substring(5, 19));
			break;
		case 2:
		case 4:
		case 6:
			submessage = SOTDMASubmessageType.SlotNumber;
			slotCount = DecoderImpl.convertToUnsignedInteger(encodedString.substring(5, 19));
			break;
		case 1:
			submessage = SOTDMASubmessageType.UTCHourAndMinute;
			hour = DecoderImpl.convertToUnsignedInteger(encodedString.substring(5, 10));
			minute = DecoderImpl.convertToUnsignedInteger(encodedString.substring(10, 17));
			break;
		case 0:
			submessage = SOTDMASubmessageType.SlotOffset;
			slotOffset = DecoderImpl.convertToUnsignedInteger(encodedString.substring(5, 19));
			break;
		}
		return new SOTDMA(
				value,
				communicationStateType,
				synchState,
				slotTimeout,
				submessage,
				receivedStations,
				slotCount,
				hour,
				minute,
				slotOffset
				);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"value\"").append(":").append(getValue()).append(",")
		.append("\"state\"").append(":").append(String.format("\"%s\"", communicationStateType)).append(",")
		.append("\"synchState\"").append(":").append(synchState).append(",")
		.append("\"slotTimeout\"").append(":").append(slotTimeout).append(",")
		.append("\"submessage\"").append(":").append(String.format("\"%s\"", submessage)).append(",")
		.append("\"receivedStations\"").append(":").append(receivedStations).append(",")
		.append("\"slotCount\"").append(":").append(slotCount).append(",")
		.append("\"hour\"").append(":").append(hour).append(",")
		.append("\"minute\"").append(":").append(minute).append(",")
		.append("\"slotOffset\"").append(":").append(slotOffset)
		.append("}");
		return builder.toString();	
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((communicationStateType == null) ? 0 : communicationStateType.hashCode());
		result = prime * result	+ ((synchState == null) ? 0 : synchState.hashCode());
		result = prime * result	+ ((slotTimeout == null) ? 0 : slotTimeout.hashCode());
		result = prime * result	+ ((submessage == null) ? 0 : submessage.hashCode());
		result = prime * result	+ ((receivedStations == null) ? 0 : receivedStations.hashCode());
		result = prime * result	+ ((slotCount == null) ? 0 : slotCount.hashCode());
		result = prime * result	+ ((hour == null) ? 0 : hour.hashCode());
		result = prime * result	+ ((minute == null) ? 0 : minute.hashCode());
		result = prime * result	+ ((slotOffset == null) ? 0 : slotOffset.hashCode());
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
		SOTDMA other = (SOTDMA) obj;
		if (communicationStateType !=  other.communicationStateType)
			return false;		
		if (synchState == null) {
			if (other.synchState != null)
				return false;
		} else if (!synchState.equals(other.synchState))
			return false;		
		if (slotTimeout == null) {
			if (other.slotTimeout != null)
				return false;
		} else if (!slotTimeout.equals(other.slotTimeout))
			return false;		
		if (submessage != other.submessage)
			return false;		
		if (receivedStations == null) {
			if (other.receivedStations != null)
				return false;
		} else if (!receivedStations.equals(other.receivedStations))
			return false;		
		if (slotCount == null) {
			if (other.slotCount != null)
				return false;
		} else if (!slotCount.equals(other.slotCount))
			return false;		
		if (hour == null) {
			if (other.hour != null)
				return false;
		} else if (!hour.equals(other.hour))
			return false;		
		if (minute == null) {
			if (other.minute != null)
				return false;
		} else if (!minute.equals(other.minute))
			return false;		
		if (slotOffset == null) {
			if (other.slotOffset != null)
				return false;
		} else if (!slotOffset.equals(other.slotOffset))
			return false;
		
		return true;
	}

	private final Integer slotTimeout;
	private final SOTDMASubmessageType submessage;
	private final Integer receivedStations;
	private final Integer slotCount;
	private final Integer hour;
	private final Integer minute;
	private final Integer slotOffset;
}
