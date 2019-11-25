package com.klausner.websocket.consumer;

import com.klausner.websocket.model.ConnectedSessions;
import org.springframework.stereotype.Component;

@Component
public class ListenerFactory {

    private ConnectedSessions connectedSessions;

    public ListenerFactory(ConnectedSessions connectedSessions) {
        this.connectedSessions = connectedSessions;
    }

    public NewEventListener create() {
        return new NewEventListener(connectedSessions);
    }
}
