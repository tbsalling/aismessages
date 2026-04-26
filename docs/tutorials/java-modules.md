# Introducing Java modules in AISmessages

**Published:** 2018-12-11

This tutorial explains the adoption of the Java module system in AISmessages.

## Motivation

AISmessages started in 2011 on Java 7 and later moved to Java 8. The next step was Java 9 modules, primarily to gain stronger encapsulation and explicit dependencies between modules.

Modules allow a library to expose only the packages that belong to its supported API and hide the rest as implementation detail.

## Minimal module declaration

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

The following commits capture the key code changes:

- `b93c06a4` for the `AISMessage.java` changes
- `4c197d39` and `d7537a67` for the logging migration work

## Using a demo module to discover required exports

The article validated the exported API surface with a separate demo application. Its module declaration was:

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
import java.io.IOException;
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
                    + aisMessage.getSourceMmsi().getMMSI() + ": " + aisMessage
            )
        );
        try {
            streamReader.run();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
```

## Exported packages

AISmessages needed to export:

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

## Historical availability notes

At the time of the original article, the module-enabled version was available as:

- source: `aismessages-3.0.2`
- Maven Central artifact: `dk.tbsalling:aismessages:3.0.2`

The original article also listed the files that were changed to introduce module support, including:

- `src/main/java/module-info.java`
- `pom.xml`
- `src/main/java/dk/tbsalling/aismessages/ais/messages/AISMessage.java`
- `src/main/java/dk/tbsalling/aismessages/nmea/NMEAMessageHandler.java`
- `src/main/java/dk/tbsalling/aismessages/nmea/NMEAMessageInputStreamReader.java`

## Related documentation

- [README.md](../../README.md)
- [Immutable value objects in AISmessages v4](../articles/immutable-value-objects-v4.md)
