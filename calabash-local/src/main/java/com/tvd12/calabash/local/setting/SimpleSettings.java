package com.tvd12.calabash.local.setting;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class SimpleSettings implements Settings {

    @Getter
    @Setter
    protected int mapEvictionInterval;
    @Getter
    @Setter
    protected String atomicLongMapName;

    protected final Map<String, EntityMapSetting> mapSettings;

    public SimpleSettings() {
        this.mapSettings = new HashMap<>();
        this.atomicLongMapName = DEFAULT_ATOMIC_LONG_MAP_NAME;
        this.mapEvictionInterval = DEFAULT_MAP_EVICTION_INTERVAL;
    }

    public void addMapSetting(EntityMapSetting setting) {
        mapSettings.put(setting.getMapName(), setting);
    }

    @Override
    public EntityMapSetting getMapSetting(String mapName) {
        EntityMapSetting mapSetting = mapSettings.get(mapName);
        if (mapSetting == null) {
            mapSetting = newDefaultMapSetting(mapName);
        }
        return mapSetting;
    }

    protected EntityMapSetting newDefaultMapSetting(String mapName) {
        synchronized (mapSettings) {
            EntityMapSetting mapSetting = mapSettings.get(mapName);
            if (mapSetting == null) {
                mapSetting = new SimpleEntityMapSetting();
                ((SimpleEntityMapSetting) mapSetting).setMapName(mapName);
                mapSettings.put(mapName, mapSetting);
            }
            return mapSetting;
        }
    }
}
