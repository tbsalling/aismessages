package dk.tbsalling.aismessages.nmea;

import dk.tbsalling.aismessages.ais.messages.AISMessage;
import org.junit.jupiter.api.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static dk.tbsalling.aismessages.testsupport.NmeaSentences.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The UDP transport ({@link NMEAMessageUDPSocket}), exercised against loopback sockets on free ports. Sentences are synthetic.
 */
public class NMEAMessageSocketTest {

    private static final String VALID_1 = sentence(message(1, 168));
    private static final String VALID_2 = sentence(message(3, 168));

    /** Valid sentences interleaved with blank lines and every kind of sentence the transports must log and skip. */
    private static final String DATAGRAM = String.join("\r\n",
            VALID_1,
            "",
            "garbage",
            withChecksum("GPGGA,1,1,,A,0000,0"),
            sentence(message(1, 163)),
            withChecksum("AIVDM,X,1,,A,0000,0"),
            "\\s:STATION1*00\\" + VALID_1,
            tagBlock(Map.of("c", "abc")) + VALID_1,
            VALID_2,
            "");

    private static int freeUdpPort() throws Exception {
        try (DatagramSocket probe = new DatagramSocket(0, InetAddress.getLoopbackAddress())) {
            return probe.getLocalPort();
        }
    }

    @Test
    public void udpSocketDeliversValidSentencesAndStopsOnRequest() throws Exception {
        int port = freeUdpPort();
        List<AISMessage> decoded = new CopyOnWriteArrayList<>();
        NMEAMessageHandler handler = new NMEAMessageHandler("UDP", decoded::add);
        NMEAMessageUDPSocket udp = new NMEAMessageUDPSocket("127.0.0.1", port, handler);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Thread runner = new Thread(() -> {
            try {
                udp.run();
            } catch (Throwable t) {
                failure.set(t);
            }
        });
        runner.start();
        try {
            byte[] data = DATAGRAM.getBytes(StandardCharsets.UTF_8);
            assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
                try (DatagramSocket sender = new DatagramSocket()) {
                    // The receiver may not be bound yet, so resend until the first datagram has been processed.
                    while (decoded.size() < 2) {
                        sender.send(new DatagramPacket(data, data.length, InetAddress.getLoopbackAddress(), port));
                        Thread.sleep(50);
                    }
                }
            });
        } finally {
            udp.requestStop();
            runner.join(5000);
        }

        assertFalse(runner.isAlive());
        assertNull(failure.get(), () -> "run() failed: " + failure.get());
        assertTrue(decoded.size() >= 2);
        assertEquals(0, decoded.size() % 2, "each processed datagram yields exactly the two valid messages");
    }

    @Test
    public void udpSocketStoppedBeforeRunReturnsImmediately() throws Exception {
        NMEAMessageUDPSocket udp = new NMEAMessageUDPSocket("127.0.0.1", freeUdpPort(), m -> fail("nothing should be received"));
        udp.requestStop();

        assertTimeoutPreemptively(Duration.ofSeconds(5), udp::run);
    }

    @Test
    public void udpSocketReportsPortAlreadyInUse() throws Exception {
        try (DatagramSocket occupier = new DatagramSocket(0, InetAddress.getLoopbackAddress())) {
            NMEAMessageUDPSocket udp = new NMEAMessageUDPSocket("127.0.0.1", occupier.getLocalPort(), m -> {});

            assertThrows(java.net.SocketException.class, udp::run);
        }
    }
}
