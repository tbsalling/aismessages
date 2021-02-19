package dk.tbsalling.aismessages.nmea.tagblock;

import dk.tbsalling.aismessages.ais.messages.types.TAGBlockParameterCodeType;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidTagBlock;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NMEATagBlock {
    private final Long timestamp;
    private final String destinationId;
    private final String sentenceGrouping;
    private final Integer lineCount;
    private final Long relativeTime;
    private final String sourceId;
    private final String text;
    private final Integer checksum;
    private final String rawMessage;
    private final Boolean valid;
    private Map<TAGBlockParameterCodeType, NMEATagBlockParameterCode> parameterMap;

    public final Long getTimestamp() {
        return timestamp;
    }

    public final String getDestinationId() {
        return destinationId;
    }

    public final String getSentenceGrouping() {
        return sentenceGrouping;
    }

    public final Integer getLineCount() {
        return lineCount;
    }

    public final Long getRelativeTime() {
        return relativeTime;
    }

    public final String getSourceId() {
        return sourceId;
    }

    public final String getText() {
        return text;
    }

    public final String getRawMessage() {
        return rawMessage;
    }

    public static NMEATagBlock fromString(String nmeaTagBlockString) {
        return new NMEATagBlock(nmeaTagBlockString);
    }

    private NMEATagBlock(String rawMessage) {
        final String nmeaTagBlockRegEx = "^\\\\.*\\*[0-9A-Fa-f]{2}\\\\$";

        if (! rawMessage.matches(nmeaTagBlockRegEx))
            throw new NMEAParseException(rawMessage, "Message does not comply with regexp \"" + nmeaTagBlockRegEx + "\"");

        String rawMessageCleaned = rawMessage.substring(1, rawMessage.length() - 1);
        String[] msg = rawMessageCleaned.split("\\*");

        if (msg.length != 2)
            throw new NMEAParseException(rawMessage, "Checksum separator expected to be asterisk(*)");

        String parameterString = msg[0];
        String checkSum = msg[1];

        String[] parameters = parameterString.split(",");
        parameterMap = new HashMap<>();
        for (String parameter : parameters) {
            String[] code = parameter.split(":");
            if (code.length != 2)
                throw new NMEAParseException(rawMessage, "Parameter code and its value has to be separator by colon(:)");
            if (TAGBlockParameterCodeType.contains(code[0])) {
                NMEATagBlockParameterCode nmeaTagBlockParameterCode = NMEATagBlockParameterCode.fromString(TAGBlockParameterCodeType.valueOf(code[0]), code[1]);
                parameterMap.put(nmeaTagBlockParameterCode.getCode() , nmeaTagBlockParameterCode);
            }
        }

        this.timestamp = parameterMap.containsKey(TAGBlockParameterCodeType.c)
                ? Long.valueOf(parameterMap.get(TAGBlockParameterCodeType.c).getValue()) : null;
        this.destinationId = parameterMap.containsKey(TAGBlockParameterCodeType.d)
                ? parameterMap.get(TAGBlockParameterCodeType.d).getValue() : null;
        this.sentenceGrouping = parameterMap.containsKey(TAGBlockParameterCodeType.g)
                ? parameterMap.get(TAGBlockParameterCodeType.g).getValue() : null;
        this.lineCount = parameterMap.containsKey(TAGBlockParameterCodeType.n)
                ? Integer.valueOf(parameterMap.get(TAGBlockParameterCodeType.n).getValue()) : null;
        this.relativeTime = parameterMap.containsKey(TAGBlockParameterCodeType.r)
                ? Long.valueOf(parameterMap.get(TAGBlockParameterCodeType.r).getValue()) : null;
        this.sourceId = parameterMap.containsKey(TAGBlockParameterCodeType.s)
                ? parameterMap.get(TAGBlockParameterCodeType.s).getValue() : null;
        this.text = parameterMap.containsKey(TAGBlockParameterCodeType.t)
                ? parameterMap.get(TAGBlockParameterCodeType.t).getValue() : null;

        this.checksum = isBlank(checkSum) ? null : Integer.valueOf(checkSum, 16);
        this.rawMessage = rawMessage;
        this.valid = validate(parameterString);
        if (!valid)
            throw new InvalidTagBlock(rawMessage);
    }

    private Boolean validate(String checkString) {
        int checksum = 0;
        for (int i = 0; i < checkString.length(); i++) {
            checksum ^= (byte)(checkString.charAt(i));
        }

        return checksum == this.checksum;
    }

    private final static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<String> parameters = new ArrayList<>();
        builder.append(" TagBlock").append("{");
        if (timestamp != null)
            parameters.add(String.format(" %s: %s", TAGBlockParameterCodeType.c.getName(), timestamp));
        if (destinationId != null)
            parameters.add(String.format(" %s: %s ", TAGBlockParameterCodeType.d.getName(), destinationId));
        if (sentenceGrouping != null)
            parameters.add(String.format(" %s: %s", TAGBlockParameterCodeType.g.getName(), sentenceGrouping));
        if (lineCount != null)
            parameters.add(String.format(" %s: %s", TAGBlockParameterCodeType.n.getName(), lineCount));
        if (relativeTime != null)
            parameters.add(String.format(" %s: %s", TAGBlockParameterCodeType.r.getName(), relativeTime));
        if (sourceId != null)
            parameters.add(String.format(" %s: %s", TAGBlockParameterCodeType.s.getName(), sourceId));
        if (text != null)
            parameters.add(String.format(" %s: %s", TAGBlockParameterCodeType.t.getName(), text));
        builder.append(String.join(",", parameters)).append(" }");;
        return builder.toString();
    }
}
