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


public class IMO {

	public IMO(int imo) {
		this.imo = imo;
	}
	
	public static IMO valueOf(int imo) {
		return new IMO(imo);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMO imo1 = (IMO) o;

        if (imo != imo1.imo) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return imo;
    }

    @Override
	public String toString() {
        return "IMO [imo=%d]".formatted(imo);
	}

    public int intValue() {
        return imo;
    }

	private final int imo;
}
