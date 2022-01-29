package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    @Disabled
    public void canDecode_githubIssue38() {
        String msg = "!AIVDM,1,1,,B,H3m9T21HTe<H`u8B22222222220,0*0F";
        NMEAMessage nmeaMessage = NMEAMessage.fromString(msg);
        AISMessage aisMessage = AISMessage.create(nmeaMessage);

        System.out.println(aisMessage.toString());
    }

    @Test
    public void canDecode_partAB() {
        String partA = "!AIVDM,1,1,,A,H42O55i18tMET00000000000000,2*6D";
        String partB = "!AIVDM,1,1,,A,H42O55lti4hhhilD3nink000?050,0*40";

        NMEAMessage nmeaMessageA = NMEAMessage.fromString(partA);
        NMEAMessage nmeaMessageB = NMEAMessage.fromString(partB);

        AISMessage aisMessagePartA = AISMessage.create(nmeaMessageA);
        AISMessage aisMessagePartB = AISMessage.create(nmeaMessageB);

        System.out.println(aisMessagePartA.toString());
        assertTrue(aisMessagePartA instanceof ClassBCSStaticDataReport);
        assertEquals(24, aisMessagePartA.getMessageType().getCode().intValue());
        assertEquals(0, ((ClassBCSStaticDataReport) aisMessagePartA).getPartNumber().intValue());
        assertEquals(271041815, aisMessagePartA.getSourceMmsi().getMMSI().intValue());
        assertEquals("PROGUY", ((ClassBCSStaticDataReport) aisMessagePartA).getShipName());

        System.out.println(aisMessagePartB.toString());
        assertTrue(aisMessagePartB instanceof ClassBCSStaticDataReport);
        assertEquals(24, aisMessagePartB.getMessageType().getCode().intValue());
        assertEquals(1, ((ClassBCSStaticDataReport) aisMessagePartB).getPartNumber().intValue());
        assertEquals(271041815, aisMessagePartB.getSourceMmsi().getMMSI().intValue());
        assertEquals(60, ((ClassBCSStaticDataReport) aisMessagePartB).getShipType().getCode().intValue());
        assertEquals("1D00014", ((ClassBCSStaticDataReport) aisMessagePartB).getVendorId());
        assertEquals("TC6163", ((ClassBCSStaticDataReport) aisMessagePartB).getCallsign());
        assertEquals(0, ((ClassBCSStaticDataReport) aisMessagePartB).getToBow().intValue());
        assertEquals(15, ((ClassBCSStaticDataReport) aisMessagePartB).getToStern().intValue());
        assertEquals(0, ((ClassBCSStaticDataReport) aisMessagePartB).getToPort().intValue());
        assertEquals(5, ((ClassBCSStaticDataReport) aisMessagePartB).getToStarboard().intValue());
    }

}