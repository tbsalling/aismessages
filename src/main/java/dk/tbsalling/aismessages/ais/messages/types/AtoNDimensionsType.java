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
 * AtoN dimensions type of Message 28 (ITU-R M.1371-6 Table 84). It defines what the 9-bit dimension A and the
 * 11-bit dimension B of the message represent; the raw values are exposed unconverted by the message, and the unit
 * for each type is documented on the constant.
 */
public enum AtoNDimensionsType {
	/** Default: A and B are the AtoN dimensions as for Message 21 (metres). */
	AtoNDimensions(0),
	/**
	 * Height and structural area. The reported position is the midpoint of a shape encompassing the structure.
	 * A = radius of the circle in 1 m steps (0-511, 511 = 511 m or greater); B = height above sea level in 0.1 m
	 * steps (0-2047, 2047 = 204.7 m or greater).
	 */
	HeightAndStructuralArea(1),
	/**
	 * Swing circle (type of AtoN 20-30). The reported position is the centre of the swing circle; its radius is
	 * A × 100 m + B × 1 m (B 0-100; 101-2047 reserved).
	 */
	SwingCircle(2),
	/**
	 * Mobile AtoN vector. A = COG in true degrees (0-359; 360 = unreported; 361 = dynamically positioned on station;
	 * 362 = purposely adrift; 363 = tethered; 364 = unknown (default); 365-511 reserved). B = SOG in knots
	 * (0-59; 60 = unreported; 61-2047 reserved).
	 */
	MobileAtoNVector(3),
	/**
	 * Area polygon. The reported position is the start of a vertex whose end is the next vertex broadcast from the
	 * same MMSI/MRN. A = sequence number of the vertex (1-8; 0 closes the polygon to vertex 1; 9-511 reserved);
	 * B = total number of lines in the polygon (0-8; 9-2047 reserved).
	 */
	AreaPolygon(4),
	/**
	 * Area circle. The reported position is the centre of a circular area; its radius is A × 1 m (0-99;
	 * 100-511 reserved) + B × 100 m.
	 */
	AreaCircle(5),
	/**
	 * Boundary line 1. The reported position is the start of a right-hand line bounding an area. A = orientation
	 * clockwise from true north in degrees (0-359; 360-511 reserved); B = length in 10 m steps.
	 */
	BoundaryLine1(6),
	/**
	 * Area sector, connected clockwise with an accompanying {@link #BoundaryLine1} from the same position.
	 * A = orientation in degrees (0-359); B = length in 10 m steps.
	 */
	AreaSector(7),
	/**
	 * Boundary line 2. The reported position is the midpoint of a line bounding an area. A = orientation in degrees
	 * (0-359); B = length in 10 m steps.
	 */
	BoundaryLine2(8),
	/**
	 * Area quadrilateral, combined with an accompanying {@link #BoundaryLine2} or {@link #LargeBoundaryLine2}.
	 * A = orientation in degrees (0-359); B = length of the diagonal in 10 m steps.
	 */
	AreaQuadrilateral(9),
	/** As {@link #BoundaryLine1} but with B in 200 m steps. */
	LargeBoundaryLine1(10),
	/** As {@link #AreaSector} but combined with {@link #LargeBoundaryLine1}, B in 200 m steps. */
	LargeAreaSector(11),
	/** As {@link #BoundaryLine2} but with B in 200 m steps. */
	LargeBoundaryLine2(12),
	/** As {@link #AreaQuadrilateral} but combined with {@link #LargeBoundaryLine2}, B in 200 m steps. */
	LargeAreaQuadrilateral(13),
	/** 14 = reserved for future use. */
	ReservedForFutureUse14(14),
	/** 15 = reserved for future use. */
	ReservedForFutureUse15(15);

	AtoNDimensionsType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	private final int code;

	public static AtoNDimensionsType fromInteger(int code) {
		for (AtoNDimensionsType b : AtoNDimensionsType.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static AtoNDimensionsType fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}
}
