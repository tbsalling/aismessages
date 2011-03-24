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

package dk.tbsalling.aismessages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.tbsalling.aismessages.decoder.Decoder;
import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.messages.BaseStationReport;
import dk.tbsalling.aismessages.messages.BinaryBroadcastMessage;
import dk.tbsalling.aismessages.messages.ClassBCSStaticDataReport;
import dk.tbsalling.aismessages.messages.DataLinkManagement;
import dk.tbsalling.aismessages.messages.DecodedAISMessage;
import dk.tbsalling.aismessages.messages.EncodedAISMessage;
import dk.tbsalling.aismessages.messages.Interrogation;
import dk.tbsalling.aismessages.messages.PositionReportClassAAssignedSchedule;
import dk.tbsalling.aismessages.messages.PositionReportClassAResponseToInterrogation;
import dk.tbsalling.aismessages.messages.PositionReportClassAScheduled;
import dk.tbsalling.aismessages.messages.ShipAndVoyageData;
import dk.tbsalling.aismessages.messages.StandardClassBCSPositionReport;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.IMO;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.messages.types.ShipType;

public class DecoderImplTest {
	
	private static Decoder decoder;
	
	@BeforeClass
	public static void setUp() {
		decoder = new DecoderImpl();
	}
	
	@Test
	public void canConvertToUnsignedInteger() {
		// 1011100101011100011011001111 -> -123.450533333333
		
		assertEquals(Float.valueOf(0), Float.valueOf(DecoderImpl.convertToFloat("0000000000000000000000000000")));
		assertEquals(Float.valueOf(1), Float.valueOf(DecoderImpl.convertToFloat("0000000000000000000000000001")));
		assertEquals(Float.valueOf(0), Float.valueOf(DecoderImpl.convertToFloat("1111111111111111111111111111")));
		assertEquals(Float.valueOf(-123.450533333333f), Float.valueOf(DecoderImpl.convertToFloat("1011100101011100011011001111") / 600000f));// 74070320
		assertEquals(Float.valueOf(37.21113f),          Float.valueOf(DecoderImpl.convertToFloat("001010101001010110110010100") / 600000f));
		// 181 degrees (0x6791AC0 hex)
		// Decoder.convertToUnsignedInteger(bitString)
		// 91 degrees (0x3412140 hex)
		// Course over ground will be 3600 (0xE10)
	}

	//
	// Message type 1
	// 
	
	@Test
	public void canDecodePositionReportClassAScheduled() {
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("1773mj001To8m<TE8;R<nb@p08@B", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.PositionReportClassAScheduled, decodedAISMessage.getMessageType());
		assertEquals((Integer) 0, decodedAISMessage.getRepeatIndicator());
		PositionReportClassAScheduled message = (PositionReportClassAScheduled) decodedAISMessage;
		assertEquals(MMSI.valueOf(477165000L), message.getSourceMmsi());
		assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
		assertEquals((Integer) 0, message.getRateOfTurn());
		assertEquals((Float) 10.0f, message.getSpeedOverGround());
		assertTrue(message.getPositionAccurate());
		assertEquals(Float.valueOf(36.923534f), message.getLatitude());
		assertEquals(Float.valueOf(-123.89992f), message.getLongitude());
		assertEquals(Float.valueOf(329f), message.getCourseOverGround());
		assertEquals((Integer) 28, message.getSecond());
		assertEquals((Integer) 328, message.getTrueHeading());
		assertEquals(ManeuverIndicator.NotAvailable, message.getManeuverIndicator());
		assertFalse(message.getRaimFlag());
	}

	//
	// Message type 2
	// 

