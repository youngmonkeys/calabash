package com.tvd12.calabash.core;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMapPersist {
	
	Map<ByteArray, byte[]> loadAll();
	
	Map<ByteArray, byte[]> load(Set<ByteArray> keys);
	
	byte[] load(ByteArray key);
	
	void persist(ByteArray key, byte[] value);
	
	void persist(Map<ByteArray, byte[]> m);
	
	void delete(ByteArray key);
	
	void delete(Set<ByteArray> keys);
	
}
