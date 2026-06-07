package dk.tbsalling.aismessages.bench;

import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Micro-benchmark for {@link NMEAMessage} construction (parsing).
 *
 * <p>This isolates the cost of constructing a {@code NMEAMessage} from a raw NMEA sentence string,
 * including tag-block detection, field splitting, and checksum parsing. It is the right benchmark
 * to measure improvements to the parsing hot-path (e.g., caching compiled {@link java.util.regex.Pattern}
 * instances as {@code static final} fields).
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class NMEAMessageParseBenchmark {

    private String[] rawLines;
    private int index;

    @Setup
    public void setup() throws IOException {
        List<String> list = new ArrayList<>();
        try (InputStream in = NMEAMessageParseBenchmark.class.getResourceAsStream("/bench/sample-nmea.txt");
             BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                list.add(line);
            }
        }
        if (list.isEmpty()) {
            throw new IllegalStateException("sample-nmea.txt yielded no messages");
        }
        rawLines = list.toArray(new String[0]);
        index = 0;
    }

    @Benchmark
    public void parseOne(Blackhole bh) {
        String line = rawLines[index];
        index = (index + 1) % rawLines.length;
        bh.consume(new NMEAMessage(line));
    }
}