	@Test
	public void canDecodePositionReportClassAAssignedSchedule() {
		// !AIVDM,1,1,,A,24RjBV0028o:pnNEBeU<pJF>0PT@,0*3F
		
		/*
		User ID	304911000	
		Navigation Status	0	Under way using engine
		Rate of Turn (ROT)	0	Turning right at up to 708 degrees per minute or higher
		Speed Over Ground (SOG)	13.6	
		Position Accuracy	1	A DGPS-quality fix with an accuracy of < 10 ms
		Longitude	-123.450533333333	West
		Latitude	37.2111266666667	North
		Course Over Ground (COG)	329.7	
		True Heading (HDG)	331	
		Time Stamp	7	
		Reserved for regional	0	Not available (default)
		RAIM flag	0	RAIM not in use (default)
		Communication State	133392	Sync state: UTC Indirect; Slot Timeout: This was the last transmission in this slot; Slot offset: 2320
		COMMUNICATION_SYNC_STATE	1	Sync state: UTC Indirect
		COMMUNICATION_SLOT_TIMEOUT	0	Slot Timeout: This was the last transmission in this slot
		COMMUNICATION_SUB_MESSAGE	2320	
		COMMUNICATION_UTC_HOUR	No value	
		COMMUNICATION_UTC_MINUTE	No value	
		COMMUNICATION_TIME_STAMP	No value	
		COMMUNICATION_SLOT_NUMBER	No value	
		COMMUNICATION_RECEIVED_STATIONS	No value	
		COMMUNICATION_SLOT_OFFSET	2320	
		*/
		
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("24RjBV0028o:pnNEBeU<pJF>0PT@", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.PositionReportClassAAssignedSchedule, decodedAISMessage.getMessageType());
		assertEquals((Integer) 0, decodedAISMessage.getRepeatIndicator());
		PositionReportClassAAssignedSchedule message = (PositionReportClassAAssignedSchedule) decodedAISMessage;
		assertEquals(MMSI.valueOf(304911000L), message.getSourceMmsi());
		assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
		assertEquals((Integer) 0, message.getRateOfTurn());
		assertEquals((Float) 13.6f, message.getSpeedOverGround());
		assertTrue(message.getPositionAccurate());
		assertEquals(Float.valueOf(37.21113f), message.getLatitude());
		assertEquals(Float.valueOf(-123.45053f), message.getLongitude());
		assertEquals(Float.valueOf(329.7f), message.getCourseOverGround());
		assertEquals((Integer) 331, message.getTrueHeading());
		assertEquals((Integer) 7, message.getSecond());
		assertEquals(ManeuverIndicator.NotAvailable, message.getManeuverIndicator());
		assertFalse(message.getRaimFlag());
	}

	//
	// Message type 3
	// 

