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
 * AtoN on-station status of Message 28 (ITU-R M.1371-6 Table 84).
 */
public enum AtoNOnStationStatus {
	/** 0 = on station (default). */
	OnStation(0),
	/** On station or on course (mobile AtoN). */
	OnStationOrOnCourse(1),
	/** On station, but damaged, occulted, submerged or otherwise not properly visible. */
	OnStationNotProperlyVisible(2),
	/** Off station; a virtual AtoN reporting the intended position of an AtoN that is reporting itself off-position. */
	OffStationIntendedPosition(3),
	/** 4 = off station, location unknown. */
	OffStationLocationUnknown(4),
	/** 5 = off station, but reporting its current position. */
	OffStationReportingCurrentPosition(5),
	/** 6 = off station, adrift. */
	OffStationAdrift(6),
	/** 7 = off station, removed or relocated. */
	OffStationRemovedOrRelocated(7),
	/** On station, as a new or temporary AtoN. */
	OnStationNewOrTemporary(8),
	/** Unmarked navigation hazard reported by a vessel (type of AtoN should be reference point, accompanied by a Message 14). */
	UnmarkedNavigationHazard(9),
	/** Unmarked obstruction (type of AtoN should be reference point, accompanied by a Message 14). */
	UnmarkedObstruction(10),
	/** 11 = reserved for future use. */
	ReservedForFutureUse11(11),
	/** 12 = reserved for future use. */
	ReservedForFutureUse12(12),
	/** 13 = reserved for future use. */
	ReservedForFutureUse13(13),
	/** 14 = reserved for future use. */
	ReservedForFutureUse14(14),
	/** 15 = reserved for future use. */
	ReservedForFutureUse15(15);

	AtoNOnStationStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static AtoNOnStationStatus fromInteger(int code) {
		for (AtoNOnStationStatus b : AtoNOnStationStatus.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static AtoNOnStationStatus fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
