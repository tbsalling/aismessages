package dk.tbsalling.aismessages.messages.types;

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

public enum TAGBlockParameterType {
    c ("time"),
    d ("destinationId"),
    g ("sentenceGrouping"),
    n ("time"),
    r ("destinationId"),
    s ("sentenceGrouping"),
    t ("text");
    
    private final String name;       

    private TAGBlockParameterType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String getName(){
       return name;
    }
}