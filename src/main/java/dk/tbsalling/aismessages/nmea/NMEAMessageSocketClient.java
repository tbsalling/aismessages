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


import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.function.Consumer;

@Deprecated
@Log
public class NMEAMessageSocketClient {

	@SuppressWarnings("unused")
	private NMEAMessageSocketClient() {
		this.socketAddress = null;
		this.nmeaMessageConsumer = null;
	}

	public NMEAMessageSocketClient(String host, Integer port, Consumer<? super NMEAMessage> nmeaMessageConsumer) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(host);
		this.socketAddress = new InetSocketAddress(inetAddress, port);
		this.nmeaMessageConsumer = nmeaMessageConsumer;
	}

    @SuppressWarnings("unused")
	public void requestStop() {
		if (streamReader != null)
			streamReader.requestStop();
	}

	public void run() throws IOException {
        log.info("NMEAMessageSocketClient running.");
	    Socket socket = new Socket();
	    socket.connect(socketAddress);
        log.info("Connected to AIS server on " + socketAddress.toString());
	    InputStream inputStream = socket.getInputStream();
	    streamReader = new NMEAMessageInputStreamReader(inputStream, nmeaMessageConsumer);
	    streamReader.run();
	    // TODO: Close socket
        log.info("NMEAMessageSocketClient stopping.");
	}

	private NMEAMessageInputStreamReader streamReader;
	private final SocketAddress socketAddress;
	private final Consumer<? super NMEAMessage> nmeaMessageConsumer;
}
