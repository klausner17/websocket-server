package com.klausner.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Getter
@AllArgsConstructor
public class ConnectedSessions {
    private List<WebSocketSession> sessions;
}
