package com.tvd12.calabash.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyGenerics;

public interface EntityMapPersist<K, V> {

	V load(K key);
	
	void persist(K key, V value);
	
	default void persist(Map<K, V> m) {
		for(K key : m.keySet()) {
			V value = m.get(key);
			persist(key, value);
		}
	}
	
	default Map<K, V> load(Set<K> keys) {
		Map<K, V> keyValues = new HashMap<>();
		for(K key : keys) {
			V value = load(key);
			keyValues.put(key, value);
		}
		return keyValues;
	}
	
	default Object loadByQuery(Object query) {
		return null;
	}
	
	default void delete(K key) {
	}
	
	default void delete(Set<K> keys) {
		for(K key : keys)
			delete(key);
	}
	
	default Map<K, V> loadAll() {
		return new HashMap<>();
	}
	
	default Class<?> getKeyType() {
		try {
			Class<?> writerClass = getClass();
			Class<?>[] args = EzyGenerics.getGenericInterfacesArguments(writerClass, EntityMapPersist.class, 2);
			return args[0];
		}
		catch(Exception e) {
			throw new UnimplementedOperationException("must implement 'getKeyType' function", e);
		}
	}
	
	default Class<?> getValueType() {
		try {
			Class<?> writerClass = getClass();
			Class<?>[] args = EzyGenerics.getGenericInterfacesArguments(writerClass, EntityMapPersist.class, 2);
			return args[1];
		}
		catch(Exception e) {
			throw new UnimplementedOperationException("must implement 'getValueType' function", e);
		}
	}

}
