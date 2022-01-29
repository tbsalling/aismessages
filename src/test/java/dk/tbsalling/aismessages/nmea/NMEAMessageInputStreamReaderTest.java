package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class NMEAMessageInputStreamReaderTest {

    private static Consumer<NMEAMessage> nmeaMessageHandler;

    @BeforeAll
    public static void setUp() {
        nmeaMessageHandler = new NMEAMessageHandler("TEST");
    }

    @Test
    public void catchesInvalidMessageExceptionsInInputStream() throws IOException {
        String nmeaStream =
                "!AIVDM,1,1,,B,402=481uaUcf;OQ55JS9ITi025Jp,0*2B\n" +
                        "!AIVDM,1,1,,B,58LAM242B9POUKWWW<0a>0<4E<58,0*6E\n" +  // invalid
                        "!AIVDM,1,1,,A,33nr7t001f13KNTOahh2@QpF00vh,0*58\n";

        InputStream inputStream = new ByteArrayInputStream(nmeaStream.getBytes(StandardCharsets.UTF_8));

        new NMEAMessageInputStreamReader(inputStream, nmeaMessageHandler).run();
    }

    @Test
    public void catchesInvalidMessageExceptionsInList() throws IOException {
        List<String> nmeaQueue = new ArrayList<>(List.of(
                "!AIVDM,1,1,,B,402=481uaUcf;OQ55JS9ITi025Jp,0*2B",
                "!AIVDM,1,1,,B,58LAM242B9POUKWWW<0a>0<4E<58,0*6E",  // invalid
                "!AIVDM,1,1,,A,33nr7t001f13KNTOahh2@QpF00vh,0*58"
        ));

        new NMEAMessageInputStreamReader(nmeaQueue, nmeaMessageHandler).run();
    }

    @Test
    public void catchesInvalidMessageExceptionsInQueue() throws IOException {
        List<String> nmeaQueue = new LinkedList<>(List.of(
                "!AIVDM,1,1,,B,402=481uaUcf;OQ55JS9ITi025Jp,0*2B",
                "!AIVDM,1,1,,B,58LAM242B9POUKWWW<0a>0<4E<58,0*6E",  // invalid
                "!AIVDM,1,1,,A,33nr7t001f13KNTOahh2@QpF00vh,0*58"
        ));

        new NMEAMessageInputStreamReader(nmeaQueue, nmeaMessageHandler).run();
    }

}