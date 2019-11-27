# Chat Websocket

## Description
This a simple project to create a chat server.

## Motivation
Create a chat system in a scalable platform.

## Tools used
- [Apache Pulsar](https://pulsar.apache.org)
- [Redis](https://redis.io/)
- [Spring Boot](https://spring.io)

## Requisites to debug
- [Maven 3+](http://maven.apache.org/download.cgi)
- [JDK 11+](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [Docker](https://docs.docker.com/)

## Running on docker
Create a file in root folder with name **chat-websocket.env** with the content below:

```
SERVER_PORT=8080
PULSAR_SERVER_URL=pulsar://apache-pulsar:6650
REDIS_CLIENT_URL=redis
REDIS_CLIENT_PORT=6379
```

Execute the command to build

- `$ docker build -t chat-websocket .`

and execute the command to run

- `$ docker-compose up`

## Executing
To connect in webscocket use the `ws://localhost:8080/chat/{{username}}` endpoint.

To send messages use the `{"to": "{{another-user}}", "content": "{{message}}"}` pattern.

I am using [wscat](https://www.npmjs.com/package/wscat) to connect.
