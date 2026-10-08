---
name: release
description: >
  Guide for creating a new release of aismessages and publishing it to Maven Central.
  Use this skill when asked to make a release, create a new version, bump the version,
  publish to Maven Central, or cut a release.
allowed-tools: shell
---

# Release Skill for aismessages

Follow these steps in order. Do not skip steps. Abort with a clear error message if any step fails.

> **Important:** This project does NOT use `maven-release-plugin` (deprecated 2025-06-30).
> The release process is: manually update files → `./mvnw deploy` → publish via Maven Central portal.

---

## Step 1 — Read the current version

Run this command to get the current project version:

```
./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout
```

The output will be something like `5.0.0-SNAPSHOT`. Record it.

---

## Step 2 — Analyze changes since the last release tag

> **Important:** Do not rely on commit messages alone — they may be inaccurate or incomplete.
> You must inspect the actual code changes to determine what has changed.

First, find the last release tag:

```
git describe --tags --abbrev=0 --match "aismessages-*"
```

Then inspect the full diff of every changed file since that tag:

```
git diff $(git describe --tags --abbrev=0 --match "aismessages-*") HEAD -- src/main/java
```

Study the diff carefully. Focus on the public API surface under `src/main/java/dk/tbsalling/aismessages/`:

- **Removed or renamed public classes, interfaces, methods, constructors, or fields** → MAJOR
- **Changed signatures of existing public methods or constructors** → MAJOR
- **Raised minimum Java version** (check `pom.xml` `maven.compiler.release`) → MAJOR
- **New public classes, interfaces, methods, or constructors added** (with no removals) → MINOR
- **Changes confined to private/package-private code, tests, docs, or `pom.xml` dependency versions** → PATCH

Also read `RELEASE_NOTES.md`: the current SNAPSHOT section documents intent; use it as a cross-check against the diff, not as the primary source of truth.

---

## Step 3 — Recommend a version and ask for confirmation

Present your analysis and recommendation to the user. For example:

> Based on the commits since `aismessages-4.1.2`:
> - Breaking API changes detected (BitString replaces String, Java version raised to 25)
> - **Recommendation: MAJOR bump → `5.0.0`**
>
> Confirm version bump type: **major** / minor / patch — or enter a custom version number.

Wait for the user to confirm or override before continuing.

---

## Step 4 — Compute the release version

Strip `-SNAPSHOT` from the current version and apply the confirmed bump:

- **patch**: `X.Y.Z-SNAPSHOT` → `X.Y.Z`
- **minor**: `X.Y.Z-SNAPSHOT` → `X.(Y+1).0`
- **major**: `X.Y.Z-SNAPSHOT` → `(X+1).0.0`
- **custom**: use exactly what the user specified

If the user confirmed "patch" (the default when SNAPSHOT already has the right base), the release version is simply the current version with `-SNAPSHOT` removed.

---

## Step 5 — Update `RELEASE_NOTES.md`

Edit `RELEASE_NOTES.md`:

1. Find the line that starts with `## Version X.Y.Z-SNAPSHOT` (the top SNAPSHOT entry).
2. Replace it with `## Version X.Y.Z` (using the release version from Step 4).
3. Directly below the version heading, add a **Release Date** line:
   ```
   **Release Date:** YYYY-MM-DD
   ```
   Use today's date.
4. Update the **Full Changelog** link at the bottom of that section to point from the previous release tag to the new tag:
   ```
   **Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-PREV...aismessages-NEW
   ```

---

## Step 6 — Update `pom.xml`

Edit `pom.xml`:

Find the top-level `<version>` element (the third line of the file, value like `5.0.0-SNAPSHOT`) and replace it with the release version (e.g., `5.0.0`). Do not change any dependency or plugin version elements.

Verify the change is correct by running:

```
./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout
```

The output must match the release version exactly (no `-SNAPSHOT`).

---

## Step 7 — Run the tests

Run:

```
./mvnw -DskipITs test
```

If the tests fail, **abort the release**. Report the failure and do not proceed to Step 8. The user must fix the tests before retrying.

---

## Step 8 — Commit the release changes

```
git add pom.xml RELEASE_NOTES.md
git commit -m "Release X.Y.Z"
```

---

## Step 9 — Create the annotated tag

```
git tag -a aismessages-X.Y.Z -m "Release X.Y.Z"
```

Use the exact release version number.

---

## Step 10 — Push commit and tag

```
git push
git push origin aismessages-X.Y.Z
```

---

## Step 11 — Deploy to Maven Central

Run these three commands in sequence, stopping if any fails:

```
mvn clean
mvn package
mvn deploy
```

This compiles, signs (GPG), and uploads the artifacts to the Maven Central portal staging area.

> **If GPG signing fails** with "No pinentry" or similar, advise the user to unlock their GPG agent:
> ```
> killall gpg-agent && gpg-agent --daemon --pinentry-program /opt/homebrew/bin/pinentry
> ```
> Then retry `mvn package` and `mvn deploy`.

Wait for the command to finish successfully before continuing.

---

## Step 12 — Remind the user to publish on Maven Central

Inform the user:

> ✅ Artifacts uploaded. To make the release publicly available on Maven Central:
>
> 1. Go to https://central.sonatype.com/publishing
> 2. Find the pending deployment
> 3. Click **Publish**
>
> After clicking Publish, it may take up to 2 hours for the artifacts to appear in Maven Central search.

Wait for the user to acknowledge before proceeding to Step 13.

---

## Step 13 — Bump to the next development version

Compute the next development version (patch + 1 with `-SNAPSHOT`):
- `5.0.0` → `5.0.1-SNAPSHOT`

Edit `pom.xml`: replace the release version with the next SNAPSHOT version.

Edit `RELEASE_NOTES.md`: add a new SNAPSHOT section at the very top (above the just-released section):

```markdown
## Version 5.0.1-SNAPSHOT

**Development Version**

_No changes yet._

---
```

Commit and push:

```
git add pom.xml RELEASE_NOTES.md
git commit -m "Prepare next development iteration 5.0.1-SNAPSHOT"
git push
```

---

## Done

The release is complete. The repository is now back in a development state at the next SNAPSHOT version.
