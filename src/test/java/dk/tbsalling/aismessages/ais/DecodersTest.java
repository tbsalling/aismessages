package dk.tbsalling.aismessages.ais;

import org.junit.jupiter.api.Test;

import static dk.tbsalling.aismessages.ais.BitStringParser.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.BitStringParser.UNSIGNED_LONG_DECODER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecodersTest {

    @Test
    public void canConvertToULong() {
        assertEquals(566517000L, UNSIGNED_LONG_DECODER.apply("100001110001000101110100001000").longValue());
        assertEquals(9577991L, UNSIGNED_LONG_DECODER.apply("000000100100100010011000000111").longValue());
    }

    @Test
    public void canConvertToUnsignedInteger() {
        // 1011100101011100011011001111 -> -123.450533333333

        assertEquals(0.0f, FLOAT_DECODER.apply("0000000000000000000000000000").floatValue(), 0.0f);
        assertEquals(1.0f, FLOAT_DECODER.apply("0000000000000000000000000001").floatValue(), 0.0f);
        assertEquals(-1.0f, FLOAT_DECODER.apply("1111111111111111111111111111").floatValue(), 0.0f);
        assertEquals(-123.450533333333f, FLOAT_DECODER.apply("1011100101011100011011001111") / 600000f, 1e-16);// 74070320
        assertEquals(37.21113f,          FLOAT_DECODER.apply("001010101001010110110010100") / 600000f, 1e-16);
        // 181 degrees (0x6791AC0 hex)
        // Decoder.convertToUnsignedInteger(bitString)
        // 91 degrees (0x3412140 hex)
        // Course over ground will be 3600 (0xE10)
    }


}