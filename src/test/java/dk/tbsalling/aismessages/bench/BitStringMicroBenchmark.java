package dk.tbsalling.aismessages.bench;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Micro-benchmarks for the packed {@link BitString} accessors across representative
 * AIS bit fields. Baseline numbers for the old String-based decoder
 * ({@code BitStringParser} + {@code BitDecoder}) are captured in
 * {@code target/jmh-baseline.txt} prior to deletion.
 * <p>
 * Run via {@link BenchmarkRunner}. Each benchmark is intentionally configured for
 * short execution suitable for in-session capture; for production-grade numbers
 * rerun with more warmup/measurement iterations.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class BitStringMicroBenchmark {

    /**
     * 168-bit position report (type 1) bit string, hand-built so field reads
     * exercise the same bit positions as the real decoder.
     */
    private BitString bitString;

    @Setup
    public void setup() {
        StringBuilder sb = new StringBuilder(168);
        sb.append("000001");        // msg type = 1 (bits 0..6)
        sb.append("00");            // repeat indicator (bits 6..8)
        sb.append("000011101011110010101010111100"); // 30-bit MMSI (bits 8..38)
        sb.append("0000");          // nav status (bits 38..42)
        sb.append("01111111");      // rot (bits 42..50)
        sb.append("0001010100");    // sog (bits 50..60)
        sb.append("1");             // accuracy (bits 60..61)
        sb.append("0001010100110010110011001100"); // 28-bit longitude (bits 61..89)
        sb.append("000101010011001011001100110"); // 27-bit latitude (bits 89..116)
        sb.append("001011001011");  // cog (bits 116..128)
        sb.append("001011001");     // heading (bits 128..137)
        sb.append("110010");        // second (bits 137..143)
        sb.append("01");            // maneuver (bits 143..145)
        sb.append("000");           // spare (bits 145..148)
        sb.append("0");             // raim (bits 148..149)
        sb.append("0011001100110011001"); // comm state (bits 149..168)
        bitString = BitString.ofBitString(sb.toString());
    }

    @Benchmark
    public int newGetUnsignedInt_6bit() {
        return bitString.getUnsignedInt(0, 6);
    }

    @Benchmark
    public int newGetUnsignedInt_30bit_mmsi() {
        return bitString.getUnsignedInt(8, 38);
    }

    @Benchmark
    public int newGetSignedInt_28bit_longitude() {
        return bitString.getSignedInt(61, 89);
    }

    @Benchmark
    public String newGetSixBitAsciiString_120bit() {
        return AISText.decode(bitString, 0, 120);
    }
}
