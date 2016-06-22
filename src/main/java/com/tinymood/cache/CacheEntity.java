package com.tinymood.cache;

/**
 * Created by hztaoran on 2016/6/22 0022.
 */

/**
 * @author hztaoran
 * Cache数据结构
 */
public class CacheEntity {
    private Object key;
    private Object value;
    private int index;
    private int hitCount;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }
}
