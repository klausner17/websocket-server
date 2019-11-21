package com.klausner.websocket.model;

import lombok.Builder;
import lombok.Data;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;
import org.springframework.web.socket.WebSocketSession;

@Data
@Builder
public class Endpoint {
    private WebSocketSession session;
    private Producer producer;
    private Consumer consumer;
}
