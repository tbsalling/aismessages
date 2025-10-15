package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.io.Serializable;
import java.time.Instant;

@SuppressWarnings("serial")
public record Metadata(
        NMEAMessage[] nmeaMessages,
        String bitString,
        NMEATagBlock nmeaTagBlock,
        String source,
        Instant received
) implements Serializable {

    // Convenience constructor for source only
    public Metadata(String source) {
        this(null, null, null, source, Instant.now());
    }

    // Convenience constructor for source and received
    public Metadata(String source, Instant received) {
        this(null, null, null, source, received);
    }

    // Backward-compatible getters with "get" prefix
    public NMEAMessage[] getNmeaMessages() {
        return nmeaMessages;
    }

    public String getBitString() {
        return bitString;
    }

    public NMEATagBlock getNmeaTagBlock() {
        return nmeaTagBlock;
    }

    public String getSource() {
        return source;
    }

    public Instant getReceived() {
        return received;
    }

    // Override equals() for backward compatibility (only compare source and received)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        if (source != null ? !source.equals(metadata.source) : metadata.source != null) return false;
        return received != null ? received.equals(metadata.received) : metadata.received == null;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (received != null ? received.hashCode() : 0);
        return result;
    }

    private static final String decoderVersion = AISMessage.VERSION;
    private static final String category = "AIS";
}
