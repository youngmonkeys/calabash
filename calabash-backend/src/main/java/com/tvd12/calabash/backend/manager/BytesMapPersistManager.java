package com.tvd12.calabash.backend.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.backend.BytesMapPersist;

public class BytesMapPersistManager {

	protected final Map<String, BytesMapPersist> mapPersists = new HashMap<>();
	
	public BytesMapPersist getMapPersist(String mapName) {
		BytesMapPersist mapPersist = mapPersists.get(mapName);
		return mapPersist;
	}
	
}
