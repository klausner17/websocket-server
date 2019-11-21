package com.klausner.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klausner.websocket.model.Endpoint;
import com.klausner.websocket.model.Message;
import com.klausner.websocket.publisher.ProducerBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;

@Component
public class ChatEndpoint extends TextWebSocketHandler {

    private static HashMap<String, Endpoint> chatEndpoints = new HashMap<>();
    private ProducerBuilder producerBuilder;
    private ObjectMapper objectMapper;

    public ChatEndpoint(ProducerBuilder producerBuilder, ObjectMapper objectMapper) {
        this.producerBuilder = producerBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            String userId = session.getAttributes().get("userId").toString();
            Endpoint endpoint = Endpoint.builder()
                    .producer(producerBuilder.build(userId))
                    .session(session)
                    .build();

            chatEndpoints.put(userId, endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Message msg = objectMapper.readValue(message.asBytes(), Message.class);
        msg.setFrom(session.getId());
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
        chatEndpoints.remove(session.getId());
    }


    private void sendMessage(Message message) {
        Endpoint endpoint = chatEndpoints.get(message.getTo());
        try {
            endpoint.getSession().sendMessage(new TextMessage(objectMapper.writeValueAsBytes(message)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
