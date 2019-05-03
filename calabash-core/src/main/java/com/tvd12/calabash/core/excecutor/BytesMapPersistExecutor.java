package com.tvd12.calabash.core.excecutor;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMapPersistExecutor {

	Map<ByteArray, byte[]> loadAll(String mapName);
	
	Map<ByteArray, byte[]> load(String mapName, Set<ByteArray> keys);
	
	byte[] load(String mapName, ByteArray key);
	
	void persist(String mapName, ByteArray key, byte[] value);
	
	void persist(String mapName, Map<ByteArray, byte[]> m);
	
	void delete(String mapName, ByteArray key);
	
	void delete(String mapName, Set<ByteArray> keys);
	
}
