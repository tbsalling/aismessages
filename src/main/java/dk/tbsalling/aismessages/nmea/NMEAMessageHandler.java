/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011--3 by S-Consult ApS, DK31327490, http://tbsalling.dk, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

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

	private static final System.Logger LOG = System.getLogger(NMEAMessageHandler.class.getName());

	private final String source;
    private final List<NMEAMessage> messageFragments = new ArrayList<>();
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
        LOG.log(DEBUG, "Received for processing: %s".formatted(nmeaMessage.getRawMessage()));

		int numberOfFragments = nmeaMessage.getNumberOfFragments() == null ? -1 : nmeaMessage.getNumberOfFragments();
		if (numberOfFragments <= 0) {
            LOG.log(WARNING, "NMEA message is invalid: %s".formatted(nmeaMessage.toString()));
			messageFragments.clear();
		} else if (numberOfFragments == 1) {
			LOG.log(DEBUG, "Handling unfragmented NMEA message");
            AISMessage aisMessage = AISMessage.create(Instant.now(), source, nmeaMessage.getTagBlock(), nmeaMessage);
            sendToAisMessageReceivers(aisMessage);
			messageFragments.clear();
		} else {
			int fragmentNumber = nmeaMessage.getFragmentNumber();
            LOG.log(DEBUG, "Handling fragmented NMEA message with fragment number %d".formatted(fragmentNumber));
			if (fragmentNumber < 0) {
                LOG.log(WARNING, "Fragment number cannot be negative: %d: %s".formatted(fragmentNumber, nmeaMessage.getRawMessage()));
				messageFragments.clear();
			} else if (fragmentNumber > numberOfFragments) {
                LOG.log(DEBUG, "Fragment number %d higher than expected %d: %s".formatted(fragmentNumber, numberOfFragments, nmeaMessage.getRawMessage()));
				messageFragments.clear();
			} else {
				int expectedFragmentNumber = messageFragments.size() + 1;
                LOG.log(DEBUG, "Expected fragment number is: %d: %s".formatted(expectedFragmentNumber, nmeaMessage.getRawMessage()));

				if (expectedFragmentNumber != fragmentNumber) {
                    LOG.log(DEBUG, "Expected fragment number %d; not %d: %s".formatted(expectedFragmentNumber, fragmentNumber, nmeaMessage.getRawMessage()));
					messageFragments.clear();
				} else {
					messageFragments.add(nmeaMessage);
                    LOG.log(DEBUG, "nmeaMessage.getNumberOfFragments(): %d".formatted(nmeaMessage.getNumberOfFragments()));
                    LOG.log(DEBUG, "messageFragments.size(): %d".formatted(messageFragments.size()));
					if (nmeaMessage.getNumberOfFragments() == messageFragments.size()) {
                        AISMessage aisMessage = AISMessage.create(Instant.now(), source, nmeaMessage.getTagBlock(), messageFragments.toArray(new NMEAMessage[0]));
                        sendToAisMessageReceivers(aisMessage);
						messageFragments.clear();
					} else
						LOG.log(DEBUG, "Fragmented message not yet complete; missing " + (nmeaMessage.getNumberOfFragments() - messageFragments.size()) + " fragment(s).");
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
    public List<NMEAMessage> flush() {
        List<NMEAMessage> unhandled = List.copyOf(messageFragments);
		messageFragments.clear();
		return unhandled;
	}

}
