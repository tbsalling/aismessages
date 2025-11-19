# AISmessages Release Notes

Developer-oriented release notes for AISmessages - a Java-based library for decoding AIS messages from digital VHF radio traffic.

---

## Version 4.1.3-SNAPSHOT

**Development Version**

### New Features & Improvements

**Expanded Application Specific Messages (ASM) - IMO SN.1/Circ.289:**
- Added 12 new ASM decoders for IMO standardized messages (DAC=001)
- Text Description (FI=0, 1) - `TextDescription`
- UTC/Date Inquiry (FI=10) - `UtcDateInquiry`
- UTC/Date Response (FI=11) - `UtcDateResponse`
- Tidal Window (FI=14) - `TidalWindow`
- VTS Generated/Synthetic Targets (FI=17) - `VtsGeneratedSyntheticTargets`
- Marine Traffic Signal (FI=18, 19) - `MarineTrafficSignal`
- Area Notice (FI=22, 23) - `AreaNotice`
- Dangerous Cargo Indication (FI=25) - `DangerousCargoIndication`
- Environmental (FI=26) - `Environmental`
- Route Information (FI=27, 28) - `RouteInformation`
- Meteorological and Hydrographical Data (FI=31) - `MeteorologicalAndHydrographicalData`
- Weather Observation (FI=21) - `WeatherObservation`
- Brings total ASM support to 18 message types (previously 6)

**UDP Receiver Support:**
- New `NMEAMessageUDPSocket` class for receiving AIS messages via UDP
- Enables real-time AIS data reception from UDP sources
- Simple API: bind to host and port, provide message handler
- Complete `UDPDemoApp` example demonstrating UDP receiver usage
- Ideal for integration with AIS data feeds and network sources

**NMEA Message Handling Improvements:**
- New `NMEAMessageHandlerStrict` class for stricter NMEA message validation
- Enhanced error handling and message validation options

**Dependency Management:**
- Added Dependabot configuration for automated weekly dependency updates
- Configured to check Maven dependencies, GitHub Actions, and Maven wrapper updates
- Grouped dependency updates by type for easier review

**Testing Improvements:**
- Comprehensive NMEA message validation test coverage
- Added NMEAMessageTest with 9 test cases for checksum validation
- Added NMEAMessageHandlerTest with 5 test cases for message handling
- Added NMEAMessageInputStreamReaderTest with 4 test cases for stream processing
- Improved test infrastructure for NMEA message processing and validation

**Dependency Updates:**

*GitHub Actions:*
- actions/checkout: 4 → 5
- actions/setup-java: 4 → 5

*Maven Plugins:*
- maven-gpg-plugin: 3.2.6 → 3.2.8
- maven-javadoc-plugin: 3.10.1 → 3.12.0
- maven-assembly-plugin: 3.7.0 → 3.7.1
- maven-surefire-plugin: 3.5.1 → 3.5.4
- central-publishing-maven-plugin: 0.8.0 → 0.9.0

*Test Dependencies:*
- junit-jupiter: 5.9.2 → 6.0.1
- mockito-junit-jupiter: 2.13.1 → 5.14.2

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.0.0...HEAD

---

## Version 4.0.0

**Major Version Update - Java 21 Required**

### Breaking Changes
- **Minimum Java version increased from 11 to 21**
- AIS message classes are now immutable value objects using Lombok @Value
- Architectural refactoring: parsing logic separated from data models

### New Features & Improvements

**Immutable Message Objects:**
- All AIS message classes now use Lombok @Value annotation for true immutability
- Message objects are now pure data carriers with no parsing responsibilities
- Improved thread-safety and reduced mutation-related bugs
- Added EqualsAndHashCode support for all message types

**Architecture Improvements:**
- Introduced BitStringParser class to separate parsing concerns from value objects
- Cleaner separation between NMEA message handling and AIS message decoding
- Enhanced maintainability through better single-responsibility design

**Performance & Memory Improvements:**

Version 4 represents a fundamental shift from the lazy decoding approach used in versions 2.x and 3.x to an eager parsing model with immutable value objects. This architectural change dramatically reduces garbage collection pressure and memory churning:

- **Eager parsing eliminates WeakReference overhead**: Earlier versions used lazy decoding with WeakReference caching for field values. This approach required the garbage collector to manage and potentially reclaim cached field values, creating constant GC pressure. V4 parses all fields upfront into final immutable fields, eliminating WeakReference overhead entirely.

- **Single-pass parsing**: V4 decodes each AIS message in one pass during construction, storing all field values in final fields. In contrast, lazy decoding could trigger repeated parsing if the garbage collector reclaimed cached values, leading to unpredictable CPU spikes and allocation churn.

