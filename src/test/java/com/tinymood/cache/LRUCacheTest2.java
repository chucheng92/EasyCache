package com.tinymood.cache;

/**
 * Created by hztaoran on 2016/6/23 0023.
 */
public class LRUCacheTest2 {
    public static void main(String[] args) {

        // 测试顺序
        LRUCache<String, String> cache = new LRUCache<>(3);

        cache.put("a", "A");
        cache.put("b", "B");
        cache.put("c", "C");
        cache.get("a");

        System.out.println(cache.toString());
    }
}
