# Performance analysis — String → BitString refactor

**Published:** 2026-06-02
**Updated:** 2026-06-02

AISmessages historically represented decoded AIS payload bits as a `String` of `'0'`/`'1'` characters. In v4.1.3 this
was replaced by a packed, immutable `long[]`-backed `BitString` with typed accessors.

This article documents the measured impact of that internal change: **significantly faster field extraction**, **faster
end-to-end decoding**, and **substantially reduced retained heap**, especially for large binary payloads.

## Summary

| Dimension                                         | Before        | After        | Improvement      |
|---------------------------------------------------|---------------|--------------|------------------|
| `getUnsignedInt` 6 bits (message type)            | 10.95 ns/op   | 0.83 ns/op   | **13.2×**        |
| `getUnsignedInt` 30 bits (MMSI)                   | 29.09 ns/op   | 0.85 ns/op   | **34.4×**        |
| `getSignedInt` 28 bits (longitude, cross-word)    | 26.59 ns/op   | 0.84 ns/op   | **31.5×**        |
| `getSixBitAsciiString` 120 bits (ship name)       | 647.56 ns/op  | 44.71 ns/op  | **14.5×**        |
| End-to-end `AISMessageFactory.create` (decodeOne) | 1195.30 ns/op | 110.03 ns/op | **10.9×**        |
| Retained heap at 168 bits (typical message)       | 208 bytes     | 72 bytes     | **2.9× smaller** |
| Retained heap at 1100 bits (max binary payload)   | 1144 bytes    | 192 bytes    | **6.0× smaller** |

The end-to-end speedup is larger than initially projected (2–3×) because the prior decoding path relied on per-field
`String.substring` + `Integer.parseUnsignedInt(..., 2)` which dominated overall runtime and allocation pressure.

## What changed

### Old representation (pre v4.1.3)

- Payload bits represented as a `String` of `'0'`/`'1'`.
- Field extraction typically looked like:
    - `string.substring(begin, end)`
    - `Integer.parseUnsignedInt(substring, 2)` / `Long.parseLong(...)`
- Six-bit ASCII decoding used relatively allocation-heavy helpers (map lookups, string building and post-processing).

### New representation (v4.1.3+)

- Payload bits represented as an immutable `BitString`:
    - storage: packed `long[]` (64 bits per `long`)
    - access: shift + mask (at most two `long` loads per read)
- Six-bit ASCII decoding uses table lookups and emits a single `String`.

## Methodology

### Hardware / runtime

- **Machine:** Apple M2 Max, macOS 26.4.1
- **JDK:** Temurin OpenJDK 21.0.8 (LTS), HotSpot 64-Bit Server VM
- **JVM flags:** JMH defaults (no `-Xms`/`-Xmx` overrides for these runs)
- **Library:** `dk.tbsalling:aismessages` 4.1.3-SNAPSHOT

### Benchmarks

Source code under `../../src/test/java/dk/tbsalling/aismessages/bench/`:

- `BitStringMicroBenchmark` — per-field accessors (message type, MMSI, longitude, ship name) against a synthetic 168-bit
  position-report payload.
- `AISMessageFactoryBenchmark` — end-to-end `AISMessageFactory.create` on a 20-message corpus of real single-fragment
  NMEA sentences at `../../src/test/resources/bench/sample-nmea.txt` (types 1, 3, 4, 18).
- `RetainedSizeReport` — JOL `GraphLayout` retained-size measurement of `BitString` against the prior `String`
  representation at 72 / 168 / 424 / 1100 bit widths.

### JMH configuration

`@BenchmarkMode(AverageTime)` / `@OutputTimeUnit(NANOSECONDS)`, 1 fork, 2 warmup iterations × 1 s, 3 measurement
iterations × 1 s.

These short windows were used for quick capture; for production-grade precision, rerun with the JMH defaults (5 forks ×
5 × 10 s warmup / 5 × 10 s measurement).

### How to reproduce

From the repository root:

```sh
./mvnw -Pbench test
```

Raw JMH output lands in `target/jmh-result.txt`. The JOL retained-size table is printed to stdout by
`RetainedSizeReport`. The pre-refactor baseline is saved in `target/jmh-baseline.txt` for reference.

## Throughput — micro

| Operation                                  | Old (ns/op) | New (ns/op) | Speedup |
|--------------------------------------------|-------------|-------------|---------|
| `getUnsignedInt(0, 6)` (message type)      | 10.95       | 0.83        | 13.2×   |
| `getUnsignedInt(8, 38)` (30-bit MMSI)      | 29.09       | 0.85        | 34.4×   |
| `getSignedInt(61, 89)` (28-bit longitude)  | 26.59       | 0.84        | 31.5×   |
| `getSixBitAsciiString(0, 120)` (ship name) | 647.56      | 44.71       | 14.5×   |

**Why it improves:** the old path performed a substring allocation plus a character-by-character binary parse loop per
field read. The new path is arithmetic on packed words.

The 28-bit longitude case shows cross-word reads (spanning a 64-bit boundary) remain as fast as in-word reads.

## Throughput — macro / end-to-end

