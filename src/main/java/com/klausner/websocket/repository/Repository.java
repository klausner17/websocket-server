package com.klausner.websocket.repository;

import java.util.List;

/**
 * Repository
 * @param <K> Type of primary key
 * @param <T> Type of object of the repository
 */
public interface Repository<K,T> {
    List<T> get(K id);
    void remove(K id);
    void add(T object);
}
