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

package dk.tbsalling.aismessages.demo;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.nmea.NMEAMessageHandler;
import dk.tbsalling.aismessages.nmea.NMEAMessageUDPSocket;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.function.Consumer;

/**
 * Demo application showing how to receive AIS messages via UDP.
 * 
 * This demo listens for UDP packets containing NMEA-formatted AIS messages
 * on a specified host and port.
 */
public class UDPDemoApp implements Consumer<AISMessage> {

    @Override
    public void accept(AISMessage aisMessage) {
        System.out.println("Received AIS message from UDP: " + aisMessage);
    }

    public void runDemo(String host, int port) {
        System.out.println("AISMessages UDP Demo App");
        System.out.println("-------------------------");
        System.out.println("Listening for UDP packets on " + host + ":" + port);
        System.out.println();

        try {
            NMEAMessageUDPSocket udpSocket = new NMEAMessageUDPSocket(
                host, 
                port, 
                new NMEAMessageHandler("UDPDEMO", this)
            );
            udpSocket.run();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";  // Default to localhost
        int port = 10110;           // Common AIS UDP port

        // Parse command line arguments
        if (args.length >= 1) {
            host = args[0];
        }
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[1]);
                System.err.println("Usage: java UDPDemoApp [host] [port]");
                System.exit(1);
            }
        }

        new UDPDemoApp().runDemo(host, port);
    }
}
