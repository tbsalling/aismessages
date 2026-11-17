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

/**
 * IMO ship identification number as carried in Message 5, bits 40-70 (ITU-R M.1371-6 Table 50), following the IMO
 * ship identification number scheme of IMO Resolution A.1117(30).
 */
@Value
public class IMO {

    /** The 30-bit value: 0 = not available; 1-999 999 not used; 1 000 000-9 999 999 = valid IMO number; 10 000 000-1 073 741 823 = official flag state number for ships without an IMO number. */
    int imo;

}
