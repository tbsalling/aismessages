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
import dk.tbsalling.aismessages.ais.messages.AISMessageFactory;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import lombok.extern.java.Log;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class receives NMEA messages containing armoured and encoded AIS strings.
 * An AIS message can span several NMEA messages. Whenever a complete AIS message
 * is observed in the input, an AISMessage is constructed and passed on to all registered
 * receivers of encoded AIS messages.
 * <p>
 * Fragments are reassembled per sequential message identifier and radio channel (IEC 61162-1), so multi-sentence
 * messages that arrive interleaved with each other or with single-sentence messages are still decoded. Fragment 1
 * always starts a new message for its identifier and channel; a fragment out of sequence discards that message.
 *
 * @author tbsalling
 *
 */
@Log
public class NMEAMessageHandler implements Consumer<NMEAMessage> {

	private final String source;
    /** At most this many incomplete multi-sentence messages are buffered; the oldest is dropped beyond that. */
    private static final int MAX_PENDING_MESSAGES = 20;

    /** Incomplete multi-sentence messages, keyed by sequential message identifier and radio channel, oldest first. */
    private final Map<String, List<NMEAMessage>> pendingFragments = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, List<NMEAMessage>> eldest) {
            return size() > MAX_PENDING_MESSAGES;
        }
    };
    private final List<Consumer<? super AISMessage>> aisMessageReceivers = new LinkedList<>();

    public NMEAMessageHandler(String source, Consumer<? super AISMessage>... aisMessageReceivers) {
    	this.source = source;
        for (Consumer<? super AISMessage> aisMessageReceiver : aisMessageReceivers) {
            addAisMessageReceiver(aisMessageReceiver);
        }
    }

    /**
     * Receive a single NMEA armoured AIS string (lenient mode).
     * This method logs a warning if the checksum is invalid but continues processing.
     * @param nmeaMessage the NMEAMessage to handle.
     */
    @Override
    public void accept(NMEAMessage nmeaMessage) {
        // Validate checksum once and log warning if invalid, but continue processing
        boolean checksumValid = nmeaMessage.isChecksumValid();
        if (!checksumValid) {
            log.warning("NMEA message has invalid checksum: %s".formatted(nmeaMessage.getRawMessage()));
        }
        processMessage(nmeaMessage);
    }

    /**
     * Process a NMEA message.
     * This method can be overridden by subclasses to provide custom behavior.
     * @param nmeaMessage the NMEAMessage to process.
     */
    protected void processMessage(NMEAMessage nmeaMessage) {
        log.fine("Received for processing: %s".formatted(nmeaMessage.getRawMessage()));

        int numberOfFragments = nmeaMessage.getNumberOfFragments();
        if (numberOfFragments <= 0) {
            log.warning("NMEA message is invalid: %s".formatted(nmeaMessage.toString()));
        } else if (numberOfFragments == 1) {
            log.fine("Handling unfragmented NMEA message");
            AISMessage aisMessage = AISMessageFactory.create(Instant.now(), source, nmeaMessage.getTagBlock(), nmeaMessage);
            sendToAisMessageReceivers(aisMessage);
        } else {
            processFragment(nmeaMessage, numberOfFragments);
        }
    }

    private void processFragment(NMEAMessage nmeaMessage, int numberOfFragments) {
        String key = nmeaMessage.getSequenceNumber() + "|" + nmeaMessage.getRadioChannelCode();
        int fragmentNumber = nmeaMessage.getFragmentNumber();
        log.fine("Handling fragment %d of %d for message %s".formatted(fragmentNumber, numberOfFragments, key));

        if (fragmentNumber < 1 || fragmentNumber > numberOfFragments) {
            log.fine("Fragment number %d outside 1-%d: %s".formatted(fragmentNumber, numberOfFragments, nmeaMessage.getRawMessage()));
            pendingFragments.remove(key);
            return;
        }

        List<NMEAMessage> fragments;
        if (fragmentNumber == 1) {
            // Sequential message identifiers are reused cyclically: fragment 1 always starts a new message.
            fragments = new ArrayList<>(numberOfFragments);
            pendingFragments.remove(key);
            pendingFragments.put(key, fragments);
        } else {
            fragments = pendingFragments.get(key);
            boolean inSequence = fragments != null && fragments.size() + 1 == fragmentNumber && fragments.get(0).getNumberOfFragments() == numberOfFragments;
            if (!inSequence) {
                log.fine("Fragment %d of %d out of sequence: %s".formatted(fragmentNumber, numberOfFragments, nmeaMessage.getRawMessage()));
                pendingFragments.remove(key);
                return;
            }
        }

        fragments.add(nmeaMessage);
        if (fragments.size() == numberOfFragments) {
            // Remove before decoding, so that a fragment set which fails to decode cannot poison the next one.
            pendingFragments.remove(key);
            AISMessage aisMessage = AISMessageFactory.create(Instant.now(), source, nmeaMessage.getTagBlock(), fragments.toArray(new NMEAMessage[0]));
            sendToAisMessageReceivers(aisMessage);
        } else {
            log.fine("Fragmented message %s not yet complete; missing %d fragment(s).".formatted(key, numberOfFragments - fragments.size()));
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
    public List<NMEAMessage> flush() {
        List<NMEAMessage> unhandled = pendingFragments.values().stream().flatMap(List::stream).toList();
        pendingFragments.clear();
        return unhandled;
    }

}
