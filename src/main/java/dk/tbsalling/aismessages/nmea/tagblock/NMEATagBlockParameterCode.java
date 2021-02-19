package dk.tbsalling.aismessages.nmea.tagblock;

import dk.tbsalling.aismessages.ais.messages.types.TAGBlockParameterCodeType;

public class NMEATagBlockParameterCode {

    public static NMEATagBlockParameterCode fromString(TAGBlockParameterCodeType code, String value) {
        return new NMEATagBlockParameterCode(code, value);
    }

    public final TAGBlockParameterCodeType getCode() {
        return code;
    }

    public final String getValue() {
        return value;
    }

    private NMEATagBlockParameterCode(TAGBlockParameterCodeType code, String value) {
        this.code = code;
        this.value = value;
    }

    private final TAGBlockParameterCodeType code;
    private final String value;

}

