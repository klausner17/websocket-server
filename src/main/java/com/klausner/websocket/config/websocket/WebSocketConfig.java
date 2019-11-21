package com.klausner.websocket.config.websocket;

import com.klausner.websocket.controller.ChatEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private ChatEndpoint chatEndpoint;
    private ChatHandshakeInterceptor chatHandshakeInterceptor;

    public WebSocketConfig(ChatEndpoint chatEndpoint, ChatHandshakeInterceptor chatHandshakeInterceptor) {
        this.chatEndpoint = chatEndpoint;
        this.chatHandshakeInterceptor = chatHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatEndpoint, "/chat/{userId}")
                .addInterceptors(chatHandshakeInterceptor);
    }
}
