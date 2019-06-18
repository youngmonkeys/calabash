package com.tvd12.calabash.persist.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings("unchecked")
public class SimpleMapPersistManager
		extends EzyLoggable 
		implements MapPersistManager {

	protected final Map<String, Object> mapPersists = new HashMap<>();
	
	@Override
	public <T> T getMapPersist(String mapName) {
		T mapPersist = (T)mapPersists.get(mapName);
		return mapPersist;
	}
	
	@Override
	public void addMapPersist(String mapName, Object mapPersist) {
		this.mapPersists.put(mapName, mapPersist);
	}
	
}
