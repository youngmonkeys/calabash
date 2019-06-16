package com.tvd12.calabash.local.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.local.builder.EntityMapBuilder;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.ezyfox.concurrent.EzyConcurrentHashMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LocalEntityMap<K, V>
	extends EzyLoggable
	implements EntityMap<K, V> {

	protected Map<K, V> map;
	protected EntityMapSetting setting;
	protected EzyMapLockProvider lockProvider;
	protected EntityMapPersistExecutor mapPersistExecutor;
	
	public LocalEntityMap(EntityMapBuilder builder) {
		this.setting = builder.getMapSetting();
		this.mapPersistExecutor = builder.getMapPersistExecutor();
		this.lockProvider = new EzyConcurrentHashMapLockProvider();
	}
	
	@Override
	public Map<K, V> loadAll() {
		synchronized (map) {
			Map m = mapPersistExecutor.loadAll(setting);
			map.putAll(m);
			return m;
		}
	}

	@Override
	public void set(K key, V value) {
		put(key, value);
	}
	
	@Override
	public V put(K key, V value) {
		synchronized (map) {
			V v = map.put(key, value);
			mapPersistExecutor.persist(setting, key, value);
			return v;
		}
	}

	@Override
	public void putAll(Map<K, V> m) {
		synchronized (map) {
			map.putAll(m);
			mapPersistExecutor.persist(setting, m);
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
				map.put((K)key, unloadValue);
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
	public boolean containsKey(K key) {
		synchronized (map) {
			boolean contains = map.containsKey(key);
			if(contains)
				return true;
			V unloadValue = (V)mapPersistExecutor.load(setting, key);
			if(unloadValue == null)
				return false;
			map.put(key, unloadValue);
			return true;
		}
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
