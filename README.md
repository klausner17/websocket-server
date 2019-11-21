# Chat Websocket

## Description
This a simple project to create a chat server.

## Motivation
Create a chat system in a scalable platform.

## Tools used
- [Apache Pulsar](https://pulsar.apache.org)
- [Spring Boot](https://spring.io)

## Requisites to debug
- [Maven 3+](http://maven.apache.org/download.cgi)
- [JDK 11+](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [Docker (optional)](https://docs.docker.com/)

## Running on docker
To running on a docker container follow this commands bellow:
- `$ docker build -t chat-websocket .`
- `$ docker run --name chat -p 8080:8080 chat-websocket:latest`

## Executing
To connect in webscocket use the `ws://localhost:8080/chat/{{username}}` endpoint.
To send messages use the `{"to": "{{another-user}}", "content": "{{message}}"}` pattern.

I am using [wscat](https://www.npmjs.com/package/wscat) to connect.
