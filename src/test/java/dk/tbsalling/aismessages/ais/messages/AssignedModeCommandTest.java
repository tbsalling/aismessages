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
        NMEAMessage nmeaMessage = NMEAMessage.fromString("!AIVDM,1,1,,A,@6TMCD1GOSmUBKh4,0*29");

        // Act
        AISMessage aisMessage = AISMessage.create(nmeaMessage);

        System.out.println(aisMessage.toString());

        // Assert
        assertEquals(AISMessageType.AssignedModeCommand, aisMessage.getMessageType());
        AssignedModeCommand message = (AssignedModeCommand) aisMessage;
        assertEquals(Integer.valueOf(0), message.getRepeatIndicator());
        assertEquals(MMSI.valueOf(440882000), message.getSourceMmsi());
        assertEquals(MMSI.valueOf(366972761), message.getDestinationMmsiA());
        assertEquals(Integer.valueOf(1318), message.getOffsetA());
        assertEquals(Integer.valueOf(960), message.getIncrementA());
        assertNull(message.getDestinationMmsiB());
        assertNull(message.getOffsetB());
        assertNull(message.getIncrementB());
    }
}
