package dk.tbsalling.aismessages.ais.messages.types;

import dk.tbsalling.aismessages.ais.BitString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.bits;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SOTDMA (ITU-R M.1371-6 Tables 18 and 19) and ITDMA (Table 20) communication states, decoded directly from 19-bit
 * values built from the spec layout.
 */
public class CommunicationStateTest {

    private final List<LogRecord> warnings = new ArrayList<>();
    private final Handler capture = new Handler() {
        @Override
        public void publish(LogRecord record) {
            if (record.getLevel().intValue() >= Level.WARNING.intValue()) warnings.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    };
    private final Logger sotdmaLogger = Logger.getLogger(SOTDMACommunicationState.class.getName());

    @BeforeEach
    public void captureWarnings() {
        sotdmaLogger.addHandler(capture);
    }

    @AfterEach
    public void releaseWarnings() {
        sotdmaLogger.removeHandler(capture);
    }

    private static BitString sotdma(int syncState, int slotTimeout, int subMessage) {
        return bits().u(syncState, 2).u(slotTimeout, 3).u(subMessage, 14).toBitString();
    }

    private static BitString itdma(int syncState, int slotIncrement, int numberOfSlots, boolean keep) {
        return bits().u(syncState, 2).u(slotIncrement, 13).u(numberOfSlots, 3).b(keep).toBitString();
    }

    @ParameterizedTest(name = "slot time-out {0}")
    @ValueSource(ints = {3, 5, 7})
    public void sotdmaOddTimeoutsCarryNumberOfReceivedStations(int timeout) {
        SOTDMACommunicationState s = SOTDMACommunicationState.fromBitString(sotdma(0, timeout, 16383));

        assertEquals(timeout, s.getSlotTimeout());
        assertEquals(16383, s.getNumberOfReceivedStations());
        assertNull(s.getSlotNumber());
        assertNull(s.getUtcHour());
        assertNull(s.getUtcMinute());
        assertNull(s.getSlotOffset());
    }

    @ParameterizedTest(name = "slot time-out {0}")
    @ValueSource(ints = {2, 4, 6})
    public void sotdmaEvenTimeoutsCarrySlotNumber(int timeout) {
        SOTDMACommunicationState s = SOTDMACommunicationState.fromBitString(sotdma(1, timeout, 2249));

        assertEquals(timeout, s.getSlotTimeout());
        assertEquals(2249, s.getSlotNumber());
        assertNull(s.getNumberOfReceivedStations());
        assertNull(s.getUtcHour());
        assertNull(s.getSlotOffset());
        assertTrue(warnings.isEmpty());
    }

    @Test
    public void sotdmaTimeoutOneCarriesUtcHourAndMinute() {
        // Hour in sub-message bits 13-9, minute in bits 8-2, bits 1-0 unused (Table 19).
        SOTDMACommunicationState s = SOTDMACommunicationState.fromBitString(sotdma(2, 1, (23 << 9) | (59 << 2)));

        assertEquals(1, s.getSlotTimeout());
        assertEquals(23, s.getUtcHour());
        assertEquals(59, s.getUtcMinute());
        assertNull(s.getNumberOfReceivedStations());
        assertNull(s.getSlotNumber());
        assertNull(s.getSlotOffset());
        assertTrue(warnings.isEmpty());
    }

    @Test
    public void sotdmaTimeoutZeroCarriesSlotOffset() {
        SOTDMACommunicationState s = SOTDMACommunicationState.fromBitString(sotdma(3, 0, 2250));

        assertEquals(0, s.getSlotTimeout());
        assertEquals(2250, s.getSlotOffset());
        assertNull(s.getNumberOfReceivedStations());
        assertNull(s.getSlotNumber());
        assertNull(s.getUtcHour());
        assertNull(s.getUtcMinute());
    }

    @ParameterizedTest(name = "sync state {0}")
    @ValueSource(ints = {0, 1, 2, 3})
    public void decodesEverySyncState(int code) {
        assertEquals(SyncState.fromInteger(code), SOTDMACommunicationState.fromBitString(sotdma(code, 0, 0)).getSyncState());
        assertEquals(SyncState.fromInteger(code), ITDMACommunicationState.fromBitString(itdma(code, 0, 0, false)).getSyncState());
    }

    @Test
    public void sotdmaSlotNumberOutOfRangeIsDecodedAndLogged() {
        SOTDMACommunicationState s = SOTDMACommunicationState.fromBitString(sotdma(0, 2, 2250));

        assertEquals(2250, s.getSlotNumber());
        assertEquals(1, warnings.size());
        assertTrue(warnings.get(0).getMessage().contains("slotNumber"));
    }

    @Test
    public void sotdmaUtcHourAndMinuteOutOfRangeAreDecodedAndLogged() {
        SOTDMACommunicationState s = SOTDMACommunicationState.fromBitString(sotdma(0, 1, (31 << 9) | (127 << 2)));

        assertEquals(31, s.getUtcHour());
        assertEquals(127, s.getUtcMinute());
        assertEquals(2, warnings.size());
        assertTrue(warnings.get(0).getMessage().contains("utcHour"));
        assertTrue(warnings.get(1).getMessage().contains("utcMinute"));
    }

    @ParameterizedTest(name = "number-of-slots code {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    public void itdmaDecodesEveryNumberOfSlotsCode(int code) {
        // Codes 0-4 mean 1-5 slots; codes 5-7 mean 1-3 slots at slot increment + 8192 (Table 20).
        ITDMACommunicationState s = ITDMACommunicationState.fromBitString(itdma(2, 8191, code, code % 2 == 0));

        assertEquals(code <= 4 ? 8191 : 8191 + 8192, s.getSlotIncrement());
        assertEquals(code <= 4 ? code + 1 : code - 4, s.getNumberOfSlots());
        assertEquals(code % 2 == 0, s.getKeepFlag());
    }

    @ParameterizedTest(name = "{0} bits")
    @ValueSource(ints = {0, 18, 20})
    public void wrongLengthYieldsNull(int length) {
        BitString bits = BitString.ofBitString("1".repeat(length));

        assertNull(SOTDMACommunicationState.fromBitString(bits));
        assertNull(ITDMACommunicationState.fromBitString(bits));
    }
}
