package dk.tbsalling.aismessages.ais.messages;

/**
 * Marker interface for AIS messages with decoded values.
 * Previously used for lazy decoding with caching, now messages eagerly decode all fields
 * for immutability. Kept for backward compatibility and future extensibility.
 */
public interface CachedDecodedValues {
}
