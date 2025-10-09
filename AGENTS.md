# Working with AI Coding Agents

This repository is a Java project built with Maven. When collaborating with any AI coding agent, follow these guidelines for effective results:

## Project Overview

- **Language & build tool:** Java 8+ with Maven (`mvnw`, `pom.xml`)
- **Source code:** `src/main/java`
- **Tests:** `src/test/java`
- **Key packages:** AIS message decoder in `dk.tbsalling.aismessages`; demos in `dk.tbsalling.aismessages.demo`

## Using AI Agents Effectively

1. **Share context:**
   - State your task clearly
   - Provide relevant files/snippets
   - Specify build/test commands

2. **Structured prompts:**
   - Break tasks into steps
   - Review and agree on plans before code changes
   - Request unified diffs

3. **Planning files:**
   - For any medium or large task, capture the agreed-upon plan in a `planning.md` file before implementing code changes.
   - Store planning files under `planning/<task-name>/planning.md` (create the folder if it does not exist).
   - Keep each planning file short (1-2 pages) and include sections for context, goals, milestones, and open questions.

4. **Minimal diffs:**
   - Focus on single concerns per change
   - Ask for diffs that apply cleanly

5. **Tight feedback loop:**
   - Run tests after changes
   - Report errors or failures

6. **Document changes:**
   - Update docs or comments when behavior changes

## Build & Test

- Full build: `./mvnw clean verify`
- Quick test: `./mvnw -DskipITs test` or `./mvnw -Dtest=ClassName test`
- No external runtime dependencies

## Code Style

- Standard Java conventions (4-space indent, meaningful names, prefer immutability)
- Avoid unnecessary dependencies
- Keep public APIs backward compatible

## Pull Request Checklist

- [ ] All tests pass locally
- [ ] Docs/changelog updated if needed
- [ ] PR summary includes motivation, approach, results
- [ ] Note any follow-up tasks