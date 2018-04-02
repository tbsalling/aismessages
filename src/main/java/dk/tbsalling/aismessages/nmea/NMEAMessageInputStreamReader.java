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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.*;

public class NMEAMessageInputStreamReader {

	private static final System.Logger LOG = System.getLogger(NMEAMessageInputStreamReader.class.getName());

	public NMEAMessageInputStreamReader(InputStream inputStream, Consumer<? super NMEAMessage> nmeaMessageHandler) {
		this.nmeaMessageHandler = nmeaMessageHandler;
		this.inputStream = inputStream;
	}

	public final void requestStop() {
		this.stopRequested.set(true);
	}

	public void run() throws IOException {
	    LOG.log(INFO, "NMEAMessageInputStreamReader running.");

		InputStreamReader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
		BufferedReader bufferedReader = new BufferedReader(reader);
		String string;
		while ((string = bufferedReader.readLine()) != null && !isStopRequested()) {
			try {
				NMEAMessage nmea = NMEAMessage.fromString(string);
				nmeaMessageHandler.accept(nmea);
				LOG.log(DEBUG, "Received: " + nmea.toString());
			} catch (InvalidMessage invalidMessageException) {
				LOG.log(WARNING, "Received invalid AIS message: \"" + string + "\"");
			} catch (UnsupportedMessageType unsupportedMessageTypeException) {
				LOG.log(WARNING, "Received unsupported NMEA message: \"" + string + "\"");
			} catch (NMEAParseException parseException) {
				LOG.log(WARNING, "Received non-compliant NMEA message: \"" + string + "\"");
			}
		}

		LOG.log(INFO, "NMEAMessageInputStreamReader stopping.");
	}

	public final Boolean isStopRequested() {
		return this.stopRequested.get();
	}

	private final AtomicBoolean stopRequested = new AtomicBoolean(false);
	private final InputStream inputStream;
	private final Consumer<? super NMEAMessage> nmeaMessageHandler;
}
