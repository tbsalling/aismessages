package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.BitString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NMEAArmouringTest {

    // -- decode --------------------------------------------------------------

    @Test
    public void decode_singleCharacter() {
        BitString bs = NMEAArmouring.decode("1", 0);
        assertEquals(6, bs.length());
        assertEquals("000001", bs.toString());
    }

    @Test
    public void decode_basicMappingZeroPadding() {
        // "01A": '0' -> 000000, '1' -> 000001, 'A' -> 010001
        assertEquals("000000000001010001", NMEAArmouring.decode("01A", 0).toString());
    }

    @Test
    public void decode_appliesPaddingBits() {
        // '1' -> "000001" trimmed of 4 padding bits -> "00"
        BitString bs = NMEAArmouring.decode("1", 4);
        assertEquals(2, bs.length());
        assertEquals("00", bs.toString());
    }

    @Test
    public void decode_paddingBoundsCoverFullRange() {
        for (int p = 0; p <= 5; p++) {
            assertEquals(6 - p, NMEAArmouring.decode("0", p).length());
        }
    }

    @Test
    public void decode_rejectsCharacterInGap() {
        // 'X' (88) falls in the gap between the two armouring ranges
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> NMEAArmouring.decode("X", 0));
        assertTrue(ex.getMessage().contains("Invalid six-bit character"));
    }

    @Test
    public void decode_rejectsCharacterBelowRange() {
        // '!' (33) is below the valid armouring range
        assertThrows(IllegalArgumentException.class, () -> NMEAArmouring.decode("!", 0));
    }

    @Test
    public void decode_rejectsNullInput() {
        assertThrows(IllegalArgumentException.class, () -> NMEAArmouring.decode(null, 0));
    }

    @Test
    public void decode_rejectsBadPadding() {
        assertThrows(IllegalArgumentException.class, () -> NMEAArmouring.decode("0", -1));
        assertThrows(IllegalArgumentException.class, () -> NMEAArmouring.decode("0", 6));
    }

    @Test
    public void decode_knownBoundaryMappings() {
        // '0' (48) -> 0  ;  ':' (58) -> 10  ;  ';' (59) -> 11
        // 'O' (79) -> 31 ;  'P' (80) -> 32  ;  '`' (96) -> 40  ;  'w' (119) -> 63
        assertEquals("001010", NMEAArmouring.decode(":", 0).toString());
        assertEquals("001011", NMEAArmouring.decode(";", 0).toString());
        assertEquals("011111", NMEAArmouring.decode("O", 0).toString());
        assertEquals("100000", NMEAArmouring.decode("P", 0).toString());
        assertEquals("111111", NMEAArmouring.decode("w", 0).toString());
    }

    @Test
    public void decode_emptyInputProducesEmptyBitString() {
        BitString bs = NMEAArmouring.decode("", 0);
        assertEquals(0, bs.length());
        assertEquals("", bs.toString());
    }

    // -- encode --------------------------------------------------------------

    @Test
    public void encode_singleCharacter() {
        assertEquals("1", NMEAArmouring.encode(BitString.ofBitString("000001")));
    }

    @Test
    public void encode_zeroPadsPartialFinalChunk() {
        // 5 bits "00001" + zero-pad of 1 bit = 6 bits 000010 -> '2'
        assertEquals("2", NMEAArmouring.encode(BitString.ofBitString("00001")));
    }

    @Test
    public void encode_knownBoundaryMappings() {
        assertEquals("0", NMEAArmouring.encode(BitString.ofBitString("000000"))); //  0 -> '0'
        assertEquals(":", NMEAArmouring.encode(BitString.ofBitString("001010"))); // 10 -> ':'
        assertEquals("?", NMEAArmouring.encode(BitString.ofBitString("001111"))); // 15 -> '?'
        assertEquals("@", NMEAArmouring.encode(BitString.ofBitString("010000"))); // 16 -> '@'
        assertEquals("W", NMEAArmouring.encode(BitString.ofBitString("100111"))); // 39 -> 'W'
        assertEquals("`", NMEAArmouring.encode(BitString.ofBitString("101000"))); // 40 -> '`'
        assertEquals("w", NMEAArmouring.encode(BitString.ofBitString("111111"))); // 63 -> 'w'
    }

    @Test
    public void encode_rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> NMEAArmouring.encode(null));
    }

    // -- round trip ----------------------------------------------------------

    @Test
    public void roundTripAligned() {
        BitString original = BitString.ofBitString("000001010001100111111110");
        assertEquals(original, NMEAArmouring.decode(NMEAArmouring.encode(original), 0));
    }

    @Test
    public void roundTripUnalignedWithPadding() {
        BitString original = BitString.ofBitString("1011010011001"); // 13 bits
        int paddingBits = (6 - original.length() % 6) % 6;
        String encoded = NMEAArmouring.encode(original);
        assertEquals(original, NMEAArmouring.decode(encoded, paddingBits));
    }

    @Test
    public void roundTripFullAisLikePayload() {
        // 168-bit payload (multiple of 6)
        StringBuilder sb = new StringBuilder(168);
        for (int i = 0; i < 28; i++) {
            String chunk = Integer.toBinaryString(i + 1);
            sb.append("0".repeat(6 - chunk.length())).append(chunk);
        }
        BitString original = BitString.ofBitString(sb.toString());
        String encoded = NMEAArmouring.encode(original);
        assertEquals(28, encoded.length());
        assertEquals(original, NMEAArmouring.decode(encoded, 0));
    }
}
