# Plan: Upgrade project to Java 25

## Context
- Current project targets Java 8+ and uses Maven with the wrapper.
- Java 25 (LTS) introduces new language features and may require dependency and plugin updates for compatibility.
- Maven compiler, surefire, failsafe, and other build plugins may need version bumps to recognise Java 25 release values.

## Goals
1. Update the build configuration so the project compiles and runs tests with Java 25.
2. Ensure CI/build tooling recognises the new Java version (compiler, documentation, wrappers).
3. Verify project documentation reflects the upgraded Java requirement.

## Milestones & Tasks
1. **Assess current configuration**
   - Inspect `pom.xml` for `maven.compiler.source/target` or toolchain configuration.
   - Identify plugins and dependencies that may need upgrading (compiler, surefire, failsafe, jacoco, etc.).
2. **Upgrade build tooling**
   - Bump `maven-compiler-plugin` (and related plugin) versions to ones supporting Java 25.
   - Adjust source/target compatibility to `25` or appropriate release configuration.
   - Update the Maven wrapper distribution if necessary to a version tested with Java 25.
3. **Update dependencies**
   - Check for libraries that may not support Java 25; upgrade them to the latest compatible releases.
4. **Adjust documentation**
   - Update README or other docs to state Java 25 requirement.
5. **Verification**
   - Run `./mvnw -version` to confirm Maven sees Java 25.
   - Execute `./mvnw clean verify` under Java 25 to ensure tests/build succeed.

## Risks & Open Questions
- Are there transitive dependencies using APIs removed or restricted in Java 25?
- Do we need to configure module system or preview features for any components?
- Will CI environments support Java 25 immediately, or do we need fallback instructions?

## Status
- [x] Build configuration, dependencies, and documentation updated for Java 25 support.
- [x] Remote `master` history merged into the working branch to ensure compatibility.
