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

package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.Metadata;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * This class receives NMEA messages containing armoured and encoded AIS strings.
 * An AIS message can span several NMEA messages. Whenever a complete AIS message
 * is observed in the input, an AISMessage is constructed and passed on to all registered
 * receivers of encoded AIS messages.
 *
 * @author tbsalling
 *
 */
public class NMEAMessageHandler implements Consumer<NMEAMessage> {

    private static final Logger LOG = Logger.getLogger(NMEAMessageHandler.class.getName());

    private final String source;
    private final ArrayList<NMEAMessage> messageFragments = new ArrayList<>();
    private final List<Consumer<? super AISMessage>> aisMessageReceivers = new LinkedList<>();

    public NMEAMessageHandler(String source, Consumer<? super AISMessage>... aisMessageReceivers) {
    	this.source = source;
        for (Consumer<? super AISMessage> aisMessageReceiver : aisMessageReceivers) {
            addAisMessageReceiver(aisMessageReceiver);
        }
    }
    
    /**
     * Receive a single NMEA amoured AIS string
     * @param nmeaMessage the NMEAMessage to handle.
     */
    @Override
    public void accept(NMEAMessage nmeaMessage) {
		LOG.finer("Received for processing: " + nmeaMessage.getRawMessage());
		
		if (! nmeaMessage.isValid()) {
			LOG.warning("NMEA message is invalid: " + nmeaMessage.toString());
			return;
		}
		
		int numberOfFragments = nmeaMessage.getNumberOfFragments();
		if (numberOfFragments <= 0) {
			LOG.warning("NMEA message is invalid: " + nmeaMessage.toString());
			messageFragments.clear();
		} else if (numberOfFragments == 1) {
			LOG.finest("Handling unfragmented NMEA message");
            AISMessage aisMessage = AISMessage.create(new Metadata(source), nmeaMessage);
            sendToAisMessageReceivers(aisMessage);
			messageFragments.clear();
		} else {
			int fragmentNumber = nmeaMessage.getFragmentNumber();
			LOG.finest("Handling fragmented NMEA message with fragment number " + fragmentNumber);
			if (fragmentNumber < 0) {
				LOG.warning("Fragment number cannot be negative: " + fragmentNumber + ": " + nmeaMessage.getRawMessage());
				messageFragments.clear();
			} else if (fragmentNumber > numberOfFragments) {
				LOG.fine("Fragment number " + fragmentNumber + " higher than expected " + numberOfFragments + ": " + nmeaMessage.getRawMessage());
				messageFragments.clear();
			} else {
				int expectedFragmentNumber = messageFragments.size() + 1;
				LOG.finest("Expected fragment number is: " + expectedFragmentNumber + ": " + nmeaMessage.getRawMessage());
				
				if (expectedFragmentNumber != fragmentNumber) {
					LOG.fine("Expected fragment number " + expectedFragmentNumber + "; not " + fragmentNumber + ": " + nmeaMessage.getRawMessage());
					messageFragments.clear();
				} else {
					messageFragments.add(nmeaMessage);
					LOG.finest("nmeaMessage.getNumberOfFragments(): " + nmeaMessage.getNumberOfFragments());
					LOG.finest("messageFragments.size(): " + messageFragments.size());
					if (nmeaMessage.getNumberOfFragments() == messageFragments.size()) {
                        AISMessage aisMessage = AISMessage.create(new Metadata(source), messageFragments.toArray(new NMEAMessage[messageFragments.size()]));
                        sendToAisMessageReceivers(aisMessage);
						messageFragments.clear();
					} else
						LOG.finest("Fragmented message not yet complete; missing " + (nmeaMessage.getNumberOfFragments() - messageFragments.size()) + " fragment(s).");
				}
			}
		}
	}

    /** Send encoded AIS message to all interested receivers. */
    private void sendToAisMessageReceivers(final AISMessage aisMessage) {
        aisMessageReceivers.forEach(r -> r.accept(aisMessage));
    }

    /**
     * Add a consumer of encoded AIS messages.
     * @param aisMessageReceiver The consumer to add.
     */
    @SuppressWarnings("unused")
    public void addAisMessageReceiver(Consumer<? super AISMessage> aisMessageReceiver) {
        aisMessageReceivers.add(aisMessageReceiver);
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
