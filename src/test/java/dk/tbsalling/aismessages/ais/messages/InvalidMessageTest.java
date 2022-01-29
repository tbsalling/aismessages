package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvalidMessageTest {

    @Test
    public void invalid() {
        assertThrows(InvalidMessage.class, () -> AISMessage.create(NMEAMessage.fromString("!AIVDM,1,1,,B,58LAM242B9POUKWWW<0a>0<4E<58,0*6E")));
    }

}
