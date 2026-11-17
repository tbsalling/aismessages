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
 * Meaning of the speed over ground field of Messages 1, 2, 3, 9, 18, 19 and 27 (ITU-R M.1371-6 Tables 46, 57, 68,
 * 69 and 83, field "SOG"). Only {@link #Measured} and {@link #AtLeastMaximum} carry a speed.
 */
public enum SpeedOverGroundIndicator {
	/** A measured speed below the field's maximum: 0-102.1 knots (Tables 46, 68, 69), 0-1021 knots (Table 57) or 0-61 knots (Table 83). */
	Measured,
	/** The speed is the field's maximum or higher: 102.2 knots (Tables 46, 68, 69), 1 022 knots (Table 57) or 62 knots (Table 83); the speed holds that lower bound. */
	AtLeastMaximum,
	/** Speed over ground not available (default): 102.3 knots (Tables 46, 68, 69), 1 023 knots (Table 57) or 63 knots (Table 83). */
	NotAvailable
}
