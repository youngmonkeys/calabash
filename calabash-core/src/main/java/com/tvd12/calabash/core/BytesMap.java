package com.tvd12.calabash.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMap extends IMap {
	
	Map<ByteArray, byte[]> loadAll();
	
	void set(ByteArray key, byte[] value);

	void putAll(Map<ByteArray, byte[]> m);
	
	byte[] put(ByteArray key, byte[] value);
	
	byte[] get(ByteArray key);
	
	byte[] getByQuery(ByteArray key, byte[] query);
	
	Map<ByteArray, byte[]> get(Collection<ByteArray> keys);

	byte[] remove(ByteArray key);
	
	void remove(Set<ByteArray> keys);
	
	long addAndGet(ByteArray key, long delta);
	
	default void set(byte[] key, byte[] value) {
		set(ByteArray.wrap(key), value);
	}
	
	default byte[] put(byte[] key, byte[] value) {
		return put(ByteArray.wrap(key), value);
	}
	
	default void setAll(Map<byte[], byte[]> m) {
		Map<ByteArray, byte[]> tmp = new HashMap<>(m.size());
		for(Entry<byte[], byte[]> e : m.entrySet())
			tmp.put(ByteArray.wrap(e.getKey()), e.getValue());
		putAll(tmp);
	}
	
	default byte[] get(byte[] key) {
		return get(ByteArray.wrap(key));
	}
	
	default byte[] getByQuery(byte[] key, byte[] query) {
		return getByQuery(ByteArray.wrap(key), query);
	}
	
	default boolean containsKey(byte[] key) {
		return get(key) != null;
	}
	
	default byte[] remove(byte[] key) {
		return remove(ByteArray.wrap(key));
	}
	
	default void remove(byte[][] keys) {
		Set<ByteArray> tmp = new HashSet<>(keys.length);
		for(byte[] key : keys)
			tmp.add(ByteArray.wrap(key));
		remove(tmp);;
	}
	
	default long addAndGet(String key, long delta) {
		return addAndGet(ByteArray.wrap(key), delta);
	}
	
}
