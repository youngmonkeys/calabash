package com.tvd12.calabash.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MapPartitions {

	private MapPartitions() {
	}
	
	public static int getPartitionIndex(int maxPartition, Object key) {
		int hashCode = key.hashCode();
		int index = Math.abs(hashCode % maxPartition);
		return index;
	}
	
	public static <K> Map<Integer, Set<K>> classifyKeys(int maxPartition, Collection<K> keys) {
		Map<Integer, Set<K>> answer = new HashMap<>();
		for(K key : keys) {
			int index = getPartitionIndex(maxPartition, key);
			Set<K> v = answer.get(index);
			if(v == null) {
				v = new HashSet<>();
				answer.put(index, v);
			}
			v.add(key);
		}
		return answer;
	}
	
	public static <K, V> Map<Integer, Map<K, V>> classifyMaps(int maxPartition, Map<K, V> map) {
		Map<Integer, Map<K, V>> answer = new HashMap<>();
		for(K key : map.keySet()) {
			int index = getPartitionIndex(maxPartition, key);
			Map<K, V> v = answer.get(index);
			if(v == null) {
				v = new HashMap<>();
				answer.put(index, v);
			}
			v.put(key, map.get(key));
		}
		return answer;
}
	
}
