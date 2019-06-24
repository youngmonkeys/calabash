package com.tvd12.calabash.local.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleEntityUniqueFactory implements EntityUniqueFactory {

	protected final Map<String, Map<Object, Function>> uniqueKeyMapss;
	
	public SimpleEntityUniqueFactory() {
		this.uniqueKeyMapss = new HashMap<>();
	}
	
	protected SimpleEntityUniqueFactory(Builder builder) {
		this.uniqueKeyMapss = builder.uniqueKeyMapss;
	}
	
	@Override
	public Map<Object, Function> newUniqueKeyMaps(String mapName) {
		Map<Object, Function> uniqueKeyMaps = uniqueKeyMapss.get(mapName);
		if(uniqueKeyMaps != null)
			return uniqueKeyMaps;
		return new HashMap<>();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityUniqueFactory> {
		
		protected Map<String, Map<Object, Function>> uniqueKeyMapss;
		
		public Builder() {
			this.uniqueKeyMapss = new HashMap<>();
		}
		
		public Builder addUniqueKeyMaps(String mapName, Map<Object, Function> uniqueKeyMaps) {
			uniqueKeyMapss.put(mapName, uniqueKeyMaps);
			return this;
		}
		
		public <V> UniqueKeyMapsBuilder<V> newUniqueKeyMapsBuilder(String mapName) {
			return new UniqueKeyMapsBuilder<>(this, mapName);
		}
		
		@Override
		public EntityUniqueFactory build() {
			return new SimpleEntityUniqueFactory(this);
		}
		
	}
	
	public static class UniqueKeyMapsBuilder<V> implements EzyBuilder<Map<Object, Function<V, Object>>> {
		
		protected Builder parent;
		protected String mapName;
		protected Map<Object, Function<V, Object>> uniqueKeyMaps;
		
		public UniqueKeyMapsBuilder(Builder parent, String mapName) {
			this.parent = parent;
			this.mapName = mapName;
			this.uniqueKeyMaps = new HashMap<>();
		}
		
		public UniqueKeyMapsBuilder<V> addUniqueKeyMap(Object uniqueName, Function<V,	Object> map) {
			uniqueKeyMaps.put(uniqueName, map);
			return this;
		}
		
		@Override
		public Map<Object, Function<V, Object>> build() {
			this.parent.addUniqueKeyMaps(mapName, (Map)uniqueKeyMaps);
			return uniqueKeyMaps;
		}
	}

}
