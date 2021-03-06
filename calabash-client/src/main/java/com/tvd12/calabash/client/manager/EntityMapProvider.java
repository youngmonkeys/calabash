package com.tvd12.calabash.client.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.client.factory.EntityMapFactory;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityMapProvider {

	protected final Map<String, EntityMap> maps;
	protected final EntityMapFactory mapFactory;
	
	protected EntityMapProvider(Builder builder) {
		this.maps = new HashMap<>();
		this.mapFactory = builder.mapFactory;
	}
	
	public <K, V> EntityMap<K, V> getMap(String name) {
		EntityMap<K, V> map = maps.get(name);
		if(map == null)
			map = newMap(name);
		return map;
	}
	
	public <K, V> EntityMap<K, V> getMap(
			String name, Class<K> keyType, Class<V> valueType) {
		EntityMap<K, V> map = maps.get(name);
		if(map == null)
			map = newMap(name, keyType, valueType);
		return map;
	}
	
	protected <K, V> EntityMap<K, V> newMap(String name) {
		synchronized (maps) {
			EntityMap<K, V> map = maps.get(name);
			if(map == null) {
				map = mapFactory.newMap(name);
				maps.put(name, map);
			}
			return map;
		}
	}
	
	protected <K, V> EntityMap<K, V> newMap(
			String name, Class<K> keyType, Class<V> valueType) {
		synchronized (maps) {
			EntityMap<K, V> map = maps.get(name);
			if(map == null) {
				map = mapFactory.newMap(name, keyType, valueType);
				maps.put(name, map);
			}
			return map;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityMapProvider> {
		
		protected EntityMapFactory mapFactory;
		
		public Builder mapFactory(EntityMapFactory mapFactory) {
			this.mapFactory = mapFactory;
			return this;
		}
		
		@Override
		public EntityMapProvider build() {
			return new EntityMapProvider(this);
		}
		
	}
	
}
