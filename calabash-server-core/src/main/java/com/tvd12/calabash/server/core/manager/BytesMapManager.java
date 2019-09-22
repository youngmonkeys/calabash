package com.tvd12.calabash.server.core.manager;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.manager.MapManager;

public interface BytesMapManager extends MapManager {

	BytesMap getMap(String mapName);
	
}
