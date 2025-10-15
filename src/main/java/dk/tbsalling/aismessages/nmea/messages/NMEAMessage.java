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

package dk.tbsalling.aismessages.nmea.messages;

import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import dk.tbsalling.aismessages.nmea.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMEAMessage {


    public final boolean isValid() {
        // messageType is parsed eagerly in constructor
        if (messageType == null || messageType.length() != 5) return false;
        String type = messageType.substring(2);
        return ("VDM".equals(type) || "VDO".equals(type));
    }

    @SuppressWarnings("unused")
    public String getMessageType() {
        return messageType;
    }

    @SuppressWarnings("unused")
    public Integer getNumberOfFragments() {
        return numberOfFragments;
    }

    @SuppressWarnings("unused")
    public Integer getFragmentNumber() {
        return fragmentNumber;
    }

    @SuppressWarnings("unused")
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    @SuppressWarnings("unused")
    public String getRadioChannelCode() {
        return radioChannelCode;
    }

    @SuppressWarnings("unused")
    public String getEncodedPayload() {
        return encodedPayload;
    }

    @SuppressWarnings("unused")
    public Integer getFillBits() {
        return fillBits;
    }

    @SuppressWarnings("unused")
    public Integer getChecksum() {
        return checksum;
    }

    @SuppressWarnings("unused")
    public String getRawMessage() {
        return rawMessage;
    }

    public NMEATagBlock getTagBlock() {
        return tagBlock;
    }

    public NMEAMessage(String input) {
        // Parse tag block first and strip it from the working string
        final String nmeaTagBlockRegEx = "^\\\\.*\\*[0-9A-Fa-f]{2}\\\\";
        String working = input;
        NMEATagBlock parsedTagBlock = null;
        Pattern tbPattern = Pattern.compile(nmeaTagBlockRegEx);
        Matcher tbMatcher = tbPattern.matcher(working);
        if (tbMatcher.lookingAt()) {
            String nmeaTagBlockString = working.substring(tbMatcher.start(), tbMatcher.end());
            parsedTagBlock = NMEATagBlock.fromString(nmeaTagBlockString);
            working = working.substring(tbMatcher.end());
        }
        this.tagBlock = parsedTagBlock; // can be null

        // Store the raw NMEA sentence (without tag block if it existed)
        this.rawMessage = working;

        // Basic structural validations before parsing fields
        final String nmeaMessageRegExp = "^!.*\\*[0-9A-Fa-f]{2}$";
        if (!rawMessage.matches(nmeaMessageRegExp))
            throw new NMEAParseException(rawMessage, "Message does not comply with regexp \"" + nmeaMessageRegExp + "\"");

        String[] fields = rawMessage.split(",", -1); // keep empty strings
        if (fields.length != 7)
            throw new NMEAParseException(rawMessage, "Expected 7 fields separated by commas; got " + fields.length);

        String[] lastFieldParts = fields[6].split("\\*", -1);
        if (lastFieldParts.length != 2)
            throw new NMEAParseException(rawMessage, "Expected checksum fields to start with *");

        // Eagerly parse and assign all fields (allowing blanks as null where applicable)
        this.messageType = (isBlank(fields[0]) ? null : fields[0].replace("!", ""));
        this.numberOfFragments = (isBlank(fields[1]) ? null : Integer.valueOf(fields[1]));
        this.fragmentNumber = (isBlank(fields[2]) ? null : Integer.valueOf(fields[2]));
        this.sequenceNumber = (isBlank(fields[3]) ? null : Integer.valueOf(fields[3]));
        this.radioChannelCode = (isBlank(fields[4]) ? null : fields[4]);
        this.encodedPayload = (isBlank(fields[5]) ? null : fields[5]);
        this.fillBits = (isBlank(lastFieldParts[0]) ? null : Integer.valueOf(lastFieldParts[0]));
        this.checksum = (isBlank(lastFieldParts[1]) ? null : Integer.valueOf(lastFieldParts[1], 16));

        // Validate supported message type
        if (!isValid()) {
            throw new UnsupportedMessageType(this.messageType);
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    @Override
    public String toString() {
        return "NMEAMessage{" +
                "rawMessage='" + rawMessage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NMEAMessage that = (NMEAMessage) o;
        return rawMessage.equals(that.rawMessage);
    }

    @Override
    public int hashCode() {
        return rawMessage.hashCode();
    }

    private final String rawMessage;
    private final NMEATagBlock tagBlock;
    private final String messageType;
    private final Integer numberOfFragments;
    private final Integer fragmentNumber;
    private final Integer sequenceNumber;
    private final String radioChannelCode;
    private final String encodedPayload;
    private final Integer fillBits;
    private final Integer checksum;
}
