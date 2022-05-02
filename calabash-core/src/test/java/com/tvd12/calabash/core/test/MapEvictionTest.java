package com.tvd12.calabash.core.test;

import com.tvd12.calabash.eviction.MapEviction;
import com.tvd12.calabash.eviction.SimpleMapEviction;
import com.tvd12.ezyfox.collect.Sets;
import org.testng.annotations.Test;

import java.util.List;

public class MapEvictionTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        MapEviction mapEviction = new SimpleMapEviction(() -> 1);
        mapEviction.updateKeyTime("1");
        mapEviction.updateKeysTime(Sets.newHashSet("2", "3"));
        Thread.sleep(2000);
        mapEviction.updateKeysTime(Sets.newHashSet("4", "5"));
        List evictableKeys = mapEviction.getEvictableKeys();
        assert evictableKeys.size() >= 3;
        mapEviction.removeKey("1");
        mapEviction.removeKeys(Sets.newHashSet("2", "3"));
    }
}
