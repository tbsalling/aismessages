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

package dk.tbsalling.aismessages.demoapps;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;

import dk.tbsalling.aismessages.DecodedAISMessageHandler;
import dk.tbsalling.aismessages.NMEAMessageSocketClient;
import dk.tbsalling.aismessages.messages.DecodedAISMessage;

public class SocketDemoApp implements DecodedAISMessageHandler {

	public void handleMessageReceived(DecodedAISMessage message) {
		System.out.println("AIS: " + message);
	}

	public void runDemo() {
		System.out.println("AISMessages Demo App");
		System.out.println("--------------------");

		try {
			NMEAMessageSocketClient nmeaMessageHandler = new NMEAMessageSocketClient("207.7.148.216", 9009, this);
			nmeaMessageHandler.run();
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SocketDemoApp demoApp = new SocketDemoApp();
		demoApp.runDemo();
	}
	
}