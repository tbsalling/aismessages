package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

/**
 * Metadata associated with a decoded AIS message.
 * Contains information about the message source, timing, and decoding context.
 *
 * @param received Timestamp when the message was received
 * @param nmeaTagBlock Optional NMEA tag block with additional metadata
 * @param nmeaMessages Array of NMEA messages that were assembled to form this AIS message
 * @param decoderVersion Version of the decoder that processed this message
 * @param bitString Binary representation of the AIS message payload
 * @param source Identifier of the message source
 */
public record Metadata(
        Instant received,
        NMEATagBlock nmeaTagBlock,
        NMEAMessage[] nmeaMessages,
        String decoderVersion,
        BitString bitString,
        String source
) {
}
