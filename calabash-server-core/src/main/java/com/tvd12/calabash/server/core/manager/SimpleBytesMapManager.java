package com.tvd12.calabash.server.core.manager;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.server.core.factory.BytesMapFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBytesMapManager
    extends EzyLoggable
    implements BytesMapManager, StatisticsAware {

    protected final BytesMapFactory mapFactory;
    protected final Map<String, BytesMap> maps;

    protected SimpleBytesMapManager(Builder builder) {
        this.maps = new HashMap<>();
        this.mapFactory = builder.mapFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public BytesMap getMap(String mapName) {
        BytesMap map = maps.get(mapName);
        if (map == null) {
            map = newMap(mapName);
        }
        return map;
    }

    protected BytesMap newMap(String mapName) {
        synchronized (maps) {
            BytesMap map = maps.get(mapName);
            if (map == null) {
                map = mapFactory.newMap(mapName);
                maps.put(mapName, map);
            }
            return map;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void getMapList(List buffer) {
        synchronized (maps) {
            buffer.addAll(maps.values());
        }
    }

    @Override
    public void addStatistics(Map<String, Object> statistics) {
        synchronized (maps) {
            statistics.put("numberOfMaps", maps.size());
            List<Map<String, Object>> mapStats = new ArrayList<>();
            for (String mapName : maps.keySet()) {
                BytesMap map = maps.get(mapName);
                Map<String, Object> mapStat = new HashMap<>();
                mapStat.put("name", mapName);
                ((StatisticsAware) map).addStatistics(mapStat);
                mapStats.add(mapStat);
            }
            statistics.put("maps", mapStats);
        }
    }

    public static class Builder implements EzyBuilder<BytesMapManager> {

        protected BytesMapFactory mapFactory;

        public Builder mapFactory(BytesMapFactory mapFactory) {
            this.mapFactory = mapFactory;
            return this;
        }

        @Override
        public BytesMapManager build() {
            return new SimpleBytesMapManager(this);
        }
    }
}
