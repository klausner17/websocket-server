package com.klausner.websocket.model;

import lombok.Data;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;

import javax.websocket.Session;

@Data
public class Endpoint {
    private Session session;
    private Producer producer;
    private Consumer consumer;
}
