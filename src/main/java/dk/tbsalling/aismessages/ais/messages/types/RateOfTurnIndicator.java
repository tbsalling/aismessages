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
 * Meaning of the rate of turn field of Messages 1, 2 and 3 (ROT_AIS, ITU-R M.1371-6 Table 46). Only
 * {@link #Measured} values carry a rate of turn in degrees per minute; the other constants are the field's special
 * codes.
 */
public enum RateOfTurnIndicator {
	/** -126 to +126: rate of turn from a turn indicator, ROT_AIS = 4.733 × √ROT_sensor; negative = turning left. */
	Measured,
	/** +127 = turning right at more than 5° per 30 s; no turn indicator available. */
	TurningRightWithoutTurnIndicator,
	/** -127 = turning left at more than 5° per 30 s; no turn indicator available. */
	TurningLeftWithoutTurnIndicator,
	/** -128 (80 hex) = no turn information available (default). */
	NotAvailable;

	/**
	 * Classifies a transmitted rate of turn.
	 *
	 * @param rawRateOfTurn the transmitted value ROT_AIS, -128 to +127.
	 * @return the indicator for that value.
	 */
	public static RateOfTurnIndicator fromRawRateOfTurn(int rawRateOfTurn) {
		return switch (rawRateOfTurn) {
			case 127 -> TurningRightWithoutTurnIndicator;
			case -127 -> TurningLeftWithoutTurnIndicator;
			case -128 -> NotAvailable;
			default -> Measured;
		};
	}
}
