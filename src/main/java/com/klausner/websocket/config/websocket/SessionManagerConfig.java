package com.klausner.websocket.config.websocket;

import com.klausner.websocket.model.ConnectedSessions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Configuration
public class SessionManagerConfig {

    @Bean
    @Scope("singleton")
    public ConnectedSessions clients() {
        return new ConnectedSessions(new ArrayList<>());
    }

}
