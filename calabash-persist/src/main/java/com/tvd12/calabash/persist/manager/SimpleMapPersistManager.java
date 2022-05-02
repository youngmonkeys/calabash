package com.tvd12.calabash.persist.manager;

import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SimpleMapPersistManager
    extends EzyLoggable
    implements MapPersistManager {

    protected final Map<String, Object> mapPersists = new HashMap<>();

    @Override
    public <T> T getMapPersist(String mapName) {
        return (T) mapPersists.get(mapName);
    }

    @Override
    public void addMapPersist(String mapName, Object mapPersist) {
        this.mapPersists.put(mapName, mapPersist);
    }
}
