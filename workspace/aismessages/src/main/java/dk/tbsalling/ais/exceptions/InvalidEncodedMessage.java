package dk.tbsalling.ais.exceptions;

import dk.tbsalling.ais.messages.EncodedAISMessage;

@SuppressWarnings("serial")
public class InvalidEncodedMessage extends RuntimeException {

	public final EncodedAISMessage getEncodedMessage() {
		return encodedMessage;
	}

	public InvalidEncodedMessage(EncodedAISMessage encodedMessage) {
		this.encodedMessage = encodedMessage;
	}
	
	private final EncodedAISMessage encodedMessage;

}
