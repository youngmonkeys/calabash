package com.tvd12.calabash.persist;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleNameIdMapPersist implements NameIdMapPersist {

	protected final AtomicInteger idGentor;
	protected final Map<String, Integer> nameById;
	
	public SimpleNameIdMapPersist() {
		this(new HashMap<>());
	}
	
	public SimpleNameIdMapPersist(Map<?, ?> map) {
		this.idGentor = new AtomicInteger();
		this.nameById = new ConcurrentHashMap<>();
		for(Object name : map.keySet()) {
			Integer id = Integer.parseInt(map.get(name).toString());
			this.nameById.put(name.toString(), id);
		}
	}

	@Override
	public Integer load(String key) {
		return nameById.computeIfAbsent(key, k -> idGentor.incrementAndGet());
	}

	@Override
	public void persist(String key, Integer value) {
		nameById.put(key, value);
	}
	
}
