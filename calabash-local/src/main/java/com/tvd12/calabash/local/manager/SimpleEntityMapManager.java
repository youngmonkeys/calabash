package com.tvd12.calabash.local.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.local.factory.EntityMapFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class SimpleEntityMapManager extends EzyLoggable implements EntityMapManager {

	protected final EntityMapFactory mapFactory;
	protected final Map<String, EntityMap> maps;
	
	protected SimpleEntityMapManager(Builder builder) {
		this.maps = new HashMap<>();
		this.mapFactory = builder.mapFactory;
	}
	
	@Override
	public EntityMap getMap(String mapName) {
		EntityMap map = maps.get(mapName);
		if(map == null)
			map = newMap(mapName);
		return map;
	}
	
	protected EntityMap newMap(String mapName) {
		synchronized (maps) {
			EntityMap map = maps.get(mapName);
			if(map == null) {
				map = mapFactory.newMap(mapName);
				maps.put(mapName, map);
			}
			return map;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getMapList(List buffer) {
		synchronized (maps) {
			for(EntityMap map : maps.values())
				buffer.add(map);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityMapManager> {

		protected EntityMapFactory mapFactory;
		
		public Builder mapFactory(EntityMapFactory mapFactory) {
			this.mapFactory = mapFactory;
			return this;
		}
		
		@Override
		public EntityMapManager build() {
			return new SimpleEntityMapManager(this);
		}
	}

}
