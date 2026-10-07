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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Convenience API for decoding NMEA AIS sentences into typed AIS message objects.
 * <p>
 * This keeps the low-level reassembly and parsing pipeline intact while providing a more
 * consumer-friendly entry point for applications that simply want decoded AIS messages.
 */
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

        List<AISMessage> messages = new ArrayList<>();
        NMEAMessageHandler handler = new NMEAMessageHandler("DECODER", messages::add);

        for (String nmeaSentence : nmeaSentences) {
            if (nmeaSentence == null || nmeaSentence.isBlank()) {
                continue;
            }

            try {
                handler.accept(new NMEAMessage(nmeaSentence));
            } catch (InvalidMessage | UnsupportedMessageType | NMEAParseException ignored) {
                // Consumer-oriented decoder ignores malformed or unsupported NMEA input
            }
        }

        return List.copyOf(messages);
    }

    /**
     * Decode all NMEA sentences from an input stream into the successfully decoded AIS messages.
     * Invalid or unsupported lines are ignored.
     */
    public static List<AISMessage> decode(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream cannot be null.");

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read AIS NMEA stream.", e);
        }

        return decode(lines);
    }
}