| Benchmark                              | Old (ns/op) | New (ns/op) | Speedup |
|----------------------------------------|-------------|-------------|---------|
| `AISMessageFactory.create` (full path) | 1195.30     | 110.03      | 10.9×   |

The end-to-end benchmark decodes a 20-message corpus (types 1 / 3 / 4 / 18), including NMEA framing, payload decode,
message-subclass construction, and `Metadata` allocation.

A single decode extracts ~15–20 fields. Previously each field read allocated at least one substring, so the `BitString`
change not only reduces CPU but also slashes allocation and downstream GC pressure.

## Retained heap

JOL `GraphLayout.totalSize()` of a single payload instance, comparing the `String` form against `BitString`:

| Payload width | String form | BitString | Ratio |
|---------------|-------------|-----------|-------|
| 72 bits       | 112 bytes   | 64 bytes  | 1.75× |
| 168 bits      | 208 bytes   | 72 bytes  | 2.89× |
| 424 bits      | 464 bytes   | 104 bytes | 4.46× |
| 1100 bits     | 1144 bytes  | 192 bytes | 5.96× |

The advantage widens with payload size because `String` scales with roughly one byte per bit (Java 9+ Latin-1
compression), while `BitString` packs 64 bits per `long`.

At ≥1000 bits (used by `BinaryMessageMultipleSlot` and larger ASMs such as `AreaNotice`,
`MeteorologicalAndHydrographicalData`, and `WeatherObservation`), the saving is ~950 bytes per decoded message.

For a long-running consumer keeping 1 million decoded messages in memory at the typical 168-bit width, the savings are
roughly **136 MB** of heap.

## Allocation

Per-call allocation rate was not captured with `-prof gc` for this in-session run. Reproduce via:

```sh
./mvnw -Pbench test -Djmh.opts="-prof gc"
```

Qualitatively, the new path eliminates:

- the payload-wide `String` produced by the old decode step
- one `String.substring` allocation per extracted field
- intermediate buffers used during the old six-bit ASCII decode

Remaining allocations on the decode path are the `BitString` itself, typed field values, and the standard
message-subclass + `Metadata` object graph.

## Caveats

- JMH numbers are steady-state (post-warmup). Cold-start workloads will compress the relative difference somewhat.
- The macro benchmark uses single-fragment messages only. Multi-fragment reassembly goes through `NMEAMessageHandler`;
  the same per-fragment `BitString` win applies, but fragment buffering is unchanged.
- Benchmark windows are intentionally short here. Rerun with default JMH settings for tighter confidence.

## Appendix — raw JMH output

### Baseline (pre-refactor, `target/jmh-baseline.txt`)

```
Benchmark                                                Mode  Cnt     Score    Error  Units
AISMessageFactoryBenchmark.decodeOne                     avgt    3  1195.303 ± 39.366  ns/op
BitStringMicroBenchmark.newGetSignedInt_28bit_longitude  avgt    3     0.859 ±  0.021  ns/op
BitStringMicroBenchmark.newGetSixBitAsciiString_120bit   avgt    3    44.643 ±  1.520  ns/op
BitStringMicroBenchmark.newGetUnsignedInt_30bit_mmsi     avgt    3     0.857 ±  0.069  ns/op
BitStringMicroBenchmark.newGetUnsignedInt_6bit           avgt    3     0.852 ±  0.190  ns/op
BitStringMicroBenchmark.oldGetSignedInt_28bit_longitude  avgt    3    26.587 ±  1.285  ns/op
BitStringMicroBenchmark.oldGetString_120bit              avgt    3   647.564 ± 86.243  ns/op
BitStringMicroBenchmark.oldGetUnsignedInt_30bit_mmsi     avgt    3    29.090 ±  7.946  ns/op
BitStringMicroBenchmark.oldGetUnsignedInt_6bit           avgt    3    10.953 ±  0.598  ns/op
```

### Post-refactor (`target/jmh-result.txt`)

```
Benchmark                                                Mode  Cnt    Score   Error  Units
AISMessageFactoryBenchmark.decodeOne                     avgt    3  110.034 ± 2.638  ns/op
BitStringMicroBenchmark.newGetSignedInt_28bit_longitude  avgt    3    0.843 ± 0.126  ns/op
BitStringMicroBenchmark.newGetSixBitAsciiString_120bit   avgt    3   44.707 ± 1.681  ns/op
BitStringMicroBenchmark.newGetUnsignedInt_30bit_mmsi     avgt    3    0.846 ± 0.036  ns/op
BitStringMicroBenchmark.newGetUnsignedInt_6bit           avgt    3    0.828 ± 0.041  ns/op
```

### JOL retained-size report

```
AIS payload retained heap size — String vs BitString
bits         | String form (bytes)    | BitString (bytes)      | ratio
--------------------------------------------------------------------------------
72           | 112                    | 64                     | 1.75x
168          | 208                    | 72                     | 2.89x
424          | 464                    | 104                    | 4.46x
1100         | 1144                   | 192                    | 5.96x
```

## Related documentation

- [AISmessages documentation index](../README.md)
- [Immutable value objects in AISmessages v4](immutable-value-objects-v4.md)
- [Release notes](../../RELEASE_NOTES.md)
