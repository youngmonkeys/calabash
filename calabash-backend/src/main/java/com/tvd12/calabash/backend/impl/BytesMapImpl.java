package com.tvd12.calabash.backend.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import com.tvd12.calabash.backend.builder.BytesMapBuilder;
import com.tvd12.calabash.backend.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.backend.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.backend.setting.MapSetting;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.concurrent.EzyConcurrentHashMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;

public class BytesMapImpl implements BytesMap {

	protected final MapSetting setting;
	protected final EzyMapLockProvider lockProvider;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	protected final Map<ByteArray, byte[]> map = new HashMap<>();
	
	public BytesMapImpl(BytesMapBuilder builder) {
		this.setting = builder.getMapSetting();
		this.mapBackupExecutor = builder.getMapBackupExecutor();
		this.mapPersistExecutor = builder.getMapPersistExecutor();
		this.lockProvider = new EzyConcurrentHashMapLockProvider();
	}
	
	@Override
	public Map<ByteArray, byte[]> loadAll() {
		synchronized (map) {
			Map<ByteArray, byte[]> all = mapPersistExecutor.loadAll(setting);
			map.putAll(all);
			mapBackupExecutor.backup(setting, all);
			return all;
		}
	}
	
	@Override
	public void set(ByteArray key, byte[] value) {
		put(key, value);
	}
	
	@Override
	public byte[] put(ByteArray key, byte[] value) {
		synchronized (map) {
			byte[] old = map.put(key, value);
			mapBackupExecutor.backup(setting, key, value);
			mapPersistExecutor.persist(setting, key, value);
			return old;
		}
	}
	
	@Override
	public void putAll(Map<ByteArray, byte[]> m) {
		synchronized (map) {
			map.putAll(m);
			mapBackupExecutor.backup(setting, m);
			mapPersistExecutor.persist(setting, m);
		}
	}

	@Override
	public byte[] get(ByteArray key) {
		byte[] value = null;
		synchronized (map) {
			value = map.get(key);
		}
		if(value != null)
			return value;
		value = load(key);
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
			unloadValue = mapPersistExecutor.load(setting, key);
		}
		finally {
			keyLock.unlock();
		}
		if(unloadValue != null) {
			synchronized (map) {
				map.put(key, unloadValue);
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
			Map<ByteArray, byte[]> unloadItems = mapPersistExecutor.load(setting, keys);
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
		return answer;
	}
	
	@Override
	public boolean containsKey(ByteArray key) {
		synchronized (map) {
			boolean contains = map.containsKey(key);
			if(contains)
				return true;
			byte[] unloadValue = mapPersistExecutor.load(setting, key);
			if(unloadValue == null)
				return false;
			map.put(key, unloadValue);
			return true;
		}
	}

	@Override
	public byte[] remove(ByteArray key) {
		synchronized (map) {
			byte[] removed = map.remove(key);
			mapBackupExecutor.remove(setting, key);
			mapPersistExecutor.delete(setting, key);
			return removed;
		}
	}
	
	@Override
	public void remove(Set<ByteArray> keys) {
		synchronized (map) {
			for(ByteArray key : keys)
				map.remove(key);
			mapBackupExecutor.remove(setting, keys);
			mapPersistExecutor.delete(setting, keys);
		}
	}

	@Override
	public Set<ByteArray> getAllKeys(boolean loadAll) {
		synchronized (map) {
			if(loadAll) loadAll();
			Set<ByteArray> keySet = new HashSet<>(map.keySet());
			return keySet;
		}
	}

	@Override
	public List<byte[]> getAllValues(boolean loadAll) {
		synchronized (map) {
			if(loadAll) loadAll();
			List<byte[]> values = new ArrayList<>(map.values());
			return values;
		}
	}

	@Override
	public Set<Entry<ByteArray, byte[]>> getAllEntries(boolean loadAll) {
		synchronized (map) {
			if(loadAll) loadAll();
			Set<Entry<ByteArray, byte[]>> entrySet = new HashSet<>(map.entrySet());
			return entrySet;
		}
	}
	
	@Override
	public int size(boolean loadAll) {
		synchronized (map) {
			if(loadAll) loadAll();
			int size = map.size();
			return size;
		}
	}

	@Override
	public boolean isEmpty(boolean loadAll) {
		synchronized (map) {
			if(loadAll) loadAll();
			boolean empty = map.isEmpty();
			return empty;
		}
	}
	
	@Override
	public void clear(boolean deleteAll) {
		synchronized (map) {
			Set<ByteArray> keySet = new HashSet<>(map.keySet());
			map.clear();
			mapBackupExecutor.clear(setting);
			if(deleteAll) {
				mapPersistExecutor.delete(setting, keySet);
			}
		}
	}
	
	@Override
	public String getName() {
		return setting.getMapName();
	}
}
