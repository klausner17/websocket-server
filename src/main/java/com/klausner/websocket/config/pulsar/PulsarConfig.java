package com.klausner.websocket.config.pulsar;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarConfig {

    @Bean
    public PulsarClient pulsarClient(@Value("${pulsar.server.url}") String serverUrl)
            throws PulsarClientException {

        return PulsarClient.builder()
                .serviceUrl(serverUrl)
                .build();
    }
}
