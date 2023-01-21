package dev.foggies.prisoncore.api;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Storage<K, V> {

    private final Map<K, V> storage = new ConcurrentHashMap<>();

    public void add(K key, V value) {
        this.storage.put(key, value);
    }

    public V get(K key) {
        return this.storage.get(key);
    }

    public boolean contains(K key) {
        return this.storage.containsKey(key);
    }

    public void remove(K key) {
        this.storage.remove(key);
    }

}
