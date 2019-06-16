package com.tvd12.calabash.local.manager;

import com.tvd12.calabash.core.EntityMapPersist;

@SuppressWarnings("rawtypes")
public interface EntityMapPersistManager {

	EntityMapPersist getMapPersist(String mapName);
	
	void addMapPersist(String mapName, EntityMapPersist mapPersist);
	
}
