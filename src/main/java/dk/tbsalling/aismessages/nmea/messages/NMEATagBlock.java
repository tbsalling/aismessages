package dk.tbsalling.aismessages.nmea.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.tbsalling.aismessages.exceptions.InvalidTagBlock;
import dk.tbsalling.aismessages.messages.types.TAGBlockParameterType;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;

public class NMEATagBlock {
	
	public static NMEATagBlock fromString(String nmeaTagBlockString) {
		return new NMEATagBlock(nmeaTagBlockString);
	}

	public final boolean isValid() {
		return valid;
	}

	public final Long getUnixTime() {
		return unixTime;
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

	public final Long getRrelativeTime() {
		return relativeTime;
	}
	
	public final String getSourceId() {
		return sourceId;
	}
	
	public final String getText() {
		return text;
	}
	
	public final Integer getChecksum() {
		return checksum;
	}
		
	public final String getRawMessage() {
		return rawMessage;
	}
	
	public final Map<TAGBlockParameterType, NMEATagBlockParameter> getParameterMap() {
		return Collections.unmodifiableMap(parameterMap);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		List<String> parameters = new ArrayList<String>();
		builder.append("\"tag\"").append(":").append("{");
		if (unixTime != null)
			parameters.add(String.format("%s:%s", "\"timestamp\"", unixTime));
		if (destinationId != null)
			parameters.add(String.format("%s:\"%s\"", "\"destination\"", destinationId));
		if (sentenceGrouping != null)
			parameters.add(String.format("%s:\"%s\"", "\"grouping\"", sentenceGrouping));
		if (lineCount != null)
			parameters.add(String.format("%s:%s", "\"line\"", lineCount));
		if (relativeTime != null)
			parameters.add(String.format("%s:%s", "\"relative\"", relativeTime));
		if (sourceId != null)
			parameters.add(String.format("%s:\"%s\"", "\"source\"", sourceId));
		if (text != null)
			parameters.add(String.format("%s:\"%s\"", "\"text\"", text));
		builder.append(String.join(",", parameters)).append("}");;
		return builder.toString();
	}
	
	private NMEATagBlock() {
		this.unixTime = null;
		this.destinationId = null;
		this.sentenceGrouping = null;
		this.lineCount = null;
		this.relativeTime = null;
		this.sourceId = null;
		this.text = null;
		this.checksum = null;
		this.rawMessage = null;
		this.valid = false;
		this.parameterMap = new HashMap<TAGBlockParameterType, NMEATagBlockParameter>();
	}
	
	private NMEATagBlock(String rawMessage) {		
		final String nmeaTagBlockRegEx = "^\\\\.*\\*[0-9A-Fa-f]{2}\\\\$";
		
		if (! rawMessage.matches(nmeaTagBlockRegEx))
			throw new NMEAParseException(rawMessage, "Message does not comply with regexp \"" + nmeaTagBlockRegEx + "\"");

		String rawMessageCleaned = rawMessage.substring(1, rawMessage.length() - 1);
		String[] msg = rawMessageCleaned.split("\\*");
		
		if (msg.length != 2)
			throw new NMEAParseException(rawMessage, "Expected 2 fields separated by asterix; got " + msg.length);
		
		String parameterString = msg[0];
		String checkSum = msg[1];
		
		String[] parameters = parameterString.split(",");
		parameterMap = new HashMap<TAGBlockParameterType, NMEATagBlockParameter>();
		for (String parameter : parameters) {
			String code[] = parameter.split(":");
			if (msg.length != 2)
				throw new NMEAParseException(rawMessage, "Expected 2 fields separated by colon; got " + msg.length);
			NMEATagBlockParameter nmeaTagBlockParameter = NMEATagBlockParameter.fromString(TAGBlockParameterType.valueOf(code[0]), code[1]);
			parameterMap.put(nmeaTagBlockParameter.getCode(), nmeaTagBlockParameter);
		}
		
		/*
		 * Parameter Code Dictionary
		 * c - UNIX time, c:positive integer 
		 * d - Destination-identification, d:alphanumeric string (15 char. maximum)
		 * g - Sentence-grouping, g:numeric string 
		 * n - Line-count, n:positive integer 
		 * r - Relative time, r:positive integer 
		 * s - Source-identification, s:alphanumeric string (15 char. maximum) 
		 * t - Text-string, t:valid character string
		 * 
		 */
		
		this.unixTime = parameterMap.containsKey(TAGBlockParameterType.c) ? Long.valueOf(parameterMap.get(TAGBlockParameterType.c).getValue()) : null;
		this.destinationId = parameterMap.containsKey(TAGBlockParameterType.d) ? parameterMap.get(TAGBlockParameterType.d).getValue() : null;
		this.sentenceGrouping = parameterMap.containsKey(TAGBlockParameterType.g) ? parameterMap.get(TAGBlockParameterType.g).getValue() : null;
		this.lineCount = parameterMap.containsKey(TAGBlockParameterType.n) ? Integer.valueOf(parameterMap.get(TAGBlockParameterType.n).getValue()) : null;
		this.relativeTime = parameterMap.containsKey(TAGBlockParameterType.r) ? Long.valueOf(parameterMap.get(TAGBlockParameterType.r).getValue()) : null;
		this.sourceId = parameterMap.containsKey(TAGBlockParameterType.s) ? parameterMap.get(TAGBlockParameterType.s).getValue() : null;
		this.text = parameterMap.containsKey(TAGBlockParameterType.t) ? parameterMap.get(TAGBlockParameterType.t).getValue() : null;
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
		
		if (checksum != this.checksum) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private final static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}		
		
	private final Long unixTime;
	private final String destinationId;
	private final String sentenceGrouping;
	private final Integer lineCount;
	private final Long relativeTime;
	private final String sourceId;
	private final String text;
	private final Integer checksum;
	private final String rawMessage;
	private final Boolean valid;
	private Map<TAGBlockParameterType, NMEATagBlockParameter> parameterMap;
}
