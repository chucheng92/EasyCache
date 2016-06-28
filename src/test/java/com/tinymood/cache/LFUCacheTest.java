package com.tinymood.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * LFU缓存测试
 */
public class LFUCacheTest {

    private static final String A = "A";

    private static final String B = "B";

    private static final String C = "C";

    private static final String D = "D";

    private static final String E = "E";

    private LFUCache<String, String> cache;

    private static void assertMiss(LFUCache<String, String> cache, String key) {
        assertNull(cache.get(key));
    }

    private static void assertHit(LFUCache<String, String> cache, String key, String value) {
        assertThat(cache.get(key), is(value));
    }

    private static void assertSnapshot(LFUCache<String, String> cache, String... keysAndValues) {
        List<String> actualKeysAndValues = new ArrayList<>();
        for (Map.Entry<String, String> entry : cache.snapshot().entrySet()) {
            actualKeysAndValues.add(entry.getKey());
            actualKeysAndValues.add(entry.getValue());
        }
        assertEquals(Arrays.asList(keysAndValues), actualKeysAndValues);
    }

    @Before
    public void setUp() {
        cache = new LFUCache<>(3);
    }

    @Test
    public void defaultMemorySize() {
        assertThat(cache.getMaxMemorySize(), is(2 * 1024 * 1024));
    }

    @Test
    public void logic() {
        cache.put("a", A);
        assertHit(cache, "a", A);
        assertThat(cache.getHitMap().get("a"), is(1));

        cache.put("b", B);
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertThat(cache.getHitMap().get("a"), is(2));
        assertThat(cache.getHitMap().get("b"), is(1));
        assertSnapshot(cache, "a", A, "b", B);

        cache.put("c", C);
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertThat(cache.getHitMap().get("a"), is(3));
        assertThat(cache.getHitMap().get("b"), is(2));
        assertThat(cache.getHitMap().get("c"), is(1));
        assertSnapshot(cache, "a", A, "b", B, "c", C);

        cache.put("d", D);
        assertMiss(cache, "c");
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertHit(cache, "d", D);
        assertThat(cache.getHitMap().get("a"), is(4));
        assertThat(cache.getHitMap().get("b"), is(3));
        assertThat(cache.getHitMap().get("d"), is(1));
        assertSnapshot(cache, "a", A, "b", B, "d", D);

        cache.put("e", E);
        assertMiss(cache, "c");
        assertMiss(cache, "d");
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertHit(cache, "e", E);
        assertThat(cache.getHitMap().get("a"), is(5));
        assertThat(cache.getHitMap().get("b"), is(4));
        assertThat(cache.getHitMap().get("e"), is(1));
        assertSnapshot(cache, "a", A, "b", B, "e", E);

        cache.put("d", "X-D");
        assertMiss(cache, "c");
        assertMiss(cache, "e");
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertHit(cache, "d", "X-D");
        assertThat(cache.getHitMap().get("a"), is(6));
        assertThat(cache.getHitMap().get("b"), is(5));
        assertThat(cache.getHitMap().get("d"), is(1));
        assertSnapshot(cache, "a", A, "b", B, "d", "X-D");
    }

    @Test
    public void constructorDoesNotAllowZeroCacheSize() {
        try {
            new LFUCache(0);
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
        LFUCache<String, String> cache = new LFUCache<>(1);
        cache.put("a", A);
        cache.put("b", B);
        assertMiss(cache, "a");
        assertSnapshot(cache, "b", B);
    }

    @Test
    public void removeOneItem() {
        LFUCache<String, String> cache = new LFUCache<>(1);
        cache.put("a", A);
        cache.put("b", B);
        assertMiss(cache, "a");
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
        System.out.println(cache.toString());
        System.out.println(cache.getHitMap());
        assertThat(cache.getHitMap().get("a"), is(0));
        assertThat(cache.getHitMap().get("b"), is(0));
        assertThat(cache.getHitMap().get("c"), is(0));
        assertSnapshot(cache, "a", A, "b", D, "c", C);
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
        assertThat(cache.getHitMap().get("a"), is(0));
        assertThat(cache.getHitMap().get("b"), is(0));
        assertThat(cache.getHitMap().get("c"), is(0));
        cache.clear();
        assertThat(cache.snapshot().size(), is(0));
    }

    @After
    public void tearDown() {
        cache.clear();
        cache = null;
    }
}