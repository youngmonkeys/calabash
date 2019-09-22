package com.tvd12.calabash.server.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.BytesMapPartition;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.core.util.MapPartitions;
import com.tvd12.calabash.eviction.MapEvictable;
import com.tvd12.calabash.server.core.builder.BytesMapBuilder;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.setting.MapSetting;
import com.tvd12.ezyfox.util.EzyLoggable;

public class BytesMapImpl
		extends EzyLoggable
		implements BytesMap, MapEvictable, StatisticsAware  {

	protected final int maxPartition;
	protected final MapSetting setting;
	protected final BytesMapPartition[] partitions;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	
	public BytesMapImpl(BytesMapBuilder builder) {
		this.setting = builder.getMapSetting();
		this.mapBackupExecutor = builder.getMapBackupExecutor();
		this.mapPersistExecutor = builder.getMapPersistExecutor();
		this.maxPartition = setting.getMaxPartition();
		this.partitions = newPartitions();
	}
	
	protected BytesMapPartition[] newPartitions() {
		BytesMapPartition[] array = new BytesMapPartition[maxPartition];
		for(int i = 0 ; i < array.length ; ++i) 
			array[i] = newPartition();
		return array;
	}
	
	protected BytesMapPartition newPartition() {
		return BytesMapPartitionImpl.builder()
				.mapSetting(setting)
				.mapBackupExecutor(mapBackupExecutor)
				.mapPersistExecutor(mapPersistExecutor)
				.build();
	}
	
	@Override
	public Map<ByteArray, byte[]> loadAll() {
		Map<ByteArray, byte[]> m = mapPersistExecutor.loadAll(setting);
		putAllToPartitions(m);
		return m;
	}
	
	protected void putAllToPartitions(Map<ByteArray, byte[]> m) {
		Map<Integer, Map<ByteArray, byte[]>> cm = MapPartitions.classifyMaps(maxPartition, m);
		for(Integer index : cm.keySet())
			partitions[index].putAll(cm.get(index));
	}
	
	@Override
	public void set(ByteArray key, byte[] value) {
		put(key, value);
	}
	
	@Override
	public byte[] put(ByteArray key, byte[] value) {
		byte[] v = putToPartition(key, value);
		return v;
	}
	
	protected byte[] putToPartition(ByteArray key, byte[] value) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		byte[] v = partitions[pindex].put(key, value);
		return v;
	}
	
	@Override
	public void putAll(Map<ByteArray, byte[]> m) {
		putAllToPartitions(m);
	}

	@Override
	public byte[] get(ByteArray key) {
		byte[] value = getFromPartition(key);
		return value;
		
	}
	
	protected byte[] getFromPartition(ByteArray key) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		byte[] value = partitions[pindex].get(key);
		return value;
	}
	
	@Override
	public Map<ByteArray, byte[]> get(Set<ByteArray> keys) {
		Map<ByteArray, byte[]> answer = new HashMap<>();
		Map<Integer, Set<ByteArray>> ckeys = MapPartitions.classifyKeys(maxPartition, keys);
		for(Integer index : ckeys.keySet()) {
			Set<ByteArray> pkeys = ckeys.get(index);
			Map<ByteArray, byte[]> fragment = partitions[index].get(pkeys);
			answer.putAll(fragment);
		}
		return answer;
	}
	
	@Override
	public byte[] remove(ByteArray key) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		byte[] v = partitions[pindex].remove(key);
		return v;
	}
	
	@Override
	public void remove(Set<ByteArray> keys) {
		Map<Integer, Set<ByteArray>> ckeys = MapPartitions.classifyKeys(maxPartition, keys);
		for(Integer index : ckeys.keySet()) {
			Set<ByteArray> pkeys = ckeys.get(index);
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
