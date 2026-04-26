# AIS Application-Specific Messages

**Published:** 2025-01-20
**Updated:** 2026-04-26

Most AIS users know the standard position reports and static vessel data messages. AIS also supports **Application-Specific Messages (ASM)**, which carry specialized structured data for weather, port operations, safety, inland waterways, and other domain-specific use cases.

## What are application-specific messages?

Application-specific payloads are transmitted via:

- **message type 6**: Addressed Binary Message
- **message type 8**: Binary Broadcast Message

These messages are identified by:

1. **DAC** (Designated Area Code), which identifies the region or organization defining the format
2. **FI** (Function Identifier), which identifies the specific message type within that DAC

Examples:

- `DAC=001, FI=11` → UTC and Date Response
- `DAC=200, FI=10` → European inland ship static and voyage data
- `DAC=316, FI=1` → United States PAWSS family messages

## IMO SN.1/Circ.289 international messages

The IMO circular SN.1/Circ.289 standardizes 24 international message types under `DAC=001`.

### Weather and environmental data

- **FI=31**: Meteorological and Hydrographical Data
- **FI=21**: Weather Observation from Ship
- **FI=26**: Environmental messages

These support transmission of wind, air pressure, wave height, salinity, ice conditions, and related observations.

### Safety and navigation

- **FI=22, 23**: Area Notice
- **FI=18, 19**: Marine Traffic Signal
- **FI=25**: Dangerous Cargo Indication
- **FI=14**: Tidal Window

These messages support hazard warnings, traffic control, cargo awareness, and passage planning.

### Port and vessel operations

- **FI=20**: Berthing Data
- **FI=40**: Number of Persons on Board
- **FI=27, 28**: Route Information

### Vessel information

- **FI=24**: Extended Ship Static and Voyage Related Data
- **FI=0, 1**: Text Description

## Why application-specific messages matter

- enhanced maritime safety through richer operational data
- better port optimization and traffic coordination
- improved environmental monitoring
- better support for compliance and emergency response
- inland waterway management beyond the standard deep-sea AIS profiles

## Decoding ASMs with AISmessages

AISmessages supports ASM decoding directly from the parent binary AIS message. Decode NMEA sentences using `NMEAMessageHandler` or `AISInputStreamReader`, then cast the resulting `AISMessage` to access the embedded ASM.

### Example: meteorological and hydrographical data

```java
import dk.tbsalling.aismessages.ais.messages.AISMessage;
import dk.tbsalling.aismessages.ais.messages.BinaryBroadcastMessage;
import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.asm.MeteorologicalAndHydrographicalData;
import dk.tbsalling.aismessages.nmea.NMEAMessageHandler;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataExample {
    public static void main(String[] args) {
        String nmea = "!AIVDM,1,1,,A,85MwpKiKf:MPiQa:ofV@v2mQTfB26oEtbEVqh4j1QDQPHjhpkNJ3,0*11";

        List<AISMessage> decoded = new ArrayList<>();
        NMEAMessageHandler handler = new NMEAMessageHandler("EXAMPLE", decoded::add);
        handler.accept(new NMEAMessage(nmea));
        handler.flush();

        for (AISMessage aisMessage : decoded) {
            if (aisMessage instanceof BinaryBroadcastMessage binaryMessage) {
                ApplicationSpecificMessage asm = binaryMessage.getApplicationSpecificMessage();

                if (asm instanceof MeteorologicalAndHydrographicalData weather) {
                    System.out.println("Weather Report from MMSI: " + binaryMessage.getSourceMmsi());
                    System.out.println("Position: " + weather.getLatitude() + ", " + weather.getLongitude());
                    System.out.println("Wind Speed: " + weather.getWindSpeed() + " knots");
                    System.out.println("Air Temperature: " + weather.getAirTemperature() + " °C");
                    System.out.println("Wave Height: " + weather.getWaveHeight() + " m");
                }
            }
        }
    }
}
```

### Example: inland waterway data

```java
import dk.tbsalling.aismessages.ais.messages.asm.InlandShipStaticAndVoyageRelatedData;

if (asm instanceof InlandShipStaticAndVoyageRelatedData inland) {
    System.out.println("European Vessel ID: "
        + inland.getUniqueEuropeanVesselIdentificationNumber());
    System.out.println("Length: " + inland.getLengthOfShip() + " m");
    System.out.println("Beam: " + inland.getBeamOfShip() + " m");
    System.out.println("Ship Type Code: " + inland.getShipOrCombinationType());
    System.out.println("Draught: " + inland.getDraught() + " m");
}
```

### Example: area notices

```java
if (asm instanceof AreaNotice notice) {
    System.out.println("Notice Type: " + notice.getNoticeType());
    System.out.println("Start: " + notice.getMonth() + "/" + notice.getDay()
        + " " + notice.getHour() + ":" + notice.getMinute());
    System.out.println("Duration: " + notice.getDurationMinutes() + " minutes");
}
```

### Example: unknown messages

```java
if (asm instanceof UnknownApplicationSpecificMessage unknown) {
    System.out.println("DAC: " + unknown.getDesignatedAreaCode());
    System.out.println("FI: " + unknown.getFunctionalId());
    System.out.println("Binary Data: " + unknown.getBinaryData());
}
```

### Example: processing a mixed stream

```java
import dk.tbsalling.aismessages.AISInputStreamReader;
import dk.tbsalling.aismessages.ais.messages.BinaryBroadcastMessage;
import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.asm.MeteorologicalAndHydrographicalData;
import java.io.InputStream;

AISInputStreamReader reader = new AISInputStreamReader(
    inputStream,
    aisMessage -> {
        if (aisMessage instanceof BinaryBroadcastMessage binaryMessage) {
            ApplicationSpecificMessage asm = binaryMessage.getApplicationSpecificMessage();
            if (asm instanceof MeteorologicalAndHydrographicalData weather) {
                System.out.println("Wind: " + weather.getWindSpeed() + " kt at " + weather.getWindDirection() + "°");
            }
        }
    }
);

reader.run();
```

## Supported messages in AISmessages

The repository README maintains the current support overview. The supported ASM set includes:

**International (`DAC=001`)**

- Text Description (`FI=0, 1`)
- UTC/Date Inquiry (`FI=10`)
- UTC/Date Response (`FI=11`)
- Tidal Window (`FI=14`)
- VTS Generated/Synthetic Targets (`FI=17`)
- Marine Traffic Signal (`FI=18, 19`)
- Berthing Data (`FI=20`)
- Weather Observation from Ship (`FI=21`)
- Area Notice (`FI=22, 23`)
- Extended Ship Static and Voyage Related Data (`FI=24`)
- Dangerous Cargo Indication (`FI=25`)
- Environmental (`FI=26`)
- Route Information (`FI=27, 28`)
- Meteorological and Hydrographical Data (`FI=31`)
- Number of Persons on Board (`FI=40`)

**Regional**

- `DAC=200, FI=10`: Inland Ship Static and Voyage Related Data

## Real-world uses

- port community systems
- weather services
- vessel traffic services
- search and rescue
- environmental monitoring

## Further reading

- [What is AIS?](what-is-ais.md)
- [Immutable value objects in AISmessages v4](immutable-value-objects-v4.md)
- [README.md](../../README.md)
- [ITU-R M.1371 AIS specification](http://www.itu.int/rec/R-REC-M.1371-5-201402-I)
- [IALA AIS service](https://www.iala-aism.org/technical/ais/)
