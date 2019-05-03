package com.tvd12.calabash.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.builder.BytesMapBuilder;
import com.tvd12.calabash.core.excecutor.BytesMapBackupExecutor;
import com.tvd12.calabash.core.excecutor.BytesMapPersistExecutor;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.io.EzySets;

import lombok.Getter;

public class BytesMapImpl implements BytesMap {

	@Getter
	protected final String name;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	protected final Object synchronizedLock = new Object();
	protected final Map<ByteArray, byte[]> map = new HashMap<>();
	
	public BytesMapImpl(BytesMapBuilder builder) {
		this.name = builder.getMapName();
		this.mapBackupExecutor = builder.getMapBackupExecutor();
		this.mapPersistExecutor = builder.getMapPersistExecutor();
	}
	
	@Override
	public void loadAll() {
		synchronized (synchronizedLock) {
			Map<ByteArray, byte[]> all = mapPersistExecutor.loadAll(name);
			map.putAll(all);
			mapBackupExecutor.backup(name, all);
		}
	}
	
	@Override
	public void set(ByteArray key, byte[] value) {
		synchronized (synchronizedLock) {
			put(key, value);
		}
	}
	
	@Override
	public byte[] put(ByteArray key, byte[] value) {
		synchronized (synchronizedLock) {
			byte[] old = map.put(key, value);
			mapBackupExecutor.backup(name, key, value);
			mapPersistExecutor.persist(name, key, value);
			return old;
		}
	}
	
	@Override
	public void putAll(Map<ByteArray, byte[]> m) {
		synchronized (synchronizedLock) {
			map.putAll(m);
			mapBackupExecutor.backup(name, m);
			mapPersistExecutor.persist(name, m);
		}
	}

	@Override
	public byte[] get(ByteArray key) {
		synchronized (synchronizedLock) {
			byte[] value = map.get(key);
			if(value != null)
				return value;
			byte[] unloadValue = mapPersistExecutor.load(name, key);
			if(unloadValue == null)
				return null;
			map.put(key, unloadValue);
			return unloadValue;
		}
	}
	
	@Override
	public Map<ByteArray, byte[]> get(Set<ByteArray> keys) {
		Map<ByteArray, byte[]> answer = new HashMap<>();
		Set<ByteArray> loadedKeys = new HashSet<>();
		synchronized (synchronizedLock) {
			for(ByteArray key : keys) {
				byte[] value = map.get(key);
				if(value != null) {
					loadedKeys.add(key);
					answer.put(key, value);
				}
			}
			if(loadedKeys.size() < keys.size()) {
				Set<ByteArray> unloadKeys = EzySets.newHashSet(keys, loadedKeys);
				Map<ByteArray, byte[]> unloadItems = mapPersistExecutor.load(name, unloadKeys);
				map.putAll(unloadItems);
				answer.putAll(unloadItems);
			}
		}
		return answer;
	}
	
	@Override
	public boolean containsKey(ByteArray key) {
		synchronized (synchronizedLock) {
			boolean contains = map.containsKey(key);
			if(contains)
				return true;
			byte[] unloadValue = mapPersistExecutor.load(name, key);
			if(unloadValue == null)
				return false;
			map.put(key, unloadValue);
			return true;
		}
	}

	@Override
	public byte[] remove(ByteArray key) {
		synchronized (synchronizedLock) {
			byte[] removed = map.remove(key);
			mapPersistExecutor.delete(name, key);
			mapBackupExecutor.remove(name, key);
			return removed;
		}
	}
	
	@Override
	public void remove(Set<ByteArray> keys) {
		synchronized (synchronizedLock) {
			for(ByteArray key : keys)
				map.remove(key);
			mapBackupExecutor.remove(name, keys);
			mapPersistExecutor.delete(name, keys);
		}
	}

	@Override
	public Set<ByteArray> getAllKeys(boolean loadAll) {
		synchronized (synchronizedLock) {
			if(loadAll) loadAll();
			Set<ByteArray> keySet = new HashSet<>(map.keySet());
			return keySet;
		}
	}

	@Override
	public List<byte[]> getAllValues(boolean loadAll) {
		synchronized (synchronizedLock) {
			if(loadAll) loadAll();
			List<byte[]> values = new ArrayList<>(map.values());
			return values;
		}
	}

	@Override
	public Set<Entry<ByteArray, byte[]>> getAllEntries(boolean loadAll) {
		synchronized (synchronizedLock) {
			if(loadAll) loadAll();
			Set<Entry<ByteArray, byte[]>> entrySet = new HashSet<>(map.entrySet());
			return entrySet;
		}
	}
	
	@Override
	public int size(boolean loadAll) {
		synchronized (synchronizedLock) {
			if(loadAll) loadAll();
			int size = map.size();
			return size;
		}
	}

	@Override
	public boolean isEmpty(boolean loadAll) {
		synchronized (synchronizedLock) {
			if(loadAll) loadAll();
			boolean empty = map.isEmpty();
			return empty;
		}
	}
	
	@Override
	public void clear(boolean deleteAll) {
		synchronized (synchronizedLock) {
			Set<ByteArray> keySet = new HashSet<>(map.keySet());
			map.clear();
			mapBackupExecutor.clear(name);
			if(deleteAll) {
				mapPersistExecutor.delete(name, keySet);
			}
		}
	}

}
