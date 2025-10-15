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
import lombok.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
public class NMEAMessage {

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

        // Eagerly parse and assign all fields (using sentinel -1 for missing numeric fields)
        this.messageType = (isBlank(fields[0]) ? null : fields[0].replace("!", ""));
        this.numberOfFragments = (isBlank(fields[1]) ? -1 : Integer.parseInt(fields[1]));
        this.fragmentNumber = (isBlank(fields[2]) ? -1 : Integer.parseInt(fields[2]));
        this.sequenceNumber = (isBlank(fields[3]) ? -1 : Integer.parseInt(fields[3]));
        this.radioChannelCode = (isBlank(fields[4]) ? null : fields[4]);
        this.encodedPayload = (isBlank(fields[5]) ? null : fields[5]);
        this.fillBits = (isBlank(lastFieldParts[0]) ? -1 : Integer.parseInt(lastFieldParts[0]));
        this.checksum = (isBlank(lastFieldParts[1]) ? -1 : Integer.parseInt(lastFieldParts[1], 16));

        // Validate supported message type
        if (!isValid())
            throw new UnsupportedMessageType(this.messageType);
    }

    private boolean isValid() {
        if (messageType == null || messageType.length() != 5) return false;
        String type = messageType.substring(2);
        return ("VDM".equals(type) || "VDO".equals(type));
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    String rawMessage;
    NMEATagBlock tagBlock;
    String messageType;
    int numberOfFragments;
    int fragmentNumber;
    int sequenceNumber;
    String radioChannelCode;
    String encodedPayload;
    int fillBits;
    int checksum;
}
