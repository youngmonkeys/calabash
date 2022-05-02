package com.tvd12.calabash.core.manager;

import com.tvd12.calabash.concurrent.Executors;
import com.tvd12.calabash.eviction.MapEvictable;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapEvictionManager
    extends EzyLoggable
    implements EzyStartable, EzyStoppable {

    protected final int evictionInterval;
    protected final MapManager mapManager;
    protected final List<Object> mapListBuffer;
    protected final ScheduledExecutorService scheduledExecutorService;

    protected MapEvictionManager(Builder builder) {
        this.mapManager = builder.mapManager;
        this.evictionInterval = builder.evictionInterval;
        this.mapListBuffer = new ArrayList<>();
        this.scheduledExecutorService = newScheduledExecutorService();
    }

    public static Builder builder() {
        return new Builder();
    }

    protected ScheduledExecutorService newScheduledExecutorService() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor("map-eviction");
        Runtime.getRuntime().addShutdownHook(new Thread(service::shutdown));
        return service;
    }

    @Override
    public void start() {
        this.scheduledExecutorService.scheduleAtFixedRate(
            this::evict,
            evictionInterval,
            evictionInterval, TimeUnit.SECONDS);
    }

    protected void evict() {
        try {
            mapManager.getMapList(mapListBuffer);
            for (Object map : mapListBuffer) {
                ((MapEvictable) map).evict();
            }
        } catch (Exception e) {
            logger.error("map eviction error", e);
        } finally {
            mapListBuffer.clear();
        }
    }

    @Override
    public void stop() {
        this.scheduledExecutorService.shutdown();
    }

    public static class Builder implements EzyBuilder<MapEvictionManager> {

        protected int evictionInterval;
        protected MapManager mapManager;

        public Builder mapManager(MapManager mapManager) {
            this.mapManager = mapManager;
            return this;
        }

        public Builder evictionInterval(int evictionInterval) {
            this.evictionInterval = evictionInterval;
            return this;
        }

        @Override
        public MapEvictionManager build() {
            return new MapEvictionManager(this);
        }
    }
}
