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
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,H5NLOjTUG5CD=1BG46mqhj0P7130,0*78");

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.ClassBCSStaticDataReport, aisMessage.getMessageType());
        ClassBCSStaticDataReport message = (ClassBCSStaticDataReport) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(367468490), message.getSourceMmsi());
        assertEquals(1, message.getPartNumber());
        assertNull(message.getShipName());
        assertEquals(ShipType.PleasureCraft, message.getShipType());
        assertEquals("WESTMAR", message.getVendorId());
        assertEquals("WDF5902", message.getCallsign());
        assertEquals(4, message.getToBow());
        assertEquals(7, message.getToStern());
        assertEquals(3, message.getToStarboard());
        assertEquals(1, message.getToPort());
        assertEquals(MMSI.valueOf(8417347), message.getMothershipMmsi());

    }

    @Test
    @Disabled
    public void canDecode_githubIssue38() {
        // Arrange
        String msg = "!AIVDM,1,1,,B,H3m9T21HTe<H`u8B22222222220,0*0F";
        NMEAMessage nmeaMessage = new NMEAMessage(msg);

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());
    }

    @Test
    public void canDecode_githubIssue47() {
        // Arrange
        String msg = "!AIVDM,1,1,,,H7tKep@H>0u8<PTB222222222200,2*01";
        NMEAMessage nmeaMessage = new NMEAMessage(msg);

        // Act
        AISMessage aisMessage = AISMessage.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertInstanceOf(ClassBCSStaticDataReport.class, aisMessage);
        assertEquals(24, aisMessage.getMessageType().getCode());
        assertEquals(0, ((ClassBCSStaticDataReport) aisMessage).getPartNumber());
        assertEquals(533130721, aisMessage.getSourceMmsi().intValue());
        assertEquals("FC ORCHID", ((ClassBCSStaticDataReport) aisMessage).getShipName());
    }

    @Test
    public void canDecode_partAB() {
        // Arrange
        String partA = "!AIVDM,1,1,,A,H42O55i18tMET00000000000000,2*6D";
        String partB = "!AIVDM,1,1,,A,H42O55lti4hhhilD3nink000?050,0*40";

        NMEAMessage nmeaMessageA = new NMEAMessage(partA);
        NMEAMessage nmeaMessageB = new NMEAMessage(partB);

        // Act
        AISMessage aisMessagePartA = AISMessage.create(null, null, null, nmeaMessageA);
        AISMessage aisMessagePartB = AISMessage.create(null, null, null, nmeaMessageB);

        // Assert
        System.out.println(aisMessagePartA.toString());
        assertTrue(aisMessagePartA instanceof ClassBCSStaticDataReport);
        assertEquals(24, aisMessagePartA.getMessageType().getCode());
        assertEquals(0, ((ClassBCSStaticDataReport) aisMessagePartA).getPartNumber());
        assertEquals(271041815, aisMessagePartA.getSourceMmsi().intValue());
        assertEquals("PROGUY", ((ClassBCSStaticDataReport) aisMessagePartA).getShipName());

        System.out.println(aisMessagePartB.toString());
        assertTrue(aisMessagePartB instanceof ClassBCSStaticDataReport);
        assertEquals(24, aisMessagePartB.getMessageType().getCode());
        assertEquals(1, ((ClassBCSStaticDataReport) aisMessagePartB).getPartNumber());
        assertEquals(271041815, aisMessagePartB.getSourceMmsi().intValue());
        assertEquals(60, ((ClassBCSStaticDataReport) aisMessagePartB).getShipType().getCode());
        assertEquals("1D00014", ((ClassBCSStaticDataReport) aisMessagePartB).getVendorId());
        assertEquals("TC6163", ((ClassBCSStaticDataReport) aisMessagePartB).getCallsign());
        assertEquals(0, ((ClassBCSStaticDataReport) aisMessagePartB).getToBow());
        assertEquals(15, ((ClassBCSStaticDataReport) aisMessagePartB).getToStern());
        assertEquals(0, ((ClassBCSStaticDataReport) aisMessagePartB).getToPort());
        assertEquals(5, ((ClassBCSStaticDataReport) aisMessagePartB).getToStarboard());
    }

}
