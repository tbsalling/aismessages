# Creating a Spring Boot based AIS message decoder

**Published:** 2018-09-13

> **Historical note:** This tutorial uses the separate `aisdecoder` demo service and AISmessages 2.2.x. The concepts still explain how to receive NMEA sentences over HTTP and decode them into AIS message objects.

This tutorial shows how to build a small Spring Boot service that accepts a JSON array of NMEA strings and responds with decoded AIS messages in JSON format.

## Target API

Request:

```text
POST http://localhost:8080/decode
Content-Type: application/json

[
  "!AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53",
  "!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
  "!AIVDM,2,2,0,B,00000000000,2*27"
]
```

Response shape:

```json
[
  {
    "repeatIndicator": 0,
    "sourceMmsi": { "mmsi": 576048000 },
    "navigationStatus": "UnderwayUsingEngine",
    "rateOfTurn": 0,
    "speedOverGround": 6.6,
    "positionAccuracy": false,
    "latitude": 37.912167,
    "longitude": -122.42299,
    "courseOverGround": 350.0,
    "trueHeading": 355,
    "second": 40,
    "specialManeuverIndicator": "NotAvailable",
    "raimFlag": false,
    "messageType": "PositionReportClassAScheduled",
    "transponderClass": "A",
    "valid": true
  }
]
```

## Initialize the Spring Boot project

Generate a starter project from `https://start.spring.io` and verify that the unmodified application builds and runs.

![Spring Initializr setup](../assets/images/blog_spring_initializr.png)

## Add AISmessages as a dependency

The original tutorial used:

```groovy
dependencies {
  compile group: 'dk.tbsalling', name: 'aismessages', version: '2.2.3'
}
```

If you are recreating the example today, use the current AISmessages release instead of the historical version.

## Add the controller

```java
@RestController
public class AisdecoderController {
    @Autowired
    private AisdecoderService aisdecoderService;

    @RequestMapping(
        value = "/decode",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<AISMessage> decode(@RequestBody List<String> nmea) {
        return aisdecoderService.decode(nmea);
    }
}
```

## Add the decode service

The core idea is to feed each input NMEA sentence into `NMEAMessageHandler`, which reconstructs complete AIS messages and emits them via a callback.

```java
@Service
@RequestScope
public class AisdecoderService implements Consumer<AISMessage> {

    private final List<AISMessage> aisMessages = new LinkedList<>();

    public List<AISMessage> decode(List<String> nmeaMessagesAsStrings) {
        NMEAMessageHandler nmeaMessageHandler = new NMEAMessageHandler("SRC1", this);

        nmeaMessagesAsStrings.forEach(nmeaMessageAsString -> {
            try {
                NMEAMessage nmeaMessage = NMEAMessage.fromString(nmeaMessageAsString);
                nmeaMessageHandler.accept(nmeaMessage);
            } catch (NMEAParseException e) {
                System.err.printf(e.getMessage());
            }
        });

        List unparsedMessages = nmeaMessageHandler.flush();
        unparsedMessages.forEach(unparsedMessage ->
            System.err.println("NMEA message not used: " + unparsedMessage)
        );

        return aisMessages;
    }

    @Override
    public void accept(AISMessage aisMessage) {
        aisMessages.add(aisMessage);
    }
}
```

## Why `NMEAMessageHandler` matters

AIS-to-NMEA is not always a 1:1 mapping. Some AIS payloads require multiple NMEA fragments. `NMEAMessageHandler` keeps track of those fragments and only emits a decoded AIS message when all required fragments have been received.

## Run the service

The original demo instructions were:

```text
$ git clone https://github.com/tbsalling/aisdecoder.git
$ cd aisdecoder/
$ git checkout 7c02cbcef2ff273ab157e41fa71b193ae3304a93
$ ./gradlew build
$ ./gradlew bootRun
```

Then call the service:

```text
$ curl -X POST http://localhost:8080/decode \
    -H 'Content-Type: application/json' \
    -d '[ "!AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53" ]'
```

## Related tutorials

- [What is AIS?](../articles/what-is-ais.md)
- [Creating, sharing, and running a Docker image to decode AIS messages](docker-decoder.md)
- [Running AISdecoder in a Kubernetes cluster on AWS](kubernetes-on-aws.md)
