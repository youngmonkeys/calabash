package com.tvd12.calabash.local.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

import com.tvd12.calabash.core.EntityMapPartition;
import com.tvd12.calabash.eviction.MapEviction;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.calabash.local.unique.EntityUniques;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyConcurrentHashMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;
import com.tvd12.ezyfox.function.EzyVoid;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityMapPartitionImpl<K, V>
	extends EzyLoggable
	implements EntityMapPartition<K, V> {

	protected final Map<K, V> map;
	protected final MapEviction mapEviction;
	protected final EntityUniques<V> uniques;
	protected final EntityMapSetting mapSetting;
	protected final EzyMapLockProvider lockProvider;
	protected final EntityMapPersistExecutor mapPersistExecutor;
	protected final Map<Object, Function<V, Object>> uniqueKeyMaps;

	protected final static EzyVoid EMPTY_FUNC = () -> {};
	
	public EntityMapPartitionImpl(Builder builder) {
		this.map = new HashMap<>();
		this.mapSetting = builder.mapSetting;
		this.uniques = builder.uniques;
		this.uniqueKeyMaps = builder.uniqueKeyMaps;
		this.mapPersistExecutor = builder.mapPersistExecutor;
		this.lockProvider = new EzyConcurrentHashMapLockProvider();
		this.mapEviction = new MapEviction(mapSetting.getEvictionSetting());
	}
	
	protected EntityUniques newUniques() {
		return new EntityUniques<>(uniqueKeyMaps);
	}
	
	@Override
	public void set(K key, V value) {
		put(key, value);
	}
	
	@Override
	public V put(K key, V value) {
		V v = null;
		synchronized (map) {
			v = map.put(key, value);
			mapPersistExecutor.persist(mapSetting, key, value);
		}
		synchronized (uniques) {
			uniques.putValue(value);
		}
		mapEviction.updateKeyTime(key);
		return v;
	}

	@Override
	public void putAll(Map<K, V> m) {
		synchronized (map) {
			map.putAll(m);
			mapPersistExecutor.persist(mapSetting, m);
		}
		synchronized (uniques) {
			uniques.putValues(m.values());
		}
		mapEviction.updateKeysTime(m.keySet());
	}
	
	@Override
	public V get(Object key) {
		V value = null;
		synchronized (map) {
			value = map.get(key);
		}
		if(value == null)
			value = load(key);
		return value;
	}
	
	protected V load(Object key) {
		Lock lock = lockProvider.provideLock(key);
		V unloadValue = null;
		lock.lock();
		try {
			V value = map.get(key);
			if(value != null)
				return value;
			unloadValue = (V)mapPersistExecutor.load(mapSetting, key);
		}
		finally {
			lock.unlock();
		}
		if(unloadValue != null) {
			synchronized (map) {
				if(!map.containsKey(key))
					map.put((K)key, unloadValue);
			}
			synchronized (uniques) {
				uniques.putValue(unloadValue);
			}
			mapEviction.updateKeyTime(key);
		}
		return unloadValue;
	}

	@Override
	public Map<K, V> get(Set<K> keys) {
		Map<K, V> answer = new HashMap<>();
		Set<K> unloadKeys = new HashSet<>();
		synchronized (map) {
			for(K key : keys) {
				V value = map.get(key);
				if(value != null)
					answer.put(key, value);
				else
					unloadKeys.add(key);
			}
		}
		if(unloadKeys.size() > 0) {
			Map<K, V> unloadItems = mapPersistExecutor.load(mapSetting, unloadKeys);
			synchronized (map) {
				for(K key : unloadItems.keySet()) {
					V value = map.get(key);
					if(value == null) {
						value = unloadItems.get(key);
						map.put(key, value);
					}
					answer.put(key, value);
				}
			}
		}
		mapEviction.updateKeysTime(answer.keySet());
		return answer;
	}
	
	@Override
	public boolean containsKey(K key) {
		V unloadValue = null;
		boolean contains = false;
		synchronized (map) {
			contains = map.containsKey(key);
			if(!contains) {
				unloadValue = (V)mapPersistExecutor.load(mapSetting, key);
				if(unloadValue == null) {
					contains = false;
				}
				else {
					map.put(key, unloadValue);
					contains = true;
				}
			}
		}
		if(unloadValue != null) {
			synchronized (uniques) {
				uniques.putValue(unloadValue);
			}
		}
		if(contains) {
			mapEviction.updateKeyTime(key);
		}
		return contains;
	}

	@Override
	public V remove(Object key) {
		V v = null;
		synchronized (map) {
			v = map.remove(key);
			if(v != null)
				mapPersistExecutor.delete(mapSetting, key);
		}
		if(v != null) {
			synchronized (uniques) {
				uniques.removeValue(v);
			}
			lockProvider.removeLock(key);
			mapEviction.removeKey(key);
		}
		return v;
	}

	@Override
	public void remove(Set<K> keys) {
		remove(keys, () -> mapPersistExecutor.delete(mapSetting, keys));
		mapEviction.removeKeys(keys);
	}
	
	protected void remove(Collection<K> keys, EzyVoid postRemoveFunc) {
		List<V> removedValues = new ArrayList<>();
		synchronized (map) {
			for(K key : keys) {
				V v = map.remove(key);
				if(v != null) {
					removedValues.add(v);
				}
			}
			postRemoveFunc.apply();
		}
		synchronized (uniques) {
			uniques.removeValues(removedValues);
		}
		for(K key : keys) {
			lockProvider.removeLock(key);
		}
	}

	@Override
	public void clear() {
		Set<K> keys = null;
		synchronized (map) {
			keys = new HashSet<>(map.keySet());
		}
		remove(keys);
	}
	
	@Override
	public void evict() {
		List<K> evictableKeys = mapEviction.getEvictableKeys();
		remove(evictableKeys, EMPTY_FUNC);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityMapPartition> {

		protected Map uniqueKeyMaps;
		protected EntityUniques uniques;
		protected EntityMapSetting mapSetting;
		protected EntityMapPersistExecutor mapPersistExecutor;
		
		public Builder uniqueKeyMaps(Map uniqueKeyMaps) {
			this.uniqueKeyMaps = uniqueKeyMaps;
			return this;
		}
		
		public Builder uniques(EntityUniques uniques) {
			this.uniques = uniques;
			return this;
		}
		
		public Builder mapSetting(EntityMapSetting mapSetting) {
			this.mapSetting = mapSetting;
			return this;
		}
		
		public Builder mapPersistExecutor(EntityMapPersistExecutor mapPersistExecutor) {
			this.mapPersistExecutor = mapPersistExecutor;
			return this;
		}
		
		@Override
		public EntityMapPartition build() {
			return new EntityMapPartitionImpl(this);
		}
		
	}

}
