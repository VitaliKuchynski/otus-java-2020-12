package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();

    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {

        listenEvent(key, value, "PUT");

        if (!cache.containsKey(key)) {
            cache.put(key, value);
        }
    }

    @Override
    public void remove(K key) {

        listenEvent(key, cache.get(key), "REMOVE");

        cache.remove(key);
    }

    @Override
    public V get(K key) {

        listenEvent(key, cache.get(key), "GET");

           return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void listenEvent(K key, V value, String action){

        for (HwListener listener : listeners){

            try {
                listener.notify(key, value, action);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
