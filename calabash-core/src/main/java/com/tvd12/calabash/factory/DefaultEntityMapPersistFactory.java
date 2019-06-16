package com.tvd12.calabash.factory;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.core.EntityMapPersist;

@SuppressWarnings("rawtypes")
public class DefaultEntityMapPersistFactory implements EntityMapPersistFactory {

	protected final Map<String, EntityMapPersist> entityMapPersists;
	
	public DefaultEntityMapPersistFactory() {
		this.entityMapPersists = new HashMap<>();
	}
	
	@Override
	public EntityMapPersist<?, ?> newMapPersist(String mapName) {
		EntityMapPersist entityMapPersist = entityMapPersists.get(mapName);
		if(entityMapPersist != null)
			return entityMapPersist;
		return null;
	}
	
	public void addMapPersist(String mapName, EntityMapPersist entityMapPersist) {
		this.entityMapPersists.put(mapName, entityMapPersist);
	}
	
}
