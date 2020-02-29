package com.tvd12.calabash.local.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.EntityMapPartition;
import com.tvd12.calabash.core.prototype.Prototypes;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.core.util.MapPartitions;
import com.tvd12.calabash.eviction.MapEvictable;
import com.tvd12.calabash.local.builder.EntityMapBuilder;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityMapImpl<K, V>
	extends EzyLoggable
	implements EntityMap<K, V>, MapEvictable, StatisticsAware {

	protected final int maxPartition;
	protected final Prototypes prototypes;
	protected final EntityMapSetting setting;
	protected final EntityMapPersistExecutor mapPersistExecutor;
	protected final EntityMapPartition<K, V>[] partitions;
	
	public EntityMapImpl(EntityMapBuilder builder) {
		this.setting = builder.getMapSetting();
		this.prototypes = builder.getPrototypes();
		this.mapPersistExecutor = builder.getMapPersistExecutor();
		this.maxPartition = setting.getMaxPartition();
		this.partitions = newPartitions();
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
		V copyValue = prototypes.copy(value);
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
		Map<K, V> copy = prototypes.copyMap(m);
		putAllToPartitions(copy);
	}
	
	@Override
	public V get(Object key) {
		V value = getFromPartition(key);
		V copyValue = prototypes.copy(value);
		return copyValue;
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
		Map<K, V> copyMap = prototypes.copyMap(answer);
		return copyMap;
	}
	
	@Override
	public V getByQuery(K key, Object query) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		V value = partitions[pindex].getByQuery(key, query);
		return value;
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
	
	@Override
	public void addStatistics(Map<String, Object> statistics) {
		statistics.put("size", size());
	}

	@Override
	public String getName() {
		return setting.getMapName();
	}

}
