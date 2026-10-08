package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.BaseStationReport;
import dk.tbsalling.aismessages.ais.messages.DataReport;
import dk.tbsalling.aismessages.ais.messages.DynamicDataReport;
import dk.tbsalling.aismessages.ais.messages.PositionReport;
import dk.tbsalling.aismessages.ais.messages.PositionReportClassAScheduled;
import dk.tbsalling.aismessages.ais.messages.ShipAndVoyageData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AISMessageRouterTest {

    private static final AISMessage POSITION_REPORT = AISMessageDecoder.decode("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
    private static final AISMessage RESPONSE_TO_INTERROGATION = AISMessageDecoder.decode("!AIVDM,1,1,,A,35Ml=50Oh@o>Lf2EVPJI>nqP017A,0*51");
    private static final AISMessage CLASS_B_POSITION_REPORT = AISMessageDecoder.decode("!AIVDM,1,1,,A,B5NJ;PP005l4ot5Isbl03wsUkP06,0*76");
    private static final AISMessage BASE_STATION_REPORT = AISMessageDecoder.decode("!AIVDM,1,1,,B,4h3Ovk1udp6I9o>jPHEdjdW000S:,0*0C");
    private static final AISMessage SHIP_AND_VOYAGE_DATA = AISMessageDecoder.decode("!AIVDM,2,1,1,A,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09", "!AIVDM,2,2,1,A,888888888888880,2*2E").getFirst();

    @Test
    public void routesToHandlerForExactClass() {
        List<String> routed = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReportClassAScheduled.class, _ -> routed.add("scheduled"))
                .on(ShipAndVoyageData.class, _ -> routed.add("voyage"))
                .build();

        router.accept(POSITION_REPORT);
        router.accept(SHIP_AND_VOYAGE_DATA);

        assertEquals(List.of("scheduled", "voyage"), routed);
    }

    @Test
    public void routesToHandlerForSuperclass() {
        List<AISMessage> routed = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReport.class, routed::add)
                .build();

        router.accept(POSITION_REPORT);
        router.accept(BASE_STATION_REPORT);

        assertEquals(List.of(POSITION_REPORT), routed);
    }

    @Test
    public void routesToHandlerForInterface() {
        List<DynamicDataReport> routed = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(DynamicDataReport.class, routed::add)
                .build();

        router.accept(POSITION_REPORT);
        router.accept(CLASS_B_POSITION_REPORT);
        router.accept(SHIP_AND_VOYAGE_DATA);

        assertEquals(List.of(POSITION_REPORT, CLASS_B_POSITION_REPORT), routed);
    }

    @Test
    public void mostSpecificHandlerWinsRegardlessOfRegistrationOrder() {
        List<String> routed = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(DataReport.class, _ -> routed.add("data"))
                .on(DynamicDataReport.class, _ -> routed.add("dynamic"))
                .on(PositionReport.class, _ -> routed.add("position"))
                .build();

        router.accept(POSITION_REPORT);
        router.accept(CLASS_B_POSITION_REPORT);
        router.accept(SHIP_AND_VOYAGE_DATA);

        assertEquals(List.of("position", "dynamic", "data"), routed);
    }

    @Test
    public void mostSpecificHandlerWinsWhenRegisteredFirst() {
        List<String> routed = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReport.class, _ -> routed.add("position"))
                .on(DynamicDataReport.class, _ -> routed.add("dynamic"))
                .on(DataReport.class, _ -> routed.add("data"))
                .build();

        router.accept(POSITION_REPORT);
        router.accept(CLASS_B_POSITION_REPORT);
        router.accept(SHIP_AND_VOYAGE_DATA);

        assertEquals(List.of("position", "dynamic", "data"), routed);
    }

    @Test
    public void exactClassWinsOverSuperclass() {
        List<String> routed = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReportClassAScheduled.class, _ -> routed.add("scheduled"))
                .on(PositionReport.class, _ -> routed.add("position"))
                .build();

        router.accept(POSITION_REPORT);
        router.accept(RESPONSE_TO_INTERROGATION);

        assertEquals(List.of("scheduled", "position"), routed);
    }

    @Test
    public void unmatchedMessagesGoToOtherwise() {
        List<AISMessage> routed = new ArrayList<>();
        List<AISMessage> unmatched = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReport.class, routed::add)
                .otherwise(unmatched::add)
                .build();

        router.accept(POSITION_REPORT);
        router.accept(BASE_STATION_REPORT);

        assertEquals(List.of(POSITION_REPORT), routed);
        assertEquals(List.of(BASE_STATION_REPORT), unmatched);
    }

    @Test
    public void unmatchedMessagesAreIgnoredWithoutOtherwise() {
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReport.class, _ -> fail("Should not be routed"))
                .build();

        assertDoesNotThrow(() -> router.accept(BASE_STATION_REPORT));
    }

    @Test
    public void duplicateRegistrationIsRejected() {
        AISMessageRouter.Builder builder = AISMessageRouter.builder().on(PositionReport.class, _ -> { });

        assertThrows(IllegalStateException.class, () -> builder.on(PositionReport.class, _ -> { }));
    }

    @Test
    public void typeMatchingNoAISMessageIsRejected() {
        AISMessageRouter.Builder builder = AISMessageRouter.builder().on(String.class, _ -> { });

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    public void nullMessageIsRejected() {
        AISMessageRouter router = AISMessageRouter.builder().build();

        assertThrows(NullPointerException.class, () -> router.accept(null));
    }

    @Test
    public void handlerExceptionGoesToOnErrorAndRoutingContinues() {
        IllegalStateException failure = new IllegalStateException("boom");
        List<RuntimeException> errors = new ArrayList<>();
        List<AISMessage> routed = new ArrayList<>();
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReport.class, _ -> { throw failure; })
                .on(BaseStationReport.class, routed::add)
                .onError(errors::add)
                .build();

        router.accept(POSITION_REPORT);
        router.accept(BASE_STATION_REPORT);

        assertEquals(List.of(failure), errors);
        assertEquals(List.of(BASE_STATION_REPORT), routed);
    }

    @Test
    public void handlerExceptionPropagatesWithoutOnError() {
        IllegalStateException failure = new IllegalStateException("boom");
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReport.class, _ -> { throw failure; })
                .build();

        assertSame(failure, assertThrows(IllegalStateException.class, () -> router.accept(POSITION_REPORT)));
    }
}
