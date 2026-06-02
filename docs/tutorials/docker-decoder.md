# Creating, sharing, and running a Docker image to decode AIS messages

**Published:** 2018-09-24
**Updated:** 2026-04-26

This tutorial shows how to package a Spring Boot based AIS decoder service into a Docker image, run it locally, and publish it to a registry.

## Prerequisites

- Docker installed locally
- a Spring Boot service that uses AISmessages
- the application built so the runnable JAR exists in `target/`

With Maven, the build step is typically:

```text
$ ./mvnw package
```

## Add a Dockerfile

```Dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*.jar /app/app.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

Key points:

- `FROM` picks the base image
- `COPY` places the built artifact into the image
- `ENTRYPOINT` defines how the container starts
- `EXPOSE` documents the runtime port

## Build the image

```text
$ docker build -t aisdecoder:local .
```

## Run the image

```text
$ docker run --rm -p 8080:8080 aisdecoder:local
```

With the container running, call the decoder:

```text
$ curl -X POST http://localhost:8080/decode \
    -H 'Content-Type: application/json' \
    -d '[ "!AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53" ]'
```

## Build with a tag

To publish the image, tag it with a registry-qualified name. For example:

```text
$ docker tag aisdecoder:local ghcr.io/<owner>/aisdecoder:latest
```

Run by tag:

```text
$ docker run --rm -p 8080:8080 ghcr.io/<owner>/aisdecoder:latest
```

## Publish to a registry

```text
$ docker login ghcr.io
$ docker push ghcr.io/<owner>/aisdecoder:latest
```

After that, anyone with Docker can fetch and run it:

```text
$ docker pull ghcr.io/<owner>/aisdecoder:latest
$ docker run --rm -p 8080:8080 ghcr.io/<owner>/aisdecoder:latest
```

## Why this matters

Containerization makes the decoder service much easier to share and deploy. Instead of rebuilding the service from source on every machine, users can pull a prepared image and run it immediately.

## Related tutorials

- [Creating a Spring Boot based AIS message decoder](spring-boot-decoder.md)
- [Running AISdecoder in a Kubernetes cluster on AWS](kubernetes-on-aws.md)
