/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013-3 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import dk.tbsalling.aismessages.decoder.Decoder;
import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.messages.DecodedAISMessage;
import dk.tbsalling.aismessages.messages.EncodedAISMessage;
import dk.tbsalling.aismessages.messages.Metadata;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

public class NMEAMessageReceiver {

    private static final Logger log = Logger.getLogger(NMEAMessageReceiver.class.getName());

    private final Decoder decoder = new DecoderImpl();
    private final DecodedAISMessageHandler aisMessageHandler;
    private final String source;
    private final ArrayList<NMEAMessage> messageFragments = new ArrayList<NMEAMessage>();
    
    public NMEAMessageReceiver(String source, DecodedAISMessageHandler aisMessageHandler) {
    	this.source = source;
    	this.aisMessageHandler = aisMessageHandler;
    }
    
    /**
     * Receive a single NMEA amoured AIS string
     * @param nmeaMessage the NMEAMessage to handle.
     */
	public void handleMessageReceived(NMEAMessage nmeaMessage) {
		Metadata metadata = new Metadata();
		metadata.setSource(source);
		metadata.setProcessedAt(new Date());
		long startTime = System.nanoTime();

		log.finer("Received for processing: " + nmeaMessage.getRawMessage());
		
		if (! nmeaMessage.isValid()) {
			log.warning("NMEA message is invalid: " + nmeaMessage.toString());
			return;
		}
		
//		if (! ("AIVDM".equals(nmeaMessage.getMessageType()) || ("AIVDO".equals(nmeaMessage.getMessageType())))) {
//			log.warning("NMEA messages of type " + nmeaMessage + " cannot be treated by " + this.getClass().getSimpleName());
//			return;
//		}
		
		if(!nmeaMessage.isValid()) {
			log.warning("NMEA messages of type " + nmeaMessage.getMessageType() + " cannot be treated by " + this.getClass().getSimpleName());
			return;
		}
		
		int numberOfFragments = nmeaMessage.getNumberOfFragments();
		if (numberOfFragments <= 0) {
			log.warning("NMEA message is invalid: " + nmeaMessage.toString());
			messageFragments.clear();
		} else if (numberOfFragments == 1) {
			log.finest("Handling unfragmented NMEA message");
			String payload = nmeaMessage.getEncodedPayload();
			int fillBits = nmeaMessage.getFillBits();
			EncodedAISMessage encodedAISMessage = new EncodedAISMessage(payload, fillBits);
			DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
			messageFragments.clear();
			metadata.setProcessedIn(Short.valueOf((short) ((System.nanoTime() - startTime)/1000)));
			decodedAISMessage.setMetadata(metadata);
			aisMessageHandler.handleMessageReceived(decodedAISMessage);
		} else {
			int fragmentNumber = nmeaMessage.getFragmentNumber();

			log.finest("Handling fragmented NMEA message with fragment number " + fragmentNumber);

			if (fragmentNumber < 0) {
				log.warning("Fragment number cannot be negative: " + fragmentNumber + ": " + nmeaMessage.getRawMessage());
				messageFragments.clear();
			} else if (fragmentNumber > numberOfFragments) {
				log.fine("Fragment number " + fragmentNumber + " higher than expected " + numberOfFragments + ": " + nmeaMessage.getRawMessage());
				messageFragments.clear();
			} else {
				int expectedFragmentNumber = messageFragments.size() + 1;
				log.finest("Expected fragment number is: " + expectedFragmentNumber + ": " + nmeaMessage.getRawMessage());
				
				if (expectedFragmentNumber != fragmentNumber) {
					log.fine("Expected fragment number " + expectedFragmentNumber + "; not " + fragmentNumber + ": " + nmeaMessage.getRawMessage());
					messageFragments.clear();
				} else {
					messageFragments.add(nmeaMessage);
					log.finest("nmeaMessage.getNumberOfFragments(): " + nmeaMessage.getNumberOfFragments());
					log.finest("messageFragments.size(): " + messageFragments.size());
					if (nmeaMessage.getNumberOfFragments() == messageFragments.size()) {
						int fillBits = -1;
						StringBuffer payload = new StringBuffer();
						Iterator<NMEAMessage> iterator = messageFragments.iterator();
						while (iterator.hasNext()) {
							NMEAMessage m = iterator.next();
							payload.append(m.getEncodedPayload());
							if (! iterator.hasNext()) {
								fillBits = m.getFillBits();
							}
						}
						EncodedAISMessage encodedAISMessage = new EncodedAISMessage(payload.toString(), fillBits);
						DecodedAISMessage decodedAISMessage = decoder.decode(encodedAISMessage);
						messageFragments.clear();
						decodedAISMessage.setMetadata(metadata);
						aisMessageHandler.handleMessageReceived(decodedAISMessage);
					} else
						log.finest("Fragmented message not yet complete; missing " + (nmeaMessage.getNumberOfFragments() - messageFragments.size()) + " fragment(s).");
				}
			}
		}
	}

	/**
	 * Empty buffer of unhandled messages and return those not handled.
     * @return List of unhandled NMEAMessages.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<NMEAMessage> flush() {
		ArrayList<NMEAMessage> unhandled = (ArrayList<NMEAMessage>) messageFragments.clone();
		messageFragments.clear();
		return unhandled;
	}
}
