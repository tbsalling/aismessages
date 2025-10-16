package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionReportClassAScheduledTest {

    @Test
    void toString_includes_parent_class_fields() {
        // Arrange: construct a minimal, valid PositionReportClassAScheduled instance using protected constructor
        MMSI mmsi = new MMSI(123456789);
        int repeatIndicator = 0;
        NMEAMessage[] nmeaMessages = new NMEAMessage[0];
        String bitString = "000001" + "0".repeat(162); // type 1 + padding to 168 bits
        String source = "TEST";
        Instant received = Instant.EPOCH;

        NavigationStatus navigationStatus = NavigationStatus.UnderwayUsingEngine;
        int rateOfTurn = 0;
        float speedOverGround = 12.3f;
        boolean positionAccuracy = true;
        float latitude = 55.0f;
        float longitude = 12.0f;
        float courseOverGround = 90.0f;
        int trueHeading = 180;
        int second = 30;
        ManeuverIndicator specialManeuverIndicator = ManeuverIndicator.NotAvailable;
        boolean raimFlag = true;
        CommunicationState communicationState = SOTDMACommunicationState.fromBitString("0000000000000000000");
        int rawRateOfTurn = 0;
        int rawSpeedOverGround = 123;
        int rawLatitude = 100000;
        int rawLongitude = 200000;
        int rawCourseOverGround = 3590;

        PositionReportClassAScheduled msg = new PositionReportClassAScheduled(
                mmsi, repeatIndicator, null, nmeaMessages, bitString, source, received,
                navigationStatus, rateOfTurn, speedOverGround, positionAccuracy, latitude, longitude,
                courseOverGround, trueHeading, second, specialManeuverIndicator, raimFlag, communicationState,
                rawRateOfTurn, rawSpeedOverGround, rawLatitude, rawLongitude, rawCourseOverGround);

        // Act
        String s = msg.toString();

        // Assert: ensure fields from parent classes are present in toString
        assertTrue(s.contains("navigationStatus="), "toString should include field from PositionReport parent (navigationStatus)");
        assertTrue(s.contains("speedOverGround="), "toString should include field from PositionReport parent (speedOverGround)");
        assertTrue(s.contains("sourceMmsi="), "toString should include field from AISMessage grandparent (sourceMmsi)");
    }
}
