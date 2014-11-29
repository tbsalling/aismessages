package dk.tbsalling.aismessages.messages.types;

public abstract class CommunicationState {

	protected CommunicationState(
			Integer value,
			CommunicationStateType communicationStateType,
			Integer synchState
			) {
		this.value = value;
		this.communicationStateType = communicationStateType;
		this.synchState = synchState;
	}
	
	public Integer getValue() {
		return value;
	}
	
	private final Integer value;
	protected final CommunicationStateType communicationStateType;
	protected final Integer synchState;
}
