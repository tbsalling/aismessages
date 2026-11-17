package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;

import static org.junit.jupiter.api.Assertions.*;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;

public class StandardClassBCSPositionReportTest {

    @Test
    public void canDecode1() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,B5NJ;PP005l4ot5Isbl03wsUkP06,0*76");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(367430530), message.getSourceMmsi());
        assertEquals(0b00000000, message.getRegionalReserved1());
        assertEquals(0.0f, message.getSpeedOverGround(), 0.0f);
        assertEquals(0, message.getRawSpeedOverGround());
        assertFalse(message.isPositionAccuracy());
        assertEquals(22671021 / 600000.0, message.getLatitude(), 0.0f);
        assertEquals(22671021, message.getRawLatitude());
        assertEquals(-73360392 / 600000.0, message.getLongitude(), 0.0f);
        assertEquals(-73360392, message.getRawLongitude());
        assertEquals(0.0f, message.getCourseOverGround(), 0.0f);
        assertEquals(0, message.getRawCourseOverGround());
        assertEquals(511, message.getTrueHeading());
        assertEquals(55, message.getSecond());
        assertEquals(0b00, message.getRegionalReserved2());
        assertTrue(message.isCsUnit());
        assertFalse(message.isDisplay());
        assertTrue(message.isDsc());
        assertTrue(message.isBand());
        assertTrue(message.isMessage22());
        assertFalse(message.isAssigned());
        assertFalse(message.isRaimFlag());
        assertFalse(message.isTransmitPowerLow());
        //assertEquals(0b11100000000000000110, message.getRadioStatus());
    }

    @Test
    public void canDecode2() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,B>1VUFP00vK`auV0eUulKwv0RJGT,0*09");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(941204826), message.getSourceMmsi());
        assertEquals(0b00000000, message.getRegionalReserved1());
        assertEquals(0.3f, message.getSpeedOverGround(), 0.0f);
        assertTrue(message.isPositionAccuracy());
        assertEquals(25212511 / 600000.0, message.getLatitude(), 0.0f);
        assertEquals(-52620037 / 600000.0, message.getLongitude(), 0.0f);
        assertEquals(186.2f, message.getCourseOverGround(), 0.0f);
        assertEquals(511, message.getTrueHeading());
        assertEquals(60, message.getSecond());
        assertEquals(0b00, message.getRegionalReserved2());
        assertFalse(message.isCsUnit());
        assertFalse(message.isDisplay());
        assertFalse(message.isDsc());
        assertTrue(message.isBand());
        assertFalse(message.isMessage22());
        assertFalse(message.isAssigned());
        assertFalse(message.isRaimFlag());
        assertFalse(message.isTransmitPowerLow());
    }

    @Test
    public void canDecodeITDMACommunicationState() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,B6CdCm0t3`tba35f@V9faHi7kP06,0*58");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.StandardClassBCSPositionReport, aisMessage.getMessageType());
        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(423302100), message.getSourceMmsi());
        assertEquals(0b00001111, message.getRegionalReserved1());
        assertEquals(1.4f, message.getSpeedOverGround(), 0.0f);
        assertTrue(message.isPositionAccuracy());
        assertEquals(24003170 / 600000.0, message.getLatitude(), 0.0f);
        assertEquals(31806598 / 600000.0, message.getLongitude(), 0.0f);
        assertEquals(177f, message.getCourseOverGround(), 0.0f);
        assertEquals(177, message.getTrueHeading());
        assertEquals(34, message.getSecond());
        assertEquals(0b00, message.getRegionalReserved2());
        assertTrue(message.isCsUnit());
        assertTrue(message.isDisplay());
        assertTrue(message.isDsc());
        assertTrue(message.isBand());
        assertTrue(message.isMessage22());
        assertFalse(message.isAssigned());
        assertFalse(message.isRaimFlag());
        assertFalse(message.isTransmitPowerLow());
        assertTrue(message.isCommStateSelectorFlag());

        CommunicationState communicationState = message.getCommunicationState();   // 1100000000000000110b = 3, slot incr = 6
        assertEquals(SyncState.BaseIndirect, communicationState.getSyncState());
        assertTrue(communicationState instanceof ITDMACommunicationState);
        if (communicationState instanceof ITDMACommunicationState itdmaCommunicationState) {
            assertEquals(0, itdmaCommunicationState.getSlotIncrement());
            assertEquals(3, itdmaCommunicationState.getNumberOfSlots());
            assertFalse(itdmaCommunicationState.getKeepFlag());
        }
    }

    /**
     * Class B "SO" unit: Class B unit flag 0 and a SOTDMA communication state (selector 0, slot time-out 3, 12 received
     * stations); transmit power low. ITU-R M.1371-6 Table 68. Synthetic sentence built from the spec layout.
     */
    @Test
    public void canDecodeClassBSoWithSotdmaCommunicationState() {
        Bits b = bits().u(18, 6).u(0, 2).u(219000301, 30).u(0, 8).u(123, 10).b(true).s(6450000, 28).s(33300000, 27).u(901, 12).u(90, 9).u(15, 6)
                .b(true).b(false).b(false).b(true).b(false).b(true).b(true).b(false).b(true).b(false)
                .u(0, 2).u(3, 3).u(12, 14);
        assertEquals(168, b.length());

        StandardClassBCSPositionReport message = (StandardClassBCSPositionReport) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertFalse(message.isCsUnit());
        assertTrue(message.isTransmitPowerLow());
        assertEquals(12.3f, message.getSpeedOverGround(), 0.0001f);
        assertEquals(90.1f, message.getCourseOverGround(), 0.0001f);
        assertEquals(90, message.getTrueHeading());
        assertTrue(message.isRaimFlag());
        assertFalse(message.isCommStateSelectorFlag());
        SOTDMACommunicationState communicationState = (SOTDMACommunicationState) message.getCommunicationState();
        assertEquals(SyncState.UTCDirect, communicationState.getSyncState());
        assertEquals(3, communicationState.getSlotTimeout());
        assertEquals(12, communicationState.getNumberOfReceivedStations());
    }
}
