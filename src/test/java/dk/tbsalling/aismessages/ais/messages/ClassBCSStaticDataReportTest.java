package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassBCSStaticDataReportTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,A,H5NLOjTUG5CD=1BG46mqhj0P7130,0*78"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.ClassBCSStaticDataReport, aisMessage.getMessageType());
        ClassBCSStaticDataReport message = (ClassBCSStaticDataReport) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(367468490), message.getSourceMmsi());
        assertEquals((Integer) 1, message.getPartNumber());
        assertNull(message.getShipName());
        assertEquals(ShipType.PleasureCraft, message.getShipType());
        assertEquals("WESTMAR", message.getVendorId());
        assertEquals("WDF5902", message.getCallsign());
        assertEquals((Integer) 4, message.getToBow());
        assertEquals((Integer) 7, message.getToStern());
        assertEquals((Integer) 3, message.getToStarboard());
        assertEquals((Integer) 1, message.getToPort());
        assertEquals(MMSI.valueOf(8417347), message.getMothershipMmsi());

    }
}