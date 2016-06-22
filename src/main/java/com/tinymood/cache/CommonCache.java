package com.tinymood.cache;

/**
 * Created by hztaoran on 2016/6/22 0022.
 */

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hztaoran
 * 缓存算法的公共实现
 */
public class CommonCache {

    //LinkedHashMap实现缓存池
    private Map<String, Object> table;

    public CommonCache() {
        table = new LinkedHashMap<String, Object>();
    }

    public final synchronized void addElement(Object key, Object value) {
        int index;
        Object obj;
        // get the entry from the map
        obj = table.get(key);
        // If we have the cache already in our table
        // then get it and replace
        if (null != obj) {
            CacheEntity cache = (CacheEntity) obj;
            cache.setKey(key);
            cache.setValue(value);
        }

        // if we get null,then we need replace strategy...
    }

    public Map<String, Object> getTable() {
        return table;
    }

    public void setTable(Map<String, Object> table) {
        this.table = table;
    }
}
