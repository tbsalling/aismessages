package dk.tbsalling.aismessages.ais;

import org.junit.Test;

import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;
import static org.junit.Assert.assertEquals;

public class DecodersTest {

    @Test
    public void canConvertToULong() {
        assertEquals(Long.valueOf(566517000), UNSIGNED_LONG_DECODER.apply("100001110001000101110100001000"));
        assertEquals(Long.valueOf(9577991), UNSIGNED_LONG_DECODER.apply("000000100100100010011000000111"));
    }

    @Test
    public void canConvertToUnsignedInteger() {
        // 1011100101011100011011001111 -> -123.450533333333

        assertEquals(Float.valueOf(0), FLOAT_DECODER.apply("0000000000000000000000000000"));
        assertEquals(Float.valueOf(1), FLOAT_DECODER.apply("0000000000000000000000000001"));
        assertEquals(Float.valueOf(0), FLOAT_DECODER.apply("1111111111111111111111111111"));
        assertEquals(-123.450533333333f, FLOAT_DECODER.apply("1011100101011100011011001111") / 600000f, 1e-16);// 74070320
        assertEquals(37.21113f,          FLOAT_DECODER.apply("001010101001010110110010100") / 600000f, 1e-16);
        // 181 degrees (0x6791AC0 hex)
        // Decoder.convertToUnsignedInteger(bitString)
        // 91 degrees (0x3412140 hex)
        // Course over ground will be 3600 (0xE10)
    }


}