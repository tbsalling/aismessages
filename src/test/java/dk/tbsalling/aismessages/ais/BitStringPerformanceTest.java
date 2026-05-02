package dk.tbsalling.aismessages.ais;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.BitSet;

/**
 * Performance benchmark comparing BitString (BitSet-based) vs String-based bit representation.
 * 
 * This benchmark measures:
 * 1. Memory usage (object size)
 * 2. Creation time
 * 3. Substring extraction time
 * 4. toString() conversion time
 */
public class BitStringPerformanceTest {

    private static final int WARMUP_ITERATIONS = 1000;
    private static final int BENCHMARK_ITERATIONS = 100000;
    private static final String TEST_BIT_STRING_168 = "000001000010000011000100000101000110000111001000001001001010001011001100001101001110001111010000010001010010010011010100010101010110010111011000011001011010011011011100011101011110011111100000100001100010100011100100100101";

    @Test
    @Disabled("Performance benchmark - run manually when needed")
    public void benchmarkBitStringCreation() {
        System.out.println("\n=== BitString Creation Benchmark ===");
        
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            new BitString(TEST_BIT_STRING_168);
            createStringRepresentation(TEST_BIT_STRING_168);
        }
        
        // Benchmark BitString creation
        long startBitString = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            new BitString(TEST_BIT_STRING_168);
        }
        long bitStringTime = System.nanoTime() - startBitString;
        
        // Benchmark String creation (simulating old approach)
        long startString = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            createStringRepresentation(TEST_BIT_STRING_168);
        }
        long stringTime = System.nanoTime() - startString;
        
        System.out.printf("BitString creation: %.2f ms (%.2f ns per operation)\n", 
            bitStringTime / 1_000_000.0, bitStringTime / (double) BENCHMARK_ITERATIONS);
        System.out.printf("String creation:    %.2f ms (%.2f ns per operation)\n", 
            stringTime / 1_000_000.0, stringTime / (double) BENCHMARK_ITERATIONS);
        System.out.printf("Speedup: %.2fx\n", stringTime / (double) bitStringTime);
    }

    @Test
    @Disabled("Performance benchmark - run manually when needed")
    public void benchmarkSubstringExtraction() {
        System.out.println("\n=== Substring Extraction Benchmark ===");
        
        BitString bitString = new BitString(TEST_BIT_STRING_168);
        String stringRep = TEST_BIT_STRING_168;
        
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            bitString.substring(0, 6);
            bitString.substring(8, 38);
            bitString.substring(50, 100);
            stringRep.substring(0, 6);
            stringRep.substring(8, 38);
            stringRep.substring(50, 100);
        }
        
        // Benchmark BitString substring
        long startBitString = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            bitString.substring(0, 6);
            bitString.substring(8, 38);
            bitString.substring(50, 100);
        }
        long bitStringTime = System.nanoTime() - startBitString;
        
        // Benchmark String substring
        long startString = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            stringRep.substring(0, 6);
            stringRep.substring(8, 38);
            stringRep.substring(50, 100);
        }
        long stringTime = System.nanoTime() - startString;
        
        System.out.printf("BitString substring: %.2f ms (%.2f ns per operation)\n", 
            bitStringTime / 1_000_000.0, bitStringTime / (double) (BENCHMARK_ITERATIONS * 3));
        System.out.printf("String substring:    %.2f ms (%.2f ns per operation)\n", 
            stringTime / 1_000_000.0, stringTime / (double) (BENCHMARK_ITERATIONS * 3));
        System.out.printf("Speedup: %.2fx\n", stringTime / (double) bitStringTime);
    }

    @Test
    @Disabled("Performance benchmark - run manually when needed")
    public void benchmarkToString() {
        System.out.println("\n=== toString() Conversion Benchmark ===");
        
        BitString bitString = new BitString(TEST_BIT_STRING_168);
        
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            bitString.toString();
        }
        
        // Benchmark BitString toString
        long startBitString = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            bitString.toString();
        }
        long bitStringTime = System.nanoTime() - startBitString;
        
        System.out.printf("BitString toString: %.2f ms (%.2f ns per operation)\n", 
            bitStringTime / 1_000_000.0, bitStringTime / (double) BENCHMARK_ITERATIONS);
    }

    @Test
    @Disabled("Performance benchmark - run manually when needed")
    public void benchmarkMemoryUsage() {
        System.out.println("\n=== Memory Usage Estimate ===");
        
        // String memory estimate (UTF-16, 2 bytes per char + object overhead)
        int stringMemory = TEST_BIT_STRING_168.length() * 2 + 24; // ~24 bytes object overhead
        
        // BitString memory estimate
        // BitSet uses long[] internally, so (bits/64 + 1) * 8 bytes + object overhead
        int bitSetArraySize = (TEST_BIT_STRING_168.length() / 64 + 1) * 8;
        int bitStringMemory = bitSetArraySize + 24 + 4; // BitSet overhead + BitString overhead + int length
        
        System.out.printf("String representation: ~%d bytes\n", stringMemory);
        System.out.printf("BitString representation: ~%d bytes\n", bitStringMemory);
        System.out.printf("Memory savings: %.1f%%\n", 
            (1.0 - bitStringMemory / (double) stringMemory) * 100);
    }

    @Test
    public void runAllBenchmarks() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          BitString Performance Benchmark Results              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        
        benchmarkMemoryUsage();
        benchmarkBitStringCreation();
        benchmarkSubstringExtraction();
        benchmarkToString();
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Benchmark Complete                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Simulates the old String-based representation approach.
     */
    private String createStringRepresentation(String bitString) {
        // Just return the string - this simulates storing it as a String
        return bitString;
    }
}
