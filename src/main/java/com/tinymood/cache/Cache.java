package com.tinymood.cache;

/**
 * Cache接口
 *
 * @author hztaoran
 * @version 1.0
 */
public interface Cache<K,V> {
    /**
     * Get value for {@code key} or null
     *
     * @param key
     * @return the value or {@code null}
     */
    V get(K key);

    /**
     * put an value in the cache for specified {@code key}
     *
     * @param key
     * @param value
     * @return the previous value
     */
    V put(K key, V value);

    /**
     * remove the entry for {@code key} if exist or return {@code null}
     *
     * @param key
     * @return the previous value or {@code null}
     */
    V remove(K key);

    /**
     * clear all the entries in the cache
     */
    void clear();

    /**
     * return the max memory size of cache
     *
     * @return max memory size
     */
    int getMaxMemorySize();

    /**
     * return the current memory size of the cache
     *
     * @return current memory size
     */
    int getMemorySize();
}
