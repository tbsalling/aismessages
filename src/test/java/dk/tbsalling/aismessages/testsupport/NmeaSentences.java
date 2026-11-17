package dk.tbsalling.aismessages.testsupport;

import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.nmea.NMEAArmouring;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds synthetic AIS payloads and NMEA 0183 sentences for tests, so that test vectors are derived from the
 * ITU-R M.1371-6 message tables instead of being copied from third-party captures.
 */
public final class NmeaSentences {

    private NmeaSentences() {
    }

    /** Fluent builder of an AIS payload, most significant bit first (ITU-R M.1371-6 § A2-3.3.7). */
    public static final class Bits {
        private final StringBuilder bits = new StringBuilder();

        /** Append {@code value} as an unsigned field of {@code width} bits. */
        public Bits u(long value, int width) {
            if (value < 0 || (width < 63 && value >= (1L << width)))
                throw new IllegalArgumentException("value " + value + " does not fit in " + width + " unsigned bits");
            return append(value, width);
        }

        /** Append {@code value} as a two's complement field of {@code width} bits. */
        public Bits s(long value, int width) {
            long min = -(1L << (width - 1)), max = (1L << (width - 1)) - 1;
            if (value < min || value > max)
                throw new IllegalArgumentException("value " + value + " does not fit in " + width + " signed bits");
            return append(value & ((1L << width) - 1), width);
        }

        /** Append a boolean as one bit. */
        public Bits b(boolean value) {
            bits.append(value ? '1' : '0');
            return this;
        }

        /** Append {@code text} as {@code chars} six-bit ASCII characters (Table 45), padded with '@'. */
        public Bits text(String text, int chars) {
            if (text.length() > chars) throw new IllegalArgumentException("text longer than " + chars + " characters: " + text);
            for (int i = 0; i < chars; i++) {
                char c = i < text.length() ? text.charAt(i) : '@';
                int code = c >= 64 ? c - 64 : c;
                if (code < 0 || code > 63) throw new IllegalArgumentException("not a six-bit ASCII character: " + c);
                append(code, 6);
            }
            return this;
        }

        /** Append {@code n} zero bits. */
        public Bits zeros(int n) {
            bits.append("0".repeat(n));
            return this;
        }

        /** Append a raw string of '0' and '1'. */
        public Bits raw(String zerosAndOnes) {
            bits.append(zerosAndOnes);
            return this;
        }

        public int length() {
            return bits.length();
        }

        public BitString toBitString() {
            return BitString.ofBitString(bits.toString());
        }

        private Bits append(long value, int width) {
            for (int i = width - 1; i >= 0; i--) bits.append(((value >>> i) & 1) == 1 ? '1' : '0');
            return this;
        }
    }

    /** @return a new, empty payload builder */
    public static Bits bits() {
        return new Bits();
    }

    /**
     * @return a payload of exactly {@code lengthBits} bits starting with the given message type, repeat indicator 0 and
     * source MMSI 219000001, padded with zeros; for length-validation tests
     */
    public static BitString message(int type, int lengthBits) {
        Bits b = bits().u(type, 6).u(0, 2).u(219000001, 30);
        if (lengthBits < b.length()) return BitString.ofBitString(b.toBitString().getBits(0, lengthBits));
        return b.zeros(lengthBits - b.length()).toBitString();
    }

    /** @return a single-fragment {@code !AIVDM} sentence on channel A carrying {@code payload} */
    public static String sentence(BitString payload) {
        return sentence(payload, "A");
    }

    /** @return a single-fragment {@code !AIVDM} sentence on the given channel carrying {@code payload} */
    public static String sentence(BitString payload, String channel) {
        return withChecksum("AIVDM,1,1,," + channel + "," + NMEAArmouring.encode(payload) + "," + fillBits(payload));
    }

    /** @return {@link #sentence(BitString)} parsed into an {@link NMEAMessage} */
    public static NMEAMessage nmea(BitString payload) {
        return new NMEAMessage(sentence(payload));
    }

    /**
     * Split {@code payload} into consecutive {@code !AIVDM} fragments of at most {@code maxPayloadChars} armoured
     * characters each, numbered 1..n with sequential message identifier {@code sequenceId}.
     */
    public static List<String> fragments(BitString payload, int sequenceId, int maxPayloadChars) {
        String armoured = NMEAArmouring.encode(payload);
        int n = (armoured.length() + maxPayloadChars - 1) / maxPayloadChars;
        List<String> out = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String part = armoured.substring(i * maxPayloadChars, Math.min(armoured.length(), (i + 1) * maxPayloadChars));
            int fill = i == n - 1 ? fillBits(payload) : 0;
            out.add(withChecksum("AIVDM," + n + "," + (i + 1) + "," + sequenceId + ",A," + part + "," + fill));
        }
        return out;
    }

    /** @return {@code "!" + body + "*" + checksum}, where the checksum is the XOR of all characters of {@code body} */
    public static String withChecksum(String body) {
        return "!" + body + "*" + String.format("%02X", xor(body));
    }

    /** @return an IEC 61162-1 tag block such as {@code \c:1700000000,s:test*1F\} with a correct checksum */
    public static String tagBlock(Map<String, String> parameters) {
        List<String> fields = new ArrayList<>();
        parameters.forEach((k, v) -> fields.add(k + ":" + v));
        String body = String.join(",", fields);
        return "\\" + body + "*" + String.format("%02X", xor(body)) + "\\";
    }

    /** @return the number of NMEA fill bits needed to pad {@code payload} to whole six-bit characters */
    public static int fillBits(BitString payload) {
        return (6 - payload.length() % 6) % 6;
    }

    private static int xor(String s) {
        int x = 0;
        for (int i = 0; i < s.length(); i++) x ^= s.charAt(i);
        return x;
    }
}
