package com.tvd12.calabash.backend.manager;

import com.tvd12.calabash.backend.BytesMapPersist;

public interface BytesMapPersistManager {

	BytesMapPersist getMapPersist(String mapName);
	
	void addMapPersist(String mapName, BytesMapPersist mapPersist);
	
}
