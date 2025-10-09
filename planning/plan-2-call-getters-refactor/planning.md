# Plan: Refactor `callGetters` Utility for Type Safety and Logging

## Current Pain Point
- `AISMessage#callGetters` accepts a raw `Map` and swallows reflection failures by printing stack traces, reducing observability (`src/main/java/dk/tbsalling/aismessages/ais/messages/AISMessage.java`).
- The helper mixes type discovery, recursion, and error reporting responsibilities, which complicates maintenance.

## Goal
Provide a type-safe, testable mechanism for extracting getter values that integrates with the existing logging strategy and surfaces actionable errors to callers.

## Key Tasks
1. **Inventory call sites** – Confirm `dataFields()` is the only public entry point and document expectations for nested messages and collections.
2. **Define typed contract** – Change `callGetters` to accept `Map<String, Object>` (or create a new helper returning an immutable map) and propagate generics through recursive calls.
3. **Improve logging** – Replace `printStackTrace` with `LOG.log(Level, message, exception)` using context such as the getter name and target class.
4. **Harden exception flow** – Distinguish between `IllegalAccessException` and `InvocationTargetException`, wrapping them in a domain-specific runtime exception so decoding failures bubble up predictably.
5. **Add coverage** – Extend `AISMessageTest` (or create a new dedicated test) to validate successful extraction, nested objects, and failure scenarios (e.g., throwing getter) using the new contract.

## Risks & Mitigations
- *Risk:* Changing the return type could break API consumers. *Mitigation:* Keep `dataFields()` signature unchanged by adapting the new helper internally and documenting behavior.
- *Risk:* Logging may become too verbose. *Mitigation:* Use `LOG.isLoggable(...)` checks or appropriate log levels for rare failure paths.
