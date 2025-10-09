# Plan: Optimize Six-Bit Decoding Tables

## Current Pain Point
- `AISMessage.toBitString` iterates the payload character-by-character and performs `TreeMap` lookups via `charToSixBit`, which stores string keys and values (`src/main/java/dk/tbsalling/aismessages/ais/messages/AISMessage.java`).
- `Decoders.STRING_DECODER` uses another `TreeMap` (`SIX_BIT_ASCII`) for reverse lookups, adding allocation overhead during tight decoding loops.

## Goal
Replace the map-backed lookups with array- or primitive-based tables that can be indexed in constant time without per-character allocations, while keeping behavior identical.

## Key Tasks
1. **Profile hotspots** – Use JMH or microbenchmarks around `toBitString`/`STRING_DECODER` to measure current throughput and allocation rates.
2. **Design primitive tables** – Replace `charToSixBit` with a `char[]` or `String[]` indexed by the raw ASCII value (e.g., using a 128-length array) and precompute padding masks.
3. **Refactor decoding loops** – Update `toBitString` to operate on the `char[]` of the input string, appending precomputed bit patterns without creating substrings.
4. **Mirror improvements in `Decoders`** – Provide an `int -> char` array (or `byte[]`) for `SIX_BIT_ASCII` and adjust `STRING_DECODER` to use primitive indexing.
5. **Validate correctness** – Expand unit tests to cover full ASCII range, invalid characters, and padding handling; ensure round-trip conversions still pass.
6. **Benchmark post-change** – Re-run the initial benchmarks to document performance gains and include results in the plan’s follow-up report.

## Risks & Mitigations
- *Risk:* Array indexing may throw if unexpected characters appear. *Mitigation:* Add bounds checks and explicit exceptions for out-of-range values.
- *Risk:* Refactoring both directions simultaneously could hide regressions. *Mitigation:* Stage changes with granular commits and rely on tests between steps.
