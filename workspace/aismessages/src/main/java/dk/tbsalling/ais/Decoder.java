package dk.tbsalling.ais;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.AddressedBinaryMessage;
import dk.tbsalling.ais.messages.AddressedSafetyRelatedMessage;
import dk.tbsalling.ais.messages.AidToNavigationReport;
import dk.tbsalling.ais.messages.AssignedModeCommand;
import dk.tbsalling.ais.messages.BaseStationReport;
import dk.tbsalling.ais.messages.BinaryAcknowledge;
import dk.tbsalling.ais.messages.BinaryMessageMultipleSlot;
import dk.tbsalling.ais.messages.BinaryMessageSingleSlot;
import dk.tbsalling.ais.messages.ChannelManagement;
import dk.tbsalling.ais.messages.ClassBCSStaticDataReport;
import dk.tbsalling.ais.messages.DataLinkManagement;
import dk.tbsalling.ais.messages.DecodedAISMessage;
import dk.tbsalling.ais.messages.EncodedAISMessage;
import dk.tbsalling.ais.messages.GNSSBinaryBroadcastMessage;
import dk.tbsalling.ais.messages.GroupAssignmentCommand;
import dk.tbsalling.ais.messages.Interrogation;
import dk.tbsalling.ais.messages.PositionReportClassAAssignedSchedule;
import dk.tbsalling.ais.messages.PositionReportClassAResponseToInterrogation;
import dk.tbsalling.ais.messages.PositionReportClassAScheduled;
import dk.tbsalling.ais.messages.SafetyRelatedAcknowledge;
import dk.tbsalling.ais.messages.SafetyRelatedBroadcastMessage;
import dk.tbsalling.ais.messages.ShipAndVoyageData;
import dk.tbsalling.ais.messages.StandardClassBCSPositionReport;
import dk.tbsalling.ais.messages.StandardSARAircraftPositionReport;
import dk.tbsalling.ais.messages.UTCAndDateInquiry;
import dk.tbsalling.ais.messages.UTCAndDateResponse;
import dk.tbsalling.ais.messages.types.AISMessageType;

public class Decoder {

    private static final Logger log = Logger.getLogger(Decoder.class.getName());
	
	static {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("AISMessages v1.0 // Copyright (c) 2011 by S-Consult ApS, Denmark, CVR DK31327490. http://s-consult.dk.\n");
		sb.append("\n");
		sb.append("This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of\n");
		sb.append("this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 171 Second Street,\n");
		sb.append("Suite 300, San Francisco, California, 94105, USA.\n");
		sb.append("\n");
		sb.append("NOT FOR COMMERCIAL USE!\n");
		sb.append("Contact sales@s-consult.dk to obtain commercially licensed software.\n");
		sb.append("\n");
		System.err.print(sb.toString());
		log.info(sb.toString());
	}
	
	private Decoder() {
	}
	
