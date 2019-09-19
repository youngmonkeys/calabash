package com.tvd12.calabash.local.unique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.tvd12.calabash.core.statistic.StatisticsAware;

public class EntityUniques<V> implements StatisticsAware {
	
	protected final Map<Object, EntityUnique<V>> uniques;
	protected final Map<Object, Function<V, Object>> uniqueKeyMaps;
	
	public EntityUniques(Map<Object, Function<V, Object>> uniqueKeyMaps) {
		this.uniqueKeyMaps = uniqueKeyMaps;
		this.uniques = newUniques();
	}
	
	protected Map<Object, EntityUnique<V>> newUniques() {
		Map<Object, EntityUnique<V>> answer = new HashMap<>();
		Set<Object> uniqueNames = uniqueKeyMaps.keySet();
		for(Object uniqueName : uniqueNames) {
			Function<V, Object> keyMap = uniqueKeyMaps.get(uniqueName);
			answer.put(uniqueName, new EntityUnique<>(keyMap));
		}
		return answer;
	}
	
	public void putValue(V value) {
		for(EntityUnique<V> unique : uniques.values())
			unique.putValue(value);
	}
	
	public void putValues(Iterable<V> values) {
		for(V value : values)
			putValue(value);
	}
	
	public V getValue(Map<Object, Object> keys) {
		for(Object uniqueName : keys.keySet()) {
			EntityUnique<V> unique = uniques.get(uniqueName);
			if(unique == null)
				continue;
			Object uniqueKey = keys.get(uniqueName);
			V value = unique.getValue(uniqueKey);
			if(value != null)
				return value;
		}
		return null;
	}
	
	public void removeValue(V value) {
		for(EntityUnique<V> unique : uniques.values())
			unique.removeValue(value);
	}
	
	public void removeValues(Iterable<V> values) {
		for(V value : values)
			removeValue(value);
	}
	
	@Override
	public void addStatistics(Map<String, Object> statistics) {
		statistics.put("size", uniques.size());
		List<Map<String, Object>> itemStats = new ArrayList<>();
		for(Object uniqueKey : uniques.keySet()) {
			EntityUnique<V> item = uniques.get(uniqueKey);
			Map<String, Object> itemStat = new HashMap<>();
			itemStat.put("key", uniqueKey);
			((StatisticsAware)item).addStatistics(itemStat);
			itemStats.add(itemStat);
		}
		statistics.put("items", itemStats);
	}
	
}
