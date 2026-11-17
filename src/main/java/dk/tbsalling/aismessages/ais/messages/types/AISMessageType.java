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
 * AIS message type, the 6-bit message ID in bits 0-6 of every message (ITU-R M.1371-6 § A2-3.3.7.1, Table 44).
 * IDs 29-59 are undefined and 60-63 are reserved for autonomous maritime radio devices (Rec. ITU-R M.2135); neither
 * is decoded. M = transmitted by mobile stations, B = by base stations.
 */
public enum AISMessageType {

	/** 1 = position report, scheduled (Class A, M); SOTDMA communication state. */
	PositionReportClassAScheduled(1),
	/** 2 = position report, assigned scheduled (Class A, M); SOTDMA communication state. */
	PositionReportClassAAssignedSchedule(2),
	/** 3 = position report, special or response to interrogation (Class A, M); ITDMA communication state. */
	PositionReportClassAResponseToInterrogation(3), 
	/** 4 = base station report: position, UTC, date and current slot number (B). */
	BaseStationReport(4), 
	/** 5 = static and voyage related data (Class A, M). */
	ShipAndVoyageRelatedData(5), 
	/** 6 = binary addressed message (M/B). */
	AddressedBinaryMessage(6), 
	/** 7 = binary acknowledgement of received addressed binary data (M/B). */
	BinaryAcknowledge(7), 
	/** 8 = binary broadcast message (M/B). */
	BinaryBroadcastMessage(8), 
	/** 9 = standard SAR aircraft position report (M). */
	StandardSARAircraftPositionReport(9),
	/** 10 = UTC and date inquiry (M/B). */
	UTCAndDateInquiry(10),
	/** 11 = UTC and date response (M). */
	UTCAndDateResponse(11),
	/** 12 = addressed safety related message (M/B). */
	AddressedSafetyRelatedMessage(12),
	/** 13 = safety related acknowledgement (M/B). */
	SafetyRelatedAcknowledge(13),
	/** 14 = safety related broadcast message (M/B). */
	SafetyRelatedBroadcastMessage(14),
	/** 15 = interrogation, request for specific message types (M/B). */
	Interrogation(15), 
	/** 16 = assignment mode command by a competent authority (B). */
	AssignedModeCommand(16), 
	/** 17 = DGNSS broadcast binary message (B). */
	GNSSBinaryBroadcastMessage(17), 
	/** 18 = standard Class B equipment position report (M). */
	StandardClassBCSPositionReport(18), 
	/** 19 = extended Class B equipment position report; no longer required (M). */
	ExtendedClassBEquipmentPositionReport(19), 
	/** 20 = data link management message, FATDMA slot reservation (B). */
	DataLinkManagement(20), 
	/** 21 = aids-to-navigation report (M/B). */
	AidToNavigationReport(21), 
	/** 22 = channel management (B). */
	ChannelManagement(22), 
	/** 23 = group assignment command (B). */
	GroupAssignmentCommand(23), 
	/** 24 = static data report, Part A and Part B (M/B). */
	ClassBCSStaticDataReport(24), 
	/** 25 = single slot binary message (M/B). */
	BinaryMessageSingleSlot(25),
	/** 26 = multiple slot binary message with communication state (M/B). */
	BinaryMessageMultipleSlot(26),
	/** 27 = AIS satellite message, formerly long-range broadcast message (Class A and Class B "SO", M). */
	LongRangeBroadcastMessage(27),
	/** 28 = aids-to-navigation report, single slot message (M). */
	AidToNavigationReportSingleSlot(28),
	/** Not an ITU-R M.1371 message type: marks an {@code Error} placeholder for undecodable input. */
	Error(-1);

	/** Lowest message ID this library decodes. */
	public final static int MINIMUM_CODE = 1;
	/** Highest message ID this library decodes. */
	public final static int MAXIMUM_CODE = 28;

	AISMessageType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	public static AISMessageType fromInteger(int code) {
		for (AISMessageType b : AISMessageType.values()) {
			if (code == b.code) return b;
		}
		return null;
	}

	public static AISMessageType fromInteger(Integer integer) {
		if (integer == null) return null;
		return fromInteger(integer.intValue());
	}

	private final int code;
}
