package com.klausner.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klausner.websocket.consumer.PulsarSubscriber;
import com.klausner.websocket.model.ConnectedSessions;
import com.klausner.websocket.model.MessageEvent;
import com.klausner.websocket.model.UserSession;
import com.klausner.websocket.publisher.ProducerBuilder;
import com.klausner.websocket.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@Component
public class ChatEndpoint extends TextWebSocketHandler {


    private ObjectMapper objectMapper;
    private RedisRepository userSessionRepository;
    private ProducerBuilder producerBuilder;
    private PulsarSubscriber pulsarSubscriber;
    private ConnectedSessions connectedSessions;

    public ChatEndpoint(ObjectMapper objectMapper, RedisRepository userSessionRepository, ProducerBuilder producerBuilder,
                        PulsarSubscriber pulsarSubscriber, ConnectedSessions connectedSessions) {

        this.objectMapper = objectMapper;
        this.userSessionRepository = userSessionRepository;
        this.producerBuilder = producerBuilder;
        this.pulsarSubscriber = pulsarSubscriber;
        this.connectedSessions = connectedSessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String userId = session.getAttributes().get("userId").toString();
        try {
            userSessionRepository.add(userId, session.getId());
            connectedSessions.getSessions().add(session);
            pulsarSubscriber.subscribe(session.getId());

        } catch (Exception e) {
            session.close();
            log.error("Error establishing connection of user {}", userId, e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            MessageEvent msg = objectMapper.readValue(message.asBytes(), MessageEvent.class);
            msg.setFrom(session.getAttributes().get("userId").toString());
            if (msg.getTo() != null) {
                publishMessage(msg);
            }
        } catch (Exception e) {
            log.error("Error handling message", e);
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = session.getAttributes().get("userId").toString();
        userSessionRepository.remove(userId, session.getId());
        connectedSessions.getSessions().remove(session);
    }

    private void publishMessage(MessageEvent message) {
        String to = message.getTo();
        UserSession userSessions = userSessionRepository.get(to);

        userSessions.getSessions().forEach(sessionId -> {
            try {
                Producer<byte[]> producer = producerBuilder.build(sessionId);
                producer.sendAsync(objectMapper.writeValueAsBytes(message));
            } catch (Exception e) {
                log.error("Error publishing message", e);
            }
        });
    }
}
