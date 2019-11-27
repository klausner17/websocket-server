package com.klausner.websocket.consumer;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PulsarSubscriber implements Subscriber {

    private static final String SUB_PREFIX = "sub_";

    private PulsarClient pulsarClient;
    private ListenerFactory listenerFactory;

    public PulsarSubscriber(PulsarClient pulsarClient, ListenerFactory listenerFactory) {
        this.pulsarClient = pulsarClient;
        this.listenerFactory = listenerFactory;
    }

    @Override
    public void subscribe(String topic) throws PulsarClientException {
        pulsarClient.newConsumer()
                .topic(topic)
                .subscriptionName(SUB_PREFIX + UUID.randomUUID().toString())
                .subscriptionType(SubscriptionType.Exclusive)
                .messageListener(listenerFactory.create())
                .subscribe();
    }
}
