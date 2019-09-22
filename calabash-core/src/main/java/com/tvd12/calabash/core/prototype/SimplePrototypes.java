package com.tvd12.calabash.core.prototype;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimplePrototypes implements Prototypes {

	protected Map<Class, PrototypeProxy> proxies;
	
	public SimplePrototypes() {
		this(builder());
	}
	
	protected SimplePrototypes(Builder builder) {
		this.proxies = builder.proxies;
	}
	
	@Override
	public <T> T copy(T origin) {
		T copy = origin;
		if(origin == null)
			return copy;
		if(origin instanceof Prototype) {
			copy = (T)((Prototype) origin).clone();
		}
		else {
			Class objectType = origin.getClass();
			PrototypeProxy proxy = proxies.get(objectType);
			if(proxy != null) {
				copy = (T) proxy.clone(this, origin);
			}
			else if(origin instanceof Map) {
				copy = (T)copyMap((Map) origin);
			}
			else if(origin instanceof Set) {
				copy = (T)copySet((Set) origin);
			}
			else if(origin instanceof Collection) {
				copy = (T)copyList((List) origin);
			}
			else if(objectType.isArray()) {
				copy = (T) copyArray(origin);
			}
		}
		return copy;
	}
	
	@Override
	public <V> Set<V> copySet(Collection<V> origin) {
		Set<V> copy = new HashSet<>();
		for(V value : origin) {
			V copyValue = copy(value);
			copy.add(copyValue);
		}
		return copy;
	}
	
	@Override
	public <V> List<V> copyList(Collection<V> origin) {
		List<V> copy = new ArrayList<>();
		for(V value : origin) {
			V copyValue = copy(value);
			copy.add(copyValue);
		}
		return copy;
	}
	
	@Override
	public <K, V> Map<K, V> copyMap(Map<K, V> origin) {
		Map<K, V> copy = new HashMap<>();
		for(K key : origin.keySet()) {
			V value = origin.get(key);
			K copyKey = copy(key);
			V copyValue = copy(value);
			copy.put(copyKey, copyValue);
		}
		return copy;
	}
	
	protected Object copyArray(Object array) {
		Class itemType = array.getClass().getComponentType();
		int length = Array.getLength(array);
		Object copy = Array.newInstance(itemType, length);
		for(int i = 0 ; i < length ; i ++) {
			Object item = Array.get(array, i);
			Object copyItem = copy(item);
			Array.set(copy, i, copyItem);
		}
		return copy;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements PrototypesBuilder {
		
		protected Map<Class, PrototypeProxy> proxies;
		
		public Builder() {
			this.proxies = new ConcurrentHashMap<>();
		}
		
		@Override
		public PrototypesBuilder addProxy(Class objectType, PrototypeProxy proxy) {
			proxies.put(objectType, proxy);
			return this;
		}
		
		@Override
		public Prototypes build() {
			return new SimplePrototypes(this);
		}
		
	}
	
}
