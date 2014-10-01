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

package dk.tbsalling.aismessages.messages.types;

import java.io.Serializable;

import dk.tbsalling.aismessages.decoder.DecoderImpl;

@SuppressWarnings("serial")
public class ApplicationIdentifier implements Serializable {

	private ApplicationIdentifier(Integer applicationIdentifier, Integer designatedAreaCode, Integer functionalIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
		this.designatedAreaCode = designatedAreaCode;
		this.functionalIdentifier = functionalIdentifier;
	}
	
	public static ApplicationIdentifier fromEncodedString(String encodedString) {
		Integer applicationIdentifier = DecoderImpl.convertToUnsignedInteger(encodedString.substring(0, 16));
		Integer designatedAreaCode = DecoderImpl.convertToUnsignedInteger(encodedString.substring(0, 10));
		Integer functionalIdentifier = DecoderImpl.convertToUnsignedInteger(encodedString.substring(10, 16));
		return new ApplicationIdentifier(applicationIdentifier, designatedAreaCode, functionalIdentifier);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationIdentifier == null) ? 0 : applicationIdentifier.hashCode());
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
		ApplicationIdentifier other = (ApplicationIdentifier) obj;
		if (applicationIdentifier == null) {
			if (other.applicationIdentifier != null)
				return false;
		} else if (!applicationIdentifier.equals(other.applicationIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"ai\"").append(":").append(applicationIdentifier).append(",")
		.append("\"dac\"").append(":").append(designatedAreaCode).append(",")
		.append("\"fi\"").append(":").append(functionalIdentifier)
		.append("}");
		return builder.toString();	
	}

	public Integer getApplicationIdentifier() {
	    return applicationIdentifier;
	}
	
	public Integer getDesignatedAreaCode() {
		return designatedAreaCode;
	}
	
	public Integer getFunctionalIdentifier() {
		return functionalIdentifier;
	}

	private final Integer applicationIdentifier;
	private final Integer designatedAreaCode;
	private final Integer functionalIdentifier;
}

