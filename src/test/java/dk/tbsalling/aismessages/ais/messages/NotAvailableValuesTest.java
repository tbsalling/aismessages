package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AltitudeIndicator;
import dk.tbsalling.aismessages.ais.messages.types.DraughtIndicator;
import dk.tbsalling.aismessages.ais.messages.types.RateOfTurnIndicator;
import dk.tbsalling.aismessages.ais.messages.types.SpeedOverGroundIndicator;
import dk.tbsalling.aismessages.ais.messages.types.TimeStampIndicator;
import dk.tbsalling.aismessages.testsupport.NmeaSentences.Bits;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.bits;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.nmea;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Every field transmitted with its ITU-R M.1371-6 "not available" code decodes to {@code NaN}, {@code null} or a
 * {@code NotAvailable} indicator, never to the code itself; the "maximum or higher" codes decode to their lower bound.
 * Sentences are synthetic, built from the message tables.
 */
public class NotAvailableValuesTest {

    private static final int MMSI = 219000777;
    /** 181° and 91° in 1/10 000 min, and in 1/10 min. */
    private static final int LON_NA = 181 * 600_000, LAT_NA = 91 * 600_000, LON_NA_TENTHS = 181 * 600, LAT_NA_TENTHS = 91 * 600;

    private static AISMessage decode(Bits b, int expectedLength) {
        assertEquals(expectedLength, b.length());
        return AISMessageFactory.create(null, null, null, nmea(b.toBitString()));
    }

    private static Bits header(int type) {
        return bits().u(type, 6).u(0, 2).u(MMSI, 30);
    }

    /** Message 1 (Table 46) with the given SOG and time stamp; every other field not available. */
    private static PositionReport message1(int sog, int timeStamp) {
        return (PositionReport) decode(header(1).u(15, 4).s(-128, 8).u(sog, 10).b(false).s(LON_NA, 28).s(LAT_NA, 27).u(3600, 12).u(511, 9).u(timeStamp, 6)
                .u(0, 2).u(0, 3).b(false).u(0, 19), 168);
    }

    @Test
    public void positionReport() {
        PositionReport m = message1(1023, 60);

        assertTrue(Float.isNaN(m.getRateOfTurn()));
        assertEquals(RateOfTurnIndicator.NotAvailable, m.getRateOfTurnIndicator());
        assertTrue(Float.isNaN(m.getSpeedOverGround()));
        assertEquals(SpeedOverGroundIndicator.NotAvailable, m.getSpeedOverGroundIndicator());
        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
        assertTrue(Float.isNaN(m.getCourseOverGround()));
        assertNull(m.getTrueHeading());
        assertNull(m.getSecond());
        assertEquals(TimeStampIndicator.NotAvailable, m.getTimeStampIndicator());
    }

    @Test
    public void speedOverGroundMaximumIsALowerBound() {
        PositionReport m = message1(1022, 30);

        assertEquals(102.2f, m.getSpeedOverGround(), 1e-4f);
        assertEquals(SpeedOverGroundIndicator.AtLeastMaximum, m.getSpeedOverGroundIndicator());
        assertEquals(30, m.getSecond());
        assertEquals(TimeStampIndicator.Available, m.getTimeStampIndicator());
    }

    @ParameterizedTest
    @CsvSource({"61, ManualInputMode", "62, DeadReckoningMode", "63, PositioningSystemInoperative"})
    public void timeStampSpecialCodes(int timeStamp, TimeStampIndicator indicator) {
        PositionReport m = message1(100, timeStamp);

        assertNull(m.getSecond());
        assertEquals(indicator, m.getTimeStampIndicator());
        assertEquals(10.0f, m.getSpeedOverGround(), 1e-4f);
        assertEquals(SpeedOverGroundIndicator.Measured, m.getSpeedOverGroundIndicator());
    }

    @Test
    public void baseStationReport() {
        BaseStationReport m = (BaseStationReport) decode(header(4).u(0, 14).u(0, 4).u(0, 5).u(24, 5).u(60, 6).u(60, 6).b(false).s(LON_NA, 28).s(LAT_NA, 27)
                .u(0, 4).b(false).u(0, 9).b(false).u(0, 19), 168);

        assertNull(m.getYear());
        assertNull(m.getMonth());
        assertNull(m.getDay());
        assertNull(m.getHour());
        assertNull(m.getMinute());
        assertNull(m.getSecond());
        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
    }

    /** Message 5 (Table 50) with the given draught; every other field not available. */
    private static ShipAndVoyageData message5(int draught) {
        return (ShipAndVoyageData) decode(header(5).u(3, 2).u(0, 30).zeros(42).zeros(120).u(0, 8).zeros(30).u(0, 4).u(0, 4).u(0, 5).u(24, 5).u(60, 6).u(draught, 8)
                .zeros(120).b(false).u(0, 1), 424);
    }

    @Test
    public void shipAndVoyageData() {
        ShipAndVoyageData m = message5(0);

        assertNull(m.getImo());
        assertNull(m.getCallsign());
        assertNull(m.getShipName());
        assertNull(m.getDestination());
        assertNull(m.getEtaMonth());
        assertNull(m.getEtaDay());
        assertNull(m.getEtaHour());
        assertNull(m.getEtaMinute());
        assertTrue(Float.isNaN(m.getDraught()));
        assertEquals(DraughtIndicator.NotAvailable, m.getDraughtIndicator());
        assertNull(m.getLength());
        assertNull(m.getBeam());
        assertFalse(m.isReferencePointAvailable());
    }

