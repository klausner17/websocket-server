package com.klausner.websocket.encoder;

import com.google.gson.Gson;
import com.klausner.websocket.model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

    private static final Gson gson = new Gson();

    public String encode(Message message) throws EncodeException {
        return gson.toJson(message);
    }

    public void init(EndpointConfig endpointConfig) {

    }

    public void destroy() {

    }
}
