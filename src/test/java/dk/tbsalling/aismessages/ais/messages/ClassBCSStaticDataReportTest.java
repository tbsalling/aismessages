package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.Ignore;
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

    @Test
    @Ignore
    public void canDecode_githubIssue38() {
        String msg = "!AIVDM,1,1,,B,H3m9T21HTe<H`u8B22222222220,0*0F";
        NMEAMessage nmeaMessage = NMEAMessage.fromString(msg);
        AISMessage aisMessage = AISMessage.create(nmeaMessage);

        System.out.println(aisMessage.toString());
    }

}