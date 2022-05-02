package com.tvd12.calabash.local.manager;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.local.factory.EntityMapFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class SimpleEntityMapManager
    extends EzyLoggable
    implements EntityMapManager, StatisticsAware {

    protected final EntityMapFactory mapFactory;
    protected final Map<String, EntityMap> maps;

    protected SimpleEntityMapManager(Builder builder) {
        this.maps = new HashMap<>();
        this.mapFactory = builder.mapFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public EntityMap getMap(String mapName) {
        EntityMap map = maps.get(mapName);
        if (map == null) {
            map = newMap(mapName);
        }
        return map;
    }

    protected EntityMap newMap(String mapName) {
        synchronized (maps) {
            EntityMap map = maps.get(mapName);
            if (map == null) {
                map = mapFactory.newMap(mapName);
                maps.put(mapName, map);
            }
            return map;
        }
    }

    @SuppressWarnings("unchecked")
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
                EntityMap map = maps.get(mapName);
                Map<String, Object> mapStat = new HashMap<>();
                mapStat.put("name", mapName);
                ((StatisticsAware) map).addStatistics(mapStat);
                mapStats.add(mapStat);
            }
            statistics.put("maps", mapStats);
        }
    }

    public static class Builder implements EzyBuilder<EntityMapManager> {

        protected EntityMapFactory mapFactory;

        public Builder mapFactory(EntityMapFactory mapFactory) {
            this.mapFactory = mapFactory;
            return this;
        }

        @Override
        public EntityMapManager build() {
            return new SimpleEntityMapManager(this);
        }
    }
}
