# AISmessages Release Notes

Application-facing release notes for AISmessages - a Java library for decoding AIS messages from digital VHF radio
traffic.

---

## Version 4.1.3-SNAPSHOT

**Development Version**

### Breaking API Changes

**Packed `BitString` replaces `String`-of-`'0'`/`'1'` payload representation:**

- Introduced `dk.tbsalling.aismessages.ais.BitString`, a packed immutable bit vector with typed accessors such as
  `getUnsignedInt`, `getSignedInt`, `getUnsignedLong`, `getBoolean`, `getSignedFloat`, `getUnsignedFloat`, `slice`, and
  `withLengthPaddedTo`.
- Removed `dk.tbsalling.aismessages.ais.BitStringParser` and `dk.tbsalling.aismessages.ais.BitDecoder`.
- `Metadata.bitString()` now returns `BitString` instead of `String`. Call `.toString()` on the result to get the legacy
  `'0'`/`'1'` representation.
- Constructor parameter types changed from `String bitString` to `BitString bitString` on `AISMessage` and all permitted
  subclasses.
- `ApplicationSpecificMessage.binaryData` is now `BitString` instead of `String`, and
  `ApplicationSpecificMessage.create(int, int, BitString)` was updated accordingly.
- `SOTDMACommunicationState.fromBitString(...)` and `ITDMACommunicationState.fromBitString(...)` now take `BitString`.
- `AISMessageFactory.toBitString(String, int)` was removed in favor of `BitString.fromNmeaPayload(String, int)`.

### New Public APIs

- Added `dk.tbsalling.aismessages.ais.SixBitAsciiCodec` for working with custom six-bit ASCII alphabets on top of
  `BitString`.
- Added `dk.tbsalling.aismessages.ais.AISText` for AIS text decoding with AIS filler handling (`'@'` becomes space and
  surrounding whitespace is trimmed).
- Added `dk.tbsalling.aismessages.nmea.NMEAArmouring` for explicit NMEA armouring encode/decode operations.

### Performance

- `AISMessageFactory.create(...)` is now about **11x faster** end-to-end.
- Retained heap per decoded message is reduced by roughly **3x to 6x**, depending on payload size.

### Upgrade Notes

- The README now reflects the `BitString`-based API.
- NMEA terminology was standardized from "fill bits" to "padding bits" in documentation and related APIs.
- See [`docs/articles/performance-analysis.md`](docs/articles/performance-analysis.md) for benchmarks and migration
  context.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.1.2...HEAD

---

## Version 4.1.2

**Release Date:** 2025-11-19

No application-facing changes.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.1.1...aismessages-4.1.2

---

## Version 4.1.1

**Release Date:** 2025-11-19

### Performance Improvements

- Replaced the character-to-six-bit lookup with an array-based implementation.
- Delivers about **3.97x faster** character-to-six-bit conversion during AIS decoding.

### Upgrade Notes

- Added upgrade guidance for moving from AISmessages 3.3.1 to 4.1.0.
- Added Java modules examples and refreshed README usage examples to match the current API.

### Fixes

- Fixed resource leak issues.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.1.0...aismessages-4.1.1

---

## Version 4.1.0

**Release Date:** 2025-10-19

### New Features & Improvements

**Expanded Application Specific Messages (ASM) support - IMO SN.1/Circ.289:**

- Added 12 new ASM decoders for IMO standardized messages (DAC=001):
  - `TextDescription` (FI=0, 1)
  - `UtcDateInquiry` (FI=10)
  - `UtcDateResponse` (FI=11)
  - `TidalWindow` (FI=14)
  - `VtsGeneratedSyntheticTargets` (FI=17)
  - `MarineTrafficSignal` (FI=18, 19)
  - `WeatherObservation` (FI=21)
  - `AreaNotice` (FI=22, 23)
  - `DangerousCargoIndication` (FI=25)
  - `Environmental` (FI=26)
  - `RouteInformation` (FI=27, 28)
  - `MeteorologicalAndHydrographicalData` (FI=31)
- Total ASM support increased from 6 to 18 message types.

**UDP receiver support:**

- Added `NMEAMessageUDPSocket` for receiving AIS messages via UDP.
- Supports binding to a host/port and processing incoming messages through a handler callback.

**NMEA handling improvements:**

- Added `NMEAMessageHandlerStrict` for strict checksum validation and stricter NMEA message handling.

### Fixes

- Fixed the validation error message for binary broadcast messages exceeding 1008 bits.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.0.0...aismessages-4.1.0

