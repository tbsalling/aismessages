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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The 19-bit communication state carried by Messages 1-4, 9, 11, 18 and 26 (ITU-R M.1371-6 § A2-3.3.7.2.2 and
 * § A2-3.3.7.3.2). It tells receiving stations the transmitter's synchronization state and how it intends to use slots
 * in the future; it applies only to the slot and channel of the transmission it is part of.
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public sealed abstract class CommunicationState permits ITDMACommunicationState, SOTDMACommunicationState {

	/** Bits 0-2 of the communication state (Tables 18 and 20): synchronization state of the transmitting station. */
	private final SyncState syncState;

}
