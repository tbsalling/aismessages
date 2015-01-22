/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.ais.messages.types;

public enum AISMessageType {

	PositionReportClassAScheduled(1),
	PositionReportClassAAssignedSchedule(2),
	PositionReportClassAResponseToInterrogation(3), 
	BaseStationReport(4), 
	ShipAndVoyageRelatedData(5), 
	AddressedBinaryMessage(6), 
	BinaryAcknowledge(7), 
	BinaryBroadcastMessage(8), 
	StandardSARAircraftPositionReport(9),
	UTCAndDateInquiry(10),
	UTCAndDateResponse(11),
	AddressedSafetyRelatedMessage(12),
	SafetyRelatedAcknowledge(13),
	SafetyRelatedBroadcastMessage(14),
	Interrogation(15), 
	AssignedModeCommand(16), 
	GNSSBinaryBroadcastMessage(17), 
	StandardClassBCSPositionReport(18), 
	ExtendedClassBEquipmentPositionReport(19), 
	DataLinkManagement(20), 
	AidToNavigationReport(21), 
	ChannelManagement(22), 
	GroupAssignmentCommand(23), 
	ClassBCSStaticDataReport(24), 
	BinaryMessageSingleSlot(25),
	BinaryMessageMultipleSlot(26),
	LongRangeBroadcastMessage(27),
	Error(-1);

	public final static int MINIMUM_CODE = 1;
	public final static int MAXIMUM_CODE = 27;

	AISMessageType(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public String getValue() {
	    return toString();
	}

	public static AISMessageType fromInteger(Integer integer) {
		if (integer != null) {
			for (AISMessageType b : AISMessageType.values()) {
				if (integer.equals(b.code)) {
					return b;
				}
			}
		}
		return null;
	}

	private final Integer code;
}
