# Introducing Java modules in AISmessages

**Published:** 2018-12-11
**Updated:** 2026-04-26

This tutorial explains the Java module work around AISmessages and how it relates to the current codebase.

## Current status

AISmessages currently targets Java 21, but the repository does **not** currently ship a `module-info.java` descriptor. The build also disables the module path in test execution:

```xml
<useModulePath>false</useModulePath>
```

In practice, that means the current project is consumed as a regular classpath JAR rather than as a JPMS module.

## Why modules were explored

AISmessages started in 2011 on Java 7 and later moved to Java 8. Java 9 modules were explored to provide stronger encapsulation and explicit dependencies between modules.

Modules allow a library to expose only the packages that belong to its supported API and hide the rest as implementation detail.

## The original module shape

AISmessages only needed a single module, so it could use Java's single-module layout by adding `module-info.java` under `src/main/java`.

Initial form:

```java
module dk.tbsalling.ais.messages {
}
```

First useful export:

```java
module dk.tbsalling.ais.messages {
    exports dk.tbsalling.aismessages;
}
```

## Avoiding unwanted module dependencies

Compiler feedback revealed dependencies that would have required additional non-standard modules:

- `java.beans.IntrospectionException`
- `java.util.logging`, which lives in `java.logging`

To keep the design lean, the implementation was adjusted so AISmessages would avoid depending on those modules unnecessarily.

The following commits capture the key code changes from that effort:

- `b93c06a4` for the `AISMessage.java` changes
- `4c197d39` and `d7537a67` for the logging migration work

## Using a demo module to discover required exports

One useful technique was to validate the exported API surface with a separate demo application. Its module declaration was:

```java
module dk.tbsalling.ais.demo {
    requires dk.tbsalling.ais.messages;
}
```

The demo's `pom.xml` explicitly configured a module-capable Maven compiler plugin:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <source>9</source>
        <target>9</target>
    </configuration>
</plugin>
```

And the demo code exercised `AISInputStreamReader`:

```java
package dk.tbsalling.ais.demo;

import dk.tbsalling.aismessages.AISInputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DemoApp {

    public static void main(String[] args) {
        new DemoApp().runDemo();
    }

    public void runDemo() {
        InputStream inputStream = new ByteArrayInputStream(demoNmeaStrings.getBytes());
        AISInputStreamReader streamReader = new AISInputStreamReader(
            inputStream,
            aisMessage -> System.out.println(
                "Received AIS message from MMSI "
                    + aisMessage.getSourceMmsi().getMmsi() + ": " + aisMessage
            )
        );
        streamReader.run();
    }
}
```

## Exported packages

The JPMS version needed to export:

```java
module dk.tbsalling.ais.messages {
    exports dk.tbsalling.aismessages;
    exports dk.tbsalling.aismessages.ais;
    exports dk.tbsalling.aismessages.ais.exceptions;
    exports dk.tbsalling.aismessages.ais.messages;
    exports dk.tbsalling.aismessages.ais.messages.types;
    exports dk.tbsalling.aismessages.ais.messages.asm;
}
```

That export decision intentionally left some socket-based implementation classes outside the public module surface.

## What to take away today

- The project currently documents and tests a classpath-oriented integration model.
- The earlier JPMS work is still useful as a guide to the intended public package boundaries.
- If first-class JPMS support becomes a requirement again, the exported package set above is a good starting point for a new `module-info.java`.

## Historical notes

The earlier module-focused work touched files such as:

- `src/main/java/module-info.java`
- `pom.xml`
- `src/main/java/dk/tbsalling/aismessages/ais/messages/AISMessage.java`
- `src/main/java/dk/tbsalling/aismessages/nmea/NMEAMessageHandler.java`
- `src/main/java/dk/tbsalling/aismessages/nmea/NMEAMessageInputStreamReader.java`

## Related documentation

- [README.md](../../README.md)
- [Immutable value objects in AISmessages v4](../articles/immutable-value-objects-v4.md)
