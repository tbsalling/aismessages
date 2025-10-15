package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupAssignmentCommandTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,G02:Kn01R`sn@291nj600000900,2*12");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GroupAssignmentCommand, aisMessage.getMessageType());
        GroupAssignmentCommand message = (GroupAssignmentCommand) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(2268120), message.getSourceMmsi());
        assertEquals("", message.getSpare1());
        assertEquals(Float.valueOf(157.8f), message.getNorthEastLongitude());
        assertEquals(Float.valueOf(3064.2f), message.getNorthEastLatitude());
        assertEquals(Float.valueOf(109.6f), message.getSouthWestLongitude());
        assertEquals(Float.valueOf(3040.8f), message.getSouthWestLatitude());
        assertEquals(StationType.InlandWaterways, message.getStationType());
        assertEquals(ShipType.NotAvailable, message.getShipType());
        assertEquals("$", message.getSpare2());
        assertEquals(TxRxMode.TxABRxAB, message.getTransmitReceiveMode());
        assertEquals(ReportingInterval.Autonomous, message.getReportingInterval());
        assertEquals(Integer.valueOf(0), message.getQuietTime());
    }

    @Test
    public void canDecodeAlternative() {
        // Arrange
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,G02:Kn01R`sn@291nj600000900,2*12");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GroupAssignmentCommand, aisMessage.getMessageType());
        GroupAssignmentCommand message = (GroupAssignmentCommand) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(2268120), message.getSourceMmsi());
        assertEquals(Float.valueOf(157.8f), message.getNorthEastLongitude());
        assertEquals(Float.valueOf(3064.2f), message.getNorthEastLatitude());
        assertEquals(Float.valueOf(109.6f), message.getSouthWestLongitude());
        assertEquals(Float.valueOf(3040.8f), message.getSouthWestLatitude());
        assertEquals(StationType.InlandWaterways, message.getStationType());
        assertEquals(ShipType.NotAvailable, message.getShipType());
        assertEquals(TxRxMode.TxABRxAB, message.getTransmitReceiveMode());
        assertEquals(ReportingInterval.Autonomous, message.getReportingInterval());
        assertEquals(Integer.valueOf(0), message.getQuietTime());
    }
}
