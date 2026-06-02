package dk.tbsalling.aismessages.ais;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AISTextTest {

    /**
     * Helper: build a bit string from 6-bit codes for the SIX_BIT_ASCII alphabet.
     */
    private static BitString sixBits(int... codes) {
        StringBuilder sb = new StringBuilder();
        for (int c : codes) {
            String bits = Integer.toBinaryString(c & 0x3F);
            sb.append("0".repeat(6 - bits.length())).append(bits);
        }
        return BitString.ofBitString(sb.toString());
    }

    @Test
    public void decode_basicLetters() {
        // A=1, B=2, C=3
        BitString bs = sixBits(1, 2, 3);
        assertEquals("ABC", AISText.decode(bs, 0, bs.length()));
    }

    @Test
    public void decode_test() {
        // T=20, E=5, S=19, T=20
        BitString bs = BitString.ofBitString("010100000101010011010100");
        assertEquals("TEST", AISText.decode(bs, 0, 24));
    }

    @Test
    public void decode_digitsAndPunctuation() {
        BitString digits = sixBits(48, 49, 50, 51, 52, 53, 54, 55, 56, 57);
        assertEquals("0123456789", AISText.decode(digits, 0, digits.length()));
        BitString punc = sixBits(45, 46, 47, 58);
        assertEquals("-./:", AISText.decode(punc, 0, punc.length()));
    }

    @Test
    public void decode_singleAtBecomesEmpty() {
        BitString bs = sixBits(0);
        assertEquals("", AISText.decode(bs, 0, 6));
    }

    @Test
    public void decode_trimsLeadingAndTrailingAt() {
        // @HELLO@ -> "HELLO"
        BitString bs = sixBits(0, 8, 5, 12, 12, 15, 0);
        assertEquals("HELLO", AISText.decode(bs, 0, bs.length()));
    }

    @Test
    public void decode_preservesEmbeddedSpacesFromAt() {
        // H E L @ L O -> "HEL LO"
        BitString bs = sixBits(8, 5, 12, 0, 12, 15);
        assertEquals("HEL LO", AISText.decode(bs, 0, bs.length()));
    }

    @Test
    public void decode_multipleEmbeddedAtsBecomeSpaces() {
        BitString bs = sixBits(1, 0, 0, 2);
        assertEquals("A  B", AISText.decode(bs, 0, bs.length()));
    }

    @Test
    public void decode_ignoresLeftoverBitsNotMultipleOfSix() {
        // "AB" then 3 leftover bits ignored
        BitString bs = BitString.ofBitString(toBits(1, 2) + "101");
        assertEquals("AB", AISText.decode(bs, 0, bs.length()));
    }

    @Test
    public void decode_widthLessThanSixReturnsEmpty() {
        BitString bs = BitString.ofBitString("11111");
        assertEquals("", AISText.decode(bs, 0, 5));
    }

    @Test
    public void decode_allCharactersRoundTrip() {
        // Codes 1..63 — verify each maps to the expected character (code 0 = '@' would trim).
        int[] codes = new int[63];
        for (int i = 0; i < 63; i++) codes[i] = i + 1;
        BitString bs = sixBits(codes);
        String decoded = AISText.decode(bs, 0, bs.length());
        assertEquals(63, decoded.length());
        assertEquals('A', decoded.charAt(0));   // code 1
        assertEquals('Z', decoded.charAt(25));  // code 26
        assertEquals('?', decoded.charAt(62));  // code 63
        assertEquals('0', decoded.charAt(47));  // code 48
    }

    @Test
    public void decode_crossesWordBoundary() {
        // 12 six-bit characters = 72 bits — crosses the 64-bit word boundary.
        int[] codes = new int[12];
        for (int i = 0; i < 12; i++) codes[i] = 1;   // all 'A'
        BitString bs = sixBits(codes);
        assertEquals("AAAAAAAAAAAA", AISText.decode(bs, 0, bs.length()));
    }

    @Test
    public void decode_widthZeroReturnsEmpty() {
        BitString bs = BitString.ofBitString("000001000010");
        assertEquals("", AISText.decode(bs, 0, 0));
    }

    private static String toBits(int... codes) {
        StringBuilder sb = new StringBuilder();
        for (int c : codes) {
            String bits = Integer.toBinaryString(c & 0x3F);
            sb.append("0".repeat(6 - bits.length())).append(bits);
        }
        return sb.toString();
    }
}
