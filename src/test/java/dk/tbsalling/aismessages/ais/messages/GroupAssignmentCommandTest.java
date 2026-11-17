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
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GroupAssignmentCommand, aisMessage.getMessageType());
        GroupAssignmentCommand message = (GroupAssignmentCommand) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2268120), message.getSourceMmsi());
        assertEquals(0, message.getSpare1());
        assertEquals(157.8 / 60, message.getNorthEastLongitude(), 1e-9);
        assertEquals(3064.2 / 60, message.getNorthEastLatitude(), 1e-9);
        assertEquals(109.6 / 60, message.getSouthWestLongitude(), 1e-9);
        assertEquals(3040.8 / 60, message.getSouthWestLatitude(), 1e-9);
        assertEquals(StationType.InlandWaterways, message.getStationType());
        assertEquals(ShipType.NotAvailable, message.getShipType());
        assertEquals(0, message.getSpare2());
        assertEquals(TxRxMode.TxABRxAB, message.getTransmitReceiveMode());
        // ITU-R M.1371-6 Table 74: reporting interval field is bits 146-150; this sample encodes 9 = "next shorter reporting interval".
        assertEquals(ReportingInterval.NextShortReportingInterval, message.getReportingInterval());
        assertEquals(0, message.getQuietTime());
    }

    @Test
    public void canDecodeAlternative() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,G02:Kn01R`sn@291nj600000900,2*12");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.GroupAssignmentCommand, aisMessage.getMessageType());
        GroupAssignmentCommand message = (GroupAssignmentCommand) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(2268120), message.getSourceMmsi());
        assertEquals(157.8 / 60, message.getNorthEastLongitude(), 1e-9);
        assertEquals(3064.2 / 60, message.getNorthEastLatitude(), 1e-9);
        assertEquals(109.6 / 60, message.getSouthWestLongitude(), 1e-9);
        assertEquals(3040.8 / 60, message.getSouthWestLatitude(), 1e-9);
        assertEquals(StationType.InlandWaterways, message.getStationType());
        assertEquals(ShipType.NotAvailable, message.getShipType());
        assertEquals(TxRxMode.TxABRxAB, message.getTransmitReceiveMode());
        assertEquals(ReportingInterval.NextShortReportingInterval, message.getReportingInterval());
        assertEquals(0, message.getQuietTime());
    }
}
