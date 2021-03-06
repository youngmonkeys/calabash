package com.tvd12.calabash.server.core.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.converter.BytesLongConverter;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.BytesMapPartition;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.core.util.MapPartitions;
import com.tvd12.calabash.eviction.MapEvictable;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.setting.MapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

public class BytesMapImpl
		extends EzyLoggable
		implements BytesMap, MapEvictable, StatisticsAware  {

	protected final int maxPartition;
	protected final MapSetting setting;
	protected final BytesMapPartition[] partitions;
	protected final BytesLongConverter bytesLongConverter;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	
	public BytesMapImpl(Builder builder) {
		this.setting = builder.mapSetting;
		this.bytesLongConverter = builder.bytesLongConverter;
		this.mapBackupExecutor = builder.mapBackupExecutor;
		this.mapPersistExecutor = builder.mapPersistExecutor;
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
				.bytesLongConverter(bytesLongConverter)
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
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		byte[] value = partitions[pindex].get(key);
		return value;
	}
	
	@Override
	public byte[] getByQuery(ByteArray key, byte[] query) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		byte[] value = partitions[pindex].getByQuery(key, query);
		return value;
	}
	
	@Override
	public Map<ByteArray, byte[]> get(Collection<ByteArray> keys) {
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
	public long addAndGet(ByteArray key, long delta) {
		int pindex = MapPartitions.getPartitionIndex(maxPartition, key);
		long answer = partitions[pindex].addAndGet(key, delta);
		return answer;
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
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<BytesMap> {
		
		protected MapSetting mapSetting;
		protected BytesLongConverter bytesLongConverter;
		protected BytesMapBackupExecutor mapBackupExecutor;
		protected BytesMapPersistExecutor mapPersistExecutor;
		
		public Builder mapSetting(MapSetting mapSetting) {
			this.mapSetting = mapSetting;
			return this;
		}
		
		public Builder bytesLongConverter(BytesLongConverter bytesLongConverter) {
			this.bytesLongConverter = bytesLongConverter;
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
		public BytesMap build() {
			return new BytesMapImpl(this);
		}
		
	}
}
