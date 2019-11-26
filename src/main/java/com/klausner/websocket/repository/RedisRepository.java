package com.klausner.websocket.repository;

import com.klausner.websocket.model.UserSession;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;

@Component
public class RedisRepository {

    private Jedis jedis;

    public RedisRepository(Jedis jedis) {
        this.jedis = jedis;
    }

    public UserSession get(String id) {
        List<String> sessions = jedis.lrange(id, 0, -1);
        return UserSession.builder()
                .userId(id)
                .sessions(sessions)
                .build();
    }

    public void remove(String userId, String sessionId) {
        jedis.lrem(userId, 0, sessionId);
    }

    public void add(String userId, String sessionId) {
        jedis.lpush(userId, sessionId);
    }
}
