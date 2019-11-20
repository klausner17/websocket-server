package com.klausner.websocket.publisher;

import com.klausner.websocket.model.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;


@Component
public class EventPublisher implements ProducerBuilder {

    private PulsarClient pulsarClient;

    public EventPublisher(PulsarClient pulsarClient) {
        this.pulsarClient = pulsarClient;
    }


    @Override
    public Producer<byte[]> build(String topic) throws PulsarClientException {
        return pulsarClient.newProducer()
                .topic(topic)
                .create();
    }
}
