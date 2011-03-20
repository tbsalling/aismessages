/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.messages.types;

public enum NavigationStatus {
	UnderwayUsingEngine (0),
	AtAnchor(1),
	NotUnderCommand(2),
	RestrictedManoeuverability(3),
	ConstrainedByHerDraught(4),
	Moored(5),
	Aground(6),
	EngagedInFising(7),
	UnderwaySailing(8),
	ReservedForFutureUse9(9),
	ReservedForFutureUse10(10),
	ReservedForFutureUse11(11),
	ReservedForFutureUse12(12),
	ReservedForFutureUse13(13),
	ReservedForFutureUse14(14),
	NotDefined(15);

	NavigationStatus(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}

	private final Integer code;

	public static NavigationStatus fromInteger(Integer integer) {
		if (integer != null) {
			for (NavigationStatus b : NavigationStatus.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
