package com.tvd12.calabash.local.manager;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.manager.MapManager;

@SuppressWarnings("rawtypes")
public interface EntityMapManager extends MapManager {

    @Override
    EntityMap getMap(String mapName);
}
