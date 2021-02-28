package com.tvd12.calabash.core;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMap extends IMap {
	
	Map<ByteArray, byte[]> loadAll();
	
	void set(ByteArray key, byte[] value);

	void putAll(Map<ByteArray, byte[]> m);
	
	byte[] put(ByteArray key, byte[] value);
	
	byte[] get(ByteArray key);
	
	Map<ByteArray, byte[]> get(Set<ByteArray> keys);

	byte[] remove(ByteArray key);
	
	void remove(Set<ByteArray> keys);
	
	long addAndGet(ByteArray key, long delta);
	
}
