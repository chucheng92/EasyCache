package com.tinymood.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hztaoran on 2016/6/26 0026.
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
