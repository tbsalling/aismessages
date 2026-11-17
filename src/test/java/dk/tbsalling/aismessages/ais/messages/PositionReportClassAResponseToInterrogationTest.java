package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;
import dk.tbsalling.aismessages.ais.messages.types.*;

import static org.junit.jupiter.api.Assertions.*;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;

public class PositionReportClassAResponseToInterrogationTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,34RjBV0028o:pnNEBeU<pJF>0PT@,0*3F");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.PositionReportClassAResponseToInterrogation, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) aisMessage;
        assertEquals(new MMSI(304911000), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertEquals(0, message.getRateOfTurn());
        assertEquals(13.6f, message.getSpeedOverGround());
        assertTrue(message.isPositionAccuracy());
        assertEquals(22326676 / 600000.0, message.getLatitude());
        assertEquals(-74070321 / 600000.0, message.getLongitude());
        assertEquals(329.7f, message.getCourseOverGround());
        assertEquals(331, message.getTrueHeading());
        assertEquals(7, message.getSecond());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.isRaimFlag());
    }

    @Test
    public void canDecode2() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,3:U70chP@7LrG1SjrgmKF8uh00vP,0*56");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.PositionReportClassAResponseToInterrogation, aisMessage.getMessageType());
        assertEquals(0, aisMessage.getRepeatIndicator());
        PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) aisMessage;
        assertEquals(new MMSI(710000815), message.getSourceMmsi());
        assertEquals(NavigationStatus.UnderwayUsingEngine, message.getNavigationStatus());
        assertTrue(Float.isNaN(message.getRateOfTurn()));
        assertEquals(RateOfTurnIndicator.TurningLeftWithoutTurnIndicator, message.getRateOfTurnIndicator());
        assertEquals(0.7f, message.getSpeedOverGround());
        assertFalse(message.isPositionAccuracy());
        assertEquals(-13717547 / 600000.0, message.getLatitude());
        assertEquals(-25905103 / 600000.0, message.getLongitude());
        assertEquals(290.4f, message.getCourseOverGround());
        assertEquals(286, message.getTrueHeading());
        assertEquals(56, message.getSecond());
        assertEquals(ManeuverIndicator.NotAvailable, message.getSpecialManeuverIndicator());
        assertFalse(message.isRaimFlag());
    }

    @Test
    public void digest() throws NoSuchAlgorithmException {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,34RjBV0028o:pnNEBeU<pJF>0PT@,0*3F");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);
        byte[] digest = aisMessage.digest();
        String digestAsString = String.format("%040x", new java.math.BigInteger(1, digest));

        // Assert
        assertEquals("673ac3b20886868cafe7376e05092bf625f00b75", digestAsString);
    }

    /**
     * ITDMA number-of-slots code 6, meaning 2 slots at slot increment + 8192 (ITU-R M.1371-6 Table 20); also special
     * manoeuvre and transmit power low. Table 46. Synthetic sentence built from the spec layout.
     */
    @Test
    public void canDecodeItdmaNumberOfSlotsCodeBeyondFive() {
        Bits b = bits().u(3, 6).u(0, 2).u(219000401, 30).u(0, 4).s(-127, 8).u(55, 10).b(true).s(6450000, 28).s(33300000, 27).u(1800, 12).u(181, 9).u(42, 6)
                .u(2, 2).u(0, 2).b(true).b(false)
                .u(0, 2).u(1000, 13).u(6, 3).b(false);
        assertEquals(168, b.length());

        PositionReportClassAResponseToInterrogation message = (PositionReportClassAResponseToInterrogation) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(ManeuverIndicator.SpecialManeuver, message.getSpecialManeuverIndicator());
        assertTrue(message.isTransmitPowerLow());
        ITDMACommunicationState communicationState = (ITDMACommunicationState) message.getCommunicationState();
        assertEquals(1000 + 8192, communicationState.getSlotIncrement()); // code 6: 2 slots at increment + 8192
        assertEquals(2, communicationState.getNumberOfSlots());
        assertFalse(communicationState.getKeepFlag());
    }
}
