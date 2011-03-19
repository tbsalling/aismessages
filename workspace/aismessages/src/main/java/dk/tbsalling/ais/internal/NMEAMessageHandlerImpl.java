/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.ais.internal;

import java.util.logging.Logger;

import dk.tbsalling.ais.AISMessageHandler;
import dk.tbsalling.ais.NMEAMessageHandler;
import dk.tbsalling.ais.messages.DecodedAISMessage;
import dk.tbsalling.ais.messages.EncodedAISMessage;
import dk.tbsalling.nmea.messages.NMEAMessage;

public class NMEAMessageHandlerImpl implements NMEAMessageHandler {

    private static final Logger log = Logger.getLogger(NMEAMessageHandlerImpl.class.getName());

    private final Decoder decoder = new DecoderImpl();
    private final AISMessageHandler aisMessageHandler;
    
    public NMEAMessageHandlerImpl(AISMessageHandler aisMessageHandler) {
    	this.aisMessageHandler = aisMessageHandler;
    }
    
	public void handleMessageReceived(NMEAMessage nmeaMessage) {
		if (! nmeaMessage.isValid()) {
			log.severe("NMEA message is invalid: " + nmeaMessage.toString());
			return;
		}
		
		if (! ("AIVDM".equals(nmeaMessage.getMessageType()) || ("AIVDO".equals(nmeaMessage.getMessageType())))) {
			log.severe("NMEA messages of type " + nmeaMessage + " cannot be treated by " + this.getClass().getSimpleName());
			return;
		}
		
		int numberOfFragments = nmeaMessage.getNumberOfFragments();
		if (numberOfFragments == 1) {
			String payload = nmeaMessage.getEncodedPayload();
			int fillBits = nmeaMessage.getFillBits();
			EncodedAISMessage encodedAISMessage = new EncodedAISMessage(payload, fillBits);
			DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
			aisMessageHandler.handleMessageReceived(decodedAISMessage);
		} else {
			log.warning("Multi-fragment messages not yet supported.");
		}
	}
}
