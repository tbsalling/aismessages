package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice.Gps;
import static dk.tbsalling.aismessages.ais.messages.types.ShipType.Fishing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExtendedClassBEquipmentPositionReportTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,B,C69DqeP0Ar8;JH3R6<4O7wWPl@:62L>jcaQgh0000000?104222P,0*32");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.ExtendedClassBEquipmentPositionReport, aisMessage.getMessageType());
        assertEquals((Integer) 0, aisMessage.getRepeatIndicator());
        assertEquals(new MMSI(412432822), aisMessage.getSourceMmsi());

        ExtendedClassBEquipmentPositionReport message = (ExtendedClassBEquipmentPositionReport) aisMessage;
        assertEquals(0b00000000, message.getRegionalReserved1());
        assertEquals(7.1, message.getSpeedOverGround(), 1e-6);
        assertEquals(Boolean.TRUE, message.isPositionAccuracy());
        assertEquals(71396656 / 600000.0, message.getLongitude(), 1e-6);
        assertEquals(14817473 / 600000.0, message.getLatitude(), 1e-6);
        assertEquals(49.7, message.getCourseOverGround(), 1e-1);
        assertNull(message.getTrueHeading());
        assertEquals(15, message.getSecond());
        assertEquals(0b0000, message.getRegionalReserved2());
        assertEquals("ZHECANGYU4078", message.getShipName());
        assertEquals(Fishing, message.getShipType());
        assertEquals(16, message.getToBow());
        assertEquals(8, message.getToStern());
        assertEquals(4, message.getToPort());
        assertEquals(4, message.getToStarboard());
        assertEquals(Gps, message.getPositionFixingDevice());
        assertEquals(Boolean.FALSE, message.isRaimFlag());
        assertEquals(Boolean.TRUE, message.isDataTerminalReady());
        assertEquals(Boolean.FALSE, message.isAssigned());
        assertEquals(0b0000, message.getRegionalReserved3());
    }

}
