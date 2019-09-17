package com.tvd12.calabash.local.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.EntityMapPartition;
import com.tvd12.calabash.core.query.MapQuery;
import com.tvd12.calabash.core.util.MapPartitions;
import com.tvd12.calabash.core.util.Protypes;
import com.tvd12.calabash.eviction.MapEvictable;
import com.tvd12.calabash.local.builder.EntityMapBuilder;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.calabash.local.unique.EntityUniques;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMixedMapLockProxyProvider;
import com.tvd12.ezyfox.util.EzyHasIdEntity;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityMapImpl<K, V>
	extends EzyLoggable
	implements EntityMap<K, V>, MapEvictable {

	protected final int maxPartition;
	protected final EntityUniques<V> uniques;
	protected final EntityMapSetting setting;
	protected final EzyMapLockProvider lockProviderForQuery;
	protected final EntityMapPersistExecutor mapPersistExecutor;
	protected final Map<Object, Function<V, Object>> uniqueKeyMaps;
	protected final EntityMapPartition<K, V>[] partitions;
	
	public EntityMapImpl(EntityMapBuilder builder) {
		this.setting = builder.getMapSetting();
		this.uniqueKeyMaps = builder.getUniqueKeyMaps();
		this.mapPersistExecutor = builder.getMapPersistExecutor();
		this.maxPartition = setting.getMaxPartition();
		this.uniques = newUniques();
		this.partitions = newPartitions();
		this.lockProviderForQuery = new EzyMixedMapLockProxyProvider();
	}
	
	protected EntityUniques newUniques() {
		return new EntityUniques<>(uniqueKeyMaps);
	}
	
	protected EntityMapPartition<K, V>[] newPartitions() {
		EntityMapPartition<K, V>[] array = new EntityMapPartition[maxPartition];
		for(int i = 0 ; i < array.length ; ++i) 
			array[i] = newPartition();
		return array;
	}
	
	protected EntityMapPartition<K, V> newPartition() {
		return EntityMapPartitionImpl.builder()
				.mapSetting(setting)
				.uniques(uniques)
				.uniqueKeyMaps(uniqueKeyMaps)
				.mapPersistExecutor(mapPersistExecutor)
				.build();
	}
	
	@Override
	public Map<K, V> loadAll() {
		Map m = mapPersistExecutor.loadAll(setting);
		putAllToPartitions(m);
		return m;
	}
	
	protected void putAllToPartitions(Map m) {
		Map<Integer, Map> cm = MapPartitions.classifyMaps(maxPartition, m);
		for(Integer index : cm.keySet())
			partitions[index].putAll(cm.get(index));
	}

	@Override
	public void set(K key, V value) {
		put(key, value);
	}
	
	@Override
	public V put(K key, V value) {
		V copyValue = Protypes.copy(value);
		V v = putToPartition(key, copyValue);
		return v;
	}
	
	protected V putToPartition(K key, V value) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		V v = partitions[pindex].put(key, value);
		return v;
	}

	@Override
	public void putAll(Map<K, V> m) {
		Map<K, V> copy = Protypes.copyMap(m);
		putAllToPartitions(copy);
	}
	
	@Override
	public V get(Object key) {
		V value = getFromPartition(key);
		return Protypes.copy(value);
	}
	
	protected V getFromPartition(Object key) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		V value = partitions[pindex].get(key);
		return value;
	}

	@Override
	public Map<K, V> get(Set<K> keys) {
		Map<K, V> answer = new HashMap<>();
		Map<Integer, Set<K>> ckeys = MapPartitions.classifyKeys(maxPartition, keys);
		for(Integer index : ckeys.keySet()) {
			Set<K> pkeys = ckeys.get(index);
			Map<K, V> fragment = partitions[index].get(pkeys);
			answer.putAll(fragment);
		}
		return Protypes.copyMap(answer);
	}
	
	@Override
	public V getByQuery(MapQuery query) {
		K key = null;
		V value = null;
		Map<Object, Object> uniqueKeys = null;
		if(query instanceof EzyHasIdEntity) {
			EzyHasIdEntity<K> hasId = (EzyHasIdEntity<K>)query;
			key = hasId.getId();
			value = getFromPartition(key);
		}
		if(value == null) {
			uniqueKeys = query.getKeys();
			synchronized (uniques) {
				value = uniques.getValue(uniqueKeys);
			}
		}
		if(value == null)
			value = loadByQuery(query, key, uniqueKeys);
		return Protypes.copy(value);
	}
	
	protected V loadByQuery(MapQuery query, K key, Map<Object, Object> uniqueKeys) {
		V unloadValue = null;
		Lock lock = lockProviderForQuery.provideLock(query);
		lock.lock();
		try {
			V value = null;
			if(key != null)
				value = getFromPartition(key);
			if(value == null)
				value = uniques.getValue(uniqueKeys);
			if(value != null)
				return value;
			unloadValue = (V)mapPersistExecutor.loadByQuery(setting, query);
			
			if(unloadValue != null) {
				if(key != null) {
					putToPartition(key, unloadValue);
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
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		boolean answer = partitions[pindex].containsKey(key);
		return answer;
	}

	@Override
	public V remove(Object key) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		V v = partitions[pindex].remove(key);
		return v;
	}

	@Override
	public void remove(Set<K> keys) {
		Map<Integer, Set<K>> ckeys = MapPartitions.classifyKeys(maxPartition, keys);
		for(Integer index : ckeys.keySet()) {
			Set<K> pkeys = ckeys.get(index);
			partitions[index].remove(pkeys);
		}
	}

	@Override
	public void clear() {
		for(int i = 0 ; i < maxPartition ; ++i)
			partitions[i].clear();
	}
	
	@Override
	public long size() {
		long size = 0;
		for(int i = 0 ; i < maxPartition ; ++i)
			size += partitions[i].size();
		return size;
	}
	
	@Override
	public void evict() {
		for(int i = 0 ; i < maxPartition ; ++i)
			partitions[i].evict();
	}

}