	public static DecodedAISMessage decode(EncodedAISMessage encodedMessage) {
		AISMessageType messageType = encodedMessage.getMessageType();
		log.log(Level.FINER, "Attempting to decode message of type " + messageType);
		switch(messageType) {
		case ShipAndVoyageRelatedData:
			return ShipAndVoyageData.fromEncodedMessage(encodedMessage);
		case PositionReportClassAScheduled:
			return PositionReportClassAScheduled.fromEncodedMessage(encodedMessage);
		case PositionReportClassAAssignedSchedule:
			return PositionReportClassAAssignedSchedule.fromEncodedMessage(encodedMessage);
		case PositionReportClassAResponseToInterrogation:
			return PositionReportClassAResponseToInterrogation.fromEncodedMessage(encodedMessage);
		case BaseStationReport:
			return BaseStationReport.fromEncodedMessage(encodedMessage);
		case AddressedBinaryMessage: 
			return AddressedBinaryMessage.fromEncodedMessage(encodedMessage);
		case BinaryAcknowledge:
			return BinaryAcknowledge.fromEncodedMessage(encodedMessage);
		case BinaryBroadcastMessage:
			return BinaryAcknowledge.fromEncodedMessage(encodedMessage);
		case StandardSARAircraftPositionReport:
			return StandardSARAircraftPositionReport.fromEncodedMessage(encodedMessage);
		case UTCAndDateInquiry:
			return UTCAndDateInquiry.fromEncodedMessage(encodedMessage);
		case UTCAndDateResponse:
			return UTCAndDateResponse.fromEncodedMessage(encodedMessage);
		case AddressedSafetyRelatedMessage:
			return AddressedSafetyRelatedMessage.fromEncodedMessage(encodedMessage);
		case SafetyRelatedAcknowledge:
			return SafetyRelatedAcknowledge.fromEncodedMessage(encodedMessage);
		case SafetyRelatedBroadcastMessage:
			return SafetyRelatedBroadcastMessage.fromEncodedMessage(encodedMessage);
		case Interrogation:
			return Interrogation.fromEncodedMessage(encodedMessage);
		case AssignedModeCommand:
			return AssignedModeCommand.fromEncodedMessage(encodedMessage);
		case GNSSBinaryBroadcastMessage:
			return GNSSBinaryBroadcastMessage.fromEncodedMessage(encodedMessage);
		case StandardClassBCSPositionReport:
			return StandardClassBCSPositionReport.fromEncodedMessage(encodedMessage);
		case ExtendedClassBEquipmentPositionReport:
			return StandardClassBCSPositionReport.fromEncodedMessage(encodedMessage);
		case DataLinkManagement:
			return DataLinkManagement.fromEncodedMessage(encodedMessage);
		case AidToNavigationReport:
			return AidToNavigationReport.fromEncodedMessage(encodedMessage);
		case ChannelManagement:
			return ChannelManagement.fromEncodedMessage(encodedMessage);
		case GroupAssignmentCommand:
			return GroupAssignmentCommand.fromEncodedMessage(encodedMessage);
		case ClassBCSStaticDataReport:
			return ClassBCSStaticDataReport.fromEncodedMessage(encodedMessage);
		case BinaryMessageSingleSlot:
			return BinaryMessageSingleSlot.fromEncodedMessage(encodedMessage);
		case BinaryMessageMultipleSlot:
			return BinaryMessageMultipleSlot.fromEncodedMessage(encodedMessage);
		default:
			throw new UnsupportedMessageType(messageType.getCode());
		}
	}

	public static Boolean convertToBoolean(String bits) {
		return "1".equals(bits.substring(0, 1));
	}

	public static Integer convertToUnsignedInteger(String bitString) {
		return Integer.parseInt(bitString, 2);
	}
	
	public static Integer convertToSignedInteger(String bitString) {
		Integer value;
		String signBit = bitString.substring(0, 1);
		String valueBits = bitString.substring(1);
		if (signBit.endsWith("0"))
			value = convertToUnsignedInteger(valueBits);
		else {
			value = - convertToUnsignedInteger(valueBits) - 1;
		}
		return value;
	}

	public static Long convertToUnsignedLong(String bitString) {
		return Long.parseLong(bitString, 2);
	}

	public static String convertToTime(String bits) {
		Integer month = convertToUnsignedInteger(bits.substring(0, 4));
		Integer day = convertToUnsignedInteger(bits.substring(4, 9));
		Integer hour = convertToUnsignedInteger(bits.substring(9, 14));
		Integer minute = convertToUnsignedInteger(bits.substring(14,20));

		String monthAsString = month<10 ? "0"+month : ""+month;
		String dayAsString = day<10 ? "0"+day : ""+day;
		String hourAsString = hour<10 ? "0"+hour : ""+hour;
		String minuteAsString = minute<10 ? "0"+minute : ""+minute;

		return dayAsString + "-" + monthAsString + " " + hourAsString + ":" + minuteAsString;
	}

