package com.tvd12.calabash.server.core.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import com.tvd12.calabash.core.BytesMapPartition;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.eviction.MapEviction;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.setting.MapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyConcurrentHashMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;
import com.tvd12.ezyfox.function.EzyVoid;
import com.tvd12.ezyfox.util.EzyLoggable;

public class BytesMapPartitionImpl 
		extends EzyLoggable 
		implements BytesMapPartition {

	protected final MapSetting mapSetting;
	protected final MapEviction mapEviction;
	protected final Map<ByteArray, byte[]> map;
	protected final EzyMapLockProvider lockProvider;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	
	protected final static EzyVoid EMPTY_FUNC = () -> {};
	
	public BytesMapPartitionImpl(Builder builder) {
		this.map  = new HashMap<>();
		this.mapSetting = builder.mapSetting;
		this.mapBackupExecutor = builder.mapBackupExecutor;
		this.mapPersistExecutor = builder.mapPersistExecutor;
		this.lockProvider = new EzyConcurrentHashMapLockProvider();
		this.mapEviction = new MapEviction(mapSetting.getEvictionSetting());
	}
	
	@Override
	public void set(ByteArray key, byte[] value) {
		put(key, value);
	}
	
	@Override
	public byte[] put(ByteArray key, byte[] value) {
		byte[] v = null;
		synchronized (map) {
			v = map.put(key, value);
			mapBackupExecutor.backup(mapSetting, key, value);
			mapPersistExecutor.persist(mapSetting, key, value);
		}
		mapEviction.updateKeyTime(key);
		return v;
	}
	
	@Override
	public void putAll(Map<ByteArray, byte[]> m) {
		synchronized (map) {
			map.putAll(m);
			mapBackupExecutor.backup(mapSetting, m);
			mapPersistExecutor.persist(mapSetting, m);
		}
		mapEviction.updateKeysTime(m.keySet());
	}

	@Override
	public byte[] get(ByteArray key) {
		byte[] value = null;
		synchronized (map) {
			value = map.get(key);
		}
		if(value == null)
			value = load(key);
		if(value != null)
			mapEviction.updateKeyTime(key);
		return value;
		
	}
	
	protected byte[] load(ByteArray key) {
		Lock keyLock = lockProvider.provideLock(key);
		byte[] unloadValue = null;
		keyLock.lock();
		try {
			byte[] value = map.get(key);
			if(value != null)
				return value;
			unloadValue = mapPersistExecutor.load(mapSetting, key);
		}
		finally {
			keyLock.unlock();
		}
		if(unloadValue != null) {
			synchronized (map) {
				map.putIfAbsent(key, unloadValue);
			}
		}
		return unloadValue;
	}
	
	@Override
	public Map<ByteArray, byte[]> get(Set<ByteArray> keys) {
		Map<ByteArray, byte[]> answer = new HashMap<>();
		Set<ByteArray> unloadKeys = new HashSet<>();
		synchronized (map) {
			for(ByteArray key : keys) {
				byte[] value = map.get(key);
				if(value != null)
					answer.put(key, value);
				else
					unloadKeys.add(key);
			}
		}
		if(unloadKeys.size() > 0) {
			Map<ByteArray, byte[]> unloadItems = mapPersistExecutor.load(mapSetting, keys);
			synchronized (map) {
				for(ByteArray key : unloadItems.keySet()) {
					byte[] value = map.get(key);
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
	public byte[] remove(ByteArray key) {
		byte[] v = null;
		synchronized (map) {
			v = map.remove(key);
			mapBackupExecutor.remove(mapSetting, key);
			mapPersistExecutor.delete(mapSetting, key);
		}
		if(v != null) {
			lockProvider.removeLock(key);
			mapEviction.removeKey(key);
		}
		return v;
	}
	
	@Override
	public void remove(Set<ByteArray> keys) {
		remove(keys, () -> mapPersistExecutor.delete(mapSetting, keys));
		mapEviction.removeKeys(keys);
	}
	
	protected void remove(Collection<ByteArray> keys, EzyVoid postRemoveFunc) {
		synchronized (map) {
			for(ByteArray key : keys)
				map.remove(key);
			mapBackupExecutor.remove(mapSetting, keys);
			postRemoveFunc.apply();
		}
		for(ByteArray key : keys) {
			lockProvider.removeLock(key);
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
	public void clear() {
		Set<ByteArray> keys = null;
		synchronized (map) {
			keys = new HashSet<>(map.keySet());
		}
		remove(keys);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void evict() {
		List<ByteArray> evictableKeys = mapEviction.getEvictableKeys();
		remove(evictableKeys, EMPTY_FUNC);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<BytesMapPartition> {
		
		protected MapSetting mapSetting;
		protected BytesMapBackupExecutor mapBackupExecutor;
		protected BytesMapPersistExecutor mapPersistExecutor;
		
		public Builder mapSetting(MapSetting mapSetting) {
			this.mapSetting = mapSetting;
			return this;
		}
		
		public Builder mapBackupExecutor(BytesMapBackupExecutor mapBackupExecutor) {
			this.mapBackupExecutor = mapBackupExecutor;
			return this;
		}
		
		public Builder mapPersistExecutor(BytesMapPersistExecutor mapPersistExecutor) {
			this.mapPersistExecutor = mapPersistExecutor;
			return this;
		}
		
		@Override
		public BytesMapPartition build() {
			return new BytesMapPartitionImpl(this);
		}
		
	}

}
