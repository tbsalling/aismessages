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

@SuppressWarnings("serial")
public class MMSI implements Serializable {

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

	public Long getMMSI() {
	    return mmsi;
	}

	private final Long mmsi;
}
