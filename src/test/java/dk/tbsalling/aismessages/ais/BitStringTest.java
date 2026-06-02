package dk.tbsalling.aismessages.ais;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitStringTest {

    // -- helpers --------------------------------------------------------------

    private static String sixBits(int... codes) {
        StringBuilder sb = new StringBuilder();
        for (int c : codes) {
            String bits = Integer.toBinaryString(c & 0x3F);
            sb.append("0".repeat(6 - bits.length())).append(bits);
        }
        return sb.toString();
    }

    // -- construction --------------------------------------------------------

    @Test
    public void ofBitString_storesLengthAndRoundTripsToString() {
        String s = "1101001011110000";
        BitString bs = BitString.ofBitString(s);
        assertNotNull(bs);
        assertEquals(16, bs.length());
        assertEquals(s, bs.toString());
    }

    @Test
    public void ofBitString_emptyReturnsEmptyConstant() {
        assertSame(BitString.EMPTY, BitString.ofBitString(""));
        assertEquals(0, BitString.EMPTY.length());
        assertEquals("", BitString.EMPTY.toString());
    }

    @Test
    public void ofBitString_rejectsNonBinaryCharacters() {
        assertThrows(IllegalArgumentException.class, () -> BitString.ofBitString("01x10"));
    }

    @Test
    public void ofBitString_rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> BitString.ofBitString(null));
    }

    // -- of(long[], int) factory --------------------------------------------

    @Test
    public void of_constructsFromPackedWords() {
        long[] words = new long[2]; // length 10 → ((10+63)>>>6)+1 = 1+1 = 2
        // Set bits 0, 3, 9 (MSB-first): bit i at position 63 - (i & 63) of word[i>>>6]
        words[0] = (1L << 63) | (1L << 60) | (1L << 54);
        BitString bs = BitString.of(words, 10);
        assertEquals(10, bs.length());
        assertEquals("1001000001", bs.toString());
    }

    @Test
    public void of_emptyLengthReturnsEmptyConstant() {
        assertSame(BitString.EMPTY, BitString.of(new long[1], 0));
    }

    @Test
    public void of_rejectsNullWords() {
        assertThrows(IllegalArgumentException.class, () -> BitString.of(null, 0));
    }

    @Test
    public void of_rejectsNegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> BitString.of(new long[2], -1));
    }

    @Test
    public void of_rejectsTooShortWordsArray() {
        // length 64 → requires ((64+63)>>>6)+1 = 1+1 = 2 words
        assertThrows(IllegalArgumentException.class, () -> BitString.of(new long[1], 64));
    }

    // -- getBoolean / getUnsignedInt / getSignedInt --------------------------

    @Test
    public void getBoolean_returnsBitAtBegin() {
        BitString bs = BitString.ofBitString("0110");
        assertFalse(bs.getBoolean(0, 1));
        assertTrue(bs.getBoolean(1, 2));
        assertTrue(bs.getBoolean(2, 3));
        assertFalse(bs.getBoolean(3, 4));
    }

    @Test
    public void getBoolean_ignoresEndAndOnlyUsesBegin() {
        BitString bs = BitString.ofBitString("10101010");
        assertTrue(bs.getBoolean(0, 5));   // first bit is '1'
        assertFalse(bs.getBoolean(1, 8));  // bit 1 is '0'
    }

    @Test
    public void getUnsignedInt_basicWidths() {
        BitString bs = BitString.ofBitString("1011100101");
        assertEquals(11, bs.getUnsignedInt(0, 4));      // 1011
        assertEquals(37, bs.getUnsignedInt(4, 10));     // 100101
        assertEquals(741, bs.getUnsignedInt(0, 10));    // 1011100101
    }

    @Test
    public void getUnsignedInt_width32Boundary() {
        // 32-bit field, treat as raw bits (no sign extension)
        BitString bs = BitString.ofBitString("11001010111111101011101010111110");
        assertEquals(0xCAFEBABE, bs.getUnsignedInt(0, 32));
    }

    @Test
    public void getUnsignedInt_widthZeroReturnsZero() {
        BitString bs = BitString.ofBitString("1010");
        assertEquals(0, bs.getUnsignedInt(2, 2));
    }

    @Test
    public void getUnsignedInt_crossWordRead() {
        // Build a 96-bit string where a 16-bit field straddles bits [60, 76) — crosses the 64-bit boundary.
        StringBuilder sb = new StringBuilder("0".repeat(60));   // bits 0..59 = 0
        sb.append("1101010101010101");                            // bits 60..75 = pattern (0xD555)
        sb.append("0".repeat(20));                               // bits 76..95 = 0
        BitString bs = BitString.ofBitString(sb.toString());
        assertEquals(0xD555, bs.getUnsignedInt(60, 76));
    }

    @Test
    public void getUnsignedInt_lastWordRead() {
        // Build a 70-bit string with a field at the very end.
        StringBuilder sb = new StringBuilder("0".repeat(64));
        sb.append("110101");  // bits 64..69 = 0x35
        BitString bs = BitString.ofBitString(sb.toString());
        assertEquals(0x35, bs.getUnsignedInt(64, 70));
    }

    @Test
    public void getUnsignedInt_zeroPaddedReadBeyondLength() {
        BitString bs = BitString.ofBitString("0001");  // length 4
        // Request bits [0, 10): pad with zeros -> 0001000000 = 64
        assertEquals(64, bs.getUnsignedInt(0, 10));
    }

    @Test
    public void getUnsignedInt_fullyOutOfRangeReturnsZero() {
        BitString bs = BitString.ofBitString("1111");
        assertEquals(0, bs.getUnsignedInt(8, 16));
    }

    @Test
    public void getSignedInt_twoComplementVariousWidths() {
        assertEquals(-1, BitString.ofBitString("1111").getSignedInt(0, 4));
        assertEquals(-8, BitString.ofBitString("1000").getSignedInt(0, 4));
        assertEquals(7, BitString.ofBitString("0111").getSignedInt(0, 4));
        assertEquals(-128, BitString.ofBitString("10000000").getSignedInt(0, 8));
        assertEquals(127, BitString.ofBitString("01111111").getSignedInt(0, 8));
        assertEquals(42, BitString.ofBitString("00101010").getSignedInt(0, 8));
    }

    @Test
    public void getSignedInt_width28Longitude() {
        // Longitude in AIS is encoded as a 28-bit signed int (1/600000 minutes).
        // Build a value of -1 in 28-bit two's complement: all 1s.
        BitString bs = BitString.ofBitString("1".repeat(28));
        assertEquals(-1, bs.getSignedInt(0, 28));

        // 27-bit max positive then sign bit set -> most negative: 1 followed by 27 zeros = -2^27.
        BitString minBs = BitString.ofBitString("1" + "0".repeat(27));
        assertEquals(-(1 << 27), minBs.getSignedInt(0, 28));
    }

    @Test
    public void getSignedInt_width1() {
        assertEquals(-1, BitString.ofBitString("1").getSignedInt(0, 1));
        assertEquals(0, BitString.ofBitString("0").getSignedInt(0, 1));
    }

    @Test
    public void getSignedInt_width32MatchesUnsignedSemantically() {
        BitString bs = BitString.ofBitString("10000000000000000000000000000000");
        assertEquals(Integer.MIN_VALUE, bs.getSignedInt(0, 32));
    }

    @Test
    public void getSignedInt_widthZeroReturnsZero() {
        assertEquals(0, BitString.ofBitString("1010").getSignedInt(2, 2));
    }

    // -- getUnsignedLong -----------------------------------------------------

    @Test
    public void getUnsignedLong_width32() {
        BitString bs = BitString.ofBitString("11001010111111101011101010111110");
        assertEquals(0xCAFEBABEL, bs.getUnsignedLong(0, 32));
    }

    @Test
    public void getUnsignedLong_width64() {
        // All ones in 64 bits == 0xFFFFFFFFFFFFFFFF (== -1L)
        BitString bs = BitString.ofBitString("1".repeat(64));
        assertEquals(-1L, bs.getUnsignedLong(0, 64));
    }

    @Test
    public void getUnsignedLong_width64CrossingBoundary() {
        // 128-bit string with a 64-bit field starting at bit 30.
        StringBuilder sb = new StringBuilder("0".repeat(30));
        sb.append("1".repeat(64));   // bits 30..93
        sb.append("0".repeat(34));
        BitString bs = BitString.ofBitString(sb.toString());
        assertEquals(-1L, bs.getUnsignedLong(30, 94));
    }

    @Test
    public void getUnsignedLong_widthZeroReturnsZero() {
        assertEquals(0L, BitString.ofBitString("1111").getUnsignedLong(2, 2));
    }

    // -- floats --------------------------------------------------------------

    @Test
    public void floats_followIntegerSemantics() {
        BitString bs = BitString.ofBitString("11110001");
        assertEquals(-1.0f, bs.getSignedFloat(0, 4));
        assertEquals(1.0f, bs.getUnsignedFloat(4, 8));
        assertEquals(15.0f, bs.getUnsignedFloat(0, 4));
    }

    // -- getBits raw extraction ---------------------------------------------

    @Test
    public void getBits_basicExtraction() {
        BitString bs = BitString.ofBitString("1100110011");
        assertEquals("001100", bs.getBits(2, 8));
    }

    @Test
    public void getBits_emptyRange() {
        assertEquals("", BitString.ofBitString("101010").getBits(2, 2));
    }

    @Test
    public void getBits_zeroPadsBeyondLength() {
        BitString bs = BitString.ofBitString("101");
        assertEquals("01000", bs.getBits(1, 6));
    }

    // -- slice ---------------------------------------------------------------

    @Test
    public void slice_returnsExpectedSubrange() {
        BitString bs = BitString.ofBitString("1100110011");
        BitString sub = bs.slice(2, 8);
        assertEquals(6, sub.length());
        assertEquals("001100", sub.toString());
    }

    @Test
    public void slice_emptyRangeReturnsEmptyConstant() {
        BitString bs = BitString.ofBitString("1010");
        assertSame(BitString.EMPTY, bs.slice(2, 2));
    }

    @Test
    public void slice_crossesWordBoundary() {
        StringBuilder sb = new StringBuilder("0".repeat(60));
        sb.append("1101010101010101");  // bits 60..75
        sb.append("0".repeat(20));
        BitString bs = BitString.ofBitString(sb.toString());
        BitString sub = bs.slice(58, 80);   // 22 bits crossing the 64-bit boundary
        assertEquals(22, sub.length());
        assertEquals(sb.substring(58, 80), sub.toString());
    }

    @Test
    public void slice_rejectsBadRange() {
        BitString bs = BitString.ofBitString("1010");
        assertThrows(IllegalArgumentException.class, () -> bs.slice(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> bs.slice(3, 2));
        assertThrows(IllegalArgumentException.class, () -> bs.slice(0, 5));
    }

    // -- withLengthPaddedTo --------------------------------------------------

    @Test
    public void withLengthPaddedTo_padsWithZeros() {
        BitString bs = BitString.ofBitString("1101");
        BitString padded = bs.withLengthPaddedTo(12);
        assertEquals(12, padded.length());
        assertEquals("110100000000", padded.toString());
    }

    @Test
    public void withLengthPaddedTo_returnsSameWhenLengthMatches() {
        BitString bs = BitString.ofBitString("1101");
        assertSame(bs, bs.withLengthPaddedTo(4));
    }

    @Test
    public void withLengthPaddedTo_rejectsShortenedLength() {
        BitString bs = BitString.ofBitString("1101");
        assertThrows(IllegalArgumentException.class, () -> bs.withLengthPaddedTo(3));
    }

    @Test
    public void withLengthPaddedTo_crossesWordBoundary() {
        BitString bs = BitString.ofBitString("1".repeat(60));
        BitString padded = bs.withLengthPaddedTo(130);
        assertEquals(130, padded.length());
        assertEquals("1".repeat(60) + "0".repeat(70), padded.toString());
    }

    // -- equals / hashCode ---------------------------------------------------

    @Test
    public void equalsAndHashCode_basedOnContent() {
        BitString a = BitString.ofBitString("110100101101");
        BitString b = BitString.ofBitString("110100101101");
        BitString c = BitString.ofBitString("110100101100");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotSame(a, b);
    }

    @Test
    public void equals_differentLengthIsNotEqual() {
        BitString a = BitString.ofBitString("1010");
        BitString b = BitString.ofBitString("10100");
        assertNotEquals(a, b);
    }

    // -- integration ---------------------------------------------------------

    @Test
    public void readsAcrossDifferentFields() {
        // signed -1 (4b), 18-bit field, unsigned 1 (4b), boolean (1b)
        String combo = "1111" + sixBits(1, 2, 3) + "0001" + "1";
        BitString bs = BitString.ofBitString(combo);
        assertEquals(-1, bs.getSignedInt(0, 4));
        assertEquals(1, bs.getUnsignedInt(22, 26));
        assertTrue(bs.getBoolean(26, 27));
        assertEquals("1111", bs.getBits(0, 4));
        assertEquals(combo.length(), bs.length());
    }

    // -- getBoolean extended -------------------------------------------------

    @Test
    public void getBoolean_returnsFalseBeyondLength() {
        BitString bs = BitString.ofBitString("1111");
        assertFalse(bs.getBoolean(4, 5));   // begin == length
        assertFalse(bs.getBoolean(100, 101));
    }

    // -- getUnsignedInt extended --------------------------------------------

    @Test
    public void getUnsignedInt_crossWordWithSinglePopulatedBit() {
        // Bit 63 (last bit of word 0) and bit 64 (first bit of word 1) — verify both directions.
        // MSB-first: lower bit index is more significant.
        BitString a = BitString.ofBitString("0".repeat(63) + "1" + "0".repeat(64));
        assertEquals(1, a.getUnsignedInt(63, 64));  // bit 63 = '1'  -> 1
        assertEquals(1, a.getUnsignedInt(62, 64));  // bits "01"     -> 1
        assertEquals(2, a.getUnsignedInt(63, 65));  // bits "10"     -> 2 (crosses word boundary)

        BitString b = BitString.ofBitString("0".repeat(64) + "1" + "0".repeat(63));
        assertEquals(1, b.getUnsignedInt(64, 65));  // bit 64 = '1' -> 1
        assertEquals(1, b.getUnsignedInt(63, 65));  // bits "01"    -> 1 (crosses word boundary)
    }

    @Test
    public void getUnsignedInt_partialZeroPadCrossingLength() {
        // length 67 (>64 so two data words); read [62, 70) — last 3 bits read are past length.
        BitString bs = BitString.ofBitString("0".repeat(60) + "11011" + "11");
        assertEquals(67, bs.length());
        // bits 62..66 = "0111 1"... let's compute: positions 60-66 = "1101111", then bit 60=1,61=1,62=0,63=1,64=1,65=1,66=1
        // read [62, 70): bits 62..66 from data = 01111, bits 67..69 = 0 (padded) -> 01111000 = 0x78 = 120
        assertEquals(0b01111000, bs.getUnsignedInt(62, 70));
    }

    // -- getSignedInt extended ----------------------------------------------

    @Test
    public void getSignedInt_crossWordRead() {
        // Place a 12-bit signed value crossing the word boundary at bit 64.
        // 12-bit 0xFFF = -1 in 12-bit two's complement.
        StringBuilder sb = new StringBuilder("0".repeat(58));
        sb.append("111111111111");          // bits 58..69 — straddles bit-64 boundary
        sb.append("0".repeat(58));
        BitString bs = BitString.ofBitString(sb.toString());
        assertEquals(-1, bs.getSignedInt(58, 70));
    }

    @Test
    public void getSignedInt_zeroPadBeyondLengthExtendsAsNonNegative() {
        // length 4, "0001"; read [0, 8) -> bits become "00010000" = 16; signed (8-bit) → 16.
        BitString bs = BitString.ofBitString("0001");
        assertEquals(16, bs.getSignedInt(0, 8));
    }

    @Test
    public void getSignedInt_fullyOutOfRangeReturnsZero() {
        BitString bs = BitString.ofBitString("1111");
        assertEquals(0, bs.getSignedInt(10, 16));
    }

    // -- getUnsignedLong extended -------------------------------------------

    @Test
    public void getUnsignedLong_narrowWidths() {
        BitString bs = BitString.ofBitString("0000000000001111111111111111"); // 28 bits
        assertEquals(0xFFFFL, bs.getUnsignedLong(12, 28));      // 16-bit value = 65535
        assertEquals(0xFFFL, bs.getUnsignedLong(16, 28));       // 12-bit value
        assertEquals(0L, bs.getUnsignedLong(0, 12));            // 12 zero bits
    }

    @Test
    public void getUnsignedLong_crossWordNarrowerThan64() {
        // 48-bit field starting at bit 40 — spans bits 40..87, crossing the 64-bit boundary.
        StringBuilder sb = new StringBuilder("0".repeat(40));
        sb.append("1".repeat(48));   // bits 40..87
        sb.append("0".repeat(40));
        BitString bs = BitString.ofBitString(sb.toString());
        assertEquals(0xFFFFFFFFFFFFL, bs.getUnsignedLong(40, 88));
    }

    @Test
    public void getUnsignedLong_zeroPadBeyondLength() {
        BitString bs = BitString.ofBitString("0001"); // length 4
        assertEquals(64L, bs.getUnsignedLong(0, 10)); // pads to 0001000000
    }

    @Test
    public void getUnsignedLong_fullyOutOfRangeReturnsZero() {
        BitString bs = BitString.ofBitString("1111");
        assertEquals(0L, bs.getUnsignedLong(8, 64));
    }

    // -- floats extended -----------------------------------------------------

    @Test
    public void getSignedFloat_negativeAndPositive() {
        // signed 8-bit field
        assertEquals(-128.0f, BitString.ofBitString("10000000").getSignedFloat(0, 8));
        assertEquals(127.0f, BitString.ofBitString("01111111").getSignedFloat(0, 8));
        assertEquals(0.0f, BitString.ofBitString("00000000").getSignedFloat(0, 8));
    }

    @Test
    public void getUnsignedFloat_acrossRange() {
        assertEquals(0.0f, BitString.ofBitString("00000000").getUnsignedFloat(0, 8));
        assertEquals(255.0f, BitString.ofBitString("11111111").getUnsignedFloat(0, 8));
    }

    // -- getBits extended ---------------------------------------------------

    @Test
    public void getBits_crossWordBoundary() {
        StringBuilder sb = new StringBuilder("0".repeat(60));
        sb.append("110011001100");                  // bits 60..71 — crosses bit-64 boundary
        sb.append("0".repeat(56));
        BitString bs = BitString.ofBitString(sb.toString());
        assertEquals("110011001100", bs.getBits(60, 72));
    }

    @Test
    public void getBits_fullyBeyondLengthReturnsAllZeros() {
        assertEquals("0000", BitString.ofBitString("1111").getBits(10, 14));
    }

    @Test
    public void getBits_inverseRangeReturnsEmpty() {
        // end < begin -> empty (defensive)
        assertEquals("", BitString.ofBitString("1010").getBits(3, 1));
    }

    // -- slice extended ------------------------------------------------------

    @Test
    public void slice_fullRangeMatchesSource() {
        BitString bs = BitString.ofBitString("110100101101");
        BitString full = bs.slice(0, bs.length());
        assertEquals(bs, full);
        assertNotSame(bs, full);   // fresh instance, not aliased
    }

    @Test
    public void slice_chainedSliceMatchesDirectSlice() {
        BitString bs = BitString.ofBitString("111100001111000011110000");  // 24 bits
        BitString twoStep = bs.slice(4, 20).slice(2, 14);
        BitString oneStep = bs.slice(6, 18);
        assertEquals(oneStep, twoStep);
    }

    @Test
    public void slice_tailBitsAreMaskedToZero() {
        // Slice an odd width across the word boundary; subsequent reads past newLength must be 0
        // (this is the invariant that lets accessors avoid bounds checks on the slack guard word).
        StringBuilder sb = new StringBuilder("0".repeat(60));
        sb.append("111111111111");   // bits 60..71 all 1
        sb.append("1".repeat(56));   // bits 72..127 also 1
        BitString bs = BitString.ofBitString(sb.toString());
        BitString sub = bs.slice(58, 75);   // 17 bits; reads at 17..63 in sub must be 0
        assertEquals(17, sub.length());
        // First two bits (from positions 58,59 of source) were 0, then 12 ones, then 3 ones.
        // Total: "00" + "111111111111" + "111" = "00111111111111111"
        assertEquals("00111111111111111", sub.toString());
        // Internal slack: a read past newLength should yield zero (no stray set bits in slack words).
        assertEquals(0, sub.getUnsignedInt(17, 32));
    }

    @Test
    public void slice_atExactWordBoundaryBegin() {
        // begin == 64: shift==0 fast path
        StringBuilder sb = new StringBuilder("0".repeat(64));
        sb.append("1".repeat(20));
        BitString bs = BitString.ofBitString(sb.toString());
        BitString sub = bs.slice(64, 84);
        assertEquals(20, sub.length());
        assertEquals("11111111111111111111", sub.toString());
    }

    // -- withLengthPaddedTo extended ----------------------------------------

    @Test
    public void withLengthPaddedTo_fromEmpty() {
        BitString padded = BitString.EMPTY.withLengthPaddedTo(10);
        assertEquals(10, padded.length());
        assertEquals("0000000000", padded.toString());
    }

    // -- toString extended ---------------------------------------------------

    @Test
    public void toString_acrossMultipleWords() {
        String s = "1010".repeat(40);     // 160 bits, spans 3 long words
        assertEquals(s, BitString.ofBitString(s).toString());
    }

    @Test
    public void toString_emptyBitStringIsEmpty() {
        assertEquals("", BitString.EMPTY.toString());
    }

    // -- equals / hashCode extended -----------------------------------------

    @Test
    public void equals_isReflexive() {
        BitString a = BitString.ofBitString("110100101101");
        assertEquals(a, a);
    }

    @Test
    public void equals_isNullSafeAndTypeSafe() {
        BitString a = BitString.ofBitString("1010");
        assertNotEquals(a, null);
        assertNotEquals(a, "1010");        // not equal to a String of same characters
        assertNotEquals(a, Integer.valueOf(10));
    }

    @Test
    public void hashCode_differsForDifferentContent() {
        // Not strictly guaranteed by hashCode contract, but for these inputs the
        // (length, words) hash will differ — this guards against accidental constant hashing.
        BitString a = BitString.ofBitString("1100110011");
        BitString b = BitString.ofBitString("0011001100");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hashCode_stableAcrossCalls() {
        BitString a = BitString.ofBitString("11010110001011");
        int h1 = a.hashCode();
        int h2 = a.hashCode();
        assertEquals(h1, h2);
    }

    // -- EMPTY constant ------------------------------------------------------

    @Test
    public void empty_constantHasExpectedShape() {
        assertEquals(0, BitString.EMPTY.length());
        assertEquals("", BitString.EMPTY.toString());
        assertEquals(0, BitString.EMPTY.getUnsignedInt(0, 0));
        assertEquals(0L, BitString.EMPTY.getUnsignedLong(0, 0));
        assertEquals(0, BitString.EMPTY.getSignedInt(0, 0));
        assertFalse(BitString.EMPTY.getBoolean(0, 1));
        assertEquals("", BitString.EMPTY.getBits(0, 0));
    }
}
