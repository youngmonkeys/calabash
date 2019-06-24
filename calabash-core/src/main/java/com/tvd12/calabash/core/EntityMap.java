package com.tvd12.calabash.core;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.query.MapQuery;

public interface EntityMap<K, V> {

	Map<K, V> loadAll();
	
	void set(K key, V value);
	
	V put(K key, V value);

	void putAll(Map<K, V> m);
	
	V get(Object key);
	
	Map<K, V> get(Set<K> keys);
	
	V getByQuery(MapQuery query);
	
	boolean containsKey(K key);

	V remove(Object key);
	
	void remove(Set<K> keys);

	int size();

	boolean isEmpty();
	
	void clear();
	
}
