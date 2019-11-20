package com.klausner.websocket.config.pulsar;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarConfig {

    private PulsarProperties properties;

    public PulsarConfig(PulsarProperties properties) {
        this.properties = properties;
    }

    @Bean
    public PulsarClient pulsarClient() throws PulsarClientException {
        return PulsarClient.builder()
                .serviceUrl(properties.getServerUrl())
                .build();
    }
}
