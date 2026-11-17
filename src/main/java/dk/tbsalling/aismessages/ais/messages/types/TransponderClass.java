/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

package dk.tbsalling.aismessages.ais.messages.types;

/**
 * Class of AIS station that transmits a {@code DataReport}. Not a field of any ITU-R M.1371-6 message: it is
 * implied by the message type (Table 44) and the station categories of Annex 1 § A1-2. The codes are this library's own.
 */
public enum TransponderClass {
	/** Base station. */
	BS(0),  // Base station
	/** Class A shipborne mobile station (SOTDMA, IMO carriage requirement). */
	A(1),   // Class A equipment
	/** Class B shipborne mobile station, "SO" (SOTDMA) or "CS" (carrier sense TDMA). */
	B(2),   // Class B equipment
	/** Aircraft station in the maritime mobile service involved in search and rescue. */
	SAR(3); // Search and rescue airborne equipment

	TransponderClass(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	public String getValue() {
	    return toString();
	}

	private final int code;
}
