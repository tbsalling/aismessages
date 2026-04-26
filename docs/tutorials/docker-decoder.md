# Creating, sharing, and running a Docker image to decode AIS messages

**Published:** 2018-09-24

> **Historical note:** This tutorial builds on the `aisdecoder` demo application and remains useful reference material for packaging an AIS decoder service into a container image.

This tutorial shows how to package the AIS decoder service into a Docker image, run it locally, and publish it to Docker Hub.

## Prerequisites

- Docker installed locally
- the `aisdecoder` source repository cloned
- the application built so the runnable artifact exists

The original walkthrough used:

```text
$ git clone https://github.com/tbsalling/aisdecoder.git
$ git checkout 7c02cbcef2ff273ab157e41fa71b193ae3304a93
$ ./gradlew build
```

The resulting artifact was:

```text
$ ls -lh build/libs/
-rw-r--r--  1 tbsalling  staff    16M 24 Sep 11:30 aisdecoder-0.0.1-SNAPSHOT.jar
```

## Add a Dockerfile

```Dockerfile
FROM openjdk:11-jre-slim
MAINTAINER Thomas Borg Salling "tbsalling@tbsalling.dk"
COPY build/libs/aisdecoder-0.0.1-SNAPSHOT.jar /app/aisdecoder.war
ENTRYPOINT ["java", "-jar", "/app/aisdecoder.war"]
EXPOSE 8080/tcp
```

Key points:

- `FROM` picks the base image
- `COPY` places the built artifact into the image
- `ENTRYPOINT` defines how the container starts
- `EXPOSE` documents the runtime port

## Build the image

```text
$ docker build .
```

Docker will produce an image ID such as `9f37cd551132`.

## Run the image

```text
$ docker run -p 8080:8080 9f37cd551132
```

With the container running, call the decoder:

```text
$ curl -X POST http://localhost:8080/decode \
    -H 'Content-Type: application/json' \
    -d '[ "!AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53" ]'
```

## Build with a tag

Using tags is much easier than remembering image hashes:

```text
$ docker build -t tbsalling/aisdecoder:latest .
```

Run by tag:

```text
$ docker run -p 8080:8080 tbsalling/aisdecoder:latest
```

## Publish to Docker Hub

```text
$ docker login --username=tbsalling
$ docker push tbsalling/aisdecoder:latest
```

After that, anyone with Docker can fetch and run it:

```text
$ docker pull tbsalling/aisdecoder:latest
$ docker run tbsalling/aisdecoder:latest
```

## Why this matters

Containerization makes the decoder service much easier to share and deploy. Instead of rebuilding the service from source on every machine, users can pull a prepared image and run it immediately.

## Related tutorials

- [Creating a Spring Boot based AIS message decoder](spring-boot-decoder.md)
- [Running AISdecoder in a Kubernetes cluster on AWS](kubernetes-on-aws.md)
