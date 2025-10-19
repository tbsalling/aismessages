# Copilot Instructions for aismessages

## Project Context
- Java library for decoding NMEA armoured AIS messages (ITU 1371)
- Zero-dependency, lightweight, ultra-efficient eager parsing with immutable value objects
- Java 21+ with Maven build
- Published to Maven Central as `dk.tbsalling:aismessages`

## Code Style
- 4-space indentation
- Prefer immutability and final fields
- Prefer primitive types (int, long, float, double, boolean) over boxed types to minimize memory churn
- Meaningful variable names (no single letters except loop counters)
- No external runtime dependencies
- Backward compatibility is critical for public APIs

## Build & Test
- Build: `./mvnw clean verify`
- Quick test: `./mvnw -DskipITs test`
- Single test: `./mvnw -Dtest=ClassName test`

## PR/Change Guidelines
- Minimal diffs: one concern per change
- Preserve whitespace where possible for clean git history
- Match existing indentation style
- All tests must pass
- Update JavaDoc for public API changes
- Note breaking changes explicitly

## Avoid
- Adding runtime dependencies
- Breaking backward compatibility without discussion
- Reformatting unrelated code
- Overly verbose or enterprise-y patterns
- Unnecessary boxing of primitives
