package com.tvd12.calabash.core;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface EntityMap<K, V> extends IMap, Map<K, V> {

	Map<K, V> loadAll();
	
	void set(K key, V value);
	
	Map<K, V> get(Collection<K> keys);
	
	V getByQuery(K key, Object query);
	
	void remove(Set<K> keys);
	
	long sizeLong();
	
	@Override
	default boolean containsKey(Object key) {
		V value = get(key);
		return value != null;
	}
	
	@Override
	default int size() {
		return (int)sizeLong();
	}
}
