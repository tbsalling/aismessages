package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PositionReportClassAAssignedScheduleTest {

    @Test
    public void canDecode() {
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

        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,24RjBV0028o:pnNEBeU<pJF>0PT@,0*3F"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.PositionReportClassAAssignedSchedule, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        PositionReportClassAAssignedSchedule message = (PositionReportClassAAssignedSchedule) aisMessage;
        assertEquals(MMSI.valueOf(304911000), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals((Integer) 0, message.getRateOfTurn());
        assertEquals((Float) 13.6f, message.getSpeedOverGround());
        assertTrue(message.getPositionAccuracy());
        assertEquals(Float.valueOf(37.21113f), message.getLatitude());
        assertEquals(Float.valueOf(-123.45053f), message.getLongitude());
        assertEquals(Float.valueOf(329.7f), message.getCourseOverGround());
        assertEquals((Integer) 331, message.getTrueHeading());
        assertEquals((Integer) 7, message.getSecond());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.getRaimFlag());
    }
}