    @Test
    public void draughtMaximumIsALowerBound() {
        ShipAndVoyageData m = message5(255);

        assertEquals(25.5f, m.getDraught(), 1e-4f);
        assertEquals(DraughtIndicator.AtLeast25Point5Metres, m.getDraughtIndicator());
    }

    /** Message 9 (Table 57) with the given altitude; every other field not available. */
    private static StandardSARAircraftPositionReport message9(int altitude) {
        return (StandardSARAircraftPositionReport) decode(header(9).u(altitude, 12).u(1023, 10).b(false).s(LON_NA, 28).s(LAT_NA, 27).u(3600, 12).u(60, 6)
                .b(false).u(0, 7).b(false).u(0, 3).b(false).b(false).b(false).u(0, 19), 168);
    }

    @Test
    public void searchAndRescueAircraftPositionReport() {
        StandardSARAircraftPositionReport m = message9(4095);

        assertNull(m.getAltitude());
        assertEquals(AltitudeIndicator.NotAvailable, m.getAltitudeIndicator());
        assertTrue(Float.isNaN(m.getSpeedOverGround()));
        assertEquals(SpeedOverGroundIndicator.NotAvailable, m.getSpeedOverGroundIndicator());
        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
        assertTrue(Float.isNaN(m.getCourseOverGround()));
        assertNull(m.getSecond());
        assertEquals(TimeStampIndicator.NotAvailable, m.getTimeStampIndicator());
    }

    @Test
    public void altitudeMaximumIsALowerBound() {
        StandardSARAircraftPositionReport m = message9(4094);

        assertEquals(4094, m.getAltitude());
        assertEquals(AltitudeIndicator.AtLeast4094Metres, m.getAltitudeIndicator());
    }

    @Test
    public void standardClassBPositionReport() {
        StandardClassBCSPositionReport m = (StandardClassBCSPositionReport) decode(header(18).u(0, 8).u(1023, 10).b(false).s(LON_NA, 28).s(LAT_NA, 27).u(3600, 12).u(511, 9).u(60, 6)
                .u(0, 2).b(true).b(false).b(false).b(false).b(false).b(false).b(false).b(true).u(0, 19), 168);

        assertTrue(Float.isNaN(m.getSpeedOverGround()));
        assertEquals(SpeedOverGroundIndicator.NotAvailable, m.getSpeedOverGroundIndicator());
        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
        assertTrue(Float.isNaN(m.getCourseOverGround()));
        assertNull(m.getTrueHeading());
        assertNull(m.getSecond());
        assertEquals(TimeStampIndicator.NotAvailable, m.getTimeStampIndicator());
    }

    @Test
    public void extendedClassBPositionReport() {
        ExtendedClassBEquipmentPositionReport m = (ExtendedClassBEquipmentPositionReport) decode(header(19).u(0, 8).u(1023, 10).b(false).s(LON_NA, 28).s(LAT_NA, 27).u(3600, 12).u(511, 9).u(60, 6)
                .u(0, 4).zeros(120).u(0, 8).zeros(30).u(0, 4).b(false).b(false).b(false).u(0, 4), 312);

        assertTrue(Float.isNaN(m.getSpeedOverGround()));
        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
        assertTrue(Float.isNaN(m.getCourseOverGround()));
        assertNull(m.getTrueHeading());
        assertNull(m.getSecond());
        assertNull(m.getShipName());
    }

    @Test
    public void aidToNavigationReport() {
        AidToNavigationReport m = (AidToNavigationReport) decode(header(21).u(0, 5).zeros(120).b(false).s(LON_NA, 28).s(LAT_NA, 27).zeros(30).u(0, 4).u(60, 6)
                .b(false).u(0, 8).b(false).b(false).b(false).u(0, 1), 272);

        assertNull(m.getName());
        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
        assertNull(m.getSecond());
        assertEquals(TimeStampIndicator.NotAvailable, m.getTimeStampIndicator());
    }

    @Test
    public void longRangeBroadcastMessage() {
        LongRangeBroadcastMessage m = (LongRangeBroadcastMessage) decode(header(27).b(false).b(false).u(15, 4).s(LON_NA_TENTHS, 18).s(LAT_NA_TENTHS, 17).u(63, 6).u(511, 9)
                .u(1, 1).u(0, 1), 96);

        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
        assertTrue(Float.isNaN(m.getSpeedOverGround()));
        assertEquals(SpeedOverGroundIndicator.NotAvailable, m.getSpeedOverGroundIndicator());
        assertTrue(Float.isNaN(m.getCourseOverGround()));
    }

    @Test
    public void aidToNavigationReportSingleSlot() {
        AidToNavigationReportSingleSlot m = (AidToNavigationReportSingleSlot) decode(header(28).u(60, 6).s(LON_NA, 28).s(LAT_NA, 27).zeros(69), 168);

        assertNull(m.getSecond());
        assertEquals(TimeStampIndicator.NotAvailable, m.getTimeStampIndicator());
        assertTrue(Double.isNaN(m.getLongitude()));
        assertTrue(Double.isNaN(m.getLatitude()));
    }
}
