package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

public record Metadata(
        Instant received,
        NMEATagBlock nmeaTagBlock,
        NMEAMessage[] nmeaMessages,
        String decoderVersion,
        BitString bitString,
        String source
) {
}