	@Test
	public void canDecodePositionReportClassAResponseToInterrogation() {
		// !AIVDM,1,1,,A,34RjBV0028o:pnNEBeU<pJF>0PT@,0*3F
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("34RjBV0028o:pnNEBeU<pJF>0PT@", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());
		
		assertEquals(AISMessageType.PositionReportClassAResponseToInterrogation, decodedAISMessage.getMessageType());
		assertEquals((Integer) 0, decodedAISMessage.getRepeatIndicator());
		PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) decodedAISMessage;
		assertEquals(MMSI.valueOf(304911000L), message.getSourceMmsi());
		assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
		assertEquals((Integer) 0, message.getRateOfTurn());
		assertEquals((Float) 13.6f, message.getSpeedOverGround());
		assertTrue(message.getPositionAccurate());
		assertEquals(Float.valueOf(37.21113f), message.getLatitude());
		assertEquals(Float.valueOf(-123.45053f), message.getLongitude());
		assertEquals(Float.valueOf(329.7f), message.getCourseOverGround());
		assertEquals((Integer) 331, message.getTrueHeading());
		assertEquals((Integer) 7, message.getSecond());
		assertEquals(ManeuverIndicator.NotAvailable, message.getManeuverIndicator());
		assertFalse(message.getRaimFlag());
	}

	//
	// Message type 4
	// 

	@Test
	public void canDecodeBaseStationReport() {
		// !AIVDM,1,1,,B,4h3Ovk1udp6I9o>jPHEdjdW000S:,0*0C
		
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("4h3Ovk1udp6I9o>jPHEdjdW000S:", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());
		
		assertEquals(AISMessageType.BaseStationReport, decodedAISMessage.getMessageType());
		assertEquals((Integer) 3, decodedAISMessage.getRepeatIndicator());
		BaseStationReport message = (BaseStationReport) decodedAISMessage;
		assertEquals(MMSI.valueOf(3669708L), message.getSourceMmsi());
		assertEquals((Integer) 2011, message.getYear());
		assertEquals((Integer) 3, message.getMonth());
		assertEquals((Integer) 16, message.getDay());
		assertEquals((Integer) 6, message.getHour());
		assertEquals((Integer) 25, message.getMinute());
		assertEquals((Integer) 9, message.getSecond());
		assertTrue(message.getPositionAccurate());
		assertEquals(Float.valueOf(37.923283f), message.getLatitude());
		assertEquals(Float.valueOf(-122.59837f), message.getLongitude());
		assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
		assertFalse(message.getRaimFlag());
	}
	
	//
	// Message type 5
	// 

	@Test
	public void canDecodeShipAndVoyageData1() {
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("55Mwm;P09lcEL=OSW798uT4j0lDh8uE8pD00000l1@D274oDN7gUDp4iSp>@00000000000", 2);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.ShipAndVoyageRelatedData, decodedAISMessage.getMessageType());
		ShipAndVoyageData message = (ShipAndVoyageData) decodedAISMessage;
		assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(366998830L), message.getSourceMmsi());
		assertEquals(IMO.valueOf(643765L), message.getImo());
		assertEquals("WCW8912", message.getCallsign());
		assertEquals("ROYAL MELBOURNE@@@@@", message.getShipName());
		assertEquals(ShipType.Tug, message.getShipType());
		assertEquals(Integer.valueOf(10), message.getToBow());
		assertEquals(Integer.valueOf(20), message.getToStern());
		assertEquals(Integer.valueOf(7), message.getToStarboard());
		assertEquals(Integer.valueOf(2), message.getToPort());
		assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
		assertEquals(Float.valueOf("3.0"), message.getDraught());
		assertEquals("14-03 20:30", message.getEta());
		assertEquals(">US SFO 9@@@@@@@@@@@", message.getDestination());
		assertFalse(message.getDataTerminalReady());
	}
	
	@Test
	public void canDecodeShipAndVoyageData2() {
		// !AIVDM,2,1,3,A,55MuUD02;EFUL@CO;W@lU=<U=<U10V1HuT4LE:1DC@T>B4kC0DliSp=t,0*14
		// !AIVDM,2,2,3,A,888888888888880,2*27

		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("55MuUD02;EFUL@CO;W@lU=<U=<U10V1HuT4LE:1DC@T>B4kC0DliSp=t888888888888880", 2);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.ShipAndVoyageRelatedData, decodedAISMessage.getMessageType());
		ShipAndVoyageData message = (ShipAndVoyageData) decodedAISMessage;
		assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(366962000L), message.getSourceMmsi());
		assertEquals(IMO.valueOf(9131369L), message.getImo());
		assertEquals("WDD7294", message.getCallsign());
		assertEquals("MISSISSIPPI VOYAGER ", message.getShipName());
		assertEquals(ShipType.TankerHazardousD, message.getShipType());
		assertEquals(Integer.valueOf(154), message.getToBow());
		assertEquals(Integer.valueOf(36), message.getToStern());
		assertEquals(Integer.valueOf(18), message.getToStarboard());
		assertEquals(Integer.valueOf(14), message.getToPort());
		assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
		assertEquals(Float.valueOf("8.3"), message.getDraught());
		assertEquals("06-03 19:00", message.getEta());
		assertEquals("SFO 70              ", message.getDestination());
		assertFalse(message.getDataTerminalReady());
	}
	                   
	//
	// Message type 6
	// 

	//
	// Message type 7
	// 

	//
	// Message type 8
	// 

	@Test
	public void canDecodeBinaryBroadcastMessage() {
		// !AIVDM,1,1,,B,85MwpKiKf:MPiQa:ofV@v2mQTfB26oEtbEVqh4j1QDQPHjhpkNJ3,0*11

		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("85MwpKiKf:MPiQa:ofV@v2mQTfB26oEtbEVqh4j1QDQPHjhpkNJ3", 2);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.BinaryBroadcastMessage, decodedAISMessage.getMessageType());
		BinaryBroadcastMessage message = (BinaryBroadcastMessage) decodedAISMessage;
		assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(366999663L), message.getSourceMmsi());
		assertEquals((Integer) 1467, message.getDesignatedAreaCode());
		assertEquals((Integer) 8, message.getFunctionalId());
		assertEquals("1000", message.getBinaryData());
	}
	
	//
	// Message type 9
	// 

	//
	// Message type 10
	// 

	//
	// Message type 11
	// 

	//
	// Message type 12
	// 

	//
	// Message type 13
	// 

	//
	// Message type 14
	// 

	//
	// Message type 15
	// 

	@Test
	public void canDecodeInterrogationShortVariant() {
		// !AIVDM,1,1,,A,?h3Ovk1GOPph000,2*53

		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("?h3Ovk1GOPph000", 2);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.Interrogation, decodedAISMessage.getMessageType());
		Interrogation message = (Interrogation) decodedAISMessage;
		assertEquals(Integer.valueOf(3), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(3669708L), message.getSourceMmsi());
		assertEquals(MMSI.valueOf(366969740L), message.getInterrogatedMmsi1());
		assertEquals((Integer) 0, message.getType1_1());
		assertEquals((Integer) 0, message.getOffset1_1());
		assertNull(message.getType1_2());
		assertNull(message.getOffset1_2());
		assertNull(message.getInterrogatedMmsi2());
		assertNull(message.getType2_1());
		assertNull(message.getOffset2_1());
	}
	
	//
	// Message type 16
	// 

	//
	// Message type 17
	// 

	//
	// Message type 18
	// 

	@Test
	public void canDecodeStandardClassBCSPositionReport() {
		// !AIVDM,1,1,,A,B5NJ;PP005l4ot5Isbl03wsUkP06,0*76
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("B5NJ;PP005l4ot5Isbl03wsUkP06", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.StandardClassBCSPositionReport, decodedAISMessage.getMessageType());
		StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) decodedAISMessage;
		assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(367430530L), message.getSourceMmsi());
		assertEquals("00000000", message.getRegionalReserved1());
		assertEquals((Float) 0.0f, message.getSpeedOverGround());
		assertFalse(message.getPositionAccurate());
		assertEquals(Float.valueOf(37.785034f), message.getLatitude());
		assertEquals(Float.valueOf(-122.26732f), message.getLongitude());
		assertEquals(Float.valueOf(0.0f), message.getCourseOverGround());
		assertEquals((Integer) 511, message.getTrueHeading());
		assertEquals((Integer) 55, message.getSecond());
		assertEquals("00", message.getRegionalReserved2());
		assertTrue(message.getCsUnit());
		assertFalse(message.getDisplay());
		assertTrue(message.getDsc());
		assertTrue(message.getBand());
		assertTrue(message.getMessage22());
		assertFalse(message.getAssigned());
		assertFalse(message.getRaimFlag());
		assertEquals("11100000000000000110", message.getRadioStatus());
	}

	//
	// Message type 19
	// 
	
	//
	// Message type 20
	// 

	@Test
	public void canDecodeDataLinkManagementShortVariant1() {
		// !AIVDM,1,1,,A,Dh3Ovk1UAN>4,0*0A
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("Dh3Ovk1UAN>4", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.DataLinkManagement, decodedAISMessage.getMessageType());
		DataLinkManagement message = (DataLinkManagement) decodedAISMessage;
		assertEquals(Integer.valueOf(3), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(3669708L), message.getSourceMmsi());
		assertEquals((Integer) 1620, message.getOffsetNumber1());
		assertEquals((Integer) 5, message.getReservedSlots1());
		assertEquals((Integer) 7, message.getTimeout1());
		assertEquals((Integer) 225, message.getIncrement1());
		assertNull(message.getOffsetNumber2());
		assertNull(message.getReservedSlots2());
		assertNull(message.getTimeout2());
		assertNull(message.getIncrement2());
		assertNull(message.getOffsetNumber3());
		assertNull(message.getReservedSlots3());
		assertNull(message.getTimeout3());
		assertNull(message.getIncrement3());
	}
	
	@Test
	public void canDecodeDataLinkManagementShortVariant2() {
		// !AIVDM,1,1,,B,Dh3Ovk1cEN>4,0*3B
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("Dh3Ovk1cEN>4", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.DataLinkManagement, decodedAISMessage.getMessageType());
		DataLinkManagement message = (DataLinkManagement) decodedAISMessage;
		assertEquals(Integer.valueOf(3), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(3669708L), message.getSourceMmsi());
		assertEquals((Integer) 1717, message.getOffsetNumber1());
		assertEquals((Integer) 5, message.getReservedSlots1());
		assertEquals((Integer) 7, message.getTimeout1());
		assertEquals((Integer) 225, message.getIncrement1());
		assertNull(message.getOffsetNumber2());
		assertNull(message.getReservedSlots2());
		assertNull(message.getTimeout2());
		assertNull(message.getIncrement2());
		assertNull(message.getOffsetNumber3());
		assertNull(message.getReservedSlots3());
		assertNull(message.getTimeout3());
		assertNull(message.getIncrement3());
	}

	//
	// Message type 21
	// 

	//
	// Message type 22
	// 

	//
	// Message type 23
	// 

	//
	// Message type 24
	// 

	@Test
	public void canDecodeClassBCSStaticDataReportVariant1() {
		// !AIVDM,1,1,,A,H5NLOjTUG5CD=1BG46mqhj0P7130,0*78
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("H5NLOjTUG5CD=1BG46mqhj0P7130", 0);
		DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.ClassBCSStaticDataReport, decodedAISMessage.getMessageType());
		ClassBCSStaticDataReport message = (ClassBCSStaticDataReport) decodedAISMessage;
		assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
		assertEquals(MMSI.valueOf(367468490L), message.getSourceMmsi());
		assertEquals((Integer) 1, message.getPartNumber());
		assertNull(message.getShipName());
		assertEquals(ShipType.PleasureCraft, message.getShipType());
		assertEquals("WESTMAR", message.getVendorId());
		assertEquals("WDF5902", message.getCallsign());
		assertEquals((Integer) 4, message.getToBow());
		assertEquals((Integer) 7, message.getToStern());
		assertEquals((Integer) 3, message.getToStarboard());
		assertEquals((Integer) 1, message.getToPort());
		assertEquals(MMSI.valueOf(8417347L), message.getMothershipMmsi());
	}

	//
	// Message type 25
	// 

	//
	// Message type 26
	// 
}
