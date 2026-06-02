package dk.tbsalling.aismessages.bench;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.AISMessageFactory;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * End-to-end macro benchmark for {@link AISMessageFactory#create} across a corpus of
 * single-fragment AIS messages. Measures throughput and (with {@code -prof gc}) the
 * per-call allocation rate of the full decode path.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class AISMessageFactoryBenchmark {

    private NMEAMessage[] messages;
    private int index;

    @Setup
    public void setup() throws IOException {
        List<NMEAMessage> list = new ArrayList<>();
        try (InputStream in = AISMessageFactoryBenchmark.class.getResourceAsStream("/bench/sample-nmea.txt");
             BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                list.add(new NMEAMessage(line));
            }
        }
        if (list.isEmpty()) {
            throw new IllegalStateException("sample-nmea.txt yielded no messages");
        }
        messages = list.toArray(new NMEAMessage[0]);
        index = 0;
    }

    @Benchmark
    public void decodeOne(Blackhole bh) {
        NMEAMessage m = messages[index];
        index = (index + 1) % messages.length;
        AISMessage decoded = AISMessageFactory.create(Instant.EPOCH, "bench", null, m);
        bh.consume(decoded);
    }
}
