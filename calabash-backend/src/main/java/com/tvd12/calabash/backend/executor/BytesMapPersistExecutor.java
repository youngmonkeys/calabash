package com.tvd12.calabash.backend.executor;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.backend.setting.MapSetting;
import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMapPersistExecutor {

	Map<ByteArray, byte[]> loadAll(MapSetting mapSetting);
	
	Map<ByteArray, byte[]> load(MapSetting mapSetting, Set<ByteArray> keys);
	
	byte[] load(MapSetting mapSetting, ByteArray key);
	
	void persist(MapSetting mapSetting, ByteArray key, byte[] value);
	
	void persist(MapSetting mapSetting, Map<ByteArray, byte[]> m);
	
	void delete(MapSetting mapSetting, ByteArray key);
	
	void delete(MapSetting mapSetting, Set<ByteArray> keys);
	
}
