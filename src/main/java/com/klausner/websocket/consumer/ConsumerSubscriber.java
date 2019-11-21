package com.klausner.websocket.consumer;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;

public interface ConsumerSubscriber {
    Message<byte[]> receive(String topic) throws PulsarClientException;
}
