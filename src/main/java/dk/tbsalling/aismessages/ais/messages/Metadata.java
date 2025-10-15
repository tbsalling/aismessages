package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

public record Metadata(
        Instant received,
        NMEATagBlock nmeaTagBlock,
        NMEAMessage[] nmeaMessages,
        String decoderVersion,
        String bitString,
        String source
) {
}
