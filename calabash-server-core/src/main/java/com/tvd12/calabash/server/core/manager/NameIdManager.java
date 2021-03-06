package com.tvd12.calabash.server.core.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.calabash.persist.NameIdMapPersist;

public class NameIdManager {
	
	private final NameIdMapPersist nameIdMapPersist;
	private final Map<Integer, String> nameById = new ConcurrentHashMap<>();
	private final Map<String, Integer> idByName = new ConcurrentHashMap<>();
	
	public NameIdManager(NameIdMapPersist nameIdMapPersist) {
		this.nameIdMapPersist = nameIdMapPersist;
	}
	
	public String getName(int id) {
		return nameById.get(id);
	}
	
	public int getId(String name) {
		int mapId = idByName.computeIfAbsent(name, k ->
			nameIdMapPersist.load(name)
		);
		nameById.put(mapId, name);
		return mapId;
	}

}
