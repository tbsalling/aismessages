/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.nmea.NMEAMessageHandler;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import dk.tbsalling.aismessages.nmea.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Convenience API for decoding NMEA AIS sentences into typed AIS message objects.
 * <p>
 * This keeps the low-level reassembly and parsing pipeline intact while providing a more
 * consumer-friendly entry point for applications that simply want decoded AIS messages.
 */
@Log
public final class AISMessageDecoder {

    private AISMessageDecoder() {
    }

    /**
     * Decode a single NMEA sentence into a single AIS message.
     *
     * @param nmeaSentence a single NMEA sentence, such as {@code !AIVDM,...}
     * @return the decoded AIS message
     * @throws IllegalArgumentException when the sentence cannot be decoded into a message
     */
    public static AISMessage decode(String nmeaSentence) {
        Objects.requireNonNull(nmeaSentence, "nmeaSentence cannot be null.");
        List<AISMessage> messages = decode(List.of(nmeaSentence));
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("No AIS message could be decoded from the supplied NMEA sentence: %s".formatted(nmeaSentence));
        }
        return messages.getFirst();
    }

    /**
     * Decode a variable number of NMEA sentences into all successfully decoded AIS messages.
     * Invalid or unsupported lines are ignored.
     */
    public static List<AISMessage> decode(String... nmeaSentences) {
        Objects.requireNonNull(nmeaSentences, "nmeaSentences cannot be null.");
        return decode(List.of(nmeaSentences));
    }

    /**
     * Decode a list of NMEA sentences into all successfully decoded AIS messages.
     * Invalid or unsupported lines are ignored.
     */
    public static List<AISMessage> decode(List<String> nmeaSentences) {
        Objects.requireNonNull(nmeaSentences, "nmeaSentences cannot be null.");
        return decodeLenient(nmeaSentences);
    }

    /**
     * Decode all NMEA sentences from an input stream into the successfully decoded AIS messages.
     * Invalid or unsupported lines are ignored.
     */
    public static List<AISMessage> decode(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream cannot be null.");

        List<String> lines;
        try {
            lines = readLines(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read AIS NMEA stream.", e);
        }

        return decodeLenient(lines);
    }

    /**
     * Attempt to decode a single NMEA sentence into a single AIS message, without throwing.
     * <p>
     * This is the non-throwing counterpart of {@link #decode(String)}. The result is empty
     * whenever no AIS message could be decoded, for whatever reason: a malformed or unsupported
     * sentence, a {@code null} argument, or a lone fragment of a multi-fragment message. It
     * deliberately does not report <em>why</em> nothing was decoded; use {@link #decode(String)}
     * and catch if the reason matters.
     * <p>
     * Note that a bare {@code null} literal is ambiguous across the {@code tryDecode} overloads
     * and will not compile; cast it at the call site, as {@code tryDecode((String) null)}.
     * <p>
     * This method is thread-safe.
     *
     * @param nmeaSentence a single NMEA sentence, such as {@code !AIVDM,...}; may be {@code null}
     * @return the decoded AIS message, or empty if none could be decoded
     */
    public static Optional<AISMessage> tryDecode(String nmeaSentence) {
        if (nmeaSentence == null) {
            return Optional.empty();
        }
        List<AISMessage> messages = decodeLenient(List.of(nmeaSentence));
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.getFirst());
    }

    /**
     * Attempt to decode a variable number of NMEA sentences, without throwing.
     * Undecodable sentences and {@code null} elements are skipped. The result is empty if no AIS
     * message could be decoded at all; a present result always holds a non-empty, immutable list.
     */
    public static Optional<List<AISMessage>> tryDecode(String... nmeaSentences) {
        // Arrays.asList rather than List.of: List.of rejects null elements.
        return nmeaSentences == null ? Optional.empty() : tryDecode(Arrays.asList(nmeaSentences));
    }

    /**
     * Attempt to decode a list of NMEA sentences, without throwing.
     * Undecodable sentences and {@code null} elements are skipped. The result is empty if no AIS
     * message could be decoded at all; a present result always holds a non-empty, immutable list.
     * <p>
     * All fragments of a multi-fragment AIS message must be supplied to the same call, since
     * reassembly state does not survive across calls.
     */
    public static Optional<List<AISMessage>> tryDecode(List<String> nmeaSentences) {
        if (nmeaSentences == null) {
            return Optional.empty();
        }
        List<AISMessage> messages = decodeLenient(nmeaSentences);
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages);
    }

    /**
     * Attempt to decode all NMEA sentences from an input stream, without throwing.
     * The result is empty both when the stream cannot be read and when it holds no decodable AIS
     * message; use {@link #decode(InputStream)} if those two cases must be told apart.
     */
    public static Optional<List<AISMessage>> tryDecode(InputStream inputStream) {
        if (inputStream == null) {
            return Optional.empty();
        }
        try {
            return tryDecode(readLines(inputStream));
        } catch (IOException | RuntimeException e) {
            log.log(Level.FINE, "Could not read AIS NMEA stream.", e);
            return Optional.empty();
        }
    }

    /**
     * Decode leniently: null, blank, malformed and unsupported sentences are skipped.
     * Never throws.
     */
    private static List<AISMessage> decodeLenient(List<String> nmeaSentences) {
        List<AISMessage> messages = new ArrayList<>();
        NMEAMessageHandler handler = new NMEAMessageHandler("DECODER", messages::add);

        for (String nmeaSentence : nmeaSentences) {
            if (nmeaSentence == null || nmeaSentence.isBlank()) {
                continue;
            }

            try {
                handler.accept(new NMEAMessage(nmeaSentence));
            } catch (NMEAParseException | InvalidMessage | UnsupportedMessageType _) {
                // Expected: malformed or unsupported input. This is the advertised behaviour
                // of a lenient decoder, so it is not a warning.
                log.fine("Ignoring undecodable NMEA sentence: %s".formatted(nmeaSentence));
            } catch (RuntimeException e) {
                // Unexpected: NumberFormatException, InvalidTagBlock, IllegalArgumentException,
                // ArrayIndexOutOfBoundsException, ... Swallowed to honour the documented
                // contract, but logged with a stack trace: this usually means a truncated
                // payload or a decoder bug, and must not vanish silently.
                log.log(Level.WARNING, "Ignoring NMEA sentence that failed to decode unexpectedly: %s".formatted(nmeaSentence), e);
            }
        }

        return List.copyOf(messages);
    }

    private static List<String> readLines(InputStream inputStream) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
