package com.tvd12.calabash.core;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMap {
	
	void loadAll();
	
	void set(ByteArray key, byte[] value);

	void putAll(Map<ByteArray, byte[]> m);
	
	byte[] put(ByteArray key, byte[] value);
	
	byte[] get(ByteArray key);
	
	Set<ByteArray> getAllKeys(boolean loadAll);

	Collection<byte[]> getAllValues(boolean loadAll);
	
	Set<Entry<ByteArray, byte[]>> getAllEntries(boolean loadAll);
	
	Map<ByteArray, byte[]> get(Set<ByteArray> keys);

	byte[] remove(ByteArray key);
	
	void remove(Set<ByteArray> keys);

	void clear(boolean deleteAll);

	int size(boolean loadAll);

	boolean isEmpty(boolean loadAll);

	boolean containsKey(ByteArray key);
	
	String getName();
	
}
