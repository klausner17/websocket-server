package com.klausner.websocket.controller;

import com.klausner.websocket.decoder.MessageDecoder;
import com.klausner.websocket.encoder.MessageEncoder;
import com.klausner.websocket.model.Endpoint;
import com.klausner.websocket.model.Message;
import com.klausner.websocket.publisher.ProducerBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint(value = "/chat/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
@Component
public class ChatEndpoint {

    private static HashMap<String, Endpoint> chatEndpoints = new HashMap<>();
    private static HashMap<String, String> users = new HashMap<>();

    private ProducerBuilder producerBuilder;

    public ChatEndpoint(ProducerBuilder producerBuilder) {
        this.producerBuilder = producerBuilder;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        try {
            Endpoint endpoint = new Endpoint();
            endpoint.setProducer(producerBuilder.build(username));
            endpoint.setSession(session);

            chatEndpoints.put(username, endpoint);

            users.put(session.getId(), username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        chatEndpoints.remove(session.getId());
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        message.setFrom(users.get(session.getId()));
        if (message.getTo() != null) {
            sendMessage(message);
        }
    }

    private void sendMessage(Message message) {
        Endpoint endpoint = chatEndpoints.get(message.getTo());
        try {
            endpoint.getSession().getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
}
