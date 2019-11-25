package com.klausner.websocket.repository;

import com.klausner.websocket.model.UserSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("LocalUserRepository")
public class LocalUserRepository implements Repository<String, UserSession> {

    private static List<UserSession> userSessions = new ArrayList<>();

    @Override
    public List<UserSession> get(String id) {
        return userSessions.stream()
                .filter(userSession -> userSession.getUserId().equals(id))
                .collect(Collectors.toList());

    }

    @Override
    public void remove(String id) {
        userSessions = userSessions.stream()
                .filter(userSession -> !userSession.getSessionId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public void add(UserSession object) {
        userSessions.add(object);
    }
}
