package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BaseStationReportTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,4h3Ovk1udp6I9o>jPHEdjdW000S:,0*0C");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BaseStationReport, aisMessage.getMessageType());
        assertEquals(3, aisMessage.getRepeatIndicator());
        BaseStationReport message = (BaseStationReport) aisMessage;
        assertEquals(new MMSI(3669708), message.getSourceMmsi());
        assertEquals(2011, message.getYear());
        assertEquals(3, message.getMonth());
        assertEquals(16, message.getDay());
        assertEquals(6, message.getHour());
        assertEquals(25, message.getMinute());
        assertEquals(9, message.getSecond());
        assertTrue(message.isPositionAccurate());
        assertEquals(37.923283f, message.getLatitude(), 0.0f);
        assertEquals(-122.59837f, message.getLongitude(), 0.0f);
        assertEquals(PositionFixingDevice.Surveyed, message.getPositionFixingDevice());
        assertFalse(message.isRaimFlag());
    }

    @Test
    public void canDecodeCommunicationState() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,400TcdiuiT7VDR>3nIfr6>i00000,0*78");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.BaseStationReport, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        BaseStationReport message = (BaseStationReport) aisMessage;
        assertEquals(new MMSI(601011), message.getSourceMmsi());
        assertEquals(2012, message.getYear());
        assertEquals(6, message.getMonth());
        assertEquals(8, message.getDay());
        assertEquals(7, message.getHour());
        assertEquals(38, message.getMinute());
        assertEquals(20, message.getSecond());
        assertTrue(message.isPositionAccurate());
        assertEquals(-29.870832f, message.getLatitude(), 0.0f);
        assertEquals(31.033514f, message.getLongitude(), 0.0f);
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertFalse(message.isRaimFlag());

        SOTDMACommunicationState sotdmaCommunicationState = message.getCommunicationState();
        assertEquals(SyncState.UTCDirect, sotdmaCommunicationState.getSyncState());
        assertNull(sotdmaCommunicationState.getNumberOfReceivedStations());
        assertNull(sotdmaCommunicationState.getSlotNumber());
        assertEquals(0, sotdmaCommunicationState.getSlotOffset());
        assertEquals(0, sotdmaCommunicationState.getSlotTimeout());
        assertNull(sotdmaCommunicationState.getUtcHour());
        assertNull(sotdmaCommunicationState.getUtcMinute());
    }
}
