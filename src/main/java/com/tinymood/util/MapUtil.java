package com.tinymood.util;

import java.util.*;

/**
 * 提供一些实用方法
 *
 * @author hztaoran
 *
 */
public class MapUtil<K, V>{
    public static <K, V> List<Map.Entry<K, V>> sortMapEntryByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list;
        if (null == map || map.size() == 0) {
            return null;
        }
        list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if ((Integer)o1.getValue() < (Integer)o2.getValue()) {
                    return -1;
                } else if ((Integer)o1.getValue() == (Integer)o2.getValue()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return list;
    }
}
