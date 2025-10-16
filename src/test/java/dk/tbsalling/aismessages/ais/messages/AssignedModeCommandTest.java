package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AssignedModeCommandTest {

    @Test
    public void canDecode() {
        // Arrange
        NMEAMessage nmeaMessage = new NMEAMessage("!AIVDM,1,1,,A,@6TMCD1GOSmUBKh4,0*29");

        // Act
        AISMessage aisMessage = dk.tbsalling.aismessages.ais.messages.AISMessageFactory.create(null, null, null, nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.AssignedModeCommand, aisMessage.getMessageType());
        AssignedModeCommand message = (AssignedModeCommand) aisMessage;
        assertEquals(0, message.getRepeatIndicator());
        assertEquals(new MMSI(440882000), message.getSourceMmsi());
        assertEquals(new MMSI(366972761), message.getDestinationMmsiA());
        assertEquals(1318, message.getOffsetA());
        assertEquals(960, message.getIncrementA());
        assertNull(message.getDestinationMmsiB());
        assertNull(message.getOffsetB());
        assertNull(message.getIncrementB());
    }
}
