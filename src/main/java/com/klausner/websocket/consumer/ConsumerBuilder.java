package com.klausner.websocket.consumer;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.ReaderBuilder;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBuilder implements ConsumerSubscriber {

    private PulsarClient pulsarClient;

    public ConsumerBuilder(PulsarClient pulsarClient) {
        this.pulsarClient = pulsarClient;
    }

    @Override
    public Message<byte[]> receive(String topic) throws PulsarClientException {
        Consumer<byte[]> subscription = pulsarClient.newConsumer()
                .topic(topic)
                .subscribe();

        return subscription.receive();
    }
}
