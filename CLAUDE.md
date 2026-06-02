# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

AISmessages is a Java library that decodes NMEA-armoured AIS messages (ITU 1371) used in maritime navigation and safety. It is published on Maven Central as `dk.tbsalling:aismessages` and is intentionally zero runtime dependencies, lightweight, and high-throughput. Lombok is `provided`/`optional`-scoped, so it does not become a runtime dependency.

Java release target is set by `maven.compiler.release` in `pom.xml` (currently 21). Lombok is used heavily in the codebase (`@Value`, `@Getter`, `@ToString`, `@EqualsAndHashCode`, `@Log`); make sure your IDE has Lombok support enabled.

## Build & test

Use the Maven wrapper, never a system `mvn`:

- Full build (compiles, tests, packages, signs, assembles): `./mvnw clean verify`
- Tests only: `./mvnw test`
- Quick test skipping integration tests: `./mvnw -DskipITs test`
- Single test class: `./mvnw -Dtest=ClassName test`
- Several classes: `./mvnw -Dtest=BitStringTest,AISMessageFactoryTest test`
- Run JMH benchmarks + JOL retained-size report (separate Maven profile): `./mvnw -Pbench test` (writes
  `target/jmh-result.txt`).

Notes:
- `verify` invokes `maven-gpg-plugin` and will fail without a GPG key. Use `./mvnw test` or `./mvnw package -Dgpg.skip=true` when you only need a local build.
- The `templating-maven-plugin` generates `dk.tbsalling.aismessages.version.Version` at build time from `src/main/java-templates/.../Version.java`. Do not edit the generated file under `target/`; edit the template.
- Surefire sets `java.util.logging.config.file=logging.properties` for tests.

## Architecture

The decoding pipeline is layered. Understanding the seam between NMEA and AIS is the key to navigating the code:

1. **Transport** — `dk.tbsalling.aismessages.AISInputStreamReader` is the public entry point. It wraps `NMEAMessageInputStreamReader` (line-oriented stream / `List<String>` queue) and exposes a `Consumer<? super AISMessage>` callback. Sibling transports under `nmea/` are `NMEAMessageSocketClient` (TCP) and `NMEAMessageUDPSocket` (UDP).
2. **NMEA framing** — `nmea/messages/NMEAMessage` eagerly parses one NMEA sentence (`!AIVDM`/`!AIVDO`), validates its checksum, and exposes fragment metadata. Optional NMEA tag blocks (prefixed `\...\`) are parsed by `nmea/tagblock/NMEATagBlock`.
3. **Fragment reassembly** — `nmea/NMEAMessageHandler` (lenient; logs invalid checksums) and `NMEAMessageHandlerStrict` (rejects them) buffer multi-fragment messages and call `AISMessageFactory.create(...)` once all fragments arrive.
4. **AIS decoding** — `ais/messages/AISMessageFactory` decodes the 6-bit armoured payload into a `ais/BitString` and
   constructs the correct `AISMessage` subclass. `BitString` is a packed `long[]`-backed immutable bit vector (
   MSB-first) providing typed extraction (unsigned/signed ints, longs, floats, six-bit ASCII text, slices) via
   shift+mask in 1–2 long loads per field — see `docs/articles/performance-analysis.md`. `AISMessage` is a sealed
   abstract class; each
   AIS message type is one immutable permitted subclass under `ais/messages/`.
5. **Application Specific Messages (ASM)** — Binary AIS messages (types 6 and 8) carry ASM payloads decoded into `ais/messages/asm/ApplicationSpecificMessage` subclasses (IMO SN.1/Circ.289 DAC=001, plus regional DAC=200). `BinaryBroadcastMessage#getApplicationSpecificMessage()` is the access point. Unknown DAC/FI combinations resolve to `UnknownApplicationSpecificMessage`.

### Design invariants

These are load-bearing — preserve them when changing code:

- **Eager parsing, immutable value objects.** Since v4, decoded `AISMessage` instances are immutable and fully parsed up front (see `docs/articles/immutable-value-objects-v4.md`). Do not reintroduce lazy decoding, `WeakReference`s, or post-construction mutation. Instances must be safe to share across threads.
- **Zero runtime dependencies.** Lombok is compile-time only. Do not add runtime dependencies to `pom.xml`.
- **Prefer primitives over boxed types** in hot paths to keep allocation churn low.
- **Public API is backward compatible.** Breaking changes to anything under `dk.tbsalling.aismessages` need an explicit, deliberate call-out — this is a library consumed in production by external systems.

## Conventions

- 4-space indent; standard Java style; match the surrounding file's indentation/whitespace exactly so diffs stay reviewable.
- Single concern per change/PR. Avoid reformatting unrelated code.
- Update JavaDoc when public API behavior changes.

## Release

Release procedure is in `HOWTO Release.txt`. Releases are signed (GPG) and published to Maven Central via the `central-publishing-maven-plugin`; tag format is `aismessages-x.y.z`. Update `RELEASE_NOTES.md` and drop the `-SNAPSHOT` suffix in `pom.xml` before tagging.
