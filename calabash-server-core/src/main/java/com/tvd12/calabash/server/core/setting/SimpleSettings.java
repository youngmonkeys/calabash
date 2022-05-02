package com.tvd12.calabash.server.core.setting;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class SimpleSettings implements Settings {

    @Setter
    @Getter
    protected int mapEvictionInterval;
    @Getter
    @Setter
    protected String atomicLongMapName;

    protected final Map<String, MapSetting> mapSettings;

    public SimpleSettings() {
        this.mapSettings = new HashMap<>();
        this.atomicLongMapName = DEFAULT_ATOMIC_LONG_MAP_NAME;
        this.mapEvictionInterval = DEFAULT_MAP_EVICTION_INTERVAL;
    }

    public void addMapSetting(MapSetting setting) {
        mapSettings.put(setting.getMapName(), setting);
    }

    @Override
    public MapSetting getMapSetting(String mapName) {
        MapSetting mapSetting = mapSettings.get(mapName);
        if (mapSetting == null) {
            mapSetting = newDefaultMapSetting(mapName);
        }
        return mapSetting;
    }

    protected MapSetting newDefaultMapSetting(String mapName) {
        synchronized (mapSettings) {
            MapSetting mapSetting = mapSettings.get(mapName);
            if (mapSetting == null) {
                mapSetting = new SimpleMapSetting();
                mapSettings.put(mapName, mapSetting);
            }
            return mapSetting;
        }
    }
}
