# Maintaining AISmessages with Claude

This repository is a Java project built with Maven. When you collaborate with Claude to make changes, keep the following guidelines in mind so the assistant can be effective:

## Project overview
- **Language & build tool:** Java 8+ with Maven (`mvnw` and `pom.xml` provided).
- **Primary module:** Source code lives under `src/main/java`. Tests are under `src/test/java`.
- **Key entry points:** The `dk.tbsalling.aismessages` packages contain the AIS message decoder. Demo applications live in `dk.tbsalling.aismessages.demo`.

## Working with Claude
1. **Share context up front.** Provide Claude with:
   - The task you want to accomplish.
   - Any relevant source files or code snippets (use `rg` to locate files quickly).
   - Build or test commands you expect to run.
2. **Use structured prompts.** Break work into steps so Claude can reason carefully:
   - Ask for a plan before requesting code changes.
   - Review the plan together and iterate if something looks off.
   - Only after agreeing on the approach, ask Claude to draft patches or specific diffs.
3. **Prefer minimal diffs.** Claude works best when you:
   - Request changes for a single concern at a time.
   - Ask for unified diffs (`diff --git` format) that apply cleanly with `patch` or `git apply`.
4. **Keep the feedback loop tight.** After applying a patch:
   - Run the relevant tests (see below) and share outputs.
   - Let Claude know about any compilation errors or test failures so it can suggest fixes.
5. **Document changes.** Encourage Claude to update README sections or javadoc comments when behaviour changes.

## Build & test reminders
- Use the Maven wrapper: `./mvnw clean verify` to run the full build.
- For quicker checks, run `./mvnw -DskipITs test` or target specific test classes with `./mvnw -Dtest=ClassName test`.
- The project has zero external runtime dependencies, so builds should succeed offline.

## Code style notes
- The codebase follows standard Java conventions (4-space indentation, meaningful naming, immutable data where possible).
- Avoid adding new external dependencies unless necessary.
- Keep public APIs backward compatible; prefer adding new classes or methods instead of changing signatures.

## Pull request checklist
Before opening a PR with Claude:
- [ ] Ensure all tests pass locally using the Maven wrapper.
- [ ] Update documentation and changelog entries if behaviour changes.
- [ ] Provide a clear summary of the motivation, approach, and testing results.
- [ ] Include any follow-up tasks or TODOs discovered during the work.

Following these practices will help Claude remain productive while maintaining AISmessages.
