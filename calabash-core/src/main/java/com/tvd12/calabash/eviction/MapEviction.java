package com.tvd12.calabash.eviction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface MapEviction {
	
	MapEviction DEFAULT = new MapEviction() {};

	default void updateKeyTime(Object key) {}
	
	default void updateKeysTime(Collection keys) {}
	
	default void removeKey(Object key) {}

	default void removeKeys(Collection keys) {}
	
	default List getEvictableKeys() {
		return Collections.emptyList();
	}
	
}
