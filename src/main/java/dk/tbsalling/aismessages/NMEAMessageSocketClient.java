/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class NMEAMessageSocketClient {

	@SuppressWarnings("unused")
	private NMEAMessageSocketClient() {
		this.socketAddress = null;
		this.decodedMessageHandler = null;
	}

	public NMEAMessageSocketClient(String host, Integer port, DecodedAISMessageHandler decodedMessageHandler) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(host);
		this.socketAddress = new InetSocketAddress(inetAddress, port);
		this.decodedMessageHandler = decodedMessageHandler;
	}

	public void requestStop() {
		if (streamReader != null)
			streamReader.requestStop();
	}

	public void run() throws IOException {
		Socket socket = new Socket();
		socket.connect(socketAddress);
		InputStream inputStream = socket.getInputStream();
		streamReader = new NMEAMessageInputStreamReader(inputStream, decodedMessageHandler);
		streamReader.run();
	}

	private NMEAMessageInputStreamReader streamReader;
	private final SocketAddress socketAddress;
	private final DecodedAISMessageHandler decodedMessageHandler;
}
