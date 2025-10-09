# Plan: Streamline Bit-Decoding Helpers

## Current Pain Point
- Several decoders in `Decoders` rely on regex-driven replacements (`String.replaceAll`) and boxed `Boolean` constants, which generate garbage in tight loops (`src/main/java/dk/tbsalling/aismessages/ais/Decoders.java`).
- The helpers mix sign handling, string manipulation, and trimming logic, making it hard to reason about performance hot spots.

## Goal
Rework the helpers to use primitive operations (char arrays, bit twiddling, small reusable buffers) while keeping the API signatures intact for downstream consumers.

## Key Tasks
1. **Catalog heavy operations** – Identify every helper using regex replacements (`INTEGER_DECODER`, `STRING_DECODER`) or boxed constants (`STRIP_ALPHA_SIGNS`).
2. **Design primitive alternatives** – Sketch replacements using loops over `char[]` or `byte[]`, e.g., implement two’s complement manually for `INTEGER_DECODER` without intermediate strings.
3. **Implement incremental refactors** – Replace regex calls with helper methods that operate on temporary buffers, and convert `STRIP_ALPHA_SIGNS` into a primitive flag (`boolean`).
4. **Ensure thread safety** – Avoid shared mutable buffers by using local arrays or thread-safe caches; document any stateful utilities.
5. **Strengthen tests** – Extend decoder unit tests to cover negative values, trimming behavior, and Unicode edge cases to ensure functional parity.
6. **Measure improvements** – Compare allocation profiles before/after using JMH or Java Flight Recorder snapshots, capturing evidence for the performance gains.

## Risks & Mitigations
- *Risk:* Manual bit operations may introduce subtle bugs. *Mitigation:* Write focused unit tests and compare outputs with the legacy implementation during development.
- *Risk:* Refactoring could regress whitespace trimming semantics. *Mitigation:* Lock existing behavior with snapshot tests before changing the implementation.
