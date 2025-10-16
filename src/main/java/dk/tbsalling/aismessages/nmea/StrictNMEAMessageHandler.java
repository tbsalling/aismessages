/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, DK31327490, http://tbsalling.dk, Denmark.
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
import lombok.extern.java.Log;

import java.util.function.Consumer;

/**
 * A strict variant of NMEAMessageHandler that validates NMEA checksums.
 * Messages with invalid checksums are logged as errors and not processed.
 * This ensures only messages with valid checksums are decoded into AIS messages.
 *
 * @author tbsalling
 */
@Log
public class StrictNMEAMessageHandler extends NMEAMessageHandler {

    public StrictNMEAMessageHandler(String source, Consumer<? super AISMessage>... aisMessageReceivers) {
        super(source, aisMessageReceivers);
    }

    /**
     * Receive a single NMEA armoured AIS string (strict mode).
     * This method validates the checksum and only processes messages with valid checksums.
     * Messages with invalid checksums are logged as errors and not processed.
     * @param nmeaMessage the NMEAMessage to handle.
     */
    @Override
    public void accept(NMEAMessage nmeaMessage) {
        // Validate checksum once
        boolean checksumValid = nmeaMessage.isChecksumValid();
        if (!checksumValid) {
            log.severe("NMEA message has invalid checksum and will not be processed: %s".formatted(nmeaMessage.getRawMessage()));
            return;
        }
        // Process the message with valid checksum
        processMessage(nmeaMessage);
    }
}
