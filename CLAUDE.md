# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

AISmessages is a Java library that decodes NMEA-armoured AIS messages (ITU 1371) used in maritime navigation and safety. It is published on Maven Central as `dk.tbsalling:aismessages` and is intentionally zero runtime dependencies, lightweight, and high-throughput. Lombok is `provided`/`optional`-scoped, so it does not become a runtime dependency.

The reference text is Recommendation ITU-R M.1371-6 (02/2026); message layouts are its Annex 7 tables and application-specific messages its Annex 4. A local copy of the PDF may be kept at `docs/itu/`, which is git-ignored because the ITU copyright forbids redistribution.

Java release target is set by `maven.compiler.release` in `pom.xml` (currently 25). Lombok is used heavily in the codebase (`@Value`, `@Getter`, `@ToString`, `@EqualsAndHashCode`, `@Log`); make sure your IDE has Lombok support enabled. Since JDK 23, javac no longer discovers annotation processors on the classpath, so Lombok is declared explicitly in `<annotationProcessorPaths>` on `maven-compiler-plugin` (and alongside `jmh-generator-annprocess` in the `bench` profile) — any new annotation processor must be added there or it will silently not run.

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

1. **Transport** — `dk.tbsalling.aismessages.AISInputStreamReader` is the public entry point. It wraps `NMEAMessageInputStreamReader` (line-oriented stream / `List<String>` queue) and exposes a `Consumer<? super AISMessage>` callback. A sibling transport under `nmea/` is `NMEAMessageUDPSocket` (UDP). `AISMessageDecoder` is the synchronous, non-stream facade over the same pipeline: `decode(...)` throws on undecodable input, `tryDecode(...)` never throws and returns `Optional` (empty always means "nothing decoded", whatever the cause). `AISMessageRouter` is a ready-made `Consumer<AISMessage>` that dispatches to per-type handlers (most specific registered class/interface wins, resolved once in `build()`).
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
- **Minimal allocation.** Prefer primitives over boxed types to keep allocation churn low, within the field rules below.
- **Public API is backward compatible.** Breaking changes to anything under `dk.tbsalling.aismessages` need an explicit, deliberate call-out — this is a library consumed in production by external systems.

### Message field types: no magic numbers

AISmessages is a decoding library: its public API exposes decoded, self-explanatory values, so users need no knowledge of ITU-R M.1371 codes or bit layouts.

1. **Units.** Getters return values in the maritime standard units of ITU-R M.1371: degrees (coordinates, course, heading), knots (speed), metres (distances, draught, altitude), degrees per minute (rate of turn), and seconds. Never transmitted steps such as 1/10 min, 1/10 knot or 1/10 m. The Javadoc names the transmitted unit for maintainers.
2. **Decimals:** primitive `double`/`float` fields (coordinates, speed, course, rate of turn, draught) are `NaN` when not available. Boxed `Double`/`Float` fields, used only where a part can be absent, are `null` when not available or absent — never `NaN`.
3. **Integers with a not-available code** (heading, time-stamp second, altitude, date and time parts, ETA parts) are `Integer`, `null` when not available. Integers that are always valid stay primitive.
4. **Parts that may be absent** (optional trailing parts, variant-only fields, the other Message 24 part) are `null`. Never use -1 or another made-up sentinel.
5. **A field with more than one special value gets a companion `<Field>Indicator` enum** (e.g. `RateOfTurnIndicator`, `SpeedOverGroundIndicator`, `TimeStampIndicator`). The value is `NaN`/`null` unless the indicator says it holds one; for "X or higher" it holds that lower bound. A field with a single not-available code needs no indicator.
6. **No raw values.** Do not expose transmitted values next to decoded ones (no `getRaw*`). Fields that ITU-R M.1371 itself defines as spare, regional reserved, radio status or AtoN status bits stay as `int`, documented per their table.
7. **Coded values are enums**, `null` for undefined codes or absent parts. **Every enum corresponds to a table or field description of ITU-R M.1371-6, cited in its class Javadoc**, and each constant states the code(s) it stands for.
8. **Text** transmitted as all "@" (not available) is `null`, not "".
9. **Interfaces** declare the most primitive type all implementers can satisfy.
10. **Decode eagerly.** Convert codes in `AISMessageFactory` or the constructor; keep the code constants in the package-private `FieldDecoder`.
11. **Allocation.** Box only through autoboxing (`Integer.valueOf`); no custom caches and no `Optional`/`OptionalInt`. A change that adds objects to the hot-path messages (1-3, 18, 19, 27) needs a `-prof gc` JMH comparison of bytes allocated per operation.

## Conventions

- 4-space indent; standard Java style; match the surrounding file's indentation/whitespace exactly so diffs stay reviewable.
- Single concern per change/PR. Avoid reformatting unrelated code.
- Update JavaDoc when public API behavior changes.
- In the message model (`ais/messages`, `ais/messages/types`, `ais/messages/asm`) every class, field and enum constant has Javadoc. Message classes start with `Message N: <name> (ITU-R M.1371-6 § A7-3.x, Table T).`; fields with `Bits a-b (Table T): <meaning>; <units>; <what NaN/null/the indicator means>.`; converted fields name the transmitted unit; ASM classes cite IMO SN.1/Circ.289 (DAC/FI) or Annex 4.
- Never write `{@link #getX()}` to a Lombok-generated getter: javadoc runs on the undelomboked source and fails on the unresolved reference. Use `{@code getX()}` instead.

## Release

Release procedure is in `.github/skills/release/SKILL.md`. Releases are signed (GPG) and published to Maven Central via
the `central-publishing-maven-plugin`; tag format is `aismessages-x.y.z`. Update `RELEASE_NOTES.md` and drop the
`-SNAPSHOT` suffix in `pom.xml` before tagging.
