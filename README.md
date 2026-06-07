# AISmessages

AISmessages is a lightweight, zero-dependency Java library for decoding NMEA-armoured AIS messages used in maritime
navigation and safety systems.

It is built for production workloads: fast decoding, immutable message objects, predictable behavior under load, and a
small footprint that fits comfortably into high-throughput or real-time systems.

> **Performance highlights** (Apple M2 Max, single thread, JMH AverageTime)
>
> | Stage | ns/op | Throughput |
> |---|---|---|
> | NMEA sentence parse (`new NMEAMessage`) | ~351 ns | ~2.9 M sentences/sec |
> | AIS payload decode (`AISMessageFactory.create`) | ~112 ns | ~8.9 M decodes/sec |
> | Bit-field read (e.g. MMSI, lat/lon) | ~0.9 ns | >1 billion reads/sec |
>
> Decoded messages are **fully immutable** — safe to share across threads with zero synchronisation overhead.
> The internal binary representation keeps retained heap per decoded message **3–6× lower** than String-based
> approaches, keeping GC pauses short even at high message rates.

## Why teams choose AISmessages

- **Zero runtime dependencies** for simple deployment and fewer surprises
- **High performance** with eager parsing and packed bit-level decoding
- **Low memory footprint per message** to keep GC pressure down
- **Immutable value objects** that are safe to share across threads
- **Broad protocol coverage** for standard AIS messages and supported application-specific messages
- **Simple integration** from `InputStream`, UDP, sockets, or your own NMEA pipeline
- **Proven in production** for more than 15 years

If you are new to AIS, start with [What is AIS?](docs/articles/what-is-ais.md).

## Quick start

Add AISmessages from Maven Central:

```xml

<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>VERSION</version>
</dependency>
```

Replace `VERSION` with the latest stable release from Maven Central.

Then decode AIS messages directly from an `InputStream`:

```java
import dk.tbsalling.aismessages.AISInputStreamReader;

import java.io.InputStream;

public class DemoApp {
    public static void main(String[] args) {
        InputStream inputStream = ...;

        AISInputStreamReader reader = new AISInputStreamReader(
                inputStream,
                aisMessage -> System.out.println(aisMessage)
        );

        reader.run();
    }
}
```

`AISInputStreamReader` is the easiest entry point for most applications. Feed it NMEA sentences and it calls your
consumer with decoded `AISMessage` instances.

## What you get

AISmessages handles the full decoding path from NMEA framing to strongly typed AIS message objects:

1. Reads NMEA AIS sentences from streams or other sources
2. Validates and reassembles fragmented messages
3. Decodes 6-bit armoured AIS payloads
4. Produces immutable Java objects for the concrete AIS message types

Each AIS message type is represented by its own class, making the decoded data easy to consume in application code.

For example, a type 5 message is represented as `ShipAndVoyageData`, exposing fields such as IMO number, callsign, ship
name, ship type, and vessel dimensions.

## Performance

AISmessages is designed to stay fast and predictable when message rates climb.

- The architecture uses **eager parsing** and **immutable value objects**, so each decoded message is fully populated
  once and safe to pass to any thread without defensive copying.
- Structural validation reuses pre-compiled patterns and lookup tables — no unnecessary allocations on the hot path.
- The internal binary representation avoids String-based bit handling and keeps memory usage low.
- Retained heap per decoded message is roughly **3× lower for typical payload sizes and up to 6× lower for large
  payloads** compared to String-based approaches.

JMH benchmarks (Apple M2 Max, Java 21, single thread, `AverageTime` mode — lower is better):

| Benchmark | ns/op | ~msg/sec |
|---|---|---|
| `NMEAMessageParseBenchmark.parseOne` | 350.9 | ~2.9 M |
| `AISMessageFactoryBenchmark.decodeOne` | 111.8 | ~8.9 M |
| `BitStringMicroBenchmark.getUnsignedInt` (30-bit MMSI) | 0.86 | >1 B |
| `BitStringMicroBenchmark.getSignedInt` (28-bit longitude) | 0.86 | >1 B |
| `BitStringMicroBenchmark.getSixBitAsciiString` (120-bit name) | 44.98 | ~22 M |

`NMEAMessageParseBenchmark` measures the full cost of constructing a `NMEAMessage` from a raw sentence string
(tag-block detection, field splitting, checksum parsing). `AISMessageFactoryBenchmark` measures the subsequent AIS
payload decode from a pre-parsed `NMEAMessage` into a typed, immutable `AISMessage` value object.

