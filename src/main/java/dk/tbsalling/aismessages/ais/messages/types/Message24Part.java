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
 * Which part of Message 24 a {@code ClassBCSStaticDataReport} holds (ITU-R M.1371-6 Tables 76 and 77, field "Part
 * number").
 */
public enum Message24Part {
	/** 0 = Part A: the name of the MMSI-registered vessel (Table 76). */
	PartA(0),
	/** 1 = Part B: type of ship, vendor ID, call sign, dimensions, EPFD and VDES capabilities (Table 77). */
	PartB(1);

	Message24Part(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	private final int code;

	public static Message24Part fromInteger(int code) {
		for (Message24Part b : Message24Part.values()) {
			if (code == b.code) return b;
		}
		return null;
	}
}
