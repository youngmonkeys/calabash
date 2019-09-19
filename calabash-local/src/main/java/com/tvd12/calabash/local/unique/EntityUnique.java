package com.tvd12.calabash.local.unique;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.tvd12.calabash.core.statistic.StatisticsAware;

public class EntityUnique<V> implements StatisticsAware {

	protected final Map<Object, V> map;
	protected final Function<V, Object> keyMap;
	
	public EntityUnique(Function<V, Object> keyMap) {
		this.keyMap = keyMap;
		this.map = new HashMap<>();
	}
	
	public void putValue(V value) {
		Object key = keyMap.apply(value);
		map.put(key, value);
	}

	public V getValue(Object key) {
		V value = map.get(key);
		return value;
	}

	public void removeValue(V value) {
		Object key = keyMap.apply(value);
		map.remove(key);
	}
	
	@Override
	public void addStatistics(Map<String, Object> statistics) {
		statistics.put("size", map.size());
	}
	
}
