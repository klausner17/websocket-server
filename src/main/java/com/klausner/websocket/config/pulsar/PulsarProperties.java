package com.klausner.websocket.config.pulsar;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PulsarProperties {

    @Value("${pulsar.server.url}")
    private String serverUrl;
}