	public static String convertToString(String bits) {
		StringBuffer string = new StringBuffer();
		String remainingBits = new String(bits);
		while (remainingBits.length() >= 6) {
			String b = remainingBits.substring(0, 6);
			remainingBits = remainingBits.substring(6);
			Integer i = convertToUnsignedInteger(b);
			String c = sixBitAscii.get(i);
			string.append(c);
		}
		return string.toString();
	}
	
	public static String convertToBitString(String bits) {
		return bits;
	}

	private final static Map<Integer, String> sixBitAscii = new HashMap<Integer, String>();

	static {
		sixBitAscii.put(0, "@"); // 0
		sixBitAscii.put(1, "A"); // 1
		sixBitAscii.put(2, "B"); // 2
		sixBitAscii.put(3, "C"); // 3
		sixBitAscii.put(4, "D"); // 4
		sixBitAscii.put(5, "E"); // 5
		sixBitAscii.put(6, "F"); // 6
		sixBitAscii.put(7, "G"); // 7
		sixBitAscii.put(8, "H"); // 8
		sixBitAscii.put(9, "I"); // 9
		sixBitAscii.put(10, "J"); // 10
		sixBitAscii.put(11, "K"); // 11
		sixBitAscii.put(12, "L"); // 12
		sixBitAscii.put(13, "M"); // 13
		sixBitAscii.put(14, "N"); // 14
		sixBitAscii.put(15, "O"); // 15
		sixBitAscii.put(16, "P"); // 16
		sixBitAscii.put(17, "Q"); // 17
		sixBitAscii.put(18, "R"); // 18
		sixBitAscii.put(19, "S"); // 19
		sixBitAscii.put(20, "T"); // 20
		sixBitAscii.put(21, "U"); // 21
		sixBitAscii.put(22, "V"); // 22
		sixBitAscii.put(23, "W"); // 23 
		sixBitAscii.put(24, "X"); // 24
		sixBitAscii.put(25, "Y"); // 25
		sixBitAscii.put(26, "Z"); // 26 
		sixBitAscii.put(27, "["); // 27
		sixBitAscii.put(28, "\\"); // 28
		sixBitAscii.put(29, "]"); // 29
		sixBitAscii.put(30, "^"); // 30
		sixBitAscii.put(31, "_"); // 31
		sixBitAscii.put(32, " "); // 32
		sixBitAscii.put(33, "!"); // 33
		sixBitAscii.put(34, "\""); // 34
		sixBitAscii.put(35, "#"); // 35
		sixBitAscii.put(36, "$"); // 36
		sixBitAscii.put(37, "%"); // 37
		sixBitAscii.put(38, "&"); // 38
		sixBitAscii.put(39, "'"); // 39
		sixBitAscii.put(40, "("); // 40
		sixBitAscii.put(41, ")"); // 41
		sixBitAscii.put(42, "*"); // 42
		sixBitAscii.put(43, "+"); // 43 
		sixBitAscii.put(44, ","); // 44
		sixBitAscii.put(45, "-"); // 45
		sixBitAscii.put(46, "."); // 46
		sixBitAscii.put(47, "/"); // 47
		sixBitAscii.put(48, "0"); // 48
		sixBitAscii.put(49, "1"); // 49
		sixBitAscii.put(50, "2"); // 50
		sixBitAscii.put(51, "3"); // 51
		sixBitAscii.put(52, "4"); // 52
		sixBitAscii.put(53, "5"); // 53
		sixBitAscii.put(54, "6"); // 54
		sixBitAscii.put(55, "7"); // 55
		sixBitAscii.put(56, "8"); // 56
		sixBitAscii.put(57, "9"); // 57
		sixBitAscii.put(58, ":"); // 58
		sixBitAscii.put(59, ";"); // 59
		sixBitAscii.put(60, "<"); // 60
		sixBitAscii.put(61, "="); // 61
		sixBitAscii.put(62, ">"); // 62
		sixBitAscii.put(63, "?"); // 63
	}

}
