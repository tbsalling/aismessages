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

package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import dk.tbsalling.aismessages.nmea.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class NMEAMessageInputStreamReader {

	private static final Logger log = Logger.getLogger(NMEAMessageInputStreamReader.class.getName());

	public NMEAMessageInputStreamReader(InputStream inputStream, Consumer<? super NMEAMessage> nmeaMessageHandler) {
		this.nmeaMessageHandler = nmeaMessageHandler;
		this.inputStream = inputStream;
	}

	public final synchronized void requestStop() {
		this.stopRequested = true;
	}

	public void run() throws IOException {
	    log.info("NMEAMessageInputStreamReader running.");

		InputStreamReader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
		BufferedReader bufferedReader = new BufferedReader(reader);
		String string;
		while ((string = bufferedReader.readLine()) != null && !stopRequested()) {
			try {
				NMEAMessage nmea = NMEAMessage.fromString(string);
				nmeaMessageHandler.accept(nmea);
				log.fine("Received: " + nmea.toString());
			} catch (InvalidMessage invalidMessageException) {
				log.warning("Received invalid AIS message: \"" + string + "\"");
			} catch (UnsupportedMessageType unsupportedMessageTypeException) {
				log.warning("Received unsupported NMEA message: \"" + string + "\"");
			} catch (NMEAParseException parseException) {
				log.warning("Received non-compliant NMEA message: \"" + string + "\"");
			}
		}

		log.info("NMEAMessageInputStreamReader stopping.");
	}

	private synchronized Boolean stopRequested() {
		return this.stopRequested;
	}

	private Boolean stopRequested = false;
	private final InputStream inputStream;
	private final Consumer<? super NMEAMessage> nmeaMessageHandler;
}
