package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

public class AssignedModeCommandTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,@6TMCD1GOSmUBKh4,0*29");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.AssignedModeCommand, aisMessage.getMessageType());
        AssignedModeCommand message = (AssignedModeCommand) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(440882000), message.getSourceMmsi());
        assertEquals(new MMSI(366972761), message.getDestinationMmsiA());
        assertEquals(1318, message.getOffsetA());
        assertEquals(960, message.getIncrementA());
        assertNull(message.getDestinationMmsiB());
        assertNull(message.getOffsetB());
        assertNull(message.getIncrementB());
    }

    /** Two assigned stations (144 bits, ITU-R M.1371-6 Table 65). Synthetic sentence built from the spec layout. */
    @Test
    public void canDecodeTwoStations() {
        Bits b = bits().u(16, 6).u(0, 2).u(2190047, 30).u(0, 2)
                .u(219000101, 30).u(100, 12).u(4, 10)
                .u(219000202, 30).u(250, 12).u(6, 10);

        AssignedModeCommand message = (AssignedModeCommand) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(new MMSI(2190047), message.getSourceMmsi());
        assertEquals(new MMSI(219000101), message.getDestinationMmsiA());
        assertEquals(100, message.getOffsetA());
        assertEquals(4, message.getIncrementA());
        assertEquals(new MMSI(219000202), message.getDestinationMmsiB());
        assertEquals(250, message.getOffsetB());
        assertEquals(6, message.getIncrementB());
    }

    /** Reporting-rate assignment: increment 0 and offset = reports per 10 min (96 bits, Table 65). Synthetic sentence. */
    @Test
    public void canDecodeReportingRateAssignment() {
        Bits b = bits().u(16, 6).u(0, 2).u(2190047, 30).u(0, 2).u(219000101, 30).u(60, 12).u(0, 10).u(0, 4);

        AssignedModeCommand message = (AssignedModeCommand) AISMessageFactory.create(null, null, null, nmea(b.toBitString()));

        assertEquals(60, message.getOffsetA());
        assertEquals(0, message.getIncrementA());
        assertNull(message.getDestinationMmsiB());
        assertNull(message.getOffsetB());
        assertNull(message.getIncrementB());
    }
}
