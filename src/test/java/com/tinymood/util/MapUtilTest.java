package com.tinymood.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hztaoran on 2016/6/28 0028.
 */
public class MapUtilTest {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 0);
        map.put("e", 2);
        map.put("f", 4);
        List<Map.Entry<String, Integer>> list = MapUtil.sortMapEntryByValue(map);
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println("[" + entry.getKey() + "," + entry.getValue() + "]");
        }
    }
}
