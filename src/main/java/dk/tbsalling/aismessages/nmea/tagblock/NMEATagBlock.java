package dk.tbsalling.aismessages.nmea.tagblock;

import dk.tbsalling.aismessages.nmea.exceptions.InvalidTagBlock;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class NMEATagBlock {

    Long timestamp;
    String destinationId;
    String sentenceGrouping;
    Integer lineCount;
    Long relativeTime;
    String sourceId;
    String text;
    Integer checksum;
    String rawMessage;
    Boolean valid;
    Map<NMEATagBlockParameterCode, NMEATagBlockParameter> parameterMap;

    public static NMEATagBlock fromString(String nmeaTagBlockString) {
        return new NMEATagBlock(nmeaTagBlockString);
    }

    private NMEATagBlock(String rawMessage) {
        final String nmeaTagBlockRegEx = "^\\\\.*\\*[0-9A-Fa-f]{2}\\\\$";

        if (!rawMessage.matches(nmeaTagBlockRegEx))
            throw new NMEAParseException(rawMessage, "Message does not comply with regexp \"%s\"".formatted(nmeaTagBlockRegEx));

        String rawMessageCleaned = rawMessage.substring(1, rawMessage.length() - 1);
        String[] msg = rawMessageCleaned.split("\\*");

        if (msg.length != 2)
            throw new NMEAParseException(rawMessage, "Checksum separator expected to be asterisk(*)");

        String parameterString = msg[0];
        String checkSum = msg[1];

        String[] parameters = parameterString.split(",");
        parameterMap = new HashMap<>();
        for (String parameter : parameters) {
            String[] code = parameter.split(":", 2);
            if (code.length != 2)
                throw new NMEAParseException(rawMessage, "Parameter code and its value has to be separated by colon(:)");
            if (NMEATagBlockParameterCode.contains(code[0])) {
                var tagBlockParameter = new NMEATagBlockParameter(NMEATagBlockParameterCode.valueOf(code[0]), code[1]);
                parameterMap.put(tagBlockParameter.getCode(), tagBlockParameter);
            }
        }

        this.timestamp = parameterMap.containsKey(NMEATagBlockParameterCode.c)
                ? parseLong(rawMessage, NMEATagBlockParameterCode.c) : null;
        this.destinationId = parameterMap.containsKey(NMEATagBlockParameterCode.d)
                ? parameterMap.get(NMEATagBlockParameterCode.d).getValue() : null;
        this.sentenceGrouping = parameterMap.containsKey(NMEATagBlockParameterCode.g)
                ? parameterMap.get(NMEATagBlockParameterCode.g).getValue() : null;
        this.lineCount = parameterMap.containsKey(NMEATagBlockParameterCode.n)
                ? parseInt(rawMessage, NMEATagBlockParameterCode.n) : null;
        this.relativeTime = parameterMap.containsKey(NMEATagBlockParameterCode.r)
                ? parseLong(rawMessage, NMEATagBlockParameterCode.r) : null;
        this.sourceId = parameterMap.containsKey(NMEATagBlockParameterCode.s)
                ? parameterMap.get(NMEATagBlockParameterCode.s).getValue() : null;
        this.text = parameterMap.containsKey(NMEATagBlockParameterCode.t)
                ? parameterMap.get(NMEATagBlockParameterCode.t).getValue() : null;

        this.checksum = (checkSum == null || checkSum.isBlank()) ? null : Integer.valueOf(checkSum, 16);
        this.rawMessage = rawMessage;
        this.valid = validate(parameterString);
        if (!valid)
            throw new InvalidTagBlock(rawMessage);
    }

    /** @return the numeric value of the given parameter; a non-numeric value is an {@link NMEAParseException} */
    private Long parseLong(String rawMessage, NMEATagBlockParameterCode code) {
        String value = parameterMap.get(code).getValue();
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NMEAParseException(rawMessage, "Parameter \"%s\" (%s) is not a number: \"%s\"".formatted(code.name(), code.getName(), value));
        }
    }

    /** @return the numeric value of the given parameter; a non-numeric value, or one outside the int range, is an {@link NMEAParseException} */
    private Integer parseInt(String rawMessage, NMEATagBlockParameterCode code) {
        String value = parameterMap.get(code).getValue();
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NMEAParseException(rawMessage, "Parameter \"%s\" (%s) is not a number in the int range: \"%s\"".formatted(code.name(), code.getName(), value));
        }
    }

    private Boolean validate(String checkString) {
        int checksum = 0;
        for (int i = 0; i < checkString.length(); i++) {
            checksum ^= (byte) (checkString.charAt(i));
        }

        return checksum == this.checksum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<String> parameters = new ArrayList<>();
        builder.append(" TagBlock").append("{");
        if (timestamp != null)
            parameters.add(String.format(" %s: %s", NMEATagBlockParameterCode.c.getName(), timestamp));
        if (destinationId != null)
            parameters.add(String.format(" %s: %s ", NMEATagBlockParameterCode.d.getName(), destinationId));
        if (sentenceGrouping != null)
            parameters.add(String.format(" %s: %s", NMEATagBlockParameterCode.g.getName(), sentenceGrouping));
        if (lineCount != null)
            parameters.add(String.format(" %s: %s", NMEATagBlockParameterCode.n.getName(), lineCount));
        if (relativeTime != null)
            parameters.add(String.format(" %s: %s", NMEATagBlockParameterCode.r.getName(), relativeTime));
        if (sourceId != null)
            parameters.add(String.format(" %s: %s", NMEATagBlockParameterCode.s.getName(), sourceId));
        if (text != null)
            parameters.add(String.format(" %s: %s", NMEATagBlockParameterCode.t.getName(), text));
        builder.append(String.join(",", parameters)).append(" }");
        return builder.toString();
    }
}
