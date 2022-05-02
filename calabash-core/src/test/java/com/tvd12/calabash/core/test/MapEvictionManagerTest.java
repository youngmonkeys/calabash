package com.tvd12.calabash.core.test;

import com.tvd12.calabash.core.manager.MapEvictionManager;
import com.tvd12.calabash.core.manager.MapManager;
import com.tvd12.calabash.eviction.MapEvictable;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MapEvictionManagerTest {

    @SuppressWarnings({"rawtypes"})
    @Test
    public void test1() throws Exception {
        MapManager mapManager = mock(MapManager.class);
        doAnswer(invocation -> {
            List buffer = (List) invocation.getArguments()[0];
            MapEvictable map = mock(MapEvictable.class);
            //noinspection unchecked
            buffer.add(map);
            return buffer;
        }).when(mapManager).getMapList(any(List.class));
        MapEvictionManager manager = MapEvictionManager.builder()
            .evictionInterval(1)
            .mapManager(mapManager)
            .build();
        manager.start();
        Thread.sleep(3000L);
        manager.stop();
    }

    @Test
    public void test2() throws Exception {
        MapManager mapManager = mock(MapManager.class);
        doThrow(RuntimeException.class).when(mapManager).getMapList(any(List.class));
        MapEvictionManager manager = MapEvictionManager.builder()
            .evictionInterval(1)
            .mapManager(mapManager)
            .build();
        manager.start();
        Thread.sleep(3000L);
        manager.stop();
    }
}
