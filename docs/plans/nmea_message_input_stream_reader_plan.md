# Plan: Harden `NMEAMessageInputStreamReader` Resource Handling

## Objectives
- Ensure `NMEAMessageInputStreamReader` releases IO resources deterministically when constructed around an `InputStream`.
- Remove unchecked casts when adapting list-based inputs to a queue, eliminating potential `ClassCastException`s and compiler warnings.
- Preserve existing public APIs and caller expectations while improving safety for long-lived ingestion scenarios.

## Current Observations
- The `InputStream` constructor wraps the stream in a `BufferedReader` captured by a `Supplier`, but the reader is never closed. The class also lacks an explicit `close` hook, so long-running readers can leak descriptors.
- When the reader is created from a `List`, the implementation performs an unchecked cast to `Queue`, relying on runtime type knowledge.
- Tests currently cover basic consumption paths but do not assert resource cleanup or generic type safety.

## Proposed Steps
1. **Refine Constructor Resource Management**
   - Introduce a `Closeable` field (or similar) that tracks the `BufferedReader` when constructed from an `InputStream`.
   - Provide a `close()` method and implement `AutoCloseable`, allowing callers (and `AISInputStreamReader`) to shut down the underlying stream.
   - Optionally employ try-with-resources inside `run()` or ensure `close()` is invoked during `requestStop()`/`run()` exit without breaking existing behavior.

2. **Encapsulate Supplier Creation**
   - Extract helper methods for creating suppliers from queues and input streams to keep constructors concise.
   - Guarantee thread-safety considerations remain unchanged; the reader is effectively single-threaded today.

3. **Eliminate Unchecked Cast**
   - Instead of casting `List` to `Queue`, check for `Collection` types that support `poll()` or copy into a new `ArrayDeque`/`LinkedList` to maintain FIFO order.
   - Update generics to avoid suppressing warnings.

4. **Update Callers & Lifecycle**
   - Audit `AISInputStreamReader` and `NMEAMessageSocketClient` to ensure they close the reader when stopping.
   - Consider adding documentation/comments about lifecycle expectations.

5. **Enhance Tests**
   - Extend unit tests to verify the reader closes its resources (e.g., custom `InputStream` that tracks `close()` calls).
   - Add tests covering `List` inputs to ensure no unchecked casts and that iteration order remains intact.

6. **Documentation & Migration Notes**
   - Document the new `close()` contract (if added) in Javadoc.
   - Note any behavioral changes (e.g., reader must be closed) in README or changelog if applicable.

## Risks & Mitigations
- **Breaking change concerns:** Adding `AutoCloseable` should be backward compatible if existing methods remain. Ensure default behavior still works for callers who never close; maybe guard `close()` to be idempotent.
- **Performance:** Copying `List` into a queue has minor overhead; document and justify with safety benefits.
- **Testing complexity:** Use simple fake streams to confirm closures without introducing flaky behavior.

## Definition of Done
- `NMEAMessageInputStreamReader` safely manages its IO resources with explicit lifecycle control.
- No unchecked warnings remain in the class.
- Unit tests cover new behavior.
- Documentation reflects lifecycle expectations for clients.
