package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionReportClassAScheduledTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,13@nePh01>PjcO4PGReoJEmL0HJg,0*67"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        PositionReportClassAScheduled message = (PositionReportClassAScheduled) aisMessage;
        assertEquals(MMSI.valueOf(219000195), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals((Integer) 0, message.getRateOfTurn());
        assertEquals((Float) 7.8f, message.getSpeedOverGround());
        assertTrue(message.getPositionAccuracy());
        assertEquals(Float.valueOf(56.56692f), message.getLatitude());
        assertEquals((Integer) 33940151, message.getRawLatitude());
        assertEquals(Float.valueOf(11.071096f), message.getLongitude());
        assertEquals((Integer) 6642658, message.getRawLongitude());
        assertEquals(Float.valueOf(189.7f), message.getCourseOverGround());
        assertEquals((Integer) 1897, message.getRawCourseOverGround());
        assertEquals((Integer) 46, message.getSecond());
        assertEquals((Integer) 186, message.getTrueHeading());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.getRaimFlag());
    }

    @Test
    public void detectInvalidMessage() {
        assertThrows(InvalidMessage.class, () -> AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,13chv900160wps@GF<FlHCjR0<0ht2cv3i,0*1A")));
    }

    @Test
    public void canDecodeCommunicationState() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,17OoHr?P009qtlQd6T<0<?wN041P,0*01"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        PositionReportClassAScheduled message = (PositionReportClassAScheduled) aisMessage;
        assertEquals(MMSI.valueOf(503175400), message.getSourceMmsi());
        assertEquals(NavigationStatus.Undefined, message.getNavigationStatus());
        assertEquals(Integer.valueOf(-731), message.getRateOfTurn());
        assertEquals(Float.valueOf(0.0f), message.getSpeedOverGround());
        assertFalse(message.getPositionAccuracy());
        assertEquals(Float.valueOf(-34.773254f), message.getLatitude());
        assertEquals(Float.valueOf(138.48856f), message.getLongitude());
        assertEquals(Float.valueOf(4.8f), message.getCourseOverGround());
        assertEquals(Integer.valueOf(47), message.getSecond());
        assertEquals(Integer.valueOf(511), message.getTrueHeading());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.getRaimFlag());

        CommunicationState communicationState = message.getCommunicationState();
        assertEquals(SyncState.UTCDirect, communicationState.getSyncState());

        assertTrue(communicationState instanceof SOTDMACommunicationState);
        SOTDMACommunicationState sotdmaCommunicationState = (SOTDMACommunicationState) communicationState;
        assertNull(sotdmaCommunicationState.getNumberOfReceivedStations());
        assertNull(sotdmaCommunicationState.getSlotNumber());
        assertNull(sotdmaCommunicationState.getSlotOffset());
        assertEquals(Integer.valueOf(1), sotdmaCommunicationState.getSlotTimeout());
        assertEquals(Integer.valueOf(0), sotdmaCommunicationState.getUtcHour());
        assertEquals(Integer.valueOf(24), sotdmaCommunicationState.getUtcMinute());
    }
}