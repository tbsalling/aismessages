package dk.tbsalling.aismessages.nmea.tagblock;

import dk.tbsalling.aismessages.ais.messages.types.TAGBlockParameterCodeType;
import lombok.Value;

@Value
public class NMEATagBlockParameterCode {

    TAGBlockParameterCodeType code;
    String value;

}

