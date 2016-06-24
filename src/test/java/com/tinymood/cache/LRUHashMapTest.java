package com.tinymood.cache;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hztaoran on 2016/6/23 0023.
 */
public class LRUHashMapTest {
    public static void main(String[] args) {
        Map<String,Object> map = new LRUHashMap<String, Object>(10);

        for (int i = 0; i < 5; i++) {
            map.put(String.valueOf(i) , i);
        }

        // 遍历 新插入的放到最后 即 0 1 2 3 4
        Iterator<Map.Entry<String, Object>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<String, Object> entry = it1.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("[" + key + ", " + value + "], size = " + map.size());
        }
        System.out.println("----------End1------------");

        // 测试访问过的
        // 1 2 3 4 0
        map.get("0");
        Iterator<Map.Entry<String, Object>> it2 = map.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<String, Object> entry = it2.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("[" + key + ", " + value + "], size = " + map.size());
        }
        System.out.println("----------End2------------");

        // 测试新加入 --> 放到最后
        // 1 2 3 4 0 5
        map.put("5", 5);
        Iterator<Map.Entry<String, Object>> it3 = map.entrySet().iterator();
        while (it3.hasNext()) {
            Map.Entry<String, Object> entry = it3.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("[" + key + ", " + value + "], size = " + map.size());
        }
        System.out.println("----------End3------------");

        // 测试capacity满了删除最旧的，维护预定义的10个缓存的稳定状态
        // 即removeEldestEntry方法
        // 1 2 3 4 0 5 6 7 8 9
        map.put("6", 6);
        map.put("7", 7);
        map.put("8", 8);
        map.put("9", 9);
        map.put("10", 10);
        Iterator<Map.Entry<String, Object>> it4 = map.entrySet().iterator();
        while (it4.hasNext()) {
            Map.Entry<String, Object> entry = it4.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("[" + key + ", " + value + "], size = " + map.size());
        }
        System.out.println("----------End4------------");
    }
}
