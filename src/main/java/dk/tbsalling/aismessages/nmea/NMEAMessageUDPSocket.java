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
import java.net.UnknownHostException;
import java.util.function.Consumer;

@Log
public class NMEAMessageUDPSocket {

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
        if (streamReader != null)
            streamReader.requestStop();
    }

    public void run() throws IOException {
        log.info("NMEAMessageUDPSocket running.");
        UDPInputStream udpStream = new UDPInputStream(host, port);
        log.info("Connected to AIS server on " + host + ":" + port);
        streamReader = new NMEAMessageInputStreamReader(udpStream, nmeaMessageConsumer);
        streamReader.run();
        udpStream.close();
        log.info("NMEAMessageUDPSocket stopping.");
    }

    private NMEAMessageInputStreamReader streamReader;
    private final String host;
    private final Integer port;
    private final Consumer<? super NMEAMessage> nmeaMessageConsumer;
}
