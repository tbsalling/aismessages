/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 * **
 */

package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.nmea.NMEAMessageHandler;
import dk.tbsalling.aismessages.nmea.NMEAMessageInputStreamReader;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * AISMessageInputStreamReader is the main entry point into the program loop
 * of an AISMessages application.
 *
 * Feed it with a stream of NMEA messages, and get your AISMessage consumer called
 * with decoded messages, whenever they are extracted from the NMEA stream.
 *
 * @see dk.tbsalling.aismessages.demo.SimpleDemoApp
 */
public class AISInputStreamReader {

    public AISInputStreamReader(List<String> stringQueue, Consumer<? super AISMessage> aisMessageConsumer) {
        this.nmeaMessageHandler = new NMEAMessageHandler("SRC", aisMessageConsumer);
        this.nmeaMessageInputStreamReader = new NMEAMessageInputStreamReader(stringQueue, this.nmeaMessageHandler::accept);
    }

    public AISInputStreamReader(InputStream inputStream, Consumer<? super AISMessage> aisMessageConsumer) {
        this.nmeaMessageHandler = new NMEAMessageHandler("SRC", aisMessageConsumer);
        this.nmeaMessageInputStreamReader = new NMEAMessageInputStreamReader(inputStream, this.nmeaMessageHandler::accept);
    }

    public AISInputStreamReader(InputStream inputStream, Function<String, String> nmeaStringPreProcessor, Consumer<? super AISMessage> aisMessageConsumer) {
        this.nmeaMessageHandler = new NMEAMessageHandler("SRC", aisMessageConsumer);
        this.nmeaMessageInputStreamReader = new NMEAMessageInputStreamReader(inputStream, nmeaStringPreProcessor, this.nmeaMessageHandler::accept);
    }

    public final void requestStop() {
        this.nmeaMessageInputStreamReader.requestStop();
    }

    public final boolean isStopRequested() {
        return this.nmeaMessageInputStreamReader.isStopRequested();
    }

    public void run() {
        this.nmeaMessageInputStreamReader.run();
    }

    private final NMEAMessageHandler nmeaMessageHandler;
	private final NMEAMessageInputStreamReader nmeaMessageInputStreamReader;
}
