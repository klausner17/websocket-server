package com.klausner.websocket.consumer;

import com.klausner.websocket.model.ConnectedSessions;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class NewEventListener implements MessageListener<byte[]> {

    private ConnectedSessions connectedSessions;

    public NewEventListener(ConnectedSessions connectedSessions) {
        this.connectedSessions = connectedSessions;
    }

    @Override
    public void received(Consumer<byte[]> consumer, Message<byte[]> msg) {
        String sessionId = consumer.getTopic();
        WebSocketSession webSocketSession = connectedSessions.getSessions().stream()
                .filter(session -> sessionId.equals(session.getId()))
                .findFirst()
                .orElse(null);

        try {
            webSocketSession.sendMessage(new BinaryMessage(msg.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
