package com.tinymood.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 继承LinkedHashMap实现FIFO缓存
 *
 * @param <K> key
 * @param <V> value
 *
 * @author hztaoran
 * @version 1.0
 */
public class FIFOHashMap<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public FIFOHashMap(int capacity) {
        super(capacity, 0.75f, false);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
