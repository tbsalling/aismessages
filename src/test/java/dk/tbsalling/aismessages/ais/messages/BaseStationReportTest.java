package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.SyncState;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BaseStationReportTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,4h3Ovk1udp6I9o>jPHEdjdW000S:,0*0C"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.BaseStationReport, aisMessage.getMessageType());
        assertEquals((Integer) 3, aisMessage.getRepeatIndicator());
        BaseStationReport message = (BaseStationReport) aisMessage;
        assertEquals(MMSI.valueOf(3669708), message.getSourceMmsi());
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

    @Test
    public void canDecodeCommunicationState() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,400TcdiuiT7VDR>3nIfr6>i00000,0*78"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.BaseStationReport, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        BaseStationReport message = (BaseStationReport) aisMessage;
        assertEquals(MMSI.valueOf(601011), message.getSourceMmsi());
        assertEquals((Integer) 2012, message.getYear());
        assertEquals((Integer) 6, message.getMonth());
        assertEquals((Integer) 8, message.getDay());
        assertEquals((Integer) 7, message.getHour());
        assertEquals((Integer) 38, message.getMinute());
        assertEquals((Integer) 20, message.getSecond());
        assertTrue(message.getPositionAccurate());
        assertEquals(Float.valueOf(-29.870832f), message.getLatitude());
        assertEquals(Float.valueOf(31.033514f), message.getLongitude());
        assertEquals(PositionFixingDevice.Gps, message.getPositionFixingDevice());
        assertFalse(message.getRaimFlag());

        SOTDMACommunicationState sotdmaCommunicationState = message.getCommunicationState();
        assertEquals(SyncState.UTCDirect, sotdmaCommunicationState.getSyncState());
        assertNull(sotdmaCommunicationState.getNumberOfReceivedStations());
        assertNull(sotdmaCommunicationState.getSlotNumber());
        assertEquals(Integer.valueOf(0), sotdmaCommunicationState.getSlotOffset());
        assertEquals(Integer.valueOf(0), sotdmaCommunicationState.getSlotTimeout());
        assertNull(sotdmaCommunicationState.getUtcHour());
        assertNull(sotdmaCommunicationState.getUtcMinute());
    }
}