---

## Version 4.0.0

**Major Version Update - Java 21 Required**

### Breaking Changes

- **Minimum Java version increased from 11 to 21.**
- AIS message classes are now immutable value objects.
- Message decoding is now eager rather than lazy, so fields are parsed at construction time.

### New Features & Improvements

**Immutable message objects:**

- Message objects are pure data carriers with no parsing responsibilities.
- Improved thread safety and fewer mutation-related bugs.
- Added stable `equals()` / `hashCode()` behavior across message types.

**Performance and memory improvements:**

- Eliminates the old `WeakReference`-based lazy-decoding overhead.
- Decodes messages in a single pass during construction.
- Moves allocation cost to construction time and avoids follow-up allocations during field access.
- Reduces GC pressure in high-throughput and memory-constrained workloads.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-3.5.1...aismessages-4.0.0

---

## Version 3.5.1

**Release Date:** 2025-07-22

No significant application-facing API changes.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-3.5.0...aismessages-3.5.1

---

## Version 3.5.0

**Release Date:** 2025-07-xx

No specific release notes available. See version 3.4.2 for previous changes.

---

## Version 3.4.2

**Release Date:** 2024-01-20

Maintenance release with no significant application-facing changes.

---

## Version 3.2.3

**Release Date:** 2022-01-11

**Major Version Update - Java 11 Required**

### Breaking Changes

- **Minimum Java version increased from 8 to 11.**
- Removed deprecated `TIME_DECODER`; use the individual ETA field getters instead.
- Updated message validation and error handling APIs.

### New Features

**NMEA Tag Block support:**

- Added support for NMEA 0183 Tag Blocks with parameters `c`, `d`, `g`, `n`, `r`, `s`, and `t`.
- Added `NMEATagBlock`, `NMEATagBlockParameterCode`, and `TAGBlockParameterCodeType`.
- Tag blocks are accessible through `AISMessage.getNmeaTagBlock()`.

**Application Specific Messages (ASM):**

- Added a framework for decoding binary application-specific messages.
- Implemented ASM decoders for:
  - DAC 1 FI 20 (`BerthingData`)
  - DAC 1 FI 24 (`ExtendedShipStaticAndVoyageRelatedData`)
  - DAC 1 FI 40 (`NumberOfPersonsOnBoard`)
  - DAC 200 FI 10 (`InlandShipStaticAndVoyageRelatedData`)
- ASM payloads are accessible through `getApplicationSpecificMessage()`.

**Enhanced ETA handling:**

- Added `getEtaAfterReceived()` returning `Optional<ZonedDateTime>`.
- Added individual ETA component getters: `getEtaMonth()`, `getEtaDay()`, `getEtaHour()`, `getEtaMinute()`.

**Raw data access:**

- Added raw-value accessors such as `getRawLatitude()`, `getRawLongitude()`, `getRawSpeedOverGround()`,
  `getRawCourseOverGround()`, and `getRawDraught()`.

### Platform Support

- Added `module-info.java` for Java Platform Module System (JPMS) support.

---

## Version 2.2.3

**Release Date:** 2018-06-15

Maintenance release for Java 8.

---

## Version 2.2.1

**Release Date:** 2016-02-25

Bug fixes and stability improvements. JDK 7 backport available.

---

## Version 2.1.0

**Release Date:** 2015-02-03

### Major Feature: `AISInputStreamReader`

- Introduced `AISInputStreamReader` for simplified stream processing.
- Added functional interface support with `Consumer<AISMessage>`.
- Added communication-state decoding for Class A position reports.

---

## Version 2.0.2

**Release Date:** 2015-01-26

### First production-ready v2 release

- Java 8 required.
- Zero runtime dependencies maintained.
- Introduced lazy decoding.
- Improved ITU 1371-5 compliance.

---

## Maven Coordinates

### Development Version (4.1.3-SNAPSHOT)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.3-SNAPSHOT</version>
</dependency>
```

### Latest Stable Version (4.1.2)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.2</version>
</dependency>
```

### Previous Stable Version (4.1.1)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.1</version>
</dependency>
```

### Previous Stable Version (4.1.0)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.0</version>
</dependency>
```

### Previous Major Version (4.0.0)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.0.0</version>
</dependency>
```

### Previous Stable Version (3.5.1)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>3.5.1</version>
</dependency>
```

---

## License

AISmessages is licensed under Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0).

**Commercial licenses available:** Contact Thomas Borg Salling <tbsalling@tbsalling.dk>
