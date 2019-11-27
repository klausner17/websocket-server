FROM maven:3.6.2-jdk-11-openj9 AS builder
COPY . .

RUN mvn clean install

FROM openjdk:11.0 AS server
COPY --from=builder target/chat-websocket*.jar chat-websocket.jar

ENV SERVER_PORT 2020
ENV PULSAR_SERVER_URL pulsar://localhost:6650
ENV REDIS_CLIENT_URL localhost
ENV REDIS_CLIENT_PORT 6379

ENTRYPOINT java \
    -Dserver.port=${SERVER_PORT} \
    -Dpulsar.server.url=${PULSAR_SERVER_URL} \
    -Dredis.client.url=${REDIS_CLIENT_URL} \
    -Dredis.client.port=${REDIS_CLIENT_PORT} \
    -jar chat-websocket.jar