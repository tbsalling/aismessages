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

import dk.tbsalling.aismessages.AISMessageDecoder;
import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.version.Version;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Demo application decoding live AIS traffic from the <a href="https://openwaters.io/api/ais/">Open Waters</a>
 * WebSocket stream.
 * <p>
 * The anonymous stream {@code wss://ais.openwaters.io/v1/stream} needs no account or token. Each {@code event} frame
 * carries the raw NMEA sentences of one AIS message, which this demo decodes with {@link AISMessageDecoder} and prints
 * as one readable line per message on standard output; status lines go to standard error.
 * It uses the JDK's own {@link HttpClient} WebSocket support, so AISmessages gains no runtime dependency.
 * <p>
 * Open Waters terms, as published by the service: the stream is free for personal use, commercial use needs a paid
 * plan, and every request must send a {@code User-Agent} naming the application, its version and a contact URL or
 * e-mail. Anonymous clients may subscribe to at most 100 square degrees, receive at most 20 messages per second per
 * stream and hold two streams at a time. Set the system property {@code aismessages.userAgent} to send your own
 * User-Agent.
 * <p>
 * Usage: {@code java WebSocketDemoApp [minLat,minLon,maxLat,maxLon]}; the default box covers Danish waters.
 */
public class WebSocketDemoApp implements WebSocket.Listener {

    private static final URI STREAM = URI.create("wss://ais.openwaters.io/v1/stream");
    private static final String DEFAULT_BOX = "54.5,8.0,58.0,13.0";
    private static final double MAX_ANONYMOUS_AREA = 100.0;

    private final double[] box;
    private final StringBuilder frame = new StringBuilder();
    private final CountDownLatch closed = new CountDownLatch(1);
    private final AtomicLong decoded = new AtomicLong();
    private final AtomicLong undecodable = new AtomicLong();

    public WebSocketDemoApp(double[] box) {
        this.box = box;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        String subscribe = String.format(Locale.ROOT, "{\"type\":\"subscribe\",\"bbox\":[[%s,%s,%s,%s]]}", box[0], box[1], box[2], box[3]);
        webSocket.sendText(subscribe, true);
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        frame.append(data);
        if (last) {
            handleFrame(frame.toString());
            frame.setLength(0);
        }
        webSocket.request(1);
        return null;
    }

    private void handleFrame(String json) {
        String type = OpenWatersEvent.type(json);
        if ("event".equals(type)) {
            List<String> sentences = OpenWatersEvent.nmeaSentences(json);
            AISMessageDecoder.tryDecode(sentences).ifPresentOrElse(messages -> messages.forEach(this::print), undecodable::incrementAndGet);
        } else if ("error".equals(type)) {
            System.err.println("Open Waters reported an error: " + json);
        }
    }

    private void print(AISMessage message) {
        decoded.incrementAndGet();
        System.out.println(LocalTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS) + "Z  " + MessageFormatter.format(message));
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.err.println("Stream closed: " + statusCode + " " + reason);
        closed.countDown();
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.err.println("Stream error: " + error);
        closed.countDown();
    }

    public void runDemo(String userAgent) throws InterruptedException {
        // Status goes to stderr, so that stdout carries only the decoded messages.
        System.err.println("AISMessages WebSocket Demo App: decoding live AIS from Open Waters (" + STREAM + ").");
        System.err.println("Free for personal use; commercial use needs an Open Waters plan.");
        System.err.printf(Locale.ROOT, "Area: latitude %s to %s, longitude %s to %s. Press Ctrl+C to stop.%n%n", box[0], box[2], box[1], box[3]);

        WebSocket webSocket = HttpClient.newHttpClient().newWebSocketBuilder()
                .header("User-Agent", userAgent)
                .buildAsync(STREAM, this)
                .join();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "bye");
            System.err.println("Decoded " + decoded.get() + " messages; " + undecodable.get() + " events could not be decoded.");
        }));
        closed.await();
    }

    /**
     * @param argument a bounding box {@code minLat,minLon,maxLat,maxLon}.
     * @return the four coordinates.
     * @throws IllegalArgumentException when the box is malformed or larger than the anonymous limit.
     */
    static double[] parseBox(String argument) {
        String[] parts = argument.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Expected minLat,minLon,maxLat,maxLon: " + argument);
        }
        double[] box = new double[4];
        for (int i = 0; i < 4; i++) {
            box[i] = Double.parseDouble(parts[i].trim());
        }
        if (box[0] >= box[2] || box[1] >= box[3] || box[0] < -90 || box[2] > 90 || box[1] < -180 || box[3] > 180) {
            throw new IllegalArgumentException("Not a valid box (minLat < maxLat, minLon < maxLon): " + argument);
        }
        double area = (box[2] - box[0]) * (box[3] - box[1]);
        if (area > MAX_ANONYMOUS_AREA) {
            throw new IllegalArgumentException("Box covers %.1f square degrees; anonymous Open Waters clients may subscribe to at most %.0f".formatted(area, MAX_ANONYMOUS_AREA));
        }
        return box;
    }

    public static void main(String[] args) throws InterruptedException {
        double[] box;
        try {
            box = parseBox(args.length >= 1 ? args[0] : DEFAULT_BOX);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("Usage: java WebSocketDemoApp [minLat,minLon,maxLat,maxLon]");
            System.exit(1);
            return;
        }
        String userAgent = System.getProperty("aismessages.userAgent",
                "AISmessages-WebSocketDemoApp/" + Version.VERSION + " (+https://github.com/tbsalling/aismessages)");
        new WebSocketDemoApp(box).runDemo(userAgent);
    }
}
