package dk.tbsalling.aismessages.bench;

import dk.tbsalling.aismessages.ais.BitString;
import org.openjdk.jol.info.GraphLayout;

/**
 * Prints the retained heap size of a {@link BitString} versus the equivalent
 * raw {@link String} of '0' and '1' characters (the prior representation) at
 * representative payload widths.
 *
 * <p>Run via {@link BenchmarkRunner#runRetainedSizeReport()} or invoke
 * {@link #main(String[])} directly from an IDE.
 */
public class RetainedSizeReport {

    private static final int[] WIDTHS = {72, 168, 424, 1100};

    public static void main(String[] args) {
        System.out.println("AIS payload retained heap size — String vs BitString");
        System.out.printf("%-12s | %-22s | %-22s | %s%n", "bits", "String form (bytes)", "BitString (bytes)", "ratio");
        System.out.println("-".repeat(80));
        for (int width : WIDTHS) {
            String bits = "1".repeat(width);
            long stringForm = GraphLayout.parseInstance(bits).totalSize();
            long packedForm = GraphLayout.parseInstance(BitString.ofBitString(bits)).totalSize();
            double ratio = (double) stringForm / packedForm;
            System.out.printf("%-12d | %-22d | %-22d | %.2fx%n", width, stringForm, packedForm, ratio);
        }
    }
}
