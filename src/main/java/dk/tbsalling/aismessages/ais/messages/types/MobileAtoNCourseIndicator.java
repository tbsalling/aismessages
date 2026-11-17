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
 * Meaning of the course over ground of a mobile AtoN in Message 28 with dimension type
 * {@link AtoNDimensionsType#MobileAtoNVector} (ITU-R M.1371-6 Table 84, "AtoN Dimensions Type" 3, dimension A). Only
 * {@link #Reported} comes with a course.
 */
public enum MobileAtoNCourseIndicator {
	/** 0-359: course over ground in true degrees. */
	Reported,
	/** 360 = course over ground unreported. */
	Unreported,
	/** 361 = dynamically positioned on station, course over ground unreported. */
	DynamicallyPositionedOnStation,
	/** 362 = purposely adrift, course over ground unreported. */
	PurposelyAdrift,
	/** 363 = tethered, course over ground unreported. */
	Tethered,
	/** 364 = course over ground unknown (default). */
	Unknown
}
