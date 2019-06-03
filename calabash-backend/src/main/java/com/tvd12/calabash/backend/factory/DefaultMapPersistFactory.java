package com.tvd12.calabash.backend.factory;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.backend.MapPersist;

@SuppressWarnings("rawtypes")
public class DefaultMapPersistFactory implements MapPersistFactory {

	protected final Map<String, MapPersist> mapPersists;
	
	public DefaultMapPersistFactory() {
		this.mapPersists = new HashMap<>();
	}
	
	@Override
	public MapPersist<?, ?> newMapPersist(String mapName) {
		MapPersist mapPersist = mapPersists.get(mapName);
		if(mapPersist != null)
			return mapPersist;
		return null;
	}
	
	public void addMapPersist(String mapName, MapPersist mapPersist) {
		this.mapPersists.put(mapName, mapPersist);
	}
	
}
