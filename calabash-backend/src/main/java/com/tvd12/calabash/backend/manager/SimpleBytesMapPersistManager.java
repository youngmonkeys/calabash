package com.tvd12.calabash.backend.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapPersistManager
		extends EzyLoggable 
		implements BytesMapPersistManager {

	protected final Map<String, BytesMapPersist> mapPersists = new HashMap<>();
	
	@Override
	public BytesMapPersist getMapPersist(String mapName) {
		BytesMapPersist mapPersist = mapPersists.get(mapName);
		return mapPersist;
	}
	
	@Override
	public void addMapPersist(String mapName, BytesMapPersist mapPersist) {
		this.mapPersists.put(mapName, mapPersist);
	}
	
}
