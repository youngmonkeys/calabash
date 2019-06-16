package com.tvd12.calabash.local.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class SimpleEntityMapPersistManager
		extends EzyLoggable 
		implements EntityMapPersistManager {

	protected final Map<String, EntityMapPersist> mapPersists = new HashMap<>();
	
	@Override
	public EntityMapPersist getMapPersist(String mapName) {
		EntityMapPersist mapPersist = mapPersists.get(mapName);
		return mapPersist;
	}
	
	@Override
	public void addMapPersist(String mapName, EntityMapPersist mapPersist) {
		this.mapPersists.put(mapName, mapPersist);
	}
	
}
