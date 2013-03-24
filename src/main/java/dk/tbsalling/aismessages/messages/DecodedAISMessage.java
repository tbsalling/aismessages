/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.messages;

import java.io.Serializable;

import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;

@SuppressWarnings("serial")
public abstract class DecodedAISMessage implements Serializable {
	
	protected DecodedAISMessage(AISMessageType messageType, Integer repeatIndicator, MMSI sourceMmsi) {
		this.messageType = messageType;
		this.repeatIndicator = repeatIndicator;
		this.sourceMmsi = sourceMmsi;
	}

	public final Metadata getMetadata() {
		return metadata;
	}
	
	public final void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public final AISMessageType getMessageType() {
		return messageType;
	}

	public final Integer getRepeatIndicator() {
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

	private Metadata metadata;
	private final AISMessageType messageType;
	private final Integer repeatIndicator;
	private final MMSI sourceMmsi;
}
