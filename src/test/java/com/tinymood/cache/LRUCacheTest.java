package com.tinymood.cache;

/**
 * Created by hztaoran on 2016/6/23 0023.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class LRUCacheTest {

    private static final String A = "A";

    private static final String B = "B";

    private static final String C = "C";

    private static final String D = "D";

    private static final String E = "E";

    private LRUCache<String, String> cache;

    private static void assertMiss(LRUCache<String, String> cache, String key) {
        assertNull(cache.get(key));
    }

    private static void assertHit(LRUCache<String, String> cache, String key, String value) {
        assertThat(cache.get(key), is(value));
    }

    private static void assertSnapshot(LRUCache<String, String> cache, String... keysAndValues) {
        List<String> actualKeysAndValues = new ArrayList<>();
        for (Map.Entry<String, String> entry : cache.snapshot().entrySet()) {
            actualKeysAndValues.add(entry.getKey());
            actualKeysAndValues.add(entry.getValue());
        }
        //FIXME
        assertEquals(Arrays.asList(keysAndValues), actualKeysAndValues);
    }

    @Before
    public void setUp() {
        cache = new LRUCache<>(3);
    }

    @After
    public void tearDown() {
        cache.clear();
        cache = null;
    }

    @Test
    public void defaultMemorySize() {
        assertThat(cache.getMaxMemorySize(), is(2 * 1024 * 1024));
    }

    @Test
    public void logic() {
        cache.put("a", A);
        assertHit(cache, "a", A);

        cache.put("b", B);
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertSnapshot(cache, "a", A, "b", B);

        cache.put("c", C);
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertSnapshot(cache, "a", A, "b", B, "c", C);

        cache.put("d", D);
        assertMiss(cache, "a");
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertHit(cache, "d", D);
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertSnapshot(cache, "d", D, "b", B, "c", C);

        cache.put("e", E);
        assertMiss(cache, "a");
        assertMiss(cache, "d");
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertHit(cache, "e", E);
        assertSnapshot(cache, "b", B, "c", C, "e", E);

        cache.put("e", "X");
        assertMiss(cache, "a");
        assertMiss(cache, "d");
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertHit(cache, "e", "X");
        assertSnapshot(cache, "b", B, "c", C, "e", "X");
    }

    @Test
    public void constructorDoesNotAllowZeroCacheSize() {
        try {
            new LRUCache(0);
            fail();
        } catch (IllegalArgumentException expected) {
            //nothing
        }
    }

    @Test
    public void cannotPutNullKey() {
        try {
            cache.put(null, "a");
            fail();
        } catch (NullPointerException expected) {
            // nothing
        }
    }

    @Test
    public void cannotPutNullValue() {
        try {
            cache.put("a", null);
            fail();
        } catch (NullPointerException expected) {
            // nothing
        }
    }

    @Test
    public void evictionWithSingletonCache() {
        LRUCache<String, String> cache = new LRUCache<>(1);
        cache.put("a", A);
        cache.put("b", B);
        assertSnapshot(cache, "b", B);
    }

    @Test
    public void removeOneItem() {
        LRUCache<String, String> cache = new LRUCache<>(1);
        cache.put("a", A);
        cache.put("b", B);
        assertNull(cache.remove("a"));
        assertSnapshot(cache, "b", B);
    }

    @Test
    public void cannotRemoveNullKey() {
        try {
            cache.remove(null);
            fail();
        } catch (NullPointerException expected) {
            // nothing
        }
    }

    /**
     * Replacing the value for a key doesn't cause an eviction but it does bring the replaced entry to
     * the front of the queue.
     */
    @Test
    public void putCauseEviction() {
        cache.put("a", A);
        cache.put("b", B);
        cache.put("c", C);
        cache.put("b", D);
        assertSnapshot(cache, "a", A, "c", C, "b", D);
    }

    @Test
    public void throwsWithNullKey() {
        try {
            cache.get(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // nothing
        }
    }

    @Test
    public void clear() {
        cache.put("a", "a");
        cache.put("b", "b");
        cache.put("c", "c");
        cache.clear();
        assertThat(cache.snapshot().size(), is(0));
    }

}
