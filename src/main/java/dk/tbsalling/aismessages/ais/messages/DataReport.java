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

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;

/**
 * Common super-interface of the AIS messages that report data about a vessel or station: dynamic position reports
 * ({@link DynamicDataReport}) and static data reports ({@link StaticDataReport}). It identifies which class of AIS
 * station transmitted the report, which is implied by the message type (ITU-R M.1371-6 Table 44) rather than carried
 * in a field.
 */
public interface DataReport {
	/** @return the class of AIS station that transmits this message type: Class A, Class B, base station or SAR aircraft */
	TransponderClass getTransponderClass();
}
