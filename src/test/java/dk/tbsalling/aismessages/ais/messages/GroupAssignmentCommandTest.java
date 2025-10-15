package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupAssignmentCommandTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,G02:Kn01R`sn@291nj600000900,2*12");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GroupAssignmentCommand, aisMessage.getMessageType());
        GroupAssignmentCommand message = (GroupAssignmentCommand) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2268120), message.getSourceMmsi());
        assertEquals("", message.getSpare1());
        assertEquals(157.8f, message.getNorthEastLongitude(), 0.0f);
        assertEquals(3064.2f, message.getNorthEastLatitude(), 0.0f);
        assertEquals(109.6f, message.getSouthWestLongitude(), 0.0f);
        assertEquals(3040.8f, message.getSouthWestLatitude(), 0.0f);
        assertEquals(StationType.InlandWaterways, message.getStationType());
        assertEquals(ShipType.NotAvailable, message.getShipType());
        assertEquals("$", message.getSpare2());
        assertEquals(TxRxMode.TxABRxAB, message.getTransmitReceiveMode());
        assertEquals(ReportingInterval.Autonomous, message.getReportingInterval());
        assertEquals(0, message.getQuietTime());
    }

    @Test
    public void canDecodeAlternative() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,G02:Kn01R`sn@291nj600000900,2*12");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GroupAssignmentCommand, aisMessage.getMessageType());
        GroupAssignmentCommand message = (GroupAssignmentCommand) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2268120), message.getSourceMmsi());
        assertEquals(157.8f, message.getNorthEastLongitude(), 0.0f);
        assertEquals(3064.2f, message.getNorthEastLatitude(), 0.0f);
        assertEquals(109.6f, message.getSouthWestLongitude(), 0.0f);
        assertEquals(3040.8f, message.getSouthWestLatitude(), 0.0f);
        assertEquals(StationType.InlandWaterways, message.getStationType());
        assertEquals(ShipType.NotAvailable, message.getShipType());
        assertEquals(TxRxMode.TxABRxAB, message.getTransmitReceiveMode());
        assertEquals(ReportingInterval.Autonomous, message.getReportingInterval());
        assertEquals(0, message.getQuietTime());
    }
}
