package com.tinymood.cache;

/**
 * Created by hztaoran on 2016/6/26 0026.
 */
public class FIFOCacheTest2 {
    public static void main(String[] args) {
        FIFOCache<String, String> cache = new FIFOCache<>(3);

        cache.put("a", "a");
        cache.put("b", "b");
        cache.put("c", "c");
        cache.clear();

        System.out.println(cache.snapshot().size());
    }
}
