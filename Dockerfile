FROM maven:3.6.2-jdk-11-openj9 AS builder
COPY . .

RUN mvn clean install

FROM openjdk:11.0 AS server
COPY --from=builder target/chat-websocket*.jar chat-websocket.jar


ENTRYPOINT java -jar chat-websocket.jar