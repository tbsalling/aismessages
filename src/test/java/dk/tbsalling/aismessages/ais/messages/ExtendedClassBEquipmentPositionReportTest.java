package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice.Gps;
import static dk.tbsalling.aismessages.ais.messages.types.ShipType.Fishing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtendedClassBEquipmentPositionReportTest {

    @Test
    public void canDecode() {
        AISMessage aisMessage = AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,C69DqeP0Ar8;JH3R6<4O7wWPl@:62L>jcaQgh0000000?104222P,0*32"));

        System.out.println(aisMessage.toString());

        assertEquals(AISMessageType.ExtendedClassBEquipmentPositionReport, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        assertEquals(new MMSI(412432822), aisMessage.getSourceMmsi());

        ExtendedClassBEquipmentPositionReport message = (ExtendedClassBEquipmentPositionReport) aisMessage;
        assertEquals("00000000", message.getRegionalReserved1());
        assertEquals(7.1, message.getSpeedOverGround(), 1e-6);
        assertEquals(Boolean.TRUE, message.getPositionAccurate());
        assertEquals(118.994422, message.getLongitude(), 1e-6);
        assertEquals(24.695787, message.getLatitude(), 1e-6);
        assertEquals(49.7, message.getCourseOverGround(), 1e-1);
        assertEquals(511, message.getTrueHeading().intValue());
        assertEquals(15, message.getSecond().intValue());
        assertEquals("0000", message.getRegionalReserved2());
        assertEquals("ZHECANGYU4078", message.getShipName());
        assertEquals(Fishing, message.getShipType());
        assertEquals(16, message.getToBow().intValue());
        assertEquals(8, message.getToStern().intValue());
        assertEquals(4, message.getToPort().intValue());
        assertEquals(4, message.getToStarboard().intValue());
        assertEquals(Gps, message.getPositionFixingDevice());
        assertEquals(Boolean.FALSE, message.getRaimFlag());
        assertEquals(Boolean.TRUE, message.getDataTerminalReady());
        assertEquals(Boolean.FALSE, message.getAssigned());
        assertEquals("00000000", message.getRegionalReserved3());
    }

}

