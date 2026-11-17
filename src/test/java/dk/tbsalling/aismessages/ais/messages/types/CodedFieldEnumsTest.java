package dk.tbsalling.aismessages.ais.messages.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Code mapping of the enums added for ITU-R M.1371-6 Tables 50, 77 and 83. */
public class CodedFieldEnumsTest {

    @Test
    public void aisVersionIndicatorCoversTable50() {
        assertEquals(AisVersionIndicator.ITU_R_M1371_1, AisVersionIndicator.fromInteger(0));
        assertEquals(AisVersionIndicator.ITU_R_M1371_3, AisVersionIndicator.fromInteger(1));
        assertEquals(AisVersionIndicator.ITU_R_M1371_5, AisVersionIndicator.fromInteger(2));
        assertEquals(AisVersionIndicator.ITU_R_M1371_6, AisVersionIndicator.fromInteger(3));
        assertNull(AisVersionIndicator.fromInteger(4));
        assertNull(AisVersionIndicator.fromInteger((Integer) null));
    }

    @Test
    public void vdesCapabilitiesCoversTable77() {
        assertEquals(VdesCapabilities.AisOnly, VdesCapabilities.fromInteger(0));
        assertEquals(VdesCapabilities.VdesAsm, VdesCapabilities.fromInteger(1));
        assertEquals(VdesCapabilities.VdesAsmVdeTer, VdesCapabilities.fromInteger(2));
        assertEquals(VdesCapabilities.VdesAsmVdeTerVdeSat, VdesCapabilities.fromInteger(3));
        assertNull(VdesCapabilities.fromInteger(4));
    }

    @Test
    public void positionLatencyCoversTable83() {
        assertEquals(PositionLatency.LessThanFiveSeconds, PositionLatency.fromInteger(0));
        assertEquals(PositionLatency.MoreThanFiveSeconds, PositionLatency.fromInteger(1));
        assertNull(PositionLatency.fromInteger(2));
    }

    @Test
    public void codesRoundTrip() {
        for (AisVersionIndicator v : AisVersionIndicator.values()) assertEquals(v, AisVersionIndicator.fromInteger(v.getCode()));
        for (VdesCapabilities v : VdesCapabilities.values()) assertEquals(v, VdesCapabilities.fromInteger(v.getCode()));
        for (PositionLatency v : PositionLatency.values()) assertEquals(v, PositionLatency.fromInteger(v.getCode()));
    }
}
