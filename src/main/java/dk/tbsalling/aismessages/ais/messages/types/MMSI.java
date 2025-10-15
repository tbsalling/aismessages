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

import lombok.Value;

@Value
public class MMSI {

	public MMSI(int mmsi) {
		this.mmsi = mmsi;
	}

    /**
     * Returns the MMSI as a 9-digit string.
     * If the MMSI is not exactly 9 digits long, an exception is thrown.
     *
     * @return the MMSI as a 9-digit string, padded with leading zeros.
     * @throws IllegalStateException if the MMSI is not exactly 9 digits long.
     */
    public String as9DigitString() {
        String mmsiAsString = as9DigitStringLenient();

        int n = mmsiAsString.length();
        if (n != 9)
            throw new IllegalStateException("mmsi %d as 9-digit string has length %d, not 9.".formatted(mmsi, n));

        return mmsiAsString;
    }

    /**
     * Returns the MMSI as a 9-digit string, but does not throw an exception if the length is not 9.
     * This is useful for cases where the MMSI might be longer than 9 digits, such as in StandardSARAircraftPositionReport messages.
     *
     * @return the MMSI as a 9-digit string, padded with leading zeros.
     */
    public String as9DigitStringLenient() {
        return String.format("%09d", mmsi);
    }

    int mmsi;
}
