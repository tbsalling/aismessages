package dk.tbsalling.aismessages.ais.messages.types;

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
public enum TAGBlockParameterCodeType {
    c("timestamp"),
    d("destinationId"),
    g("sentenceGrouping"),
    n("lineCount"),
    r("relativeTime"),
    s("sourceId"),
    t("textString");

    private final String name;

    TAGBlockParameterCodeType(String codeName) {
        name = codeName;
    }

    /**
     * Overall validation check on Tag Block, to see if any parameter codes are inside block based
     * on NMEA0183 Parameter Code Dictionary
     * @param codeName Parameter Code Name (ex. c,d,g,...)
     * @return boolean, true if it is included
     */
    public static boolean contains(String codeName) {
        for (TAGBlockParameterCodeType codeType : TAGBlockParameterCodeType.values()) {
            if (codeType.name().equals(codeName)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }
}
