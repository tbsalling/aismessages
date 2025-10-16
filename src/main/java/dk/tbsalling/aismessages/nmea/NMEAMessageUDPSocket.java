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
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Log
public class NMEAMessageUDPSocket {

    private static final int BUFFER_SIZE = 4096;

    @SuppressWarnings("unused")
    private NMEAMessageUDPSocket() {
        this.nmeaMessageConsumer = null;
        this.host = null;
        this.port = null;
    }

    public NMEAMessageUDPSocket(String host, Integer port, Consumer<? super NMEAMessage> nmeaMessageConsumer) throws UnknownHostException {
        this.host = host;
        this.port = port;
        this.nmeaMessageConsumer = nmeaMessageConsumer;
    }

    public void requestStop() {
        this.stopRequested.set(true);
        if (datagramSocket != null && !datagramSocket.isClosed()) {
            datagramSocket.close();
        }
    }

    public void run() throws IOException {
        log.info("NMEAMessageUDPSocket running.");
        
        try {
            InetAddress address = InetAddress.getByName(host);
            datagramSocket = new DatagramSocket(port, address);
            log.info("Listening for UDP packets on " + host + ":" + port);

            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (!isStopRequested()) {
                try {
                    datagramSocket.receive(packet);
                    String data = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                    
                    // Process each line in the received data
                    String[] lines = data.split("\\r?\\n");
                    for (String line : lines) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        
                        try {
                            NMEAMessage nmea = new NMEAMessage(line);
                            nmeaMessageConsumer.accept(nmea);
                            log.fine("Received: %s".formatted(nmea.toString()));
                        } catch (InvalidMessage invalidMessageException) {
                            log.warning("Received invalid AIS message: \"%s\"".formatted(line));
                        } catch (UnsupportedMessageType unsupportedMessageTypeException) {
                            log.warning("Received unsupported NMEA message: \"%s\"".formatted(line));
                        } catch (NMEAParseException parseException) {
                            log.warning("Received non-compliant NMEA message: \"%s\"".formatted(line));
                        }
                    }
                } catch (SocketException e) {
                    if (isStopRequested()) {
                        // Socket was closed intentionally
                        break;
                    } else {
                        throw e;
                    }
                }
            }
        } finally {
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                datagramSocket.close();
            }
        }

        log.info("NMEAMessageUDPSocket stopping.");
    }

    private boolean isStopRequested() {
        return this.stopRequested.get();
    }

    private final AtomicBoolean stopRequested = new AtomicBoolean(false);
    private DatagramSocket datagramSocket;
    private final String host;
    private final Integer port;
    private final Consumer<? super NMEAMessage> nmeaMessageConsumer;
}
