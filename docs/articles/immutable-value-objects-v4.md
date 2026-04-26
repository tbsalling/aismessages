# Immutable value objects in AISmessages v4

**Published:** 2025-10-19
**Updated:** 2026-04-26

AISmessages 4.0.0 introduced immutable value objects for AIS message classes and moved parsing responsibilities out of the data model.

See also the [v4.0.0 release notes](../../RELEASE_NOTES.md#version-400).

## Why change mutability and datatypes?

- **Correct value semantics**
  - true immutability across message types
  - safe use in sets, maps, de-duplication, and caches
- **Thread-safety by design**
  - no defensive copying or locking
  - instances can be shared freely across threads
- **Fewer bugs and simpler reasoning**
  - no state changes after construction
  - easier debugging and testing
- **Clear separation of concerns**
  - message classes are pure data carriers
  - parsing is handled by dedicated utilities such as `BitStringParser`

## What changed in v4

- All AIS message classes became immutable value objects using Lombok `@Value`.
- `equals()` and `hashCode()` support was added across message types.
- Parsing responsibilities were removed from the value objects and centralized in parsing utilities.
- Datatypes were reviewed for stable value semantics and mutation safety.
- Lombok was introduced as a compile-time-only dependency, while runtime dependencies remained at zero.

## Impact for users

- Treat decoded message instances as read-only.
- Existing code that mutated message fields must be refactored to construct new instances or use parser output differently.
- Collection-based operations benefit from stable equality and hashing semantics.

## Why this matters operationally

This architectural shift reduced garbage-collection pressure and allocation churn, especially in high-throughput systems that process thousands of AIS messages per second. It also aligns the data model better with concurrent and reactive usage patterns.

The current codebase continues to build on this architecture. The project targets Java 21, keeps Lombok as a provided dependency, and uses the immutable message model consistently across the decoding pipeline.

## Related documentation

- [AIS Application-Specific Messages](application-specific-messages.md)
- [README.md](../../README.md)
