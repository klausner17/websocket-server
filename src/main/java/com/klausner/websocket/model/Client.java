package com.klausner.websocket.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.apache.pulsar.client.api.Producer;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Data
@Builder
public class Client {
    @Singular
    private List<WebSocketSession> sessions;
    private Producer producer;
}
