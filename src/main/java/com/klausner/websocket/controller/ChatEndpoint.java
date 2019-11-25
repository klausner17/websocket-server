package com.klausner.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klausner.websocket.consumer.PulsarSubscriber;
import com.klausner.websocket.model.ConnectedSessions;
import com.klausner.websocket.model.MessageEvent;
import com.klausner.websocket.model.UserSession;
import com.klausner.websocket.publisher.ProducerBuilder;
import com.klausner.websocket.repository.Repository;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Component
public class ChatEndpoint extends TextWebSocketHandler {


    private ObjectMapper objectMapper;
    private Repository userSessionRepository;
    private ProducerBuilder producerBuilder;
    private PulsarSubscriber pulsarSubscriber;
    private ConnectedSessions connectedSessions;

    public ChatEndpoint(ObjectMapper objectMapper, Repository userSessionRepository, ProducerBuilder producerBuilder,
                        PulsarSubscriber pulsarSubscriber, ConnectedSessions connectedSessions) {

        this.objectMapper = objectMapper;
        this.userSessionRepository = userSessionRepository;
        this.producerBuilder = producerBuilder;
        this.pulsarSubscriber = pulsarSubscriber;
        this.connectedSessions = connectedSessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        try {
            String userId = session.getAttributes().get("userId").toString();
            userSessionRepository.add(new UserSession(userId, session.getId()));
            connectedSessions.getSessions().add(session);
            pulsarSubscriber.subscribe(session.getId());

        } catch (Exception e) {
            session.close();
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        MessageEvent msg = objectMapper.readValue(message.asBytes(), MessageEvent.class);
        msg.setFrom(session.getAttributes().get("userId").toString());
        if (msg.getTo() != null) {
            sendMessage(msg);
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        userSessionRepository.remove(session.getId());
        connectedSessions.getSessions().remove(session);
    }

    private void sendMessage(MessageEvent message) {
        String to = message.getTo();

        List<UserSession> userSessions = userSessionRepository.get(to);
        userSessions.forEach(userSession -> {
            try {
                Producer<byte[]> producer = producerBuilder.build(userSession.getSessionId());
                producer.sendAsync(objectMapper.writeValueAsBytes(message));
            } catch (PulsarClientException | JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
