package com.tvd12.calabash.core.util;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.core.Prototype;

public final class Protypes {

	private Protypes() {
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T copy(T origin) {
		T copy = origin;
		if(origin instanceof Prototype)
			copy = (T)((Prototype)origin).clone();
		return copy;
	}
	
	@SuppressWarnings({ "unchecked" })
	public static <K, V> Map<K, V> copyMap(Map<K, V> origin) {
		Map<K, V> copy = new HashMap<>();
		for(K key : origin.keySet()) {
			V value = origin.get(key);
			K copyKey = key;
			V copyValue = value;
			if(copy instanceof Prototype)
				copyKey = (K)((Prototype)key).clone();
			if(value instanceof Prototype)
				copyValue = (V)((Prototype)value).clone();
			copy.put(copyKey, copyValue);
		}
		return copy;
	}
}
