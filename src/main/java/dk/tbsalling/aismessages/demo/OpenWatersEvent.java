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
package dk.tbsalling.aismessages.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal, dependency-free reader for the JSON frames of the Open Waters AIS stream
 * ({@code wss://ais.openwaters.io/v1/stream}). Only the parts the demo needs are read: the frame {@code type} and the
 * {@code nmea} array of raw sentences. Not a general JSON parser.
 */
final class OpenWatersEvent {

    private OpenWatersEvent() {
    }

    /**
     * @param json one complete JSON text frame.
     * @return the value of the top-level {@code "type"} field, or null when there is none.
     */
    static String type(String json) {
        List<String> values = stringsAfter(json, "\"type\"", false);
        return values.isEmpty() ? null : values.get(0);
    }

    /**
     * @param json one complete JSON text frame.
     * @return the raw NMEA sentences of an {@code event} frame, in order; empty for other frames or when absent.
     */
    static List<String> nmeaSentences(String json) {
        if (!"event".equals(type(json))) {
            return List.of();
        }
        return stringsAfter(json, "\"nmea\"", true);
    }

    /** Reads the string, or array of strings, that follows {@code key} and its colon. */
    private static List<String> stringsAfter(String json, String key, boolean array) {
        int i = json.indexOf(key);
        if (i < 0) {
            return List.of();
        }
        i = skipWhitespace(json, i + key.length());
        if (i >= json.length() || json.charAt(i) != ':') {
            return List.of();
        }
        i = skipWhitespace(json, i + 1);
        List<String> values = new ArrayList<>();
        if (array) {
            if (i >= json.length() || json.charAt(i) != '[') {
                return List.of();
            }
            i = skipWhitespace(json, i + 1);
            while (i < json.length() && json.charAt(i) == '"') {
                StringBuilder value = new StringBuilder();
                i = readString(json, i, value);
                values.add(value.toString());
                i = skipWhitespace(json, i);
                if (i < json.length() && json.charAt(i) == ',') {
                    i = skipWhitespace(json, i + 1);
                }
            }
        } else if (i < json.length() && json.charAt(i) == '"') {
            StringBuilder value = new StringBuilder();
            readString(json, i, value);
            values.add(value.toString());
        }
        return values;
    }

    /** Reads the JSON string starting at the opening quote at {@code start}; returns the index after its closing quote. */
    private static int readString(String json, int start, StringBuilder out) {
        int i = start + 1;
        while (i < json.length()) {
            char c = json.charAt(i++);
            if (c == '"') {
                return i;
            }
            if (c == '\\' && i < json.length()) {
                char e = json.charAt(i++);
                switch (e) {
                    case 'n' -> out.append('\n');
                    case 'r' -> out.append('\r');
                    case 't' -> out.append('\t');
                    case 'b' -> out.append('\b');
                    case 'f' -> out.append('\f');
                    case 'u' -> {
                        if (i + 4 <= json.length()) {
                            out.append((char) Integer.parseInt(json.substring(i, i + 4), 16));
                            i += 4;
                        }
                    }
                    default -> out.append(e); // \" \\ \/
                }
            } else {
                out.append(c);
            }
        }
        return i;
    }

    private static int skipWhitespace(String json, int i) {
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
            i++;
        }
        return i;
    }
}
