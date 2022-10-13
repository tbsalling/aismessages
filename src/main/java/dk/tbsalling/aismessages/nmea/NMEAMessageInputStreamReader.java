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
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.System.Logger.Level.*;

public class NMEAMessageInputStreamReader {

	private static final System.Logger LOG = System.getLogger(NMEAMessageInputStreamReader.class.getName());

	public NMEAMessageInputStreamReader(List<String> nmeaStrings, Function<String, String> nmeaStringPreProcessor, Consumer<? super NMEAMessage> nmeaMessageHandler) {
		Objects.requireNonNull(nmeaStrings, "nmeaStrings cannot be null.");
		Objects.requireNonNull(nmeaStringPreProcessor, "nmeaStringPreProcessor cannot be null.");
		Objects.requireNonNull(nmeaMessageHandler, "nmeaMessageHandler cannot be null.");

		if (nmeaStrings instanceof Queue)
			this.stringSupplier = () -> ((Queue<String>) nmeaStrings).poll();
		else {
			final Queue<String> nmeaStringsQueue = new LinkedList<>(nmeaStrings);
			this.stringSupplier = () -> nmeaStringsQueue.poll();
		}

		this.nmeaMessagePreProcessor = nmeaStringPreProcessor;
		this.nmeaMessageHandler = nmeaMessageHandler;
	}

	public NMEAMessageInputStreamReader(List<String> nmeaStrings, Consumer<? super NMEAMessage> nmeaMessageHandler) {
		this(nmeaStrings, Function.identity(), nmeaMessageHandler);
	}

	public NMEAMessageInputStreamReader(InputStream inputStream, Function<String, String> nmeaStringPreProcessor, Consumer<? super NMEAMessage> nmeaMessageHandler) {
		this.nmeaMessageHandler = nmeaMessageHandler;
		this.nmeaMessagePreProcessor = nmeaStringPreProcessor;

		InputStreamReader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
		BufferedReader bufferedReader = new BufferedReader(reader);
		this.stringSupplier = () -> {
			try {
				return bufferedReader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public NMEAMessageInputStreamReader(InputStream inputStream, Consumer<? super NMEAMessage> nmeaMessageHandler) {
		this(inputStream, Function.identity(), nmeaMessageHandler);
	}

	public final void requestStop() {
		this.stopRequested.set(true);
	}

	public void run() {
		LOG.log(INFO, "NMEAMessageInputStreamReader running.");

		String string;
		while ((string = stringSupplier.get()) != null) {
			if (isStopRequested())
				break;

			string = nmeaMessagePreProcessor.apply(string);

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
	private final Supplier<String> stringSupplier;
	private final Function<String, String> nmeaMessagePreProcessor;
	private final Consumer<? super NMEAMessage> nmeaMessageHandler;
}
