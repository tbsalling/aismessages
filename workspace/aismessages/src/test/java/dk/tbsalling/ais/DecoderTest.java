package dk.tbsalling.ais;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.tbsalling.ais.messages.BaseStationReport;
import dk.tbsalling.ais.messages.DecodedAISMessage;
import dk.tbsalling.ais.messages.EncodedAISMessage;
import dk.tbsalling.ais.messages.PositionReportClassAAssignedSchedule;
import dk.tbsalling.ais.messages.PositionReportClassAResponseToInterrogation;
import dk.tbsalling.ais.messages.PositionReportClassAScheduled;
import dk.tbsalling.ais.messages.ShipAndVoyageData;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.IMO;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.ais.messages.types.NavigationStatus;
import dk.tbsalling.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.ais.messages.types.ShipType;

public class DecoderTest {

	//
	// Message type 1
	// 
	
	@Test
	public void canDecodePositionReportClassAScheduled() {
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("1773mj001To8m<TE8;R<nb@p08@B", 0);
		DecodedAISMessage decodedAISMessage = Decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.PositionReportClassAScheduled, decodedAISMessage.getMessageType());
		assertEquals((Integer) 0, decodedAISMessage.getRepeatIndicator());
		PositionReportClassAScheduled message = (PositionReportClassAScheduled) decodedAISMessage;
		assertEquals(MMSI.valueOf(477165000L), message.getSourceMmsi());
		assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
		assertEquals((Integer) 0, message.getRateOfTurn());
		assertEquals((Float) 10.0f, message.getSpeedOverGround());
		assertTrue(message.getPositionAccurate());
		assertEquals(Float.valueOf(2215.0f), message.getLatitude());
		assertEquals(Float.valueOf(-5987.0f), message.getLongitude());
		assertEquals(Float.valueOf(-124.0f), message.getCourseOverGround());
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
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("24RjBV0028o:pnNEBeU<pJF>0PT@", 0);
		DecodedAISMessage decodedAISMessage = Decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());

		assertEquals(AISMessageType.PositionReportClassAAssignedSchedule, decodedAISMessage.getMessageType());
		assertEquals((Integer) 0, decodedAISMessage.getRepeatIndicator());
		PositionReportClassAAssignedSchedule message = (PositionReportClassAAssignedSchedule) decodedAISMessage;
		assertEquals(MMSI.valueOf(304911000L), message.getSourceMmsi());
		assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
		assertEquals((Integer) 0, message.getRateOfTurn());
		assertEquals((Float) 13.0f, message.getSpeedOverGround());
		assertTrue(message.getPositionAccurate());
		assertEquals(Float.valueOf(2232.0f), message.getLatitude());
		assertEquals(Float.valueOf(-6014.0f), message.getLongitude());
		assertEquals(Float.valueOf(-125.0f), message.getCourseOverGround());
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
		DecodedAISMessage decodedAISMessage = Decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());
		
		assertEquals(AISMessageType.PositionReportClassAResponseToInterrogation, decodedAISMessage.getMessageType());
		assertEquals((Integer) 0, decodedAISMessage.getRepeatIndicator());
		PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) decodedAISMessage;
		assertEquals(MMSI.valueOf(304911000L), message.getSourceMmsi());
		assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
		assertEquals((Integer) 0, message.getRateOfTurn());
		assertEquals((Float) 13.0f, message.getSpeedOverGround());
		assertTrue(message.getPositionAccurate());
		assertEquals(Float.valueOf(2232.0f), message.getLatitude());
		assertEquals(Float.valueOf(-6014.0f), message.getLongitude());
		assertEquals(Float.valueOf(-125.0f), message.getCourseOverGround());
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
		DecodedAISMessage decodedAISMessage = Decoder.decode(encodedAISMessage);
		System.out.println(decodedAISMessage.toString());
		
		assertEquals(AISMessageType.BaseStationReport, decodedAISMessage.getMessageType());
		assertEquals((Integer) 0, decodedAISMessage.getRepeatIndicator());
		BaseStationReport message = (BaseStationReport) decodedAISMessage;
		assertEquals(MMSI.valueOf(304911000L), message.getSourceMmsi());

//		year=2011, month=3, day=16, hour=6, minute=25, second=9, positionAccurate=true, latitude=2275.0, longitude=-6065.0, positionFixingDevice=Surveyed, raimFlag=false
	}
	
	//
	// Message type 6
	// 

	@Test
	public void canDecodeShipAndVoyageData() {
		EncodedAISMessage encodedAISMessage = new EncodedAISMessage("55Mwm;P09lcEL=OSW798uT4j0lDh8uE8pD00000l1@D274oDN7gUDp4iSp>@00000000000", 2);
		DecodedAISMessage decodedAISMessage = Decoder.decode(encodedAISMessage);
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
	
	// 181 degrees (0x6791AC0 hex)
	// 91 degrees (0x3412140 hex)
	// Course over ground will be 3600 (0xE10)

	// !AIVDM,2,1,3,A,55MuUD02;EFUL@CO;W@lU=<U=<U10V1HuT4LE:1DC@T>B4kC0DliSp=t,0*14
	// !AIVDM,2,2,3,A,888888888888880,2*27

	//
	// Message type 6
	// 

	//
	// Message type 7
	// 

	//
	// Message type 8
	// 

	// !AIVDM,1,1,,B,85MwpKiKf:MPiQa:ofV@v2mQTfB26oEtbEVqh4j1QDQPHjhpkNJ3,0*11

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

	// !AIVDM,1,1,,A,?h3Ovk1GOPph000,2*53

	//
	// Message type 16
	// 

	//
	// Message type 17
	// 

	//
	// Message type 18
	// 

	// !AIVDM,1,1,,A,B5NJ;PP005l4ot5Isbl03wsUkP06,0*76

	//
	// Message type 19
	// 
	
	// !AIVDM,2,2,4,A,CPjDhkp88888880,2*56

	//
	// Message type 20
	// 

	// !AIVDM,1,1,,A,Dh3Ovk1UAN>4,0*0A
	// !AIVDM,1,1,,B,Dh3Ovk1cEN>4,0*3B

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

	// !AIVDM,1,1,,A,H5NLOjTUG5CD=1BG46mqhj0P7130,0*78

	//
	// Message type 25
	// 

	//
	// Message type 26
	// 

}