Run the benchmark locally with:

```sh
./mvnw -Pbench test
```

Results are written to `target/jmh-result.txt`.

For more detail, see:

- [Immutable value objects in AISmessages v4](docs/articles/immutable-value-objects-v4.md)
- [Performance analysis — String to BitString refactor](docs/articles/performance-analysis.md)

## Application-Specific Messages (ASM)

AISmessages supports decoding of application-specific messages carried in AIS message types 6 and 8.

Supported coverage includes:

- The 24 IMO SN.1/Circ.289 international application-specific message definitions for **DAC=001**
- Regional support for **DAC=200, FI=10** (`InlandShipStaticAndVoyageRelatedData`)
- A safe fallback to `UnknownApplicationSpecificMessage` for unsupported combinations

Example:

```java
import dk.tbsalling.aismessages.ais.messages.BinaryBroadcastMessage;
import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.asm.WeatherObservation;

BinaryBroadcastMessage binaryMessage = (BinaryBroadcastMessage) aisMessage;
ApplicationSpecificMessage asm = binaryMessage.getApplicationSpecificMessage();

if (asm instanceof WeatherObservation weather) {
    System.out.println("Temperature: " + weather.getAirTemperature());
        System.out.

println("Wind speed: "+weather.getAverageWindSpeed());
}
```

For the full overview, see [AIS Application-Specific Messages](docs/articles/application-specific-messages.md).

## Receiving AIS over UDP

AISmessages can also listen for AIS data over UDP:

```java
import dk.tbsalling.aismessages.nmea.NMEAMessageHandler;
import dk.tbsalling.aismessages.nmea.NMEAMessageUDPSocket;

public class UDPExample {
    public static void main(String[] args) throws Exception {
        NMEAMessageUDPSocket udpSocket = new NMEAMessageUDPSocket(
                "127.0.0.1",
                10110,
                new NMEAMessageHandler("UDPSRC", aisMessage ->
                        System.out.println("Received: " + aisMessage))
        );

        udpSocket.run();
    }
}
```

See `dk.tbsalling.aismessages.demo.UDPDemoApp` for a complete example.

## Demos and related projects

The repository includes ready-to-run demo applications:

- `dk.tbsalling.aismessages.demo.SimpleDemoApp`
- `dk.tbsalling.aismessages.demo.SocketDemoApp`
- `dk.tbsalling.aismessages.demo.UDPDemoApp`

You may also want:

- [AISutils](https://github.com/tbsalling/aisutils) for higher-level AIS functionality such as tracking and filtering
- [AIScli](https://github.com/tbsalling/aiscli) for command-line conversion, filtering, and processing

## Documentation

The [`docs/`](docs/) directory contains articles and tutorials, including:

- [What is AIS?](docs/articles/what-is-ais.md)
- [Creating a Spring Boot based AIS message decoder](docs/tutorials/spring-boot-decoder.md)
- [Creating, sharing, and running a Docker image to decode AIS messages](docs/tutorials/docker-decoder.md)
- [Running AISdecoder in a Kubernetes cluster on AWS](docs/tutorials/kubernetes-on-aws.md)
- [Introducing Java modules in AISmessages](docs/tutorials/java-modules.md)

Start here: [docs/README.md](docs/README.md)

## Real-world use

AISmessages has been used in production in a wide range of maritime solutions, including traffic monitoring, port
operations, tracking, and decision support systems.

Public references and talks include:

- OpenRemote's Safe Waterways project in the Beatrix Canal, NL ([video](https://youtu.be/_pcH0KB5J2Q))
- Dakosy's PRISE system for mega-ship sequencing on the Elbe and in the Port of
  Hamburg ([reference](https://www.dakosy.de/en/solutions/port-community-system/prise/))
- Bert Jan Schrijver's "Decode the air around you" talks at DEVOXX BE and DEVOXX
  UK ([YouTube](https://www.youtube.com/watch?v=fOlz0OcZPjc), [Parleys](https://www.parleys.com/tutorial/decoding-air-around-you-java-7-hardware))

## License

AISmessages is released under
the [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International license](LICENSE).

That means it is free for non-commercial use. If you need a commercial license or support,
contact [Thomas Borg Salling](mailto:tbsalling@tbsalling.dk?subject=[GitHub]%20AISmessages%20license).
