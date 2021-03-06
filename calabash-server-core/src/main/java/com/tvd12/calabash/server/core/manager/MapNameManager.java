package com.tvd12.calabash.server.core.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.calabash.persist.NameIdMapPersist;

public class MapNameManager {
	
	private final NameIdMapPersist mapNameIdMapPersist;
	private final Map<Integer, String> mapNameById = new ConcurrentHashMap<>();
	private final Map<String, Integer> mapIdByName = new ConcurrentHashMap<>();
	
	public MapNameManager(NameIdMapPersist mapNameIdMapPersist) {
		this.mapNameIdMapPersist = mapNameIdMapPersist;
	}
	
	public String getMapName(int mapId) {
		return mapNameById.get(mapId);
	}
	
	public int getMapId(String mapName) {
		int mapId = mapIdByName.computeIfAbsent(mapName, k ->
			mapNameIdMapPersist.load(mapName)
		);
		mapNameById.put(mapId, mapName);
		return mapId;
	}

}
