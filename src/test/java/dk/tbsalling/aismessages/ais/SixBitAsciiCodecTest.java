package dk.tbsalling.aismessages.ais;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SixBitAsciiCodecTest {

    // -- helpers -------------------------------------------------------------

    /**
     * Build a sample 64-entry alphabet using printable ASCII '0'..'o' (codes 48..111).
     * The exact characters don't matter — the codec is alphabet-agnostic; the tests just need
     * a valid, distinct ASCII alphabet to exercise mechanism behaviour.
     */
    private static char[] sampleAlphabet() {
        char[] a = new char[64];
        for (int i = 0; i < 64; i++) a[i] = (char) ('0' + i); // '0'..'o' (48..111)
        return a;
    }

    private static SixBitAsciiCodec sampleCodec() {
        return new SixBitAsciiCodec(sampleAlphabet());
    }

    // -- construction --------------------------------------------------------

    @Test
    public void construct_rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SixBitAsciiCodec(null));
    }

    @Test
    public void construct_rejectsWrongLength() {
        assertThrows(IllegalArgumentException.class, () -> new SixBitAsciiCodec(new char[63]));
        assertThrows(IllegalArgumentException.class, () -> new SixBitAsciiCodec(new char[65]));
    }

    @Test
    public void construct_rejectsNonAscii() {
        char[] a = sampleAlphabet();
        a[5] = (char) 200; // not 7-bit ASCII
        assertThrows(IllegalArgumentException.class, () -> new SixBitAsciiCodec(a));
    }

    @Test
    public void construct_rejectsDuplicates() {
        char[] a = sampleAlphabet();
        a[3] = a[10]; // create a duplicate
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new SixBitAsciiCodec(a));
        assertTrue(ex.getMessage().contains("duplicate"));
    }

    @Test
    public void construct_defensiveCopiesAlphabet() {
        // Mutating the caller's array after construction must not affect the codec.
        char[] a = sampleAlphabet();
        SixBitAsciiCodec codec = new SixBitAsciiCodec(a);
        char originalAtZero = codec.charFor(0);
        a[0] = 'Z'; // mutate caller's array
        assertEquals(originalAtZero, codec.charFor(0));
    }

    // -- encode --------------------------------------------------------------

    @Test
    public void encode_singleCharacter() {
        // Value 1 → alphabet[1] = '1'
        assertEquals("1", sampleCodec().encode(BitString.ofBitString("000001")));
    }

    @Test
    public void encode_threeCharacters() {
        // Values 0, 1, 17 → '0', '1', 'A' ('0' + 17 = 'A')
        BitString bs = BitString.ofBitString("000000000001010001");
        assertEquals("01A", sampleCodec().encode(bs));
    }

    @Test
    public void encode_zeroPadsPartialFinalChunk() {
        // 4 bits "0000" + zero-pad of 2 bits = 6 bits 000000 -> alphabet[0]
        assertEquals("0", sampleCodec().encode(BitString.ofBitString("0000")));
        // 5 bits "00001" + zero-pad of 1 bit = 6 bits 000010 -> alphabet[2] = '2'
        assertEquals("2", sampleCodec().encode(BitString.ofBitString("00001")));
    }

    @Test
    public void encode_emptyBitStringProducesEmptyString() {
        assertEquals("", sampleCodec().encode(BitString.EMPTY));
    }

    @Test
    public void encode_crossWord() {
        // 66 bits — value 1 repeated 11 times — straddles word boundary
        String binary = "000001".repeat(11);
        assertEquals("11111111111", sampleCodec().encode(BitString.ofBitString(binary)));
    }

    @Test
    public void encode_rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> sampleCodec().encode(null));
    }

    // -- decode --------------------------------------------------------------

    @Test
    public void decode_singleCharacter() {
        BitString bs = sampleCodec().decode("1", 0);
        assertEquals(6, bs.length());
        assertEquals("000001", bs.toString());
    }

    @Test
    public void decode_threeCharactersZeroPadding() {
        // '0' -> 0, '1' -> 1, 'A' -> alphabet index of 'A' = 'A' - '0' = 17
        BitString result = sampleCodec().decode("01A", 0);
        assertEquals(18, result.length());
        assertEquals("000000000001010001", result.toString());
    }

    @Test
    public void decode_appliesPaddingBits() {
        // '1' -> "000001" trimmed of 4 padding bits -> "00"
        BitString bs = sampleCodec().decode("1", 4);
        assertEquals(2, bs.length());
        assertEquals("00", bs.toString());
    }

    @Test
    public void decode_paddingBoundsCoverFullRange() {
        SixBitAsciiCodec codec = sampleCodec();
        for (int p = 0; p <= 5; p++) {
            BitString r = codec.decode("0", p);
            assertEquals(6 - p, r.length());
        }
    }

    @Test
    public void decode_rejectsInvalidCharacter() {
        // 'z' (122) is outside the sample alphabet ('0'..'o', codes 48..111)
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sampleCodec().decode("z", 0));
        assertTrue(ex.getMessage().contains("Invalid six-bit character"));
    }

    @Test
    public void decode_rejectsNullInput() {
        assertThrows(IllegalArgumentException.class, () -> sampleCodec().decode(null, 0));
    }

    @Test
    public void decode_rejectsBadPadding() {
        SixBitAsciiCodec codec = sampleCodec();
        assertThrows(IllegalArgumentException.class, () -> codec.decode("0", -1));
        assertThrows(IllegalArgumentException.class, () -> codec.decode("0", 6));
    }

    @Test
    public void decode_emptyInputProducesEmptyBitString() {
        BitString bs = sampleCodec().decode("", 0);
        assertEquals(0, bs.length());
        assertEquals("", bs.toString());
    }

    @Test
    public void decode_handlesCrossWordPacking() {
        // 11 characters * 6 bits = 66 bits — straddles the 64-bit word boundary.
        String encoded = "12345678901";
        BitString bs = sampleCodec().decode(encoded, 0);
        assertEquals(66, bs.length());
        for (int i = 0; i < 11; i++) {
            int expected = (encoded.charAt(i) - '0') & 0x3F;
            assertEquals(expected, bs.getUnsignedInt(i * 6, i * 6 + 6),
                    "six-bit value at chunk " + i);
        }
    }

    @Test
    public void decode_masksTailBits() {
        // '1' is 0b000001; with padding 1, length is 5 — the LSB of '1' is trimmed away.
        BitString bs = sampleCodec().decode("1", 1);
        assertEquals(5, bs.length());
        assertEquals(0, bs.getUnsignedInt(5, 6));
    }

    // -- decodeRange ---------------------------------------------------------

    @Test
    public void decodeRange_extractsAlphabetCharacters() {
        // 30 bits = 5 chars; codes 0,1,2,3,4 -> sample alphabet '0','1','2','3','4'
        BitString bs = BitString.ofBitString("000000000001000010000011000100");
        assertEquals("01234", sampleCodec().decodeRange(bs, 0, 30));
    }

    @Test
    public void decodeRange_subrangeOnly() {
        BitString bs = BitString.ofBitString("000000000001000010000011000100");
        // Skip the first 6 bits -> 24 bits = 4 chars: '1','2','3','4'
        assertEquals("1234", sampleCodec().decodeRange(bs, 6, 30));
    }

    @Test
    public void decodeRange_widthLessThanSixReturnsEmpty() {
        BitString bs = BitString.ofBitString("11111");
        assertEquals("", sampleCodec().decodeRange(bs, 0, 5));
    }

    @Test
    public void decodeRange_ignoresLeftoverBitsNotMultipleOfSix() {
        // 9 bits → 1 char (last 3 bits ignored)
        BitString bs = BitString.ofBitString("000001101");
        assertEquals("1", sampleCodec().decodeRange(bs, 0, 9));
    }

    // -- charFor -------------------------------------------------------------

    @Test
    public void charFor_returnsAlphabetCharacter() {
        SixBitAsciiCodec codec = sampleCodec();
        assertEquals('0', codec.charFor(0));
        assertEquals('1', codec.charFor(1));
        assertEquals((char) ('0' + 63), codec.charFor(63));
    }

    @Test
    public void charFor_masksToSixBits() {
        // 0xC0 has the low 6 bits all zero -> alphabet[0]
        assertEquals('0', sampleCodec().charFor(0xC0));
    }

    // -- round trip ----------------------------------------------------------

    @Test
    public void roundTripAligned() {
        SixBitAsciiCodec codec = sampleCodec();
        BitString original = BitString.ofBitString("000001010001100111111110");
        assertEquals(original, codec.decode(codec.encode(original), 0));
    }

    @Test
    public void roundTripUnalignedWithPadding() {
        SixBitAsciiCodec codec = sampleCodec();
        BitString original = BitString.ofBitString("1011010011001"); // 13 bits
        int paddingBits = (6 - original.length() % 6) % 6;
        assertEquals(5, paddingBits);
        String encoded = codec.encode(original);
        assertEquals(3, encoded.length());
        assertEquals(original, codec.decode(encoded, paddingBits));
    }

    @Test
    public void roundTripLongCrossWord() {
        SixBitAsciiCodec codec = sampleCodec();
        StringBuilder sb = new StringBuilder(168);
        for (int i = 0; i < 28; i++) {
            String chunk = Integer.toBinaryString(i + 1);
            sb.append("0".repeat(6 - chunk.length())).append(chunk);
        }
        BitString original = BitString.ofBitString(sb.toString());
        String encoded = codec.encode(original);
        assertEquals(28, encoded.length());
        assertEquals(original, codec.decode(encoded, 0));
    }
}
