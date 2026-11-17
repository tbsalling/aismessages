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

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.AidToNavigationReport;
import dk.tbsalling.aismessages.ais.messages.AidToNavigationReportSingleSlot;
import dk.tbsalling.aismessages.ais.messages.BaseStationReport;
import dk.tbsalling.aismessages.ais.messages.DynamicDataReport;
import dk.tbsalling.aismessages.ais.messages.ExtendedDynamicDataReport;
import dk.tbsalling.aismessages.ais.messages.PositionReport;
import dk.tbsalling.aismessages.ais.messages.ShipAndVoyageData;
import dk.tbsalling.aismessages.ais.messages.StaticDataReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Formats a decoded {@link AISMessage} as one human-readable line: MMSI, message type and the fields that matter for
 * that type, such as position, speed, course, heading, name and destination. Values that are not available
 * ({@code NaN} or {@code null}) are left out.
 */
final class MessageFormatter {

    private MessageFormatter() {
    }

    /**
     * @param message a decoded message.
     * @return one line describing the message.
     */
    static String format(AISMessage message) {
        List<String> parts = new ArrayList<>();
        parts.add(String.format(Locale.ROOT, "MMSI %09d", message.getSourceMmsi().getMmsi()));
        parts.add(String.format(Locale.ROOT, "%-50s", humanize(message.getMessageType().name())));

        switch (message) {
            case DynamicDataReport d -> addPosition(parts, d.getLatitude(), d.getLongitude());
            case BaseStationReport b -> addPosition(parts, b.getLatitude(), b.getLongitude());
            case AidToNavigationReport a -> addPosition(parts, a.getLatitude(), a.getLongitude());
            case AidToNavigationReportSingleSlot a -> addPosition(parts, a.getLatitude(), a.getLongitude());
            default -> { }
        }
        if (message instanceof DynamicDataReport d) {
            if (!Float.isNaN(d.getSpeedOverGround())) parts.add(String.format(Locale.ROOT, "%.1f kn", d.getSpeedOverGround()));
            if (!Float.isNaN(d.getCourseOverGround())) parts.add(String.format(Locale.ROOT, "course %.1f°", d.getCourseOverGround()));
        }
        if (message instanceof ExtendedDynamicDataReport e && e.getTrueHeading() != null) {
            parts.add("heading " + e.getTrueHeading() + "°");
        }
        if (message instanceof PositionReport p && p.getNavigationStatus() != null) {
            parts.add(humanize(p.getNavigationStatus().name()));
        }
        if (message instanceof StaticDataReport s) {
            addText(parts, s.getShipName());
            if (s.getCallsign() != null) parts.add("call sign " + s.getCallsign());
            if (s.getShipType() != null) parts.add(humanize(s.getShipType().name()));
            if (s.getLength() != null && s.getBeam() != null) parts.add(s.getLength() + " × " + s.getBeam() + " m");
        }
        if (message instanceof ShipAndVoyageData v) {
            if (v.getDestination() != null) parts.add("to " + v.getDestination());
            if (!Float.isNaN(v.getDraught())) parts.add(String.format(Locale.ROOT, "draught %.1f m", v.getDraught()));
        }
        switch (message) {
            case AidToNavigationReport a -> {
                addText(parts, a.getName());
                if (a.getAidType() != null) parts.add(humanize(a.getAidType().name()));
            }
            case AidToNavigationReportSingleSlot a -> {
                if (a.getAidType() != null) parts.add(humanize(a.getAidType().name()));
            }
            case BaseStationReport b when b.getYear() != null && b.getMonth() != null && b.getDay() != null && b.getHour() != null && b.getMinute() != null -> parts.add(String.format(Locale.ROOT, "%04d-%02d-%02d %02d:%02d UTC", b.getYear(), b.getMonth(), b.getDay(), b.getHour(), b.getMinute()));
            default -> { }
        }
        return String.join("  ", parts).stripTrailing();
    }

    private static void addPosition(List<String> parts, double latitude, double longitude) {
        if (!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
            parts.add(String.format(Locale.ROOT, "%.5f°%s %.5f°%s", Math.abs(latitude), latitude >= 0 ? "N" : "S", Math.abs(longitude), longitude >= 0 ? "E" : "W"));
        }
    }

    private static void addText(List<String> parts, String text) {
        if (text != null) parts.add("\"" + text + "\"");
    }

    /**
     * @param constantName an enum constant name such as {@code PositionReportClassAScheduled}.
     * @return readable words, such as {@code Position report class A scheduled}; acronyms keep their case.
     */
    static String humanize(String constantName) {
        String[] words = constantName.replace('_', ' ').split("(?<=[a-z0-9])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            boolean acronym = word.length() > 1 && word.equals(word.toUpperCase(Locale.ROOT));
            String w = acronym || word.length() == 1 ? word : word.toLowerCase(Locale.ROOT);
            sb.append(sb.isEmpty() ? Character.toUpperCase(w.charAt(0)) + w.substring(1) : " " + w);
        }
        return sb.toString();
    }
}
