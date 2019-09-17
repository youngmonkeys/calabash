package com.tvd12.calabash.local.unique;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class EntityUniques<V> {
	
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
	
}
