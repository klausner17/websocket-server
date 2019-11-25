package com.klausner.websocket.consumer;

import org.apache.pulsar.client.api.PulsarClientException;

public interface Subscriber {
    void subscribe(String topic) throws PulsarClientException;
}
