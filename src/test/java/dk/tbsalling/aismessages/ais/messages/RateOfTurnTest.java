package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.RateOfTurnIndicator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.bits;
import static dk.tbsalling.aismessages.testsupport.NmeaSentences.nmea;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Rate of turn of Messages 1, 2 and 3 (ITU-R M.1371-6 Table 46): measured values are converted from
 * ROT_AIS = 4.733 × √ROT_sensor, while the special codes ±127 and -128 are reported through
 * {@link RateOfTurnIndicator} with a rate of 0. Sentences are synthetic.
 */
public class RateOfTurnTest {

    private static PositionReport positionReport(int rawRateOfTurn) {
        return (PositionReport) AISMessageFactory.create(null, null, null, nmea(bits().u(1, 6).u(0, 2).u(219000001, 30).u(0, 4).s(rawRateOfTurn, 8).u(55, 10).b(true).s(6450000, 28).s(33300000, 27).u(1800, 12).u(181, 9).u(42, 6)
                .u(0, 2).u(0, 2).b(false).b(false).u(0, 19).toBitString()));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, Measured",
            "10, 4, Measured",
            "-10, -4, Measured",
            "126, 708, Measured",
            "-126, -708, Measured",
            "127, 0, TurningRightWithoutTurnIndicator",
            "-127, 0, TurningLeftWithoutTurnIndicator",
            "-128, 0, NotAvailable"
    })
    public void decodesRateOfTurnAndIndicator(int raw, int degreesPerMinute, RateOfTurnIndicator indicator) {
        PositionReport report = positionReport(raw);

        assertEquals(degreesPerMinute, report.getRateOfTurn());
        assertEquals(indicator, report.getRateOfTurnIndicator());
    }
}
