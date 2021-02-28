package com.tvd12.calabash.client.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityMapNameManager {
	
	private final Map<Integer, String> mapNameById = new ConcurrentHashMap<>();
	
	public void put(int mapId, String mapName) {
		this.mapNameById.put(mapId, mapName);
	}
	
	public String getMapName(int mapId) {
		return mapNameById.get(mapId);
	}

}