- **Predictable, upfront allocation**: All memory allocation happens at message construction time in a single, predictable burst. This eliminates the scattered, on-demand allocations of lazy decoding and enables more efficient GC patterns (Eden-only allocations for short-lived messages).

- **Compact memory layout**: Immutable value objects with only final primitive and reference fields have better CPU cache locality and lower per-instance overhead compared to objects with additional caching structures and weak references.

- **Zero allocation after construction**: Once constructed, V4 message objects allocate no additional memory during field access. Lazy approaches could allocate wrapper objects, strings, and cache entries on every field access if cached values were reclaimed.

The result is dramatically lower GC overhead, especially under high message throughput where V2/V3's lazy approach would cause the garbage collector to continuously manage weak references and cached field values. V4's eager approach is particularly beneficial in memory-constrained environments, real-time systems, and high-throughput scenarios processing thousands of messages per second.

**Build & Tooling Updates:**
- Java compiler target updated to Java 21
- Added Lombok 1.18.42 as provided dependency for compile-time code generation
- Maven compiler plugin updated to 3.14.1
- Lombok configuration with @Generated annotations for better tool integration

**Infrastructure:**
- GitHub Actions workflow updated for Java 21
- Added AGENTS.md with AI coding agent guidelines
- Enhanced documentation structure

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-3.5.1...HEAD

---

## Version 3.5.1 (2025-07-22)

**Build & Tooling Updates:**
- Upgraded Maven wrapper from 3.5.0 to 3.9.11
- Updated Maven plugins:
  - maven-compiler-plugin: 3.10.1 → 3.13.0
  - maven-javadoc-plugin: 3.2.0 → 3.10.1
  - maven-source-plugin: 3.2.1 → 3.3.1
  - maven-surefire-plugin: 3.0.0-M6 → 3.5.1
  - maven-assembly-plugin: 3.3.0 → 3.7.0
  - maven-gpg-plugin: 1.6 → 3.2.6
  - JUnit Jupiter: 5.7.0 → 5.9.2

**Publishing Changes:**
- Removed Sonatype OSS parent POM dependency
- Added central-publishing-maven-plugin (0.8.0) for Maven Central deployment
- Simplified release profile configuration
- Moved GPG signing configuration from profile to main build

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-3.5.0...aismessages-3.5.1

---

## Version 3.5.0 (2025-07-xx)

No specific release notes available. See version 3.4.2 for previous changes.

---

## Version 3.4.2 (2024-01-20)

Maintenance release with dependency and build updates.

---

## Version 3.2.3 (2022-01-11)

**Major Version Update - Java 11 Required**

### Breaking Changes
- **Minimum Java version increased from 8 to 11**
- Removed deprecated TIME_DECODER - use individual ETA field getters instead
- API changes in message validation and error handling

### New Features

**NMEA Tag Block Support:**
- Full support for NMEA 0183 Tag Blocks with parameters c, d, g, n, r, s, t
- New classes: NMEATagBlock, NMEATagBlockParameterCode, TAGBlockParameterCodeType
- Tag blocks accessible via AISMessage.getNmeaTagBlock()

**Application Specific Messages (ASM):**
- New framework for decoding binary application-specific messages
- Implemented ASM decoders: DAC 1 FI 20 (Berthing Data), DAC 1 FI 24 (Extended Ship Static), DAC 1 FI 40 (Persons on Board), DAC 200 FI 10 (Inland Ship Data)
- Access via getApplicationSpecificMessage()

**Enhanced ETA Handling:**
- New getEtaAfterReceived() method returns Optional<ZonedDateTime>
- Individual ETA component getters: getEtaMonth(), getEtaDay(), getEtaHour(), getEtaMinute()

**Raw Data Access:**
- New getRaw*() methods for encoded values: getRawLatitude(), getRawLongitude(), getRawSpeedOverGround(), getRawCourseOverGround(), getRawDraught()

### Infrastructure Changes
- Added module-info.java for Java Platform Module System (JPMS)
- Migrated from java.util.logging.Logger to System.Logger
- Added GitHub Actions workflow for build and test
- Zero runtime dependencies maintained

---

## Version 2.2.3 (2018-06-15)

Maintenance release for Java 8.

---

## Version 2.2.1 (2016-02-25)

Bug fixes and stability improvements. JDK 7 backport available.

---

## Version 2.1.0 (2015-02-03)

**Major Feature: AISInputStreamReader**
- Introduced AISInputStreamReader for simplified stream processing
- Functional interface support with Consumer<AISMessage>
- Communication State decoding for Class A position reports

---

## Version 2.0.2 (2015-01-26)

**First Production-Ready v2 Release**
- Java 8 required (zero-dependency maintained)
- Lazy decoding instead of eager decoding
- Memory-efficient with WeakReference usage
- Better ITU 1371-5 compliance

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

### Latest Stable Version (4.0.0)
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
