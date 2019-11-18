package com.klausner.websocket.controller;

import com.klausner.websocket.decoder.MessageDecoder;
import com.klausner.websocket.encoder.MessageEncoder;
import com.klausner.websocket.model.Message;

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
public class ChatEndpoint {

    private static HashMap<String, Session> chatEndpoints = new HashMap<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        chatEndpoints.put(username, session);
        users.put(session.getId(), username);
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
        Session session = chatEndpoints.get(message.getTo());
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
}
