/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class UTCAndDateInquiry extends AISMessage {

    public UTCAndDateInquiry(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected UTCAndDateInquiry(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuffer errorMessage = new StringBuffer();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits != 72)
            errorMessage.append(format("Message of type %s should be at exactly 72 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.UTCAndDateInquiry;
    }

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi() {
        return getDecodedValue(() -> destinationMmsi, value -> destinationMmsi = value, () -> Boolean.TRUE, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 70))));
	}

    @Override
    public String toString() {
        return "UTCAndDateInquiry{" +
                "messageType=" + getMessageType() +
                ", destinationMmsi=" + getDestinationMmsi() +
                "} " + super.toString();
    }

    private transient MMSI destinationMmsi;
}
