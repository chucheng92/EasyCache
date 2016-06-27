package com.tinymood.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hztaoran on 2016/6/26 0026.
 */
public class FIFOCache<K, V> implements Cache<K, V> {

    private static final int REMOVE_ALL = -1;

    private static final int DEFAULT_CAPACITY = 10;

    private final Map<K, V> map;

    private final int maxMemorySize;

    private int memorySize;

    private static Logger logger = LoggerFactory.getLogger(FIFOCache.class);

    public FIFOCache() {
        this(DEFAULT_CAPACITY);
    }

    public FIFOCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        this.map = new FIFOHashMap<>(capacity);
        maxMemorySize = 2 * 1024 * 1024;
    }


    @Override
    public final V get(K key) {
        Objects.requireNonNull(key, "key is null");
        V value;
        synchronized (this) {
            value = map.get(key);
            if (null != value) {
                return value;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key, "key is null");
        Objects.requireNonNull(value, "value is null");
        V oldValue;
        synchronized (this) {
            oldValue = map.put(key, value);
            memorySize += getValueSize(value);
            if (null != oldValue) {
                memorySize -= getValueSize(oldValue);
            }
            trimToSize(maxMemorySize);
        }
        return oldValue;
    }

    @Override
    public V remove(K key) {
        Objects.requireNonNull(key, "key is null");
        V oldValue;
        synchronized (this) {
            oldValue = map.remove(key);
            if (null != oldValue) {
                memorySize -= getValueSize(oldValue);
            }
        }
        return oldValue;
    }

    @Override
    public synchronized final void clear() {
        trimToSize(REMOVE_ALL);
    }

    @Override
    public synchronized final int getMaxMemorySize() {
        return maxMemorySize;
    }

    @Override
    public synchronized final int getMemorySize() {
        return memorySize;
    }

    /**
     * return a copy of current contents of the cache
     *
     * @return map
     */
    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<>(map);
    }

    /**
     * Returns the size of the entry.
     * <p>
     * The default implementation returns 1 so that max size is the maximum number of entries.
     * <p>
     * <em>Note:</em> This method should be overridden if you control memory size correctly.
     *
     * @param value value
     * @return the size of the entry.
     */
    protected int getValueSize(V value) {
        return 1;
    }

    /**
     * Returns the class name.
     * <p>
     * This method should be overridden to debug exactly.
     *
     * @return class name.
     */
    private String getClassName() {
        return FIFOCache.class.getName();
    }

    /**
     * remove the first in
     * <p>
     * <em>Note:</em> This method has to be called in synchronized block
     *
     * @param maxSize
     */
    private void trimToSize(int maxSize) {
        while (true) {
            if (memorySize <= maxSize || map.isEmpty()) {
                break;
            }
            if (memorySize < 0 || (memorySize != 0 && map.isEmpty())) {
                throw new IllegalStateException(getClassName() + ".getValueSize() is reporting inconsistent results");
            }
            Map.Entry<K, V> toRemove = map.entrySet().iterator().next();
            map.remove(toRemove.getKey());
            memorySize -= getValueSize(toRemove.getValue());
        }
    }

    @Override
    public synchronized final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append(",");
        }
        sb.append("maxMemorySize=")
                .append(maxMemorySize)
                .append(",")
                .append("memorySize=")
                .append(memorySize)
                .append("]");

        return sb.toString();
    }
}
