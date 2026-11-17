package dk.tbsalling.aismessages.nmea.tagblock;

/*
 * Parameter Code Dictionary
 * c - UNIX time / Positive Integer (Seconds/Milliseconds)
 * d - Destination-identification / Alphanumeric String (15 char. maximum)
 * g - Sentence Grouping / Numeric String (int-int-int)
 * n - Line Count / Positive Integer
 * r - Relative Time / Positive Integer
 * s - Source Identification / Alphanumeric String (15 char. maximum)
 * t - Text String / Valid Character String
 *
 */

import lombok.Getter;

/**
 * Parameter codes of an NMEA tag block (IEC 61162-1 § 7.9). Not part of ITU-R M.1371: tag blocks belong to the NMEA
 * transport layer and are parsed by {@code NMEATagBlock}.
 */
@Getter
public enum NMEATagBlockParameterCode {
    c("timestamp"),
    d("destinationId"),
    g("sentenceGrouping"),
    n("lineCount"),
    r("relativeTime"),
    s("sourceId"),
    t("textString");

    /** Descriptive name of the IEC 61162-1 tag block parameter code. */
    private final String name;

    NMEATagBlockParameterCode(String codeName) {
        name = codeName;
    }

    /**
     * Overall validation check on Tag Block, to see if any parameter codes are inside block based
     * on NMEA0183 Parameter Code Dictionary
     * @param codeName Parameter Code Name (ex. c,d,g,...)
     * @return boolean, true if it is included
     */
    public static boolean contains(String codeName) {
        for (NMEATagBlockParameterCode codeType : NMEATagBlockParameterCode.values()) {
            if (codeType.name().equals(codeName)) {
                return true;
            }
        }
        return false;
    }

}
