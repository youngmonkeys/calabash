package com.tvd12.calabash.core;

import java.util.Map;
import java.util.Set;

public interface EntityMapPartition<K, V> {

	void set(K key, V value);
	
	V put(K key, V value);

	void putAll(Map<K, V> m);
	
	V get(Object key);
	
	Map<K, V> get(Set<K> keys);
	
	V remove(Object key);
	
	void remove(Set<K> keys);

	int size();
	
	void clear();
	
	void evict();
	
}