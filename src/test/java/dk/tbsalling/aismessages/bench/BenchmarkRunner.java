package dk.tbsalling.aismessages.bench;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Surefire entry point for the JMH benchmark suite. Invoked only when the {@code bench}
 * Maven profile is active (the profile narrows the Surefire {@code <includes>} to this class).
 *
 * <p>Output is written to {@code target/jmh-result.txt}. To run:
 * <pre>./mvnw -Pbench test</pre>
 */
public class BenchmarkRunner {

    @Test
    public void runAll() throws Exception {
        new Runner(new OptionsBuilder()
                .include(BitStringMicroBenchmark.class.getName())
                .include(AISMessageFactoryBenchmark.class.getName())
                .include(NMEAMessageParseBenchmark.class.getName())
                .resultFormat(ResultFormatType.TEXT)
                .result("target/jmh-result.txt")
                .build())
                .run();
    }

    @Test
    public void runRetainedSizeReport() {
        RetainedSizeReport.main(new String[0]);
    }
}
