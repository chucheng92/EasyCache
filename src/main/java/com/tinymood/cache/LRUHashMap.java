package com.tinymood.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hztaoran on 2016/6/23 0023.
 */

/**
 * 继承{@link java.util.LinkedHashMap} 实现LRU缓存
 *
 * @param <K>
 * @param <V>
 * @author hztaoran
 */
public class LRUHashMap<K,V> extends LinkedHashMap<K,V>{

    private final int capacity;

    public LRUHashMap(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
