# AISmessages Release Notes

Developer-oriented release notes for AISmessages - a Java-based library for decoding AIS messages from digital VHF radio traffic.

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

### Latest Version (3.5.1)
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
