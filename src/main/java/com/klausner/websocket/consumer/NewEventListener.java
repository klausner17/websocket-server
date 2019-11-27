package com.klausner.websocket.consumer;

import com.klausner.websocket.model.ConnectedSessions;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class NewEventListener implements MessageListener<byte[]> {

    private ConnectedSessions connectedSessions;

    public NewEventListener(ConnectedSessions connectedSessions) {
        this.connectedSessions = connectedSessions;
    }

    @Override
    public void received(Consumer<byte[]> consumer, Message<byte[]> msg) {
        String sessionId = consumer.getTopic();
        connectedSessions.getSessions().stream()
                .filter(session -> sessionId.equals(session.getId()))
                .findFirst()
                .ifPresent(session -> sendMessage(session, msg.getData()));
    }

    private void sendMessage(WebSocketSession session, byte[] msg) {
        try {
            session.sendMessage(new TextMessage(msg));
        } catch (Exception e) {
            log.error("Error sending message to client with session id {}.", session.getId() ,e );
        }
    }
}
