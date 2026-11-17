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
 * Meaning of the maximum present static draught field of Message 5 (ITU-R M.1371-6 Table 50, field "Maximum present
 * static draught"). Only {@link #Measured} and {@link #AtLeast25Point5Metres} carry a draught.
 */
public enum DraughtIndicator {
	/** A draught of 0.1-25.4 m in 1/10 m steps. */
	Measured,
	/** 255 = draught of 25.5 m or greater; the draught holds that lower bound. */
	AtLeast25Point5Metres,
	/** 0 = not available (default). */
	NotAvailable
}
