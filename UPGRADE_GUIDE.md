# Upgrade Guide: AISmessages 3.3.1 to 4.1.0

This guide provides step-by-step instructions for upgrading projects using AISmessages from version 3.3.1 to 4.1.0. This upgrade includes breaking API changes and requires Java 21.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Breaking Changes](#breaking-changes)
- [Step-by-Step Migration](#step-by-step-migration)
- [Common Issues](#common-issues)
- [Validation](#validation)

## Prerequisites

### Java Version Requirement
AISmessages 4.1.0 requires **Java 21 or later**. Previous versions used Java 11.

**Action Required:**
1. Update your JDK to Java 21
2. Update your build configuration

### Maven Configuration
Update your `pom.xml`:

```xml
<!-- Update compiler plugin -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>21</source>
        <target>21</target>
    </configuration>
</plugin>

<!-- Update surefire plugin for Java 21 support -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.5</version>
    <!-- your configuration -->
</plugin>

<!-- Update dependency -->
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.0</version>
</dependency>
```

### Module System Changes
If you're using Java modules (module-info.java), update the module requirement:

```java
// Before
requires dk.tbsalling.ais.messages;

// After
requires aismessages;
```

AISmessages 4.1.0 is now an automatic module, so use the automatic module name instead of the explicit module name.

## Quick Start

For a quick overview of what needs to change:

1. **Update Java to 21** and build plugins
2. **Update AISmessages dependency** to 4.1.0
3. **Replace `MMSI.intValue()`** with `MMSI.getMmsi()`
4. **Replace `Metadata.getReceived()`** with `Metadata.received()`
5. **Handle primitive float returns** for lat/lon/SOG/COG
6. **Replace `NMEAMessage.fromString()`** with constructor
7. **Replace `AISMessage.create()`** with `NMEAMessageHandler`

## Breaking Changes

### 1. MMSI Class API Changes

**What Changed:** The `intValue()` method was removed and replaced with `getMmsi()`.

**Migration:**
```java
// Before
int mmsi = aisMessage.getSourceMmsi().intValue();
long mmsiLong = aisMessage.getSourceMmsi().intValue();

// After
int mmsi = aisMessage.getSourceMmsi().getMmsi();
long mmsiLong = aisMessage.getSourceMmsi().getMmsi();
```

**Find and Replace:**
- Search: `.intValue()`
- Replace: `.getMmsi()`
- Context: Only for MMSI objects

### 2. Metadata Class (Java Record)

**What Changed:** Metadata is now a Java record with record-style accessor methods (no "get" prefix).

**Migration:**
```java
// Before
Instant timestamp = metadata.getReceived();
String source = metadata.getSource();

// After
Instant timestamp = metadata.received();
String source = metadata.source();
```

**Constructor Changes:**
```java
// Before (2 parameters)
new Metadata(String source, Instant received)

// After (6 parameters)
new Metadata(
    Instant received,
    NMEATagBlock nmeaTagBlock,
    NMEAMessage[] nmeaMessages,
    String decoderVersion,
    String bitString,
    String source
)
```

### 3. Primitive Type Returns

**What Changed:** Several methods now return primitive `float` instead of boxed `Float`:
- `getLatitude()`
- `getLongitude()`
- `getSpeedOverGround()`
- `getCourseOverGround()`

**Migration:**

**Simple case:**
```java
// Before
Float lat = dynamicReport.getLatitude();
if (lat != null) {
    // use lat
}

// After
float lat = dynamicReport.getLatitude();
// Note: primitive cannot be null, check for special values like NaN if needed
if (!Float.isNaN(lat)) {
    // use lat
}
```

**Ternary operator pitfall:**
```java
// WRONG - This causes NullPointerException!
return report != null ? report.getLatitude() : null;
// The ternary tries to unbox null to primitive float

// CORRECT - Use explicit if-else
if (report != null) {
    return report.getLatitude();
} else {
    return null;
}
```

**Autoboxing considerations:**
```java
// This works - autoboxing from primitive to Float
Float latitude = dynamicReport.getLatitude();

// But be careful with ternaries mixing types
Float result = condition 
    ? dynamicReport.getLatitude()  // primitive float
    : null;                         // null reference
// This can cause NPE during autoboxing!
```

### 4. NMEAMessage Construction

**What Changed:** Static factory method `fromString()` replaced with constructor.

**Migration:**
```java
// Before
NMEAMessage msg = NMEAMessage.fromString("!AIVDM,1,1,,A,15Mv5v?P00IS0J`A86KTROvN0<5k,0*12");

// After
NMEAMessage msg = new NMEAMessage("!AIVDM,1,1,,A,15Mv5v?P00IS0J`A86KTROvN0<5k,0*12");
```

**Find and Replace:**
- Search: `NMEAMessage.fromString\(`
- Replace: `new NMEAMessage(`

### 5. AISMessage Parsing

**What Changed:** The static `AISMessage.create()` method was removed. Use `NMEAMessageHandler` instead.

**Migration:**

**Single message:**
```java
// Before
AISMessage msg = AISMessage.create(metadata, nmeaMessage);

// After
List<AISMessage> messages = new ArrayList<>();
NMEAMessageHandler handler = new NMEAMessageHandler("SOURCE", messages::add);
handler.accept(nmeaMessage);
AISMessage msg = messages.isEmpty() ? null : messages.get(0);
```

**Multiple messages (e.g., multi-part messages):**
```java
// Before
AISMessage msg = AISMessage.create(nmeaMessage1, nmeaMessage2);

// After
List<AISMessage> messages = new ArrayList<>();
NMEAMessageHandler handler = new NMEAMessageHandler("SOURCE", messages::add);
handler.accept(nmeaMessage1);
handler.accept(nmeaMessage2);
AISMessage msg = messages.isEmpty() ? null : messages.get(0);
```

**Stream processing:**
```java
// Process a stream of NMEA messages
NMEAMessageHandler handler = new NMEAMessageHandler("SOURCE", aisMessage -> {
    // Process each AIS message
    processMessage(aisMessage);
});

// Feed NMEA messages to handler
for (String nmeaLine : nmeaLines) {
    handler.accept(new NMEAMessage(nmeaLine));
}
```

## Step-by-Step Migration

### Step 1: Update Build Configuration

1. Update Java version to 21 in your build tool
2. Update maven-compiler-plugin and maven-surefire-plugin
3. Update AISmessages dependency to 4.1.0
4. Update module-info.java if using JPMS

### Step 2: Fix Compilation Errors

1. Run `mvn clean compile` to identify compilation errors
2. Address errors in this order:
   - MMSI.intValue() → getMmsi()
   - Metadata accessor methods
   - NMEAMessage.fromString() → constructor
   - AISMessage.create() → NMEAMessageHandler
   - Primitive type handling

### Step 3: Fix Runtime Issues

1. Look for ternary operators mixing primitives and null
2. Update any code checking for null on primitive returns
3. Update Metadata constructor calls if creating metadata manually

### Step 4: Update Tests

1. Update test helper methods for message creation
2. Fix test assertions comparing messages (timestamps may differ)
3. Run full test suite: `mvn test`

### Step 5: Validate

1. Run complete build: `mvn clean install`
2. Test your application thoroughly
3. Check for any deprecation warnings

## Common Issues

### Issue 1: NullPointerException with Ternary Operators

**Symptom:** NPE when using ternary operators with primitive returns

**Cause:** Mixing primitive types with null in ternary expressions
```java
// This throws NPE!
return report != null ? report.getLatitude() : null;
```

**Solution:** Use explicit if-else blocks
```java
if (report != null) {
    return report.getLatitude();
} else {
    return null;
}
```

### Issue 2: Cannot Find Symbol - intValue()

**Symptom:** Compilation error on MMSI.intValue()

**Solution:** Replace with getMmsi()
```java
// Before
aisMessage.getSourceMmsi().intValue()

// After
aisMessage.getSourceMmsi().getMmsi()
```

### Issue 3: Module Not Found

**Symptom:** "module not found: dk.tbsalling.ais.messages"

**Solution:** Update module-info.java to use automatic module name
```java
// Before
requires dk.tbsalling.ais.messages;

// After
requires aismessages;
```

### Issue 4: Test Failures Due to Timestamps

**Symptom:** Tests comparing full AISMessage objects fail due to different timestamps

**Solution:** Compare message content instead of full objects
```java
// Instead of comparing full messages
assertEquals(expectedMessage, actualMessage);

// Compare raw NMEA strings or specific fields
assertEquals(
    expectedMessage.getMetadata().nmeaMessages()[0].getRawMessage(),
    actualMessage.getMetadata().nmeaMessages()[0].getRawMessage()
);
```

### Issue 5: Unsupported Class File Version

**Symptom:** "Unsupported class file major version 65"

**Solution:** Update maven-surefire-plugin to 3.2.5 or later
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.5</version>
</plugin>
```

## Validation

After completing the migration, verify:

1. ✅ Project compiles without errors: `mvn clean compile`
2. ✅ All tests pass: `mvn test`
3. ✅ Full build succeeds: `mvn clean install`
4. ✅ Application starts and runs correctly
5. ✅ No deprecated API warnings in logs

## Code Review Checklist

When reviewing code after migration:

- [ ] All `intValue()` calls replaced with `getMmsi()`
- [ ] All `getReceived()` calls replaced with `received()`
- [ ] All `fromString()` calls replaced with constructor
- [ ] All `create()` calls replaced with `NMEAMessageHandler`
- [ ] No ternary operators mixing primitives and null
- [ ] Test comparisons updated for metadata differences
- [ ] Java version set to 21 in build configuration
- [ ] Maven plugins updated for Java 21 support

## Additional Resources

- [AISmessages 4.1.0 Release Notes](https://github.com/tbsalling/aismessages/releases/tag/4.1.0)
- [Java 21 Migration Guide](https://docs.oracle.com/en/java/javase/21/migrate/)
- [AISutils PR #XX - Example Migration](link-to-this-pr)

## Getting Help

If you encounter issues not covered in this guide:

1. Check the [AISmessages issue tracker](https://github.com/tbsalling/aismessages/issues)
2. Review the [AISutils example migration PR](link-to-this-pr)
3. Open an issue with specific error messages and code samples

## Version History

- **v1.0** (2025-10-19): Initial version covering 3.3.1 to 4.1.0 migration
