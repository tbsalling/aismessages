package dk.tbsalling.aismessages.nmea.messages;

import dk.tbsalling.aismessages.messages.types.TAGBlockParameterType;

public class NMEATagBlockParameter {
	
	public static NMEATagBlockParameter fromString(TAGBlockParameterType code, String value) {
		return new NMEATagBlockParameter(code, value);
	}
	
	public final TAGBlockParameterType getCode() {
		return code;
	}

	public final String getValue() {
		return value;
	}
	
	private NMEATagBlockParameter() {
		this.code = null;
		this.value = null;
	}
	
	private NMEATagBlockParameter(TAGBlockParameterType code, String value) {	
		this.code = code;
		this.value = value;
	}
	
	private final TAGBlockParameterType code;
	private final String value;
}
