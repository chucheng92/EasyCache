package com.tinymood.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hztaoran on 2016/6/26 0026.
 */

/**
 * 测试HashMap按容量构造时初始值
 */
public class HashMapTest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>(3);
        System.out.println(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
