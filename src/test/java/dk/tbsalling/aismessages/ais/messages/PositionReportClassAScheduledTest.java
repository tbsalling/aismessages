package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionReportClassAScheduledTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,13@nePh01>PjcO4PGReoJEmL0HJg,0*67");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        PositionReportClassAScheduled message = (PositionReportClassAScheduled) aisMessage;
        assertEquals(new MMSI(219000195), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals(0, message.getRateOfTurn());
        assertEquals(7.8f, message.getSpeedOverGround(), 0.0f);
        assertTrue(message.isPositionAccuracy());
        assertEquals(56.56692f, message.getLatitude(), 0.0f);
        assertEquals(33940151, message.getRawLatitude());
        assertEquals(11.071096f, message.getLongitude(), 0.0f);
        assertEquals(6642658, message.getRawLongitude());
        assertEquals(189.7f, message.getCourseOverGround(), 0.0f);
        assertEquals(1897, message.getRawCourseOverGround());
        assertEquals(46, message.getSecond());
        assertEquals(186, message.getTrueHeading());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.isRaimFlag());
    }

    @Test
    public void detectInvalidMessage() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,13chv900160wps@GF<FlHCjR0<0ht2cv3i,0*1A");

        // Act & Assert
        assertThrows(InvalidMessage.class, () -> AISMessage.create(null, null, null, nmeaMessage));
    }

    @Test
    public void canDecodeCommunicationState() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,17OoHr?P009qtlQd6T<0<?wN041P,0*01");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.PositionReportClassAScheduled, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        PositionReportClassAScheduled message = (PositionReportClassAScheduled) aisMessage;
        assertEquals(new MMSI(503175400), message.getSourceMmsi());
        assertEquals(NavigationStatus.Undefined, message.getNavigationStatus());
        assertEquals(-731, message.getRateOfTurn());
        assertEquals(0.0f, message.getSpeedOverGround(), 0.0f);
        assertFalse(message.isPositionAccuracy());
        assertEquals(-34.773254f, message.getLatitude(), 0.0f);
        assertEquals(138.48856f, message.getLongitude(), 0.0f);
        assertEquals(4.8f, message.getCourseOverGround(), 0.0f);
        assertEquals(47, message.getSecond());
        assertEquals(511, message.getTrueHeading());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.isRaimFlag());

        CommunicationState communicationState = message.getCommunicationState();
        assertEquals(SyncState.UTCDirect, communicationState.getSyncState());

        assertTrue(communicationState instanceof SOTDMACommunicationState);
        SOTDMACommunicationState sotdmaCommunicationState = (SOTDMACommunicationState) communicationState;
        assertNull(sotdmaCommunicationState.getNumberOfReceivedStations());
        assertNull(sotdmaCommunicationState.getSlotNumber());
        assertNull(sotdmaCommunicationState.getSlotOffset());
        assertEquals(1, sotdmaCommunicationState.getSlotTimeout());
        assertEquals(0, sotdmaCommunicationState.getUtcHour());
        assertEquals(24, sotdmaCommunicationState.getUtcMinute());
    }
}
