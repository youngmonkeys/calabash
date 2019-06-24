package com.tvd12.calabash.local.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.query.MapQuery;
import com.tvd12.calabash.local.builder.EntityMapBuilder;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.calabash.local.unique.EntityUniques;
import com.tvd12.ezyfox.concurrent.EzyConcurrentHashMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMixedMapLockProxyProvider;
import com.tvd12.ezyfox.util.EzyHasIdEntity;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityMapImpl<K, V>
	extends EzyLoggable
	implements EntityMap<K, V> {

	protected final Map<K, V> map;
	protected final EntityUniques<V> uniques;
	protected final EntityMapSetting setting;
	protected final EzyMapLockProvider lockProvider;
	protected final EzyMapLockProvider lockProviderForQuery;
	protected final EntityMapPersistExecutor mapPersistExecutor;
	protected final Map<Object, Function<V, Object>> uniqueKeyMaps;
	
	public EntityMapImpl(EntityMapBuilder builder) {
		this.map = new HashMap<>();
		this.setting = builder.getMapSetting();
		this.uniqueKeyMaps = builder.getUniqueKeyMaps();
		this.mapPersistExecutor = builder.getMapPersistExecutor();
		this.uniques = newUniques();
		this.lockProvider = new EzyConcurrentHashMapLockProvider();
		this.lockProviderForQuery = new EzyMixedMapLockProxyProvider();
	}
	
	protected EntityUniques newUniques() {
		return new EntityUniques<>(uniqueKeyMaps);
	}
	
	@Override
	public Map<K, V> loadAll() {
		Map m = null;
		synchronized (map) {
			m = mapPersistExecutor.loadAll(setting);
			map.putAll(m);
		}
		synchronized (uniques) {
			uniques.putValues(m.values());
		}
		return m;
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
			mapPersistExecutor.persist(setting, key, value);
		}
		synchronized (uniques) {
			uniques.putValue(value);
		}
		return v;
	}

	@Override
	public void putAll(Map<K, V> m) {
		synchronized (map) {
			map.putAll(m);
			mapPersistExecutor.persist(setting, m);
		}
		synchronized (uniques) {
			uniques.putValues(m.values());
		}
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
			unloadValue = (V)mapPersistExecutor.load(setting, key);
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
			Map<K, V> unloadItems = mapPersistExecutor.load(setting, unloadKeys);
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
		return answer;
	}
	
	@Override
	public V getByQuery(MapQuery query) {
		K key = null;
		V value = null;
		Map<Object, Object> uniqueKeys = null;
		if(query instanceof EzyHasIdEntity) {
			EzyHasIdEntity<K> hasId = (EzyHasIdEntity<K>)query;
			key = hasId.getId();
			synchronized (map) {
				value = map.get(key);
			}
		}
		if(value == null) {
			uniqueKeys = query.getKeys();
			synchronized (uniques) {
				value = uniques.getValue(uniqueKeys);
			}
		}
		if(value == null)
			value = loadByQuery(query, key, uniqueKeys);
		return value;
	}
	
	protected V loadByQuery(MapQuery query, K key, Map<Object, Object> uniqueKeys) {
		V unloadValue = null;
		Lock lock = lockProviderForQuery.provideLock(query);
		lock.lock();
		try {
			V value = null;
			if(key != null)
				value = map.get(key);
			if(value == null)
				value = uniques.getValue(uniqueKeys);
			if(value != null)
				return value;
			unloadValue = (V)mapPersistExecutor.loadByQuery(setting, query);
			
			if(unloadValue != null) {
				if(key != null) {
					synchronized (map) {
						map.put((K)key, unloadValue);
					}
				}
				synchronized (uniques) {
					uniques.putValue(unloadValue);
				}
			}
		}
		finally {
			lock.unlock();
		}
		return unloadValue;
	}
	
	@Override
	public boolean containsKey(K key) {
		V unloadValue = null;
		synchronized (map) {
			boolean contains = map.containsKey(key);
			if(contains)
				return true;
			unloadValue = (V)mapPersistExecutor.load(setting, key);
			if(unloadValue == null)
				return false;
			map.put(key, unloadValue);
		}
		synchronized (uniques) {
			uniques.putValue(unloadValue);
		}
		return true;
	}

	@Override
	public V remove(Object key) {
		synchronized (map) {
			V v = map.remove(key);
			mapPersistExecutor.delete(setting, key);
			return v;
		}
	}

	@Override
	public void remove(Set<K> keys) {
		synchronized (map) {
			for(K key : keys)
				map.remove(key);
			mapPersistExecutor.delete(setting, keys);
		}
	}

	@Override
	public int size() {
		synchronized (map) {
			int size = map.size();
			return size;
		}
	}
	
	@Override
	public boolean isEmpty() {
		synchronized (map) {
			boolean empty = map.isEmpty();
			return empty;
		}
	}
	
	@Override
	public void clear() {
		synchronized (map) {
			Set<K> keys = new HashSet<>(map.keySet());
			map.clear();
			mapPersistExecutor.delete(setting, keys);
		}
	}

}
