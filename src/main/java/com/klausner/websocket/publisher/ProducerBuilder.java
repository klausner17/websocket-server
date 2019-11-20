package com.klausner.websocket.publisher;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;

public interface ProducerBuilder {
    Producer<byte[]> build(String topic) throws PulsarClientException;
}
