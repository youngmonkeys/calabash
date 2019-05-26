package com.tvd12.calabash.backend;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.backend.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyGenerics;

public interface MapPersist<K, V> {

	Map<K, V> loadAll();
	
	Map<K, V> load(Set<K> keys);
	
	V load(K key);
	
	void persist(K key, V value);
	
	void persist(Map<K, V> m);
	
	void delete(K key);
	
	void delete(Set<K> keys);
	
	default Class<?> getKeyType() {
		try {
			Class<?> writerClass = getClass();
			Class<?>[] args = EzyGenerics.getGenericInterfacesArguments(writerClass, MapPersist.class, 2);
			return args[0];
		}
		catch(Exception e) {
			throw new UnimplementedOperationException("must implement 'getKeyType' function", e);
		}
	}
	
	default Class<?> getValueType() {
		try {
			Class<?> writerClass = getClass();
			Class<?>[] args = EzyGenerics.getGenericInterfacesArguments(writerClass, MapPersist.class, 2);
			return args[1];
		}
		catch(Exception e) {
			return null;
		}
	}
}
