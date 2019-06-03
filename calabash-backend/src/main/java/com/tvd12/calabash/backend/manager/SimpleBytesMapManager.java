package com.tvd12.calabash.backend.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.backend.factory.BytesMapFactory;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapManager extends EzyLoggable implements BytesMapManager {

	protected final BytesMapFactory mapFactory;
	protected final Map<String, BytesMap> maps;
	
	protected SimpleBytesMapManager(Builder builder) {
		this.maps = new HashMap<>();
		this.mapFactory = builder.mapFactory;
	}
	
	@Override
	public BytesMap getMap(String mapName) {
		BytesMap map = maps.get(mapName);
		if(map == null)
			map = newMap(mapName);
		return map;
	}
	
	protected BytesMap newMap(String mapName) {
		synchronized (maps) {
			BytesMap map = maps.get(mapName);
			if(map == null) {
				map = mapFactory.newMap(mapName);
				maps.put(mapName, map);
			}
			return map;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<BytesMapManager> {

		protected BytesMapFactory mapFactory;
		
		public Builder mapFactory(BytesMapFactory mapFactory) {
			this.mapFactory = mapFactory;
			return this;
		}
		
		@Override
		public BytesMapManager build() {
			return new SimpleBytesMapManager(this);
		}
	}

}
