package dk.tbsalling.aismessages.nmea.tagblock;

import lombok.Value;

/**
 * One parameter of an NMEA tag block: its parameter code and the value that follows it (IEC 61162-1 § 7.9).
 */
@Value
public class NMEATagBlockParameter {

    NMEATagBlockParameterCode code;
    String value;

}

