# Plan: Eliminate Static Initializer Side Effects in `AISMessage`

## Current Pain Point
- `AISMessage` defines a static initializer that prints a licensing banner directly to `System.err` when the class is loaded (`src/main/java/dk/tbsalling/aismessages/ais/messages/AISMessage.java`).
- The project already exposes a `System.Logger` instance (`private transient static final System.Logger LOG`), so emitting console output at class-load time is inconsistent with the rest of the codebase.

## Goal
Ensure the library does not emit unsolicited output on class load while still communicating licensing details through explicit, opt-in flows.

## Key Tasks
1. **Confirm usage** – Search for other classes with static initializers or banner output to avoid regressing functionality elsewhere.
2. **Introduce opt-in logging** – Replace the `System.err` banner with a method (e.g., `logVersionBanner()`) that uses the existing `LOG` logger, and call it only from demos/tests that intentionally print the notice.
3. **Preserve metadata** – Retain the banner text in a reusable constant so downstream tools can still display it when desired.
4. **Add guardrails** – Write a regression test (likely in `AISMessageTest`) that loads the class via reflection and asserts no output is emitted to `System.err` by default.
5. **Update docs** – Mention the behavioral change in README or release notes to set expectations for consumers relying on the legacy banner.

## Risks & Mitigations
- *Risk:* Some users may expect the banner automatically. *Mitigation:* Provide a documented helper and update demos to invoke it explicitly.
- *Risk:* Logging at class init might still occur if static code paths remain. *Mitigation:* Verify with integration tests and local smoke tests that class loading is silent.